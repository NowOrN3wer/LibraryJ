package com.idb.microservicedemo.library.dto.writer;


import com.idb.microservicedemo.library.dto.abstractions.BaseDto;

import java.time.OffsetDateTime;


import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WriterDto extends BaseDto {

    @NotBlank(message = "FirstName alanı boş olamaz.")
    @Size(max = 100, message = "FirstName en fazla 100 karakter olabilir.")
    private String firstName;

    @Size(max = 100, message = "LastName en fazla 100 karakter olabilir.")
    private String lastName;

    @Size(max = 500, message = "Biography en fazla 500 karakter olabilir.")
    private String biography;

    @Size(max = 100, message = "Nationality en fazla 100 karakter olabilir.")
    private String nationality;

    @PastOrPresent(message = "BirthDate gelecekte olamaz.")
    private OffsetDateTime birthDate;

    @PastOrPresent(message = "DeathDate gelecekte olamaz.")
    private OffsetDateTime deathDate;

    @Size(max = 255, message = "Website en fazla 255 karakter olabilir.")
    @Pattern(regexp = "^https?://.*$", message = "Website geçerli bir URL olmalıdır.")
    private String website;

    @Size(max = 255, message = "Email en fazla 255 karakter olabilir.")
    @Email(message = "Geçerli bir e-posta adresi olmalıdır.")
    private String email;
}