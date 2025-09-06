package com.idb.microservicedemo.library.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "UUIDv7")
    @GenericGenerator(
            name = "UUIDv7",
            strategy = "org.hibernate.id.uuid.UuidGenerator" // Hibernate 6 ile UUIDv7 destekleniyor
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id = UUID.randomUUID(); // fallback, DB generate de olabilir

    @Column(nullable = false)
    private int version = 1;

    @NotNull
    @Size(max = 255)
    @Column(name = "created_by", length = 255, nullable = false)
    private String createdBy;

    @Size(max = 255)
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now(); // Türkiye saatini zone ile ayarlayabilirsin

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_deleted", nullable = false, length = 20)
    private EntityStatus isDeleted = EntityStatus.ACTIVE;
}
