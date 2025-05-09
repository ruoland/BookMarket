package com.mysite.sbb.book;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Book implements Serializable{

    @Id
    @Pattern(regexp = "^ISBN\\d{4}$", message = "Pattern.NewBook.bookId")
    private String bookId;

    private String sellerId;

    @NotBlank
    private String name;

    @Min(value = 1, message = "Min.NewBook.unitPrice")
    private int unitPrice;

    @NotBlank
    private String author;

    @NotBlank
    private String description;

    private String publisher;

    private String category;

    private long unitsInStock;

    private String releaseDate;

    private String condition;

    @Transient // 북 이미지는 DB에 저장되지 않게 합니다
    private MultipartFile bookImage;

    private String  fileName;

    @Builder
    public Book(String bookId, String name, int unitPrice) {
		super();
		this.bookId = bookId;
		this.name = name;
		this.unitPrice = unitPrice;
	}

}
