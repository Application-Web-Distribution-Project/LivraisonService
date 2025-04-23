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
    private String userId; // ID du client qui a passé la commande
    private String livreurId; // ID du livreur assigné à cette livraison
    private String commandeId;
    private Status status;
    private String adresseLivraison;
    private LocalDateTime dateHeureCommande;
    private Double latitude; // Pour le suivi en temps réel
    private Double longitude;
    private Boolean confirmeParClient;
    private Boolean annulee;
    private String raisonAnnulation;
    private LocalDateTime dateLivraison;

    private UserDTO client; // Client qui a passé la commande (récupéré via FeignClient)
    private UserDTO livreur; // Livreur assigné (récupéré via FeignClient)
    private CommandeDTO commande; // Récupéré via FeignClient
    
    // Constructeur pour convertir une entité Livraison en DTO
    public LivraisonDTO(Livraison livraison) {
        this.id = livraison.getId();
        this.commandeId = livraison.getCommandeId();
        this.userId = livraison.getUserId();
        this.livreurId = livraison.getLivreurId();
        this.status = livraison.getStatus();
        this.adresseLivraison = livraison.getAdresseLivraison();
        this.dateHeureCommande = livraison.getDateHeureCommande();
        this.latitude = livraison.getLatitude();
        this.longitude = livraison.getLongitude();
        this.confirmeParClient = livraison.getConfirmeParClient();
        this.annulee = livraison.getAnnulee();
        this.raisonAnnulation = livraison.getRaisonAnnulation();
        this.dateLivraison = livraison.getDateLivraison();
    }
}
