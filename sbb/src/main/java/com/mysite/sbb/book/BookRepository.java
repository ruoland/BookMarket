package com.mysite.sbb.book;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByCategory(String category);

    List<Book> findByNameContainingOrAuthorContaining(String keyword, String author);

    List<Book> findByCategoryContaining(String category);
    Set<Book> findBySellerId(String sellerId);

    @Modifying
    @Query("UPDATE Book b SET b.unitsInStock = :unitsInStock WHERE b.bookId = :bookId")
    void updateUnitsInStock(@Param("bookId") String bookId, @Param("unitsInStock") int unitsInStock);

    List<Book> findByNameContainingOrAuthorContainingAndCategoryContaining(String keyword, String author, String category);
}
