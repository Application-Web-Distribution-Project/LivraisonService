package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;

import java.util.List;
import java.util.Optional;

public interface ReclamationService {
    Reclamation addReclamation(Reclamation reclamation);
    List<Reclamation> getAllReclamations();
    Optional<Reclamation> getReclamationById(Long id);
    Reclamation updateReclamation(Long id, StatusReclamation newStatus);
    void deleteReclamation(Long id);
}
