package com.mysite.sbb.order.entity;

import com.mysite.sbb.user.SbbUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY) // 사용자와 다대일 관계 설정
    @JoinColumn(name = "sbbUser_id")
    private SbbUser sbbUser;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 주문 항목 관리
    private List<OrderItem> orderItems = new ArrayList<>();
}
