package com.restaurant.reclamations.Controllers;

import com.restaurant.reclamations.Entities.Livraison;
import com.restaurant.reclamations.Services.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livraisons")
public class LivraisonController {

    @Autowired
    private LivraisonService livraisonService;

    // Récupérer toutes les livraisons
    @GetMapping("/all")
    public ResponseEntity<List<Livraison>> getAllLivraisons() {
        return ResponseEntity.ok(livraisonService.getAllLivraisons());
    }

    // Récupérer une livraison par ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Livraison> getLivraisonById(@PathVariable Long id) {
        Optional<Livraison> livraison = livraisonService.getLivraisonById(id);
        return livraison.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Ajouter une nouvelle livraison
    @PostMapping("/add")
    public ResponseEntity<Livraison> addLivraison(@RequestBody Livraison livraison) {
        return ResponseEntity.ok(livraisonService.createLivraison(livraison));
    }

    // Supprimer une livraison
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        livraisonService.deleteLivraison(id);
        return ResponseEntity.noContent().build();
    }
}
