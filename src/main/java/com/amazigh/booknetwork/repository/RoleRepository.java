package com.amazigh.booknetwork.repository;

import com.amazigh.booknetwork.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(String role);
}
