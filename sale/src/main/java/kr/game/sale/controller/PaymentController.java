package kr.game.sale.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import kr.game.sale.entity.form.PaymentDataForm;
import kr.game.sale.entity.form.PaymentForm;
import kr.game.sale.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequestMapping("/payment")
@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private IamportClient iamportClient;
    @PostConstruct
    public void init(){
        this.iamportClient = new IamportClient(apiKey,secretKey);
    }
    @Value("${imp.api.key}")
    private String apiKey;
    @Value("${imp.api.secretkey}")
    private String secretKey;

    // 결제 요청
    @PostMapping("/processing")
    public @ResponseBody IamportResponse<Payment> validateIamport(@ModelAttribute PaymentDataForm paymentData){
        String impUid = paymentData.getImpUid();
        //결제된 정보 요청
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(impUid);
        log.info("결제 요청 응답. 결제 내역 = {}",impUid);
        return payment;
    }
    // 결제 마무리
    @PostMapping("/paymentcheck")
    public ResponseEntity<String> paymentComplete(@ModelAttribute PaymentDataForm paymentData, @ModelAttribute PaymentForm payment) throws IOException {
        String orderNumber = paymentData.getMerchantUid();
        try{
            log.info("결제 성공 : 주문번호 {}",orderNumber);
            return ResponseEntity.ok().build();
        } catch(RuntimeException e){
            log.info("주문 상품 환불 진행 : 주문 번호 {}",orderNumber);
            String token = paymentService.getToken(apiKey,secretKey);
            paymentService.refundRequest(token,orderNumber,e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
