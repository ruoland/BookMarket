package com.mysite.sbb.exception;


@SuppressWarnings("serial")
public class BookIdException extends RuntimeException {

    private String bookId;

    public BookIdException(String bookId) {
        super("Invalid Book ID: " + bookId); // 부모 클래스에 메시지 전달
        this.bookId = bookId;
    }

    public String getBookId() {  //Getter() �޼���
        return bookId;
    }

    @Override
    public String toString() {
        return "BookIdException: Invalid Book ID - " + bookId;
    }
}