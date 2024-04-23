package kr.game.sale.controller;


import jakarta.servlet.http.HttpSession;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.form.NoticeForm;
import kr.game.sale.entity.form.NoticePageing;
import kr.game.sale.entity.user.Users;
import kr.game.sale.service.AdminService;
import kr.game.sale.service.GoogleGCPService;
import kr.game.sale.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notice")
@Controller
public class NoticeController {
    private final AdminService adminService;
    private final UserService userService;
    private final GoogleGCPService googleGCPService;
    // 초기값
    @GetMapping
    public String notice(Model model, HttpSession session){
        Pageable pageable = PageRequest.of(0,15);
        if(session.getAttribute("noticePage") != null){
            session.removeAttribute("noticePage");
        }
        NoticePageing noticePageing = new NoticePageing(0,10,"");
        pageingCutomer(pageable,model,noticePageing,session);
        return "notice/noticeList";
    }

    // 선택된 페이징
    @GetMapping("/customer")
    public String noticecustomer(Pageable pageable, Model model,HttpSession session){
        NoticePageing noticePageing = (NoticePageing) session.getAttribute("noticePage");
        log.info("page = {}",pageable);
        if(pageable.getPageNumber() >= (int)session.getAttribute("total")){
            return "redirect:/notice";
        }
        pageingCutomer(pageable,model,noticePageing,session);
        return "notice/noticeList";
    }
    //페이징 메소드
    private void pageingCutomer(Pageable pageable,
                                Model model,
                                NoticePageing noticePageing,
                                HttpSession session){
        Page<Notice> noticelist = adminService.getNoticeList(pageable,noticePageing.getTitle());
        List<NoticeForm> list = new ArrayList<>();
        // 현재 로그인 되어있는 유저찾기 비어있으면 조건주기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        log.info("username ={}",username);
        Users u = null;
        if(!username.equals("anonymousUser")){
            u = userService.getOneUernameUser(username);
        }
        log.info("user ={}",u);
        // view 에 담을 noticeForm
        for(Notice l : noticelist.getContent()){
            NoticeForm form = new NoticeForm(
                    l.getNoticeId(),
                    l.getNoticeTitle(),
                    l.getNoticeContent(),
                    l.getNoticeWriter(),
                    l.localDateFormater(),
                    l.getNoticeCount()
            );
            list.add(form);
        }
        // page 값 가지고오기
        int currNum = noticelist.getNumber();
        // page 값 과 end 비교
        if(currNum >= noticePageing.getEndNum()){
            noticePageing.setStartNum(currNum);
            log.info("통과");
            noticePageing.setEndNum(noticePageing.getStartNum() + 10);
        }else if(currNum < noticePageing.getStartNum()){
            noticePageing.setStartNum(noticePageing.getStartNum() - 10);
            noticePageing.setEndNum(noticePageing.getEndNum() - 10);
        }

        log.info("currNum={}",currNum);
        int total = noticelist.getTotalPages();
        log.info("total = {}",total);
        // end 값이 total 보다 크면
        if(noticePageing.getEndNum() > total){
            noticePageing.setEndNum(total);
        }
        log.info("start={}",noticePageing.getStartNum());
        log.info("end={}",noticePageing.getEndNum());
        log.info("total={}",total);
        // view 에 뿌려준다.
        session.setAttribute("noticePage",noticePageing);
        session.setAttribute("total",total);
        model.addAttribute("start",noticePageing.getStartNum());
        model.addAttribute("end",noticePageing.getEndNum());
        model.addAttribute("total",total);
        model.addAttribute("noticeList",list);
        model.addAttribute("title", noticePageing.getTitle());
        model.addAttribute("user",u);
    }

    // 검색창
    @GetMapping("/seach")
    public String search(Model model,HttpSession session,String searchvalue){
        NoticePageing noticePageing = (NoticePageing) session.getAttribute("noticePage");
        Pageable pageable = PageRequest.of(0,15);
        noticePageing.setTitle(searchvalue);
        pageingCutomer(pageable,model,noticePageing,session);
        return "notice/noticeList";
    }

    // noticeForm
    @GetMapping("/noticeForm")
    public String getNoticeForm(Long id, Model model){
        NoticeForm form = null;
        if(id != null){
            Notice notice = adminService.getOneNotice(id);
            form = new NoticeForm(
                    notice.getNoticeId(),
                    notice.getNoticeTitle(),
                    notice.getNoticeContent(),
                    notice.getNoticeWriter(),
                    notice.localDateFormater(),
                    notice.getNoticeCount()
            );
        }
        log.info("notice = {}",form);
        model.addAttribute("notice",form);
        return "notice/noticeForm";
    }
    @GetMapping("/noticeView")
    public String getNoticeView(Long id, Model model){
        NoticeForm form = null;
        if(id != null){
            Notice notice = adminService.getOneNotice(id);
            form = new NoticeForm(
                    notice.getNoticeId(),
                    notice.getNoticeTitle(),
                    notice.getNoticeContent(),
                    notice.getNoticeWriter(),
                    notice.localDateFormater(),
                    notice.getNoticeCount()
            );
        }
        log.info("notice = {}",form);
        model.addAttribute("notice",form);
        return "notice/noticeView";
    }

    @PostMapping("/image")
    public @ResponseBody Map<String,Object> getImageUpdate(MultipartRequest request) throws IOException {
        Map<String,Object> responseData = new HashMap<>();
        String gcpUrl = googleGCPService.updateImageInfo(Objects.requireNonNull(request.getFile("upload")),"notice");
        responseData.put("uploaded",true);
        responseData.put("url",gcpUrl);
        return responseData;
    }

    @PostMapping("/insert")
    public String noticeInsert(NoticeForm form){
        log.info("form={}",form);
        adminService.noticeInsert(form);
        return "redirect:/notice";
    }

    @PostMapping("/update")
    public String noticeUpdate(NoticeForm form){
        log.info("form={}",form);
        adminService.noticeUpdate(form);
        return "redirect:/notice";
    }
}
