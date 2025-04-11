package com.mysite.sbb.order;

import com.mysite.sbb.order.entity.Order;
import com.mysite.sbb.user.SbbUser;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems"})
    List<Order> findBySbbUser(SbbUser sbbUser);
}