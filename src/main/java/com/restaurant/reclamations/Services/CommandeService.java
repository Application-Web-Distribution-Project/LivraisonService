package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Commande;
import java.util.List;
import java.util.Optional;

public interface CommandeService {
    Commande addCommande(Commande commande);
    List<Commande> getAllCommandes();
    Optional<Commande> getCommandeById(Long id);
    List<Commande> getCommandesByUserId(Long userId);
    Commande updateCommande(Long id, Commande commande);
    void deleteCommande(Long id);
}
