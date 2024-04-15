package kr.game.sale.entity.admin;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;
    private String noticeTitle;
    @Lob
    @Column(length = 3000)
    private String noticeContent;
    private int noticeCount;
    private String noticeWriter;
    private LocalDateTime noticeDate;

    // 값넣는 생성자
    @Builder
    public Notice(String noticeTitle, String noticeContent, String noticeWriter) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeWriter = noticeWriter;
        this.noticeDate = LocalDateTime.now();
    }
    public void setNotice(String noticeTitle, String noticeContent, String noticeWriter,int count){
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeWriter = noticeWriter;
        this.noticeDate = LocalDateTime.now();
        this.noticeCount = count;
    }

    @QueryProjection
    public Notice(Long noticeId, String noticeTitle, String noticeContent, int noticeCount, String noticeWriter, LocalDateTime noticeDate) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeCount = noticeCount;
        this.noticeWriter = noticeWriter;
        this.noticeDate = noticeDate;
    }

    public String localDateFormater(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formated = this.noticeDate.format(formatter);
        return formated;
    }

    // 카운트 업
    public void countUp() {
        this.noticeCount +=1;
    }
}
