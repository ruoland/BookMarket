package com.mysite.sbb.cart;

import com.mysite.sbb.exception.UserNotFoundException;
import com.mysite.sbb.book.Book;
import com.mysite.sbb.book.BookService;
import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
@RequiredArgsConstructor
@Service
public class CartService {

    private final BookService bookService;

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public void removeCartItem(String username, String bookId) {
        SbbUser user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));
        Cart cart = cartRepository.findBySbbUser(user).orElseGet(Cart::new);
        cart.getBooks().stream().forEach(item -> {
            if (item.getBook().getBookId().equals(bookId)) {
                cart.getBooks().remove(item);
            }
        });
    }
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

    public List<CartItem> getCartItems(String username) {
        // 사용자의 카트에 담긴 책 목록을 반환
        return getCart(username).getBooks();
    }

    public int getTotalPrice(List<CartItem> cartItems){
        return cartItems.stream().mapToInt(item -> item.getAmount() * item.getBook().getUnitPrice()).sum();
    }
    public void addCartItem(String username, String bookId) {
        Cart cart = getCart(username);

        //카트에 추가 하려는 아이템이 이미 있는지 확인하기
        for(CartItem item : cart.getBooks()) {
            //추가하려는 아이템이 이미 카트에 있다면
            if(item.getBook().getBookId().equals(bookId)) {
                
                //카트에 존재하는 아이템의 개수 하나 증가 하고 종료
                item.setAmount(item.getAmount() + 1);
                cartRepository.save(cart);
                return;
            }
        }
        //카트에 추가 하려는 아이템이 없으면
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setBook(bookService.getBookById(bookId));
        cartItem.setAmount(1);
        cart.getBooks().add(cartItem);
        cartRepository.save(cart);
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
