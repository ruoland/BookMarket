package com.mysite.sbb.order;

import com.mysite.sbb.book.Book;
import com.mysite.sbb.user.SbbUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deliveryId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sbbUser_id")
    private SbbUser sbbUser;

    //private List<Book> deliveryItems = new ArrayList<>();
    private EnumStatus status;

    private LocalDateTime deliveryDate;
    private LocalDateTime orderedDate;
}
