package com.mysite.sbb.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrderForm {

    @NotEmpty(message = "수령인의 전화 번호를 입력 하세요.")
    private String number;

    @NotEmpty(message = "목적지 주소를 입력 하세요.")
    private String deliveryAddress;

    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;

    @NotEmpty(message = "결제 수단을 선택하세요.")
    private String paymentMethod;
}
