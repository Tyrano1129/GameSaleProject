package kr.game.sale.controller;

import kr.game.sale.entity.form.QnAForm;
import kr.game.sale.service.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnAController {
    private final QnAService qnaService;
    @PostMapping("/addQuestion")
    public String addQuestion(@ModelAttribute QnAForm qnaForm) {
        qnaService.addQnA(qnaForm);
        return "redirect:/users/userQuestion";
    }
}
