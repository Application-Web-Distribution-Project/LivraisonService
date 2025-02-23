package com.restaurant.reclamations.Controllers;

import com.restaurant.reclamations.Entities.Menu;
import com.restaurant.reclamations.Services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // Récupérer tous les menus
    @GetMapping("/all")
    public ResponseEntity<List<Menu>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    // Récupérer un menu par son ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(menuService.getMenuById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
