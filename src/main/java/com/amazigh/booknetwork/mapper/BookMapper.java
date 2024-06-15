package com.amazigh.booknetwork.mapper;

import com.amazigh.booknetwork.DTO.BookRequest;
import com.amazigh.booknetwork.DTO.BookResponse;
import com.amazigh.booknetwork.DTO.BorrowedBookResponse;
import com.amazigh.booknetwork.domain.Book;
import com.amazigh.booknetwork.domain.BookTransactionHistory;
import com.amazigh.booknetwork.utils.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

  public static Book toBook(BookRequest bookRequest) {
    Book book = new Book();
    BeanUtils.copyProperties(bookRequest, book);
    return book;
  }

  public static BookResponse toBookResponse(Book book) {
    if (book == null) return null;

    return new BookResponse(
        book.getId(),
        book.getTitle(),
        book.getAuthorName(),
        book.getIsbn(),
        book.getSynopsis(),
        book.getOwner().getFullName(),
        FileUtils.readFileFromLocation(book.getBookCover()),
        book.getRate(),
        book.isArchived(),
        book.isShareable()
    );
  }

  public static BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
    if (bookTransactionHistory == null) return null;

    return new BorrowedBookResponse(
        bookTransactionHistory.getId(),
        bookTransactionHistory.getBook().getTitle(),
        bookTransactionHistory.getBook().getAuthorName(),
        bookTransactionHistory.getBook().getIsbn(),
        bookTransactionHistory.getBook().getRate(),
        bookTransactionHistory.isReturned(),
        bookTransactionHistory.isReturnedApproved()
    );
  }
}
