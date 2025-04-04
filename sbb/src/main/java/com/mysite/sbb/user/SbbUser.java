package com.mysite.sbb.user;

import com.mysite.sbb.cart.Cart;
import com.mysite.sbb.user.signup.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "SbbUser")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class SbbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String userId;

    @NotBlank
    private String password;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "sbbUser")
    private Cart cart;

    private UserRole role;

}
