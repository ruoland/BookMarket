package com.mysite.sbb.cart;

import com.mysite.sbb.user.SbbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findBySbbUser_UserId(String username);

    Optional<Cart> findBySbbUser(SbbUser user);
}
