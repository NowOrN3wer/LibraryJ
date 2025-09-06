package com.idb.microservicedemo.library.domain.entities.writer;

import com.idb.microservicedemo.library.domain.abstractions.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "writers")
public class Writer extends BaseEntity {

    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Size(max = 500)
    @Column(name = "biography", length = 500)
    private String biography;

    @Size(max = 100)
    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "birth_date")
    private OffsetDateTime birthDate;

    @Column(name = "death_date")
    private OffsetDateTime deathDate;

    @Size(max = 255)
    @Column(name = "website", length = 255)
    private String website;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    // FullName hesaplama için transient alan
    @Transient
    public String getFullName() {
        return String.format("%s %s", firstName, lastName != null ? lastName : "").trim();
    }

    // Book ile ilişki (opsiyonel, OneToMany tarafı)
    //@OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //private Set<Book> books = new HashSet<>();
}
