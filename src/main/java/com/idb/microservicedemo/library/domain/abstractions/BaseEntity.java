package com.idb.microservicedemo.library.domain.abstractions;

import com.idb.microservicedemo.library.domain.EntityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME) // TIME = UUIDv7
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

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
