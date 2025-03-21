package com.mysite.sbb.cart;

import com.mysite.sbb.book.Book;
import com.mysite.sbb.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/user/cart")
@Controller
public class CartController {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final BookService bookService;

    @GetMapping("/add/{bookId}")
    public String addCart(Model model, @PathVariable(value = "bookId") String bookId) {
        model.addAttribute("book", bookService.getBookById(bookId));
        return "redirect:/user/cart/view";
    }

    @PostMapping("/add/{bookId}")
    public String submitAddBook(@PathVariable(value = "bookId") String bookId, Principal principal) {
        if(principal.getName() ==null){
            return "redirect:/login";
        }
        Cart cart = cartService.getCart(principal.getName());

        //카트에 이미 있는지 확인하기
        for(CartItem item : cart.getBooks()) {
            if(item.getBookId().equals(bookId)) {
                item.setAmount(item.getAmount() + 1);
                cartRepository.save(cart);
                return "redirect:/user/cart/view";
            }
        }
        //카트에 담긴 게 없으면
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBookId(bookId);
        cartItem.setAmount(1);
        cart.getBooks().add(cartItem);


        cartRepository.save(cart);

        return "redirect:/user/cart/view";
    }
    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        // 사용자 이름으로 카트 정보를 가져옴
        Cart cart = cartService.getCart(principal.getName());
        model.addAttribute("cart", cart);

        return "cart/view"; // 장바구니 페이지 템플릿 이름
    }
}
