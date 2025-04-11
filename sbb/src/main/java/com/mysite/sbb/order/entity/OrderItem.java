package com.mysite.sbb.order.entity;

import com.mysite.sbb.book.Book;
import com.mysite.sbb.user.SbbUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {

    public OrderItem(){
        setStatus(EnumStatus.ORDERED);
        setOrderedDate(LocalDateTime.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderItemId; // orderId 대신 orderItemId로 명확히 구분

    @ManyToOne(fetch = FetchType.LAZY) // 사용자와 다대일 관계 (사용자당 여러 주문 항목)
    @JoinColumn(name = "sbbUser_id")
    private SbbUser sbbUser;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 OrderItem이 하나의 Order에 속함
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 OrderItem이 하나의 Book에 속함
    @JoinColumn(name = "book_id")
    private Book book;

    private int amount; // 구매 수량

    private String deliveryAddress; // 배송지 주소

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private EnumStatus status;

    private LocalDateTime orderedDate;
}
