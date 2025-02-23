package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Livraison;

import java.util.List;
import java.util.Optional;

public interface LivraisonService {

    List<Livraison> getAllLivraisons();      // Récupérer toutes les livraisons

    Optional<Livraison> getLivraisonById(Long id); // Récupérer une livraison par ID

    Livraison createLivraison(Livraison livraison); // Ajouter une nouvelle livraison

    void deleteLivraison(Long id); // Supprimer une livraison
}
