package com.mysite.sbb.buy;

import com.mysite.sbb.cart.Cart;
import com.mysite.sbb.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/buy")
public class BuyController {

    private final CartService cartService;

    @GetMapping
    public String buyForm(Model model, Principal principal) {
        if(principal.getName() == null)
            return "redirect:/login";
        ItemBuyForm itemBuyForm = new ItemBuyForm();
        itemBuyForm.setUserId(principal.getName());
        itemBuyForm.setPrice(cartService.totalPrice(principal.getName()));
        model.addAttribute("itemBuyForm", itemBuyForm);


        return "order_form.html";
    }

    @PostMapping("/confirm")
    public String processBuy(Model model, @Valid ItemBuyForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "order_form.html"; // 에러 발생 시 다시 폼 페이지로 이동
        }
        model.addAttribute("itemBuyForm", form);
        return "order_confirm.html";
    }

}
