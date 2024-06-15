package com.amazigh.booknetwork.services;

import com.amazigh.booknetwork.DTO.FeedbackRequest;
import com.amazigh.booknetwork.DTO.FeedbackResponse;
import com.amazigh.booknetwork.commen.PageResponse;
import org.springframework.security.core.Authentication;

public interface FeedbackService {
  Integer save(FeedbackRequest request, Authentication connectedUser);

  PageResponse<FeedbackResponse> loadFeedbacksByBookId(int bookId, int pageNo, int pageSize, String sortBy, String sortDir, Authentication connectedUser);
}
