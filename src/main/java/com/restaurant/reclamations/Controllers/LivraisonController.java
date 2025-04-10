package com.restaurant.reclamations.Controllers;


import com.restaurant.reclamations.DTO.LivraisonDTO;
import com.restaurant.reclamations.Entities.Status;
import com.restaurant.reclamations.Services.LivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livraisons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LivraisonController {
    public final LivraisonService livraisonService;
    // ✅ Créer une livraison à partir d'une requête du microservice Commande
    @PostMapping("/create")
    public ResponseEntity<LivraisonDTO> creerLivraison(@RequestParam int commandeId, @RequestParam String adresseLivraison) {
        LivraisonDTO livraisonDTO = new LivraisonDTO();
        livraisonDTO.setCommandeId(commandeId);
        livraisonDTO.setAdresseLivraison(adresseLivraison);

        LivraisonDTO savedLivraison = livraisonService.createLivraison(livraisonDTO);
        return ResponseEntity.ok(savedLivraison);
    }

    // ✅ Mettre à jour le statut d'une livraison
    @PutMapping("/{id}/statut")
    public ResponseEntity<LivraisonDTO> mettreAJourStatut(@PathVariable int id, @RequestParam Status statut) {
        return ResponseEntity.ok(livraisonService.updateLivraisonStatus(id, statut));
    }

    // ✅ Lister toutes les livraisons
    @GetMapping
    public List<LivraisonDTO> getAllLivraisons() {
        return livraisonService.getAllLivraisons();
    }

    // ✅ Récupérer une livraison par ID
    @GetMapping("/{id}")
    public ResponseEntity<LivraisonDTO> getLivraisonById(@PathVariable int id) {
        LivraisonDTO livraison = livraisonService.getLivraisonById(id);
        return ResponseEntity.ok(livraison);
    }

    // ✅ Récupérer les livraisons par statut
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LivraisonDTO>> getLivraisonsByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(livraisonService.getLivraisonsByStatus(status));
    }

    // ✅ Supprimer une livraison
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable int id) {
        livraisonService.deleteLivraison(id);
        return ResponseEntity.noContent().build();
    }
}
