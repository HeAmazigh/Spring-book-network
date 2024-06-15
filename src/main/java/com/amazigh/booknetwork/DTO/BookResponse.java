package com.amazigh.booknetwork.DTO;

public record BookResponse(
    Integer id,
    String title,
    String authorName,
    String isbn,
    String synopsis,
    String owner,
    byte[] cover,
    Double rate,
    boolean archived,
    boolean shareable
) {
}
