package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.DTO.LivraisonDTO;
import java.util.List;

import com.restaurant.reclamations.Entities.Status;

public interface LivraisonService {
    LivraisonDTO createLivraison(LivraisonDTO livraisonDTO);
    LivraisonDTO updateLivraisonStatus(int id, Status status);
    List<LivraisonDTO> getAllLivraisons();
    LivraisonDTO getLivraisonById(int id);
    List<LivraisonDTO> getLivraisonsByStatus(Status status);
    void deleteLivraison(int id);
}
