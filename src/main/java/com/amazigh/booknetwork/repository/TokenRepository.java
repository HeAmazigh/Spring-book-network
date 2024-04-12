package com.amazigh.booknetwork.repository;

import com.amazigh.booknetwork.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  Optional<Token> findByToken(String token);
}
