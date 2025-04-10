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
    private Double latitude; // Pour le suivi en temps réel
    private Double longitude;
    private Boolean confirmeParClient;
    private Boolean annulee;
    private String raisonAnnulation;
    private LocalDateTime dateLivraison;

    // Constructeur pour convertir une entité Reclamation en DTO
    public LivraisonDTO(Livraison livraison) {
        this.id = livraison.getId();
        this.commandeId = livraison.getCommandeId();
        this.livreurId = Math.toIntExact(livraison.getLivreurId());
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
