package com.mysite.sbb.buy;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemBuyForm {

    @Size(min = 3, max = 25)
    private String userId;

    @NotEmpty(message = "수령인의 전화 번호를 입력 하세요.")
    private String number;

    @NotEmpty(message = "목적지 주소를 입력 하세요.")
    private String delivery;

    @Email
    private String email;

    private int price;

    private String paymentMethod;

}
