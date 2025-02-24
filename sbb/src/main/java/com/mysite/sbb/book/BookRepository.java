package com.mysite.sbb.book;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByCategory(String category);

    @Query("SELECT b FROM Book b WHERE b.publisher IN :publishers")
    Set<Book> findByPublisherIn(@Param("publishers") List<String> publishers);

    @Query("SELECT b FROM Book b WHERE b.category IN :categories")
    Set<Book> findByCategoryIn(@Param("categories") List<String> categories);
}
