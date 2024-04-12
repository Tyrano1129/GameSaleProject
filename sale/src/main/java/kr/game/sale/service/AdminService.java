package kr.game.sale.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.admin.Payment;
import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.admin.Refund;
import kr.game.sale.repository.admin.NoticeRepository;
import kr.game.sale.repository.admin.PaymentRepository;
import kr.game.sale.repository.admin.QnARepository;
import kr.game.sale.repository.admin.RefundRepository;
import kr.game.sale.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    //토큰 요청 후 환불요청
    public void refundRequest(String access_token,String merchant_uid,String reason) throws IOException {
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

        //출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        //입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String responseJson = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        br.close();
        conn.disconnect();

        log.info("결제 취소 완료 : 주문번호 {}",merchant_uid);
        System.out.println("응답 본문: " + responseJson);

        JsonObject jsonResponse = JsonParser.parseString(responseJson).getAsJsonObject();
        String resultCode = jsonResponse.get("code").getAsString();
        String resultMessage = jsonResponse.get("message").getAsString();

        System.out.println("결과 코드 = " + resultCode);
        System.out.println("결과 메시지 = " + resultMessage);
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

    //결제 데이터 저장
    @Transactional
    public void paymentInsert(Payment payment){

    }
    /* qna */

    public List<QnA> getQnAList(){
        return qnARepository.findAll();
    }
    /* refund */
    public List<Refund> getRefundList(){
        return refundRepository.findAll();
    }


    /* 공지사항 */
    public Page<Notice> getNoticeList(Pageable pageable, String title){
        return noticeRepository.searchPageSimple(pageable,title);
    }

    public void noticeInsert(){
        if(noticeRepository.countAllBy() < 1){
            for(int i = 0; i < 100; i+=1){
                noticeRepository.save(new Notice("noticeTitle"+i,"가나다라마바사아자차카타파하" + i,"test"+i));
            }
        }
    }
}
