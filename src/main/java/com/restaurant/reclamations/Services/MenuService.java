package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> getAllMenus();          // Récupérer tous les menus
    Menu getMenuById(Long id);


}
