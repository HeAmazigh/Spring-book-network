package com.amazigh.booknetwork.resources;

import com.amazigh.booknetwork.DTO.AuthRequest;
import com.amazigh.booknetwork.DTO.AuthResponse;
import com.amazigh.booknetwork.DTO.RegisterRequest;
import com.amazigh.booknetwork.services.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthResource {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) throws MessagingException {
    authService.register(request);
    return ResponseEntity.accepted().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
    return ResponseEntity.ok(authService.authenticate(request));
  }

  @GetMapping("activate-account")
  public void confirm(@RequestParam String code) throws MessagingException {
    authService.activateAccount(code);
  }
}
