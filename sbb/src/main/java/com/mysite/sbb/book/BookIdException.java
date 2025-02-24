package com.mysite.sbb.book;


@SuppressWarnings("serial")
public class BookIdException extends RuntimeException {

    private String bookId;

    public BookIdException(String bookId) {  //������
        this.bookId = bookId;
    }

    public String getBookId() {  //Getter() �޼���
        return bookId;
    }
}