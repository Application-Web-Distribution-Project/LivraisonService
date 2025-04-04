package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
}
