package com.restaurant.reclamations.Repositories;

import com.restaurant.reclamations.Entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
