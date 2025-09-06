package com.idb.microservicedemo.library.repository;

import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WriterRepository extends JpaRepository<Writer, UUID> {

    // İsteğe bağlı ekstra sorgular
    Optional<Writer> findByEmail(String email);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
