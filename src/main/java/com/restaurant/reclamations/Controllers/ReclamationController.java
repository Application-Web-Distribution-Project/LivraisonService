package com.restaurant.reclamations.Controllers;

import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;
import com.restaurant.reclamations.Services.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamations")
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;  // Injection de l'interface


    // Ajouter une réclamation
    @PostMapping("/add")
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }

    // Récupérer toutes les réclamations
    @GetMapping("/all")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    // Récupérer une réclamation par son ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable Long id) {
        return reclamationService.getReclamationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Mettre à jour le statut d'une réclamation
    @PutMapping("/update/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable Long id, @RequestParam StatusReclamation status) {
        return ResponseEntity.ok(reclamationService.updateReclamation(id, status));
    }

    // Supprimer une réclamation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }
}
