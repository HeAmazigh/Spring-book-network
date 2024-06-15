package com.amazigh.booknetwork.repository;

import com.amazigh.booknetwork.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
  @Query("""
      SELECT feedbacks
      FROM Feedback feedbacks
      WHERE feedbacks.book.id = :bookId
      """)
  Page<Feedback> findAllByBookId(int bookId, Pageable pageable);
}
