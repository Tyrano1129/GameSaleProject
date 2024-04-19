package kr.game.sale.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.game.sale.entity.admin.Notice;
import kr.game.sale.entity.form.NoticeForm;
import kr.game.sale.entity.game.Game;
import kr.game.sale.entity.game.GameSearchCondition;
import kr.game.sale.entity.game.SortType;
import kr.game.sale.repository.game.GameRepository;
import kr.game.sale.service.AdminService;
import kr.game.sale.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final GameService gameService;
    private final AdminService adminService;

    @GetMapping({"/",""})
    public String home(Model model){
        adminService.noticeinit();
        Map<String,List<Game>> map = new HashMap<>();
        List<Game> list1 = null;
        List<Game> list2 = null;
        List<Game> list3 = null;
        List<Game> list4 = null;
        Pageable pageable = PageRequest.of(0,5);
        Page<Notice> nList = adminService.getNoticeList(pageable,"");
        try {
            ///log.info("gameService getList is null =>{}",gameService.getList().size());
            if (gameService.getList().isEmpty()) { // reg date로 확인하는 로직 추가 예정
                gameService.initData();
            }
            list1 = gameService.findMainList("최신순");
            list2 = gameService.findMainList("할인율순");
            list3 = gameService.findMainList("한국어");
            list4 = gameService.findMainList("인기순");
            map.put("releaseDate",list1);
            map.put("discount",list2);
            map.put("korean",list3);
            map.put("popularity",list4);
        } catch (JsonProcessingException e) {
            log.error("initData Error => {}", e.getMessage());
        }
        model.addAttribute("nList",nList.getContent());
        model.addAttribute("listMap",map);
        return "main";

    }

}
