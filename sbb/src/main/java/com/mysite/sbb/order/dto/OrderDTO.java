package com.mysite.sbb.order.dto;

import com.mysite.sbb.book.Book;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class OrderDTO {
    private Set<Book> bookList;

    private String number;

    private String deliveryAddress;

    private String email;

    private String paymentMethod;

    private int totalPrice;


}
