package kr.game.sale.entity.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import kr.game.sale.entity.user.Users;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Setter
public class QnA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qnaId;

    @ManyToOne
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
    @Transient
    private String dateView;

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

    @QueryProjection
    public QnA(Long qnaId, Users users, String qnaTitle, String qnaContent, LocalDateTime localDateTime, boolean qnaIsAnswered, String qnaRespondent, String qnaAnwerContent) {
        this.qnaId = qnaId;
        this.users = users;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.localDateTime = localDateTime;
        this.qnaIsAnswered = qnaIsAnswered;
        this.qnaRespondent = qnaRespondent;
        this.qnaAnwerContent = qnaAnwerContent;
    }

    public boolean qnaIsAnswered(){
        return this.qnaIsAnswered;
    }
}
