package com.restaurant.reclamations.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Reclamation reclamation;
    
    @Enumerated(EnumType.STRING)
    private StatusReclamation oldStatus;
    
    @Enumerated(EnumType.STRING)
    private StatusReclamation newStatus;
    
    private String comment;
    private LocalDateTime changeDate = LocalDateTime.now();
}
