package com.idb.microservicedemo.library.domain.entities.appuser;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;
    @Column(unique = true, nullable = false, length = 100)
    private String username; // IdentityUser.UserName karşılığı
    @Column(unique = true, nullable = false, length = 255)
    private String email;
    @Column(nullable = false)
    private String password; // IdentityUser.PasswordHash karşılığı
    private String phoneNumber;
    private boolean phoneNumberConfirmed;
    private boolean emailConfirmed;
    private String refreshToken;
    private OffsetDateTime refreshTokenExpires;
    private boolean lockoutEnabled;
    private OffsetDateTime lockoutEnd;
    private int accessFailedCount;
    private boolean twoFactorEnabled;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // --- UserDetails implementasyonu ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !lockoutEnabled || (lockoutEnd != null && lockoutEnd.isBefore(OffsetDateTime.now()));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
