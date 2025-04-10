package com.restaurant.reclamations.DTO;

import com.restaurant.reclamations.Entities.Livraison;
import com.restaurant.reclamations.Entities.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LivraisonDTO {
    private int id;
    private int livreurId;
    private int commandeId;
    private Status status;
    private String adresseLivraison;
    private LocalDateTime dateHeureCommande;

    private UserDTO user; // Transient - Récupéré via FeignClient
    private CommandeDTO commande; // Transient - Récupéré via FeignClient

    // Constructeur pour convertir une entité Reclamation en DTO
    public LivraisonDTO(Livraison livraison) {
        this.id = livraison.getId();
        this.commandeId = livraison.getCommandeId();
        this.status = livraison.getStatus();
        this.adresseLivraison = livraison.getAdresseLivraison();
        this.dateHeureCommande = livraison.getDateHeureCommande();
    }
}
