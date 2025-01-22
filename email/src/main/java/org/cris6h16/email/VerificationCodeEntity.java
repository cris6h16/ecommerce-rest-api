package org.cris6h16.email;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "email_verification", indexes = {
        @Index(name = "email_verification_email_idx", columnList = "email"),
})
@Getter
class VerificationCodeEntity {
    public static final int EXP_MIN = 15;
    public static final int CODE_LENGTH = 6;
    public static final int EMAIL_MAX_LENGTH = 255;
    static final int ACTION_TYPE_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = CODE_LENGTH)
    private String code;

    @Column(nullable = false, length = ACTION_TYPE_MAX_LENGTH, name = "action_type")
    private String actionType;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "expires_at")
    private LocalDateTime expiresAt;


    protected VerificationCodeEntity() {

    }


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        this.expiresAt = this.createdAt.plusMinutes(EXP_MIN).truncatedTo(ChronoUnit.MILLIS);
        this.used = false;
    }

    VerificationCodeEntity(String email, String code, String actionType) {
        this.email = email;
        this.code = code;
        this.actionType = actionType;
    }
}
