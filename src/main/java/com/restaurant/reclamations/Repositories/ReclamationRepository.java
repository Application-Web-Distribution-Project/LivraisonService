package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByStatus(StatusReclamation status);
}


