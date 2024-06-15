package com.amazigh.booknetwork.services;

import com.amazigh.booknetwork.DTO.BookRequest;
import com.amazigh.booknetwork.DTO.BookResponse;
import com.amazigh.booknetwork.DTO.BorrowedBookResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
  Integer save(BookRequest request, Authentication connectedUser);

  BookResponse getBookById(Integer id);

  PageResponse<BookResponse> getAllBooks(int pageNo, int pageSize, String sortBy,String sortDir, Authentication connectedUse);

  PageResponse<BookResponse> getAllBooksByOwner(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser);

  PageResponse<BorrowedBookResponse> getAllBorrowedBooks(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser);

  PageResponse<BorrowedBookResponse> getAllReturnedBooks(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser);

  Integer updateShareableStatusBook(Integer bookId, Authentication connectedUser);

  Integer updateArchivedStatusBook(Integer bookId, Authentication connectedUser);

  Integer borrowBook(Integer bookId, Authentication connectedUser);

  Integer returnBorrowedBook(Integer bookId, Authentication connectedUser);

  Integer ApproveReturnBorrowedBook(Integer bookId, Authentication connectedUser);

  void uploadBookCoverPicture(MultipartFile file,Integer bookId , Authentication connectedUser);
}
