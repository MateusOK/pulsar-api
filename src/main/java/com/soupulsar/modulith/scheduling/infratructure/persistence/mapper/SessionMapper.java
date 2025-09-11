package com.soupulsar.modulith.scheduling.infratructure.persistence.mapper;

import com.soupulsar.modulith.scheduling.domain.model.Session;
import com.soupulsar.modulith.scheduling.infratructure.persistence.entity.SessionEntity;
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
        return entity;
    }

    public static Session toModel(SessionEntity entity) {
        return Session.restore(entity.getSessionId(),
                entity.getSpecialistId(),
                entity.getClientId(),
                entity.getStartAt(),
                entity.getEndAt(),
                entity.getStatus());
    }

}
