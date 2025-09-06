package com.idb.microservicedemo.library.dto.writer;

import com.idb.microservicedemo.library.dto.abstractions.BasePageRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class GetPageWriterRequest extends BasePageRequestDto {
    private String firstName;
    private String lastName;
    private String biography;
    private String nationality;
    private OffsetDateTime birthDate;
    private OffsetDateTime deathDate;
    private String website;
    private String email;
}
