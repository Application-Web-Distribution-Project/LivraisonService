package com.restaurant.reclamations.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    @JsonProperty("name") // Associe "name" du JSON à "nom" dans l'objet
    private String nom;

    private String email;
}
