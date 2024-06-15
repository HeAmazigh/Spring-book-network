package com.amazigh.booknetwork.mapper;

import com.amazigh.booknetwork.DTO.FeedbackRequest;
import com.amazigh.booknetwork.DTO.FeedbackResponse;
import com.amazigh.booknetwork.domain.Book;
import com.amazigh.booknetwork.domain.Feedback;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FeedbackMapper {

  public static Feedback toFeedback(FeedbackRequest feedbackRequest) {
    return Feedback.builder()
        .note(feedbackRequest.note())
        .comment(feedbackRequest.comment())
        .book(Book.builder()
            .id(feedbackRequest.bookId())
            .shareable(false) // Not required and has no impact :: just to satisfy lombok
            .archived(false) // Not required and has no impact :: just to satisfy lombok
            .build())
        .build();
  }

  public static FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
    return new FeedbackResponse(
        feedback.getNote(),
        feedback.getComment(),
        Objects.equals(feedback.getCreatedBy(), userId)
    );
  }
}
