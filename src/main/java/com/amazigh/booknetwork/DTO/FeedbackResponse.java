package com.amazigh.booknetwork.DTO;

public record FeedbackResponse(
    Double note,
    String comment,
    boolean ownFeedback
) {
}
