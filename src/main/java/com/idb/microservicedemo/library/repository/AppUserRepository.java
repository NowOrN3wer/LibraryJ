package com.idb.microservicedemo.library.repository;

import com.idb.microservicedemo.library.domain.entities.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByUsername(String username);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByRefreshToken(String refreshToken);
}