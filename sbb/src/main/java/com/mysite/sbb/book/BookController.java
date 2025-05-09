package com.mysite.sbb.book;

import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@RequestMapping("/books")
@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping()
    public String bookList(@RequestParam(required = false) String keyword, @RequestParam(required = false) String category, Model model) {
        List<Book> bookList =bookService.findBook(keyword, category);
        model.addAttribute("bookList", bookList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("categories", EnumCategories.values());
        return "book/book_list"; // bookList.html을 반환
    }

    //책 추가 폼
    @GetMapping("/add")
    public String addBookForm(Model model, @RequestParam(value = "language", required = false) String language) {
        if(language != null){
            LocaleContextHolder.setLocale(new Locale(language));
        }
        model.addAttribute("NewBook", new Book());
        return "book/add_book";
    }

    @PostMapping("/add")
    public String submitAddNewBook(Principal principal, @Valid @ModelAttribute("NewBook") Book book, BindingResult result) {
        if(result.hasErrors()) {
            return "book/add_book";
        }
        book.setSellerId(principal.getName());
        bookService.downloadImage(book);
        bookService.setNewBook(book);
        return "redirect:/books";
    }

    @PostMapping("/remove")
    public String removeBook(RedirectAttributes model, @RequestParam("bookId") String bookId, Principal principal) {
        Book book = bookService.getBookById(bookId);

        if(userService.canCurrentUserAccess(book.getSellerId())){
            bookService.deleteBook(bookId);
            model.addFlashAttribute("successMessage", "성공적으로 삭제 했습니다.");
        }
        else{
            model.addFlashAttribute("errorMessage","권한이 없습니다.");
        }
        return "redirect:/books"; // 수정된 부분

    }
    //책 수정 폼
    @GetMapping("/edit")
    public String editBookForm(Model model, RedirectAttributes redirectAttributes, @RequestParam("bookId") String bookId,@RequestParam(value = "language", required = false) String language, Principal principal) {
        if(language != null){
            LocaleContextHolder.setLocale(new Locale(language));
        }
        Book book = bookService.getBookById(bookId);

        if(!userService.canCurrentUserAccess(book.getSellerId())){
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/books/book?id=" + bookId;
        }
        model.addAttribute("EditBook", book);
        return "book/edit_book";
    }
    @PostMapping("/edit")
    public String submitEditBook(Principal principal, @Valid @ModelAttribute("EditBook") Book book, BindingResult result) {
        if(result.hasErrors()) {
            return "book/edit_book";
        }
        bookService.downloadImage(book);
        book.setSellerId(principal.getName());
        bookService.setNewBook(book);
        return "redirect:/books";
    }

    @GetMapping("/book")
    public String requestBookById(@RequestParam("id") String bookId, Model model) {
        Book bookById = bookService.getBookById(bookId);
        model.addAttribute("book", bookById );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            return "book/book";

        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", userService.getUser(user.getUsername()));
        return "book/book";
    }
}
