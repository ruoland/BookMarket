package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    //시작 메인메뉴 페이지
    @GetMapping("/welcome")
    public String welcomePage(){
        return "welcome";
    }

    @GetMapping
    public String welcome(){
        return "redirect:/welcome";
    }
}
