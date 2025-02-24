package com.mysite.sbb.book;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

@RequestMapping("/books")
@Controller
public class BookController {

    @Autowired
    BookService bookService;

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
        MultipartFile bookImage = book.getBookImage();

        if (!bookImage.isEmpty()) {
            try {
                // 프로젝트 루트 경로 가져오기
                String uploadDir = new ClassPathResource("static/upload/").getFile().getAbsolutePath();

                String originalFilename = bookImage.getOriginalFilename();
                String saveName = System.currentTimeMillis() + "_" + originalFilename;
                Path filePath = Paths.get(uploadDir, saveName);

                // 파일 저장
                Files.copy(bookImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                book.setFileName(saveName);
            } catch (IOException e) {
                throw new RuntimeException("도서 이미지 업로드가 실패하였습니다", e);
            }
        }

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

        return "book";
    }
}
