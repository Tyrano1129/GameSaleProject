package kr.game.sale.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import kr.game.sale.entity.form.PaymentDataForm;
import kr.game.sale.entity.form.PaymentForm;
import kr.game.sale.service.AdminService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/payment")
@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final AdminService adminService;
    private final UserService userService;
    private IamportClient iamportClient;
    @Value("${imp.api.key}")
    private String apiKey;
    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    // 결제 요청
    @PostMapping("/processing")
    public @ResponseBody IamportResponse<Payment> validateIamport(@ModelAttribute PaymentDataForm paymentData) {
        String impUid = paymentData.getImpUid();
        //결제된 정보 요청
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(impUid);
        log.info("결제 요청 응답. 결제 내역 = {}", impUid);
        return payment;
    }

    // 결제 마무리
    @PostMapping("/paymentcheck")
    public @ResponseBody ResponseEntity<String> paymentComplete(@RequestBody List<PaymentForm> list) throws IOException {
        String orderNumber = list.get(0).getMerchantUid();
        // 유저는 세션으로 가지고옴
        // 게임은 이름같은걸로 가지고옴
        // 게임 코드는 랜덤으로 만들예정
        log.info("list = {}", list);
        adminService.paymentInsert(list);
        try {
            log.info("결제 성공 : 주문번호 {}", orderNumber);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.info("주문 상품 환불 진행 : 주문 번호 {}", orderNumber);
            String token = adminService.getToken(apiKey, secretKey);
            adminService.refundRequest(token, orderNumber, e.getMessage(), 0);
            adminService.paymentErrorDelete(list.get(0).getMerchantUid());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/errorPayment")
    public @ResponseBody String paymentError(@ModelAttribute List<PaymentForm> list) throws IOException {
        log.info("list = {}", list);
        RuntimeException e = new RuntimeException();
        String orderNumber = list.get(1).getMerchantUid();
        log.info("주문 상품 환불 진행 : 주문 번호 {}", orderNumber);
        String token = adminService.getToken(apiKey, secretKey);
        adminService.refundRequest(token, orderNumber, e.getMessage(), 0);
        return "결제 실패하였습니다. 확인후에 이용해주세요.";
    }

    // 환불요청 버튼 눌렀을 때 실행되는 메서드
    @PostMapping("/refund")
    @ResponseBody
    public String gameRefund(@RequestParam String code, @RequestParam String reason) {
        log.info("code = {}", code);
        log.info("reason = {}", reason);
        userService.makeRefund(code,reason);
        return "success";
    }
}
