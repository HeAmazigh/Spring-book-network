package com.amazigh.booknetwork.services.auth;

import com.amazigh.booknetwork.DTO.AuthRequest;
import com.amazigh.booknetwork.DTO.AuthResponse;
import com.amazigh.booknetwork.DTO.RegisterRequest;
import com.amazigh.booknetwork.domain.Role;
import com.amazigh.booknetwork.domain.Token;
import com.amazigh.booknetwork.domain.User;
import com.amazigh.booknetwork.enums.EmailTemplateName;
import com.amazigh.booknetwork.repository.RoleRepository;
import com.amazigh.booknetwork.repository.TokenRepository;
import com.amazigh.booknetwork.repository.UserRepository;
import com.amazigh.booknetwork.services.JwtService;
import com.amazigh.booknetwork.services.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Value("${application.mailing.frontend.activation-url}")
  private String activationUrl;

  @Override
  public void register(RegisterRequest request) throws MessagingException {
    Role userRole = roleRepository.findByName("USER")
        // todo - Better exception handling
        .orElseThrow(() -> new IllegalStateException("ROLE USER not found"));

    User user = User.builder()
        .firstname(request.firstname())
        .lastname(request.lastname())
        .email(request.email())
        .password(passwordEncoder.encode(request.password()))
        .accountLocked(false)
        .enable(false)
        .roles(List.of(userRole))
        .build();
    
    userRepository.save(user);
    // send validation email
    sendValidationEmail(user);
  }

  @Override
  public AuthResponse authenticate(AuthRequest request) {
    var auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()
        )
    );

    var claims = new HashMap<String, Object>();
    var user = ((User) auth.getPrincipal());
    claims.put("fullname", user.getFullName());

    var jwt = jwtService.generateToken(claims, user);

    return AuthResponse.builder().token(jwt).build();
  }

  //@Transactional
  @Override
  public void activateAccount(String code) throws MessagingException {
    Token token = tokenRepository.findByToken(code).orElseThrow(
        () -> new RuntimeException("Invalid code")
    );

    if (LocalDateTime.now().isAfter(token.getExpiresAt())){
      sendValidationEmail(token.getUser());
      throw new RuntimeException("Code is expired, an email is sent to the same e-mail");
    }

    User user = userRepository.findById(token.getUser().getId()).orElseThrow(
        () -> new UsernameNotFoundException("User not found")
    );

    user.setEnable(true);
    userRepository.save(user);
    token.setValidatedAt(LocalDateTime.now());
    tokenRepository.save(token);
  }

  private void sendValidationEmail(User user) throws MessagingException {
    var newToken = generateAndSaveActivationToken(user);
    emailService.sendEmail(
        user.getEmail(),
        user.getFullName(),
        activationUrl,
        newToken,
        "Activate Account",
        EmailTemplateName.ACTIVATE_ACCOUNT
    );
  }

  private String generateAndSaveActivationToken(User user) {
    String generatedToken = generateActivationCode(6);
    Token token = Token.builder()
        .token(generatedToken)
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(15))
        .user(user)
        .build();

    tokenRepository.save(token);
    return generatedToken;
  }

  private String generateActivationCode(int length) {
    String characters = "123456789";
    StringBuilder codeBuilder = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i=0 ; i < length; i++) {
      int randomIndex = secureRandom.nextInt(characters.length());
      codeBuilder.append(characters.charAt(randomIndex));
    }
    return codeBuilder.toString();
  }
}
