package kr.game.sale.service;
import kr.game.sale.entity.user.Wishlist;
import kr.game.sale.repository.user.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    public List<Wishlist> findAll() {}
}
