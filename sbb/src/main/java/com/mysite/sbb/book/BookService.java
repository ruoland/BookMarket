package com.mysite.sbb.book;

import com.mysite.sbb.exception.BookIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBookList() {

        return bookRepository.findAll();
    }


    public List<Book> findBook(String keyword, String category) {
        if(Strings.isNotEmpty(keyword) && Strings.isEmpty(category))
            // 키워드로만 검색하기
            return bookRepository.findByNameContainingOrAuthorContaining(keyword, keyword);

        else if(Strings.isEmpty(keyword) && Strings.isNotEmpty(category))
            return bookRepository.findByCategoryContaining(category);

        else if(Strings.isNotEmpty(keyword) && Strings.isNotEmpty(category))
            return bookRepository.findByNameContainingOrAuthorContainingAndCategoryContaining(keyword, keyword, category);

        else
            return getAllBookList();
    }

    public Set<Book> getBooksBySellerId(String userId){
        return bookRepository.findBySellerId(userId);
    }
    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookIdException(bookId));
    }
    public void setNewBook(Book book) {
        bookRepository.save(book);
    }

    public void downloadImage(Book book){
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

    }
    @Transactional
    public void updateUnitsInAmount(String bookId, int unitsInStock) {
        bookRepository.updateUnitsInStock(bookId, unitsInStock);
    }

    public void deleteBook(String bookId) {
        bookRepository.deleteById(bookId);
    }
}
