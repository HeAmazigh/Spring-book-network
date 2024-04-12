package com.amazigh.booknetwork.services.email;


import com.amazigh.booknetwork.enums.EmailTemplateName;
import jakarta.mail.MessagingException;

public interface EmailService {
  void sendEmail(
      String to,
      String username,
      String confirmationUrl,
      String activationCode,
      String subject,
      EmailTemplateName emailTemplate
  ) throws MessagingException;
}
