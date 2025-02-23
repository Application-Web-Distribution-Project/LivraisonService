package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
}
