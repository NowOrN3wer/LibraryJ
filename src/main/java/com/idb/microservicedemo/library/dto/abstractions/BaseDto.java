package com.idb.microservicedemo.library.dto.abstractions;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter
@SuperBuilder @NoArgsConstructor @AllArgsConstructor
public abstract class BaseDto {
    private UUID id;
    private int version;
    private String createdBy;
    private String updatedBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}