package com.soupulsar.infrastructure.persistence.repository;

import com.soupulsar.domain.model.enums.ReviewStatus;
import com.soupulsar.infrastructure.persistence.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {

    List<ReviewEntity> findBySpecialistIdAndStatusOrderByCreatedAtDesc(UUID specialistId, ReviewStatus status);
    boolean existsByClientIdAndSpecialistIdAndStatus(UUID clientId, UUID specialistId, ReviewStatus status);
    @org.springframework.data.jpa.repository.Query("select r.specialistId, avg(r.rating) from ReviewEntity r where r.status = :status and r.specialistId in :ids group by r.specialistId")
    List<Object[]> averageRatings(@org.springframework.data.repository.query.Param("ids") List<UUID> ids, @org.springframework.data.repository.query.Param("status") ReviewStatus status);
}
