package com.amazigh.booknetwork.services.implementation;

import com.amazigh.booknetwork.DTO.BookResponse;
import com.amazigh.booknetwork.DTO.FeedbackRequest;
import com.amazigh.booknetwork.DTO.FeedbackResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import com.amazigh.booknetwork.domain.Book;
import com.amazigh.booknetwork.domain.Feedback;
import com.amazigh.booknetwork.domain.User;
import com.amazigh.booknetwork.exception.OperationNotPermittedException;
import com.amazigh.booknetwork.mapper.BookMapper;
import com.amazigh.booknetwork.mapper.FeedbackMapper;
import com.amazigh.booknetwork.repository.BookRepository;
import com.amazigh.booknetwork.repository.FeedbackRepository;
import com.amazigh.booknetwork.services.FeedbackService;
import com.amazigh.booknetwork.specification.BookSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;
  private final BookRepository bookRepository;

  @Override
  public Integer save(FeedbackRequest request, Authentication connectedUser) {
    // Get book from database
    Book book = fetchBook(request.bookId());
    // Verify if the book is archived or not shareable
    validateBookAvailability(book);
    // Get the connected user
    User user = getConnectedUser(connectedUser);

    // Verify if user in not the owner of this book
    validateUserNotOwnBook(book, user);

    // Create Feedback Object
    Feedback feedback = FeedbackMapper.toFeedback(request);

    return feedbackRepository.save(feedback).getId();
  }

  @Override
  public PageResponse<FeedbackResponse> loadFeedbacksByBookId(int bookId, int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser) {
    // Get book from database
    Book book = fetchBook(bookId);

    // create sorting instance
    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    // create pageable instance
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    // Get the connected user
    User user = getConnectedUser(connectedUser);

    Page<Feedback> allFeedbacks = feedbackRepository.findAllByBookId(bookId, pageable);

    //get content from page object
    List<Feedback> pageFeedbacks = allFeedbacks.getContent();

    List<FeedbackResponse> feedbackResponses = pageFeedbacks.stream().map(f -> FeedbackMapper.toFeedbackResponse(f, user.getId())).toList();

    return new PageResponse<>(
        feedbackResponses,
        allFeedbacks.getNumber(),
        allFeedbacks.getSize(),
        allFeedbacks.getTotalElements(),
        allFeedbacks.getTotalPages(),
        allFeedbacks.isFirst(),
        allFeedbacks.isLast()
    );
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
      throw new OperationNotPermittedException("You cannot give a feedback for an archived or not shareable book");
    }
  }

  private void validateUserNotOwnBook(Book book, User user) {
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot give a feedback to your own book");
    }
  }
}
