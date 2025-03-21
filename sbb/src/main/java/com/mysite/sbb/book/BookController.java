package com.mysite.sbb.book;

import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRole;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @PostMapping("/remove")
    public String removeBook(RedirectAttributes model, @RequestParam("bookId") String bookId, Principal principal) {
        Book book = bookService.getBookById(bookId);

        if(userService.canCurrentUserAccess(book.getAuthor())){
            bookService.setDeleteBook(bookId);
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

        if(!userService.canCurrentUserAccess(book.getAuthor())){
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/books/book?id=" + bookId;
        }
        model.addAttribute("EditBook", book);
        return "editBook";
    }
    @PostMapping("/edit")
    public String submitEditBook(@Valid @ModelAttribute("EditBook") Book book, BindingResult result) {
        if(result.hasErrors()) {
            return "editBook";
        }
        bookService.downloadImage(book);
        bookService.setUpdateBook(book);
        return "redirect:/books";
    }
    //책 추가 폼
    @GetMapping("/add")
    public String addBookForm(Model model, @RequestParam(value = "language", required = false) String language) {
        if(language != null){
            LocaleContextHolder.setLocale(new Locale(language));
        }
        model.addAttribute("NewBook", new Book());
        return "addBook";
    }

    @PostMapping("/add")
    public String submitAddNewBook(@Valid @ModelAttribute("NewBook") Book book, BindingResult result) {
        if(result.hasErrors()) {
            return "addBook";
        }
        bookService.downloadImage(book);
        bookService.setNewBook(book);
        return "redirect:/books";
    }

    @GetMapping()
    public String bookList(Model model) {
        List<Book> bookList =bookService.getAllBookList();

        model.addAttribute("bookList", bookList);
        return "bookList"; // bookList.html을 반환
    }

    @GetMapping("/book")
    public String requestBookById(@RequestParam("id") String bookId, Model model) {
        Book bookById = bookService.getBookById(bookId);
        model.addAttribute("book", bookById );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken)
            return "book";

        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", userService.getUser(user.getUsername()));
        return "book";
    }
}
