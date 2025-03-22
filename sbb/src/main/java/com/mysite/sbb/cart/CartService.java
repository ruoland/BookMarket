package com.mysite.sbb.cart;

import com.mysite.sbb.UserNotFoundException;
import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Cart getCart(String username) {
        // 사용자 조회
        SbbUser user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 카트 조회 또는 생성
        return cartRepository.findBySbbUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setSbbUser(user);
                    return cartRepository.save(newCart);
                });
    }


    public int totalAmount(String username){
        Cart cart = cartRepository.findBySbbUser_UserId(username).orElseThrow(() -> new UserNotFoundException(("사용자를 찾을 수 없었습니다:"+username)));
        return cart.getBooks().stream().mapToInt(CartItem::getAmount).sum();
    }

    public int totalPrice(String username){
        Cart cart = cartRepository.findBySbbUser_UserId(username).orElseThrow(() -> new UserNotFoundException(("사용자를 찾을 수 없었습니다:"+username)));
        return cart.getBooks().stream().mapToInt(item -> item.getAmount() * item.getBook().getUnitPrice()).sum();
    }
}
