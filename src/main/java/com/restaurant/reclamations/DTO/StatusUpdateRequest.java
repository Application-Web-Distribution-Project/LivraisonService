package com.restaurant.reclamations.DTO;

import com.restaurant.reclamations.Entities.StatusReclamation;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StatusUpdateRequest {
    private StatusReclamation newStatus;
    private String comment;
}
