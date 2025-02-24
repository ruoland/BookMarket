package com.mysite.sbb.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBookList() {
        // TODO Auto-generated method stub
        return bookRepository.findAll();
    }


    public List<Book> getBookListByCategory(String category) {
        List<Book> booksByCategory = bookRepository.findByCategory(category);
        return booksByCategory;
    }

    public Set<Book> getBookListByFilter(Map<String, List<String>> filter) {
        Set<Book> booksByPublisher = bookRepository.findByPublisherIn(filter.get("publisher"));
        Set<Book> booksByCategory = bookRepository.findByCategoryIn(filter.get("category"));

        booksByCategory.retainAll(booksByPublisher);
        return booksByCategory;
    }
    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookIdException(bookId));
    }
    public void setNewBook(Book book) {
        bookRepository.save(book);
    }

    public void setUpdateBook(Book book) {
        bookRepository.save(book);
    }

    public void setDeleteBook(String bookId) {
        bookRepository.deleteById(bookId);
    }
}
