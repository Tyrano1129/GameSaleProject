package kr.game.sale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.entity.form.GameForm;
import kr.game.sale.entity.form.NoticeForm;
import kr.game.sale.entity.form.PaymentForm;
import kr.game.sale.entity.form.QnAAmdinForm;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.review.Review;
import kr.game.sale.entity.user.Cart;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.admin.NoticeRepository;
import kr.game.sale.repository.admin.PaymentRepository;
import kr.game.sale.repository.admin.QnARepository;
import kr.game.sale.repository.admin.RefundRepository;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.repository.game.review.ReviewRepository;
import kr.game.sale.repository.user.CartRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    // Notice
    private final NoticeRepository noticeRepository;
    // Payment
    private final PaymentRepository paymentRepository;
    // user
    private final UserRepository userRepository;
    // QnA
    private final QnARepository qnARepository;
    // Refund
    private final RefundRepository refundRepository;
    // game
    private final GameRepository gameRepository;
    // Cart
    private final CartRepository cartRepository;
    // game
    private final GameService gameService;


    private Users getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인 중인 유저
        Optional<Users> users = userRepository.findByUsername(username);
        return users.isEmpty()? null : users.get();
    }
    public List<QnA> getQnAList(){
        return qnARepository.findAll();
    }

    public List<Refund> getRefundList(){
        return refundRepository.findAll();
    }
    public Payment getOnePaymet(Long id){
        return paymentRepository.findById(id).isPresent()? paymentRepository.findById(id).get() : null;
    }

    public Refund getOneRefund(Long id){
        return refundRepository.findById(id).isPresent()? refundRepository.findById(id).get() : null;
    }
    /* qna */
    private QnA getOneQnA(Long id){
        return qnARepository.findById(id).isPresent()? qnARepository.findById(id).get() : null;
    }
    public void qnaOneUpdate(QnAAmdinForm form){
        QnA qna = getOneQnA(form.getId());
        if(qna != null){
            qna.QnAAdminUpdate(form.getQnaRespondent(), form.getQnaAnwerContent());
            qnARepository.save(qna);
        }
    }
    /* game */

    // 게임추가
    public void gameInsert(GameForm form,String headerimage,List<String> screenshotsList,List<String> genresList) throws JsonProcessingException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ObjectMapper mapper = new ObjectMapper();
        Game game = Game.builder()
                .name(form.getName())
                .supportedLanguages(form.getSupportedLanguages())
                .price(form.getPrice())
                .developers(form.getDevelopers())
                .releaseDate(formatter.parse(form.getReleaseDate()))
                .stock(form.getStock())
                .genres(mapper.writeValueAsString(genresList))
                .minRequirements(form.getMinRequirements())
                .rcmRequirements(form.getRcmRequirements())
                .headerImage(headerimage)
                .screenshots(mapper.writeValueAsString(screenshotsList))
                .build();
        gameRepository.save(game);
    }

    public void gameUpdate(GameForm form) throws JsonProcessingException, ParseException {
        Game game = gameService.findOneById(form.getId());
        game.setStock(form.getStock());
        game.setMinRequirements(form.getMinRequirements());
        game.setRcmRequirements(form.getRcmRequirements());
        gameRepository.save(game);
    }

    /* notice */
    public Page<Notice> getNoticeList(Pageable pageable, String title){
        return noticeRepository.searchPageSimple(pageable,title);
    }

    public void noticeinit(){
        if(noticeRepository.countAllBy() < 1){
            for(int i = 0; i < 100; i+=1){
                noticeRepository.save(new Notice("noticeTitle"+i,"<p>가나다라마바사아자차카타파하<p>" + i,"test"+i));
            }
        }
    }
    public Notice getOneNotice(Long id){
        Notice notice = noticeRepository.findById(id).isEmpty()? null : noticeRepository.findById(id).get();
        if(notice != null) {
            notice.countUp();
            noticeRepository.save(notice);
        }
        return notice;
    }

    public void noticeInsert(NoticeForm form){
        Notice notice = Notice.builder()
                .noticeTitle(form.getTitle())
                .noticeContent(form.getContent())
                .noticeWriter(form.getWriter())
                .build();
        noticeRepository.save(notice);
    }
    public void noticeUpdate(NoticeForm form){
        Notice notice = getOneNotice(form.getId());
        if(notice != null){
            notice.setNotice(
                    form.getTitle(),form.getContent(),form.getWriter(),form.getCount()
            );
            noticeRepository.save(notice);
        }
    }
    private Payment getOnePayment(Long id){
        return paymentRepository.findById(id).isPresent()? paymentRepository.findById(id).get() : null;
    }
    public void paymentUpdate(Long id,Refund refund){
        Payment pay = getOnePayment(id);
        if(pay != null){
            pay.setPaymentResult("환불처리완료");
            refund.setRefundWhether(true);
            refundRepository.save(refund);
            paymentRepository.save(pay);
        }
    }

    /* payment */

    public void paymentInsert(List<PaymentForm> form){
        Users user = getLoggedInUser();
        for(PaymentForm list : form){
            String uuid = UUID.randomUUID().toString();
            Game game = gameService.findOneById(list.getGameId());
            if(game != null) {
                game.stockDown();
                Payment pay = Payment.builder()
                        .paymentOrdernum(list.getMerchantUid())
                        .paymentPrice(list.getPaymentPirce())
                        .gamePrice(list.getGamePrice())
                        .gameName(game.getName())
                        .gameCode(uuid)
                        .game(game)
                        .user(user)
                        .paymentResult("환불요청")
                        .build();
                cartRepository.deleteById(list.getCartId());
                gameRepository.save(game);
                paymentRepository.save(pay);
            }else{
                break;
            }
        }
    }
    // 결제 오류났을때 데이터 지우기
    public void paymentErrorDelete(String paymentOrdernum){
        paymentRepository.deleteAllByPaymentOrdernum(paymentOrdernum);
    }


    /* refund */

    //토큰 요청 후 환불요청
    public void refundRequest(String access_token,String merchant_uid,String reason,int price) throws IOException {
        URL url = new URL("https://api.iamport.kr/payments/cancel");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 요청 방식
        conn.setRequestMethod("POST");

        // 요청의 Content-type, Accept, Authorization 헤더 설정
        conn.setRequestProperty("Content-type","application/json");
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("Authorization",access_token);

        // 해당 연결을 출력 스트림(요청) 으로 사용

        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로 하는 데이터 추가.
        JsonObject json =  new JsonObject();
        json.addProperty("merchant_uid",merchant_uid);
        json.addProperty("reason",reason);
        json.addProperty("amount",price);

        //출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        //입력 스트림으로 conn 요청에 대한 응답 반환
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String responseJson = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        conn.disconnect();

        log.info("결제 취소 완료 : 주문번호 {}",merchant_uid);
        System.out.println("응답 본문: " + responseJson);

//        JsonObject jsonResponse = JsonParser.parseString(responseJson).getAsJsonObject();
//        String resultCode = jsonResponse.get("code").getAsString();
//        String resultMessage = jsonResponse.get("message").getAsString();
//
//        System.out.println("결과 코드 = " + resultCode);
//        System.out.println("결과 메시지 = " + resultMessage);
    }
    // 토큰발급
    public String getToken(String apikey,String secretKey) throws IOException{
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // 요청 방식
        conn.setRequestMethod("POST");

        //요청의 Content-Type 과 Accept 헤더 설정
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Accept","application/json");

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // json 객체에 해당 api가 필요로 하는 데이터 추가
        JsonObject json = new JsonObject();
        json.addProperty("imp_key",apikey);
        json.addProperty("imp_secret",secretKey);

        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString()); // json 객체를 문자열 형태로 http 요청 본문에 추가
        bw.flush(); // bufferedWriter 비우기
        bw.close(); // bufferedWriter 종료

        // 입력 스트림 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String accessToken = gson.fromJson(response,Map.class).get("access_token").toString();
        br.close(); // 리더 종료

        conn.disconnect(); // 연결종료

        log.info("Iamport 액서스 토큰 발급 성공 : {}",accessToken);
        return accessToken;
    }

    public Page<QnA> qnaListPageing(Pageable pageable){
        return qnARepository.serachQnA(pageable);
    }
    public Page<Refund> refundListPageing(Pageable pageable){
        return refundRepository.serchRefund(pageable);
    }
}
