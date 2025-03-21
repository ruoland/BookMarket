package com.mysite.sbb.cart;

import com.mysite.sbb.UserNotFoundException;
import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;
    private final UserRepository userRepository;

    public Cart getCart(String username) {
        // 사용자 조회
        SbbUser user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 카트 조회 또는 생성
        return repository.findBySbbUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setSbbUser(user);
                    return repository.save(newCart);
                });
    }

}
