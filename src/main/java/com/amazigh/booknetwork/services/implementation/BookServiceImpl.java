package com.amazigh.booknetwork.services.implementation;

import com.amazigh.booknetwork.DTO.BookRequest;
import com.amazigh.booknetwork.DTO.BookResponse;
import com.amazigh.booknetwork.DTO.BorrowedBookResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import com.amazigh.booknetwork.domain.Book;
import com.amazigh.booknetwork.domain.BookTransactionHistory;
import com.amazigh.booknetwork.domain.User;
import com.amazigh.booknetwork.exception.BookNotFoundException;
import com.amazigh.booknetwork.exception.OperationNotPermittedException;
import com.amazigh.booknetwork.mapper.BookMapper;
import com.amazigh.booknetwork.repository.BookRepository;
import com.amazigh.booknetwork.repository.BookTransactionHistoryRepository;
import com.amazigh.booknetwork.services.BookService;
import com.amazigh.booknetwork.services.FileStorageService;
import com.amazigh.booknetwork.specification.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
  private final FileStorageService fileStorageService;

  @Override
  public Integer save(BookRequest request, Authentication connectedUser) {
    User user = getConnectedUser(connectedUser);
    Book book = BookMapper.toBook(request);
    book.setOwner(user);
    return bookRepository.save(book).getId();
  }

  @Override
  public BookResponse getBookById(Integer id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isEmpty())
      throw new BookNotFoundException("Book with id="+ id +" not found");
    return BookMapper.toBookResponse(book.get());
  }

  @Override
  public PageResponse<BookResponse> getAllBooks(int pageNo,int pageSize, String sortBy, String sortDir, Authentication connectedUser) {
    User user = getConnectedUser(connectedUser);

    // create sorting instance
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    Page<Book> allBooks = bookRepository.findAllDisplayableBooks(pageable, user.getId());

    //get content from page object
    List<Book> pageBooks = allBooks.getContent();

    List<BookResponse> bookResponses = pageBooks.stream().map(BookMapper::toBookResponse).toList();

    return new PageResponse<>(
        bookResponses,
        allBooks.getNumber(),
        allBooks.getSize(),
        allBooks.getTotalElements(),
        allBooks.getTotalPages(),
        allBooks.isFirst(),
        allBooks.isLast()
    );
  }

  @Override
  public PageResponse<BookResponse> getAllBooksByOwner(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser) {
    User user = getConnectedUser(connectedUser);

    // create sorting instance
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    Page<Book> allBooks = bookRepository.findAll(BookSpecification.specification(user.getId()), pageable);

    //get content from page object
    List<Book> pageBooks = allBooks.getContent();

    List<BookResponse> bookResponses = pageBooks.stream().map(BookMapper::toBookResponse).toList();

    return new PageResponse<>(
        bookResponses,
        allBooks.getNumber(),
        allBooks.getSize(),
        allBooks.getTotalElements(),
        allBooks.getTotalPages(),
        allBooks.isFirst(),
        allBooks.isLast()
    );
  }

  @Override
  public PageResponse<BorrowedBookResponse> getAllBorrowedBooks(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser) {
    User user = getConnectedUser(connectedUser);

    // create sorting instance
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());

    //get content from page object
    List<BookTransactionHistory> pageBooks = borrowedBooks.getContent();

    List<BorrowedBookResponse> borrowedBookResponses = pageBooks.stream().map(BookMapper::toBorrowedBookResponse).toList();

    return new PageResponse<>(
        borrowedBookResponses,
        borrowedBooks.getNumber(),
        borrowedBooks.getSize(),
        borrowedBooks.getTotalElements(),
        borrowedBooks.getTotalPages(),
        borrowedBooks.isFirst(),
        borrowedBooks.isLast()
    );
  }

  @Override
  public PageResponse<BorrowedBookResponse> getAllReturnedBooks(int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser) {
    User user = getConnectedUser(connectedUser);

    // create sorting instance
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    Page<BookTransactionHistory> borrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());

    //get content from page object
    List<BookTransactionHistory> pageBooks = borrowedBooks.getContent();

    List<BorrowedBookResponse> borrowedBookResponses = pageBooks.stream().map(BookMapper::toBorrowedBookResponse).toList();

    return new PageResponse<>(
        borrowedBookResponses,
        borrowedBooks.getNumber(),
        borrowedBooks.getSize(),
        borrowedBooks.getTotalElements(),
        borrowedBooks.getTotalPages(),
        borrowedBooks.isFirst(),
        borrowedBooks.isLast()
    );
  }

  @Override
  public Integer updateShareableStatusBook(Integer bookId, Authentication connectedUser) {
    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);

    User user = getConnectedUser(connectedUser);

    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot update others shareable book status");
    }

    book.setShareable(!book.isShareable());
    bookRepository.save(book);

    return bookId;
  }

  @Override
  public Integer updateArchivedStatusBook(Integer bookId, Authentication connectedUser) {
    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);

    User user = getConnectedUser(connectedUser);

    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot update others archived book status");
    }

    book.setArchived(!book.isArchived());
    bookRepository.save(book);

    return bookId;
  }

  @Override
  public Integer borrowBook(Integer bookId, Authentication connectedUser) {
    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);

    // Verify if book is archived or is not shareable
    validateBookAvailability(book);

    User user = getConnectedUser(connectedUser);
    // Verify if user has not borrowed or returned his own book
    validateUserNotBorrowingOwnBook(book, user);

    // Verify if the book is not already borrowed
    validateBookNotAlreadyBorrowed(bookId, user);

    BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
        .user(user)
        .book(book)
        .returned(false)
        .returnedApproved(false)
        .build();

    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);

    // Verify if book is archived or is not shareable
    validateBookAvailability(book);

    User user = getConnectedUser(connectedUser);
    // Verify if user has not borrowed or returned his own book
    validateUserNotBorrowingOwnBook(book, user);

    BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
        .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

    bookTransactionHistory.setReturned(true);

    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  public Integer ApproveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {

    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);

    // Verify if book is archived or is not shareable
    validateBookAvailability(book);

    User user = getConnectedUser(connectedUser);
    // Verify if user is the owner of this book
    validateUserCanApproveReturnedBorrowingBook(book, user);

    BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
        .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet, You cannot approve its return"));

    bookTransactionHistory.setReturnedApproved(true);

    return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
  }

  @Override
  public void uploadBookCoverPicture(MultipartFile file, Integer bookId, Authentication connectedUser) {
    // Fitch and check if the book exist in database
    Book book = fetchBook(bookId);
    User user = getConnectedUser(connectedUser);

    String bookCover = fileStorageService.saveFile(file, user.getId());

    book.setBookCover(bookCover);

    bookRepository.save(book);
  }


  private User getConnectedUser(Authentication connectedUser){
    return ((User) connectedUser.getPrincipal());
  }

  private Book fetchBook(Integer bookId) {
    return bookRepository.findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("No book found with the ID: " + bookId));
  }

  private void validateBookAvailability(Book book) {
    if (book.isArchived() || !book.isShareable()) {
      throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
    }
  }

  private void validateUserNotBorrowingOwnBook(Book book, User user) {
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow or return your own book");
    }
  }

  private void validateUserCanApproveReturnedBorrowingBook(Book book, User user) {
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot approve a book that you do note own");
    }
  }

  private void validateBookNotAlreadyBorrowed(Integer bookId, User user) {
    final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
    if (isAlreadyBorrowed) {
      throw new OperationNotPermittedException("The requested book is already borrowed");
    }
  }
}
