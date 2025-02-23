package com.restaurant.reclamations.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String prenom;
    String nom;
    String email;
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore  // Empêche la sérialisation récursive

    List<Commande> commandes; // Un utilisateur peut avoir plusieurs commandes

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Reclamation> reclamations; // Un utilisateur peut avoir plusieurs réclamations
}
