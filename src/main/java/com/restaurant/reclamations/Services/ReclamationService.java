package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.DTO.ReclamationDTO;
import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;

import java.util.List;
import java.util.Optional;

public interface ReclamationService {
    ReclamationDTO createReclamation(ReclamationDTO reclamationDTO);
    List<ReclamationDTO> getAllReclamations();
    ReclamationDTO getReclamationById(Long id);
    void deleteReclamation(Long id);
}
