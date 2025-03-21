package com.mysite.sbb.cart;

import com.mysite.sbb.book.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    /*
       1.Cart 에는 카트 아이템이 담긴다
       2.CartItem에는 책과 주문 개수가 담긴다
     */
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private String bookId;

    private int amount;

}
