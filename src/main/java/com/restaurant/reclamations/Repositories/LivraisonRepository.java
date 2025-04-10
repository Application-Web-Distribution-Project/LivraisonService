package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.Livraison;
import com.restaurant.reclamations.Entities.Status;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    List<Livraison> findByStatus(Status status);
    List<Livraison> findByDateLivraisonBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}


