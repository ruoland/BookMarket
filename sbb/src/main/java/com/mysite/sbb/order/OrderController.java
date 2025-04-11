package com.mysite.sbb.order;


import com.mysite.sbb.cart.CartItem;
import com.mysite.sbb.cart.CartService;
import com.mysite.sbb.order.dto.OrderDTO;

import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/order")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String buyForm(Model model, Principal principal, HttpSession session) {

        java.util.List<CartItem > cartItemList = cartService.getCartItems(principal.getName());
        int totalPrice = cartService.getTotalPrice(cartItemList);

        session.setAttribute("bookList", cartItemList);
        session.setAttribute("totalPrice", totalPrice);
        model.addAttribute("itemOrderForm", new ItemOrderForm());
        model.addAttribute("totalPrice", totalPrice);
        return "order/order_form.html";
    }

    @PostMapping("/confirm")
    public String processBuy(Model model, Principal principal, @Valid ItemOrderForm form, BindingResult result, HttpSession httpSession) {
        model.addAttribute("orderForm", form);
        model.addAttribute("totalPrice", httpSession.getAttribute("totalPrice"));

        if (result.hasErrors()) {
            return "order/order_form.html"; // 에러 발생 시 다시 입력 폼 페이지로 이동
        }

        return "order/order_confirm.html";
    }

    @PostMapping("/complete")
    public String paymentComplete(Model model, Principal principal, @Valid ItemOrderForm form, BindingResult result, HttpSession httpSession) {
        if (result.hasErrors()) {
            model.addAttribute("itemOrderForm", form);
            model.addAttribute("totalPrice", httpSession.getAttribute("totalPrice"));
            return "order/order_confirm.html";
        }

        OrderDTO orderDTO = OrderDTO.builder()
                .email(form.getEmail())
                .deliveryAddress(form.getDeliveryAddress())
                .paymentMethod(form.getPaymentMethod())
                .number(form.getNumber())
                .totalPrice((Integer) httpSession.getAttribute("totalPrice"))
                .build();
        model.addAttribute("orderDTO", orderDTO);
        orderService.createOrder(principal.getName(), orderDTO);

        // 주문 완료 후 세션 데이터 제거
        httpSession.removeAttribute("bookList");
        httpSession.removeAttribute("totalPrice");

        return "order/order_complete.html";
    }


}
