package com.amazigh.booknetwork.services.auth;

import com.amazigh.booknetwork.DTO.AuthRequest;
import com.amazigh.booknetwork.DTO.AuthResponse;
import com.amazigh.booknetwork.DTO.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthService {
  void register(RegisterRequest request) throws MessagingException;
  AuthResponse authenticate(AuthRequest request);
  void activateAccount(String code) throws MessagingException;
}
