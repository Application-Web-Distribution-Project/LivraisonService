package com.restaurant.reclamations.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime dateCommande = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère vers User
    @JsonIgnore  // Empêche la sérialisation récursive

    User user;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    List<Reclamation> reclamations;
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    Menu menu;
    @OneToOne(mappedBy = "commande", cascade = CascadeType.ALL)
    Livraison livraison;


}
