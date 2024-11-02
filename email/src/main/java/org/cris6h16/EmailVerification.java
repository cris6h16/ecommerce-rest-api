package org.cris6h16;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "email_verification")
@NoArgsConstructor
public class EmailVerification {
    public static final int EXP_MIN = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = UserEntity.EMAIL_LENGTH)
    private String email;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        this.expiresAt = this.createdAt.plusMinutes(EXP_MIN).truncatedTo(ChronoUnit.MILLIS);
    }

    @PreUpdate
    public void onUpdate() {
        this.createdAt = this.createdAt.truncatedTo(ChronoUnit.MILLIS);
        this.expiresAt = this.expiresAt.truncatedTo(ChronoUnit.MILLIS);
    }

    public EmailVerification(Long id, String email, String code) {
        this.id = id;
        this.email = email;
        this.code = code;
    }
}
