package com.mysite.sbb.cart;

import com.mysite.sbb.book.Book;
import com.mysite.sbb.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        cartService.addCartItem(principal.getName(), bookId);

        return "redirect:/user/cart/view";
    }
    @PostMapping("/cart/remove")
    public ResponseEntity<?> removeItem(@RequestParam String bookId, Principal principal) {
        cartService.removeCartItem(principal.getName(), bookId);
        return ResponseEntity.ok().build(); // 성공적인 응답 반환
    }
    @GetMapping("/cart/totalPrice")
    public String getTotalPrice(Principal principal){
        java.util.List<CartItem> cartItemList = cartService.getCartItems(principal.getName());
        int totalPrice = cartService.getTotalPrice(cartItemList);
        return  String.valueOf(totalPrice);
    }

    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        // 사용자 이름으로 카트 정보를 가져옴
        Cart cart = cartService.getCart(principal.getName());
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice",cartService.totalPrice(principal.getName()));
        return "cart/view"; // 장바구니 페이지 템플릿 이름
    }
}
