package kr.game.sale.entity.admin;

import jakarta.persistence.*;
import kr.game.sale.entity.user.Users;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class QnA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users users;
    private String qnaTitle;
    @Lob
    private String qnaContent;
    private LocalDateTime localDateTime;
    private boolean qnaIsAnswered;
    private String qnaRespondent;
    @Lob
    private String qnaAnwerContent;

    // 문의자
    public QnA(Users users, String qnaTitle, String qnaContent) {
        this.users = users;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.localDateTime = LocalDateTime.now();
        this.qnaIsAnswered = false;
    }

    // 답변자
    public void QnAAdminUpdate(String qnaRespondent, String qnaAnwerContent) {
        this.qnaIsAnswered = true;
        this.qnaRespondent = qnaRespondent;
        this.qnaAnwerContent = qnaAnwerContent;
    }
}
