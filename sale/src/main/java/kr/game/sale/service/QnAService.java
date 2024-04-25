package kr.game.sale.service;

import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.form.QnAForm;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.admin.QnARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QnAService {
    private final QnARepository qnaRepository;
    private final UserService userService;
    private final GoogleGCPService googleGCPService;

    @Transactional
    public void addQnA(QnAForm qnaForm) throws IOException {
        Users users = userService.getLoggedInUser();
        String fileName = googleGCPService.updateImageInfo(qnaForm.getFileName(),"user");
        QnA qna = new QnA(users, qnaForm.getQnaTitle(), qnaForm.getQnaContent());
        qna.setFileName(fileName);
        qnaRepository.save(qna);
    }

    public List<QnA> findAllByUsers() {
        return qnaRepository.findAllByUsers(userService.getLoggedInUser());
    }
}
