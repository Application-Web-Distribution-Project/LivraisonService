package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByStatus(StatusReclamation status);

    // Recherche avancée
    @Query("SELECT r FROM Reclamation r WHERE " +
           "(:keyword IS NULL OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:startDate IS NULL OR r.dateCreation >= :startDate) AND " +
           "(:endDate IS NULL OR r.dateCreation <= :endDate)")
    Page<Reclamation> searchReclamations(
        @Param("keyword") String keyword,
        @Param("status") StatusReclamation status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    // Statistiques
    @Query("SELECT r.status as status, COUNT(r) as count FROM Reclamation r GROUP BY r.status")
    List<Object[]> getReclamationStats();
}


