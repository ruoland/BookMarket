package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistryForm {

    @Size(min = 3, max = 25)
    private String userId;

    @NotEmpty(message = "비밀 번호를 입력 하세요.")
    private String password;

    @NotEmpty(message = "확인용 비밀 번호를 입력 하세요.")
    private String passwordCheck;

    @Email
    @NotEmpty(message = "이메일을 반드시 입력하여야 합니다.")
    private String email;
}
