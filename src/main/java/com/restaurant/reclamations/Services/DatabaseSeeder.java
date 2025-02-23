package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Menu;
import com.restaurant.reclamations.Repositories.MenuRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final MenuRepository menuRepository;

    public DatabaseSeeder(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(String... args) {
        if (menuRepository.count() == 0) {
            List<Menu> menus = List.of(
                    new Menu(null, "Pizza Margherita", "Tomate, mozzarella, basilic", 12.99, null),
                    new Menu(null, "Pâtes Carbonara", "Pâtes, œuf, pancetta, parmesan", 10.50, null),
                    new Menu(null, "Burger Classique", "Steak, cheddar, salade, tomate", 8.99, null),
                    new Menu(null, "Salade César", "Poulet, parmesan, croutons, sauce César", 9.99, null)
            );
            menuRepository.saveAll(menus);
        }
    }
}
