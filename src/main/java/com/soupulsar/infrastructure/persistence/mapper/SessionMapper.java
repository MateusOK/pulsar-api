package com.soupulsar.infrastructure.persistence.mapper;

import com.soupulsar.domain.model.session.Session;
import com.soupulsar.infrastructure.persistence.entity.session.SessionEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionMapper {

    public static SessionEntity toEntity(Session session) {
        SessionEntity entity = new SessionEntity();
        entity.setSessionId(session.getSessionId());
        entity.setSpecialistId(session.getSpecialistId());
        entity.setClientId(session.getClientId());
        entity.setStartAt(session.getStartAt());
        entity.setEndAt(session.getEndAt());
        entity.setStatus(session.getStatus());
        return entity;
    }

    public static Session toModel(SessionEntity entity) {
        return Session.builder()
                .sessionId(entity.getSessionId())
                .specialistId(entity.getSpecialistId())
                .clientId(entity.getClientId())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .status(entity.getStatus())
                .build();
    }

}
