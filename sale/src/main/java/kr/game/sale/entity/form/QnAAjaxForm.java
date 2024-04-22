package kr.game.sale.entity.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kr.game.sale.entity.user.Users;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class QnAAjaxForm {
    private Long qnaId;
    private Users users;
    private String qnaTitle;
    private String qnaContent;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String localDateTime;
    private boolean qnaIsAnswered;
    private String qnaRespondent;
    private String qnaAnwerContent;

    public QnAAjaxForm(Long qnaId, Users users, String qnaTitle, String qnaContent, boolean qnaIsAnswered, String qnaRespondent, String qnaAnwerContent) {
        this.qnaId = qnaId;
        this.users = users;
        this.qnaTitle = qnaTitle;
        this.qnaContent = qnaContent;
        this.qnaIsAnswered = qnaIsAnswered;
        this.qnaRespondent = qnaRespondent;
        this.qnaAnwerContent = qnaAnwerContent;
    }
}
