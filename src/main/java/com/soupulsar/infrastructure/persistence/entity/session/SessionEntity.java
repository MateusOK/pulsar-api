package com.soupulsar.infrastructure.persistence.entity.session;

import com.soupulsar.domain.model.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Setter
@Getter
public class SessionEntity {


    @Id
    @Column(nullable = false, unique = true)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID specialistId;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

}