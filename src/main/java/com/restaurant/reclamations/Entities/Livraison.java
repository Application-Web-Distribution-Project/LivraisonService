package com.restaurant.reclamations.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.restaurant.reclamations.DTO.CommandeDTO;
import com.restaurant.reclamations.DTO.UserDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String commandeId;

    // ID du client qui a passé la commande
    String userId;
    
    // ID du livreur assigné à cette livraison
    String livreurId;
    
    @Transient
    @JsonIgnore
    UserDTO client; // Client qui a passé la commande
    
    @Transient
    @JsonIgnore
    UserDTO livreur; // Livreur assigné
    
    @Transient
    @JsonIgnore
    CommandeDTO commande; // Récupéré via FeignClient
    
    private String adresseLivraison;

    @Enumerated(EnumType.STRING)
    Status status;

    private LocalDateTime dateHeureCommande;

    @Column
    private Double latitude;
    @Column//Pour le suivi en temps réel
    private Double longitude;
    @Column
    private Boolean confirmeParClient;
    @Column
    private Boolean annulee = false;

    private String raisonAnnulation;
    private LocalDateTime dateLivraison;
}
