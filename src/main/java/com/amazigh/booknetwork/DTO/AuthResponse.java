package com.amazigh.booknetwork.DTO;

import lombok.Builder;

@Builder
public record AuthResponse(
    String token
) {}
