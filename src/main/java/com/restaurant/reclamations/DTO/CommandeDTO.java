package com.restaurant.reclamations.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandeDTO {
    private Long id;
    private String reference;
    private double montant;
    private Long userId;
}
