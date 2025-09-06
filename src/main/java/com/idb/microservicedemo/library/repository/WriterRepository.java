package com.idb.microservicedemo.library.repository;

import com.idb.microservicedemo.library.domain.entities.writer.Writer;
import com.idb.microservicedemo.library.repository.custom.WriterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WriterRepository extends JpaRepository<Writer, UUID>, WriterRepositoryCustom {

    // İsteğe bağlı ekstra sorgular
    Optional<Writer> findByEmail(String email);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
