package com.mysite.sbb.user;

import com.mysite.sbb.book.BookService;
import com.mysite.sbb.order.OrderService;
import com.mysite.sbb.user.signup.UserRegistryForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final BookService bookService;
    @GetMapping("/signup")
    public String signup(UserRegistryForm userCreateForm) {
        return "login/signup_form.html";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserRegistryForm userRegistryForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "login/signup_form.html";
        }
        if(!userRegistryForm.getPassword().equals(userRegistryForm.getPasswordCheck())){
            bindingResult.rejectValue("passwordCheck", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "login/signup_form.html";
        }
        try{
            userService.create(userRegistryForm.getUserId(), userRegistryForm.getPassword(), userRegistryForm.getEmail());
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "login/signup_form.html";
        }
        catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "login/signup_form.html";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login/login_form";
    }
    @GetMapping("/profile")
    public String myPage(Model model, Principal principal) {
        model.addAttribute("sbbUser", userService.getUser(principal.getName()));
        model.addAttribute("orders", orderService.getOrdersByUsername(principal.getName())); // 주문 목록 가져오기

        model.addAttribute("sellerBooks", bookService.getBooksBySellerId(principal.getName()));

        return "my_page";
    }

}
