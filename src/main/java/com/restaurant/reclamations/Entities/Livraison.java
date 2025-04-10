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
    int id;

    int commandeId;

    int livreurId;

    String adresseLivraison;

    @Enumerated(EnumType.STRING)
    Status status;

    LocalDateTime dateHeureCommande;
}
