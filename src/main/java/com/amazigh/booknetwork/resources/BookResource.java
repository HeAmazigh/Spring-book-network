package com.amazigh.booknetwork.resources;

import com.amazigh.booknetwork.DTO.BookRequest;
import com.amazigh.booknetwork.DTO.BookResponse;
import com.amazigh.booknetwork.DTO.BorrowedBookResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import com.amazigh.booknetwork.services.BookService;
import com.amazigh.booknetwork.utils.AppConstents;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookResource {

  private final BookService bookService;

  @PostMapping
  public ResponseEntity<Integer> save(@RequestBody @Valid BookRequest request, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.save(request, connectedUser));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> loadBookById(@PathVariable Integer id) {
    return ResponseEntity.ok(bookService.getBookById(id));
  }

  @GetMapping
  public ResponseEntity<PageResponse<BookResponse>> loadAllBooks(
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(defaultValue = AppConstents.DEFAULT_SORTING, required = false) String sortBy,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SORTING_DIRECTION, required = false) String sortDir,
      Authentication connectedUser
  ) {
    return ResponseEntity.ok(bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir, connectedUser));
  }

  @GetMapping("/owner")
  public ResponseEntity<PageResponse<BookResponse>> loadAllBooksByOwner(
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(defaultValue = AppConstents.DEFAULT_SORTING, required = false) String sortBy,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SORTING_DIRECTION, required = false) String sortDir,
      Authentication connectedUser
  ) {
    return ResponseEntity.ok(bookService.getAllBooksByOwner(pageNo, pageSize, sortBy, sortDir, connectedUser));
  }

  @GetMapping("/borrowed")
  public ResponseEntity<PageResponse<BorrowedBookResponse>> loadAllBorrowedBooks(
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(defaultValue = AppConstents.DEFAULT_SORTING, required = false) String sortBy,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SORTING_DIRECTION, required = false) String sortDir,
      Authentication connectedUser
  ) {
    return ResponseEntity.ok(bookService.getAllBorrowedBooks(pageNo, pageSize, sortBy, sortDir, connectedUser));
  }

  @GetMapping("/returned")
  public ResponseEntity<PageResponse<BorrowedBookResponse>> loadAllReturnedBooks(
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(defaultValue = AppConstents.DEFAULT_SORTING, required = false) String sortBy,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SORTING_DIRECTION, required = false) String sortDir,
      Authentication connectedUser
  ) {
    return ResponseEntity.ok(bookService.getAllReturnedBooks(pageNo, pageSize, sortBy, sortDir, connectedUser));
  }

  @PatchMapping("/shareable/{bookId}")
  public ResponseEntity<Integer> updateShareableStatusBook(@PathVariable("bookId") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.updateShareableStatusBook(bookId, connectedUser));
  }

  @PatchMapping("/archived/{bookId}")
  public ResponseEntity<Integer> updateArchivedStatusBook(@PathVariable("bookId") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.updateArchivedStatusBook(bookId, connectedUser));
  }

  @PostMapping("/borrowed/{bookId}")
  public ResponseEntity<Integer> borrowBook(@PathVariable("bookId") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
  }

  @PatchMapping("/borrow/return/{bookId}")
  public ResponseEntity<Integer> returnBorrowedBook(@PathVariable("bookId") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, connectedUser));
  }

  @PatchMapping("/borrow/return/approve/{bookId}")
  public ResponseEntity<Integer> approveReturnBorrowedBook(@PathVariable("bookId") Integer bookId, Authentication connectedUser) {
    return ResponseEntity.ok(bookService.ApproveReturnBorrowedBook(bookId, connectedUser));
  }

  @PostMapping(value = "/cover/{bookId}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadBookCoverPicture(
      @PathVariable("bookId") Integer bookId,
      @RequestPart("file") MultipartFile file,
      @Parameter()
      Authentication connectedUser
  ) {
    bookService.uploadBookCoverPicture(file, bookId, connectedUser);
    return ResponseEntity.accepted().build();
  }
}
