package kr.game.sale.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    // 카운트 업
    public void countUp() {
        this.noticeCount +=1;
    }
}
