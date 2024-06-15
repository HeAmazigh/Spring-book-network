package com.amazigh.booknetwork.DTO;

public record BorrowedBookResponse(
    Integer id,
    String title,
    String authorName,
    String isbn,
    double rate,
    boolean returned,
    boolean returnApproved
) { }
