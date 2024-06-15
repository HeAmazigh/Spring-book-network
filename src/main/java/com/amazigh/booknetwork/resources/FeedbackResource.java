package com.amazigh.booknetwork.resources;

import com.amazigh.booknetwork.DTO.FeedbackRequest;
import com.amazigh.booknetwork.DTO.FeedbackResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import com.amazigh.booknetwork.services.FeedbackService;
import com.amazigh.booknetwork.utils.AppConstents;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackResource {

  private final FeedbackService feedbackService;

  @PostMapping
  public ResponseEntity<Integer> createFeedback(@RequestBody @Valid FeedbackRequest request, Authentication connectedUser) {
    return ResponseEntity.ok(feedbackService.save(request, connectedUser));
  }

  @GetMapping("/book/{bookId}")
  public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByBookId(
      @PathVariable("bookId") Integer bookId,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(defaultValue = AppConstents.DEFAULT_SORTING, required = false) String sortBy,
      @RequestParam(defaultValue = AppConstents.DEFAULT_PAGE_SORTING_DIRECTION, required = false) String sortDir,
      Authentication connectedUser) {
    return ResponseEntity.ok(feedbackService.loadFeedbacksByBookId(bookId ,pageNo, pageSize, sortBy, sortDir, connectedUser));
  }
}
