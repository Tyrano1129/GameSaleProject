package kr.game.sale.service;

import kr.game.sale.entity.admin.QnA;
import kr.game.sale.entity.form.QnAForm;
import kr.game.sale.entity.user.Users;
import kr.game.sale.repository.admin.QnARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QnAService {
    private final QnARepository qnaRepository;
    private final UserService userService;

    @Transactional
    public void addQnA(QnAForm qnaForm) {
        Users users = userService.getLoggedInUser();
        QnA qna = new QnA(users, qnaForm.getQnaTitle(), qnaForm.getQnaContent());
        qnaRepository.save(qna);
    }

    public List<QnA> findAllByUsers() {
        return qnaRepository.findAllByUsers(userService.getLoggedInUser());
    }
}
