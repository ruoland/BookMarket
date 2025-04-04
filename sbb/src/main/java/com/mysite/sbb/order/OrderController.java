package com.mysite.sbb.order;

import com.mysite.sbb.buy.ItemBuyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/status")
    public String orderStatus(Model model, Principal principal){
        return "";
    }

    @PostMapping("/create")
    public String order(ItemBuyForm form, Principal principal){
        orderService.createOrder(form);
        return "";
    }
}
