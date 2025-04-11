package com.mysite.sbb.order;

import com.mysite.sbb.book.Book;
import com.mysite.sbb.book.BookRepository;
import com.mysite.sbb.book.BookService;
import com.mysite.sbb.cart.CartItem;
import com.mysite.sbb.exception.UserNotFoundException;
import com.mysite.sbb.cart.CartService;
import com.mysite.sbb.order.dto.OrderDTO;
import com.mysite.sbb.order.entity.Order;
import com.mysite.sbb.order.entity.OrderItem;
import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.*;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final BookService bookService;
    public void createOrder(String username, OrderDTO orderDTO) {
        SbbUser sbbUser = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));

        List<CartItem> cartItems = cartService.getCartItems(username); // 모든 카트 아이템 가져오기
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("카트가 비어 있습니다.");
        }

        // 새로운 주문 생성
        Order order = createNewOrder(username);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSbbUser(sbbUser);
            orderItem.setAmount(cartItem.getAmount() * cartItem.getBook().getUnitPrice()); // 총 가격 계산
            orderItem.setDeliveryAddress(orderDTO.getDeliveryAddress());
            orderItem.setBook(cartItem.getBook()); // Book 정보 설정
            orderItem.setOrder(order); // 새로 생성된 Order와 연결

            // Order에 OrderItem 추가
            order.getOrderItems().add(orderItem);

            // 재고 업데이트
            bookService.updateUnitsInAmount(cartItem.getBook().getBookId(),
                    (int) (cartItem.getBook().getUnitsInStock() - cartItem.getAmount()));
        }

        // 데이터 저장 (CascadeType.ALL 덕분에 orderItems도 자동 저장됨)
        orderRepository.save(order);
    }

    public Order createNewOrder(String username) {
        SbbUser sbbUser = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));
        Order newOrder = new Order();
        newOrder.setSbbUser(sbbUser);
        return orderRepository.save(newOrder); // 새 주문 저장 후 반환
    }

    public List<Order> getOrdersByUsername(String username) {
        SbbUser sbbUser = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + username));
        return orderRepository.findBySbbUser(sbbUser);
    }

}

