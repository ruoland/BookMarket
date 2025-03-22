package com.mysite.sbb.buy;

import com.mysite.sbb.cart.Cart;
import com.mysite.sbb.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/buy")
public class BuyController {

    private final BuyService buyService;
    private final CartService cartService;;
    @GetMapping("/{username}")
    public void buyForm(Model model, @PathVariable(value = "username") String username) {
        Cart cart = cartService.getCart(username);

    }
}
