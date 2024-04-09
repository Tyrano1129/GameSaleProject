package kr.game.sale.service.admin;

import kr.game.sale.entity.admin.Notice;
import kr.game.sale.repository.admin.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<Notice> getNoticeList(Pageable pageable,String title){
        return noticeRepository.searchPageSimple(pageable,title);
    }

    public void insert(){

    }
}
