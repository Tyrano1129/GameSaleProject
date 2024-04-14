package kr.game.sale.entity.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class QnAForm {
    private String qnaTitle;
    private String qnaContent;
    private MultipartFile fileName;
}
