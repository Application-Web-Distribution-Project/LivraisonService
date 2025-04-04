package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.DTO.ReclamationDTO;
import com.restaurant.reclamations.Entities.StatusReclamation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReclamationService {
    ReclamationDTO createReclamation(ReclamationDTO reclamationDTO);
    List<ReclamationDTO> getAllReclamations();
    ReclamationDTO getReclamationById(Long id);
    void deleteReclamation(Long id);
    
    // Recherche avancée avec pagination
    Page<ReclamationDTO> searchReclamations(String keyword, StatusReclamation status, 
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Mise à jour du statut avec historique
    ReclamationDTO updateStatus(Long id, StatusReclamation newStatus, String comment);
    
    // Statistiques des réclamations
    Map<StatusReclamation, Long> getReclamationStats();
    
    // Notifications
    void notifyUserStatusChange(Long reclamationId);
}
