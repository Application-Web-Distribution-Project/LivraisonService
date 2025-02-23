package com.restaurant.reclamations.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false) // Clé étrangère vers Commande
    Commande commande;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère vers User
    User user;

    String description;

    @Enumerated(EnumType.STRING)
    StatusReclamation status = StatusReclamation.EN_ATTENTE;

    LocalDateTime dateCreation = LocalDateTime.now();
    LocalDateTime dateResolution;
}
