package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.DTO.LivraisonDTO;

import java.time.LocalDate;
import java.util.List;

import com.restaurant.reclamations.Entities.Status;
import com.restaurant.reclamations.Entities.TrackingInfoDTO;

public interface LivraisonService {
    LivraisonDTO createLivraison(LivraisonDTO livraisonDTO);
    LivraisonDTO updateLivraisonStatus(int id, Status status);
    List<LivraisonDTO> getAllLivraisons();
    LivraisonDTO getLivraisonById(int id);
    List<LivraisonDTO> getLivraisonsByStatus(Status status);
    void deleteLivraison(int id);
    void updatePosition(int id, double lat, double lng);

    TrackingInfoDTO getTrackingInfo(int id);

    LivraisonDTO assignLivreur(int id, Long livreurId);

    List<LivraisonDTO> getLivraisonsBetweenDates(LocalDate start, LocalDate end);

    void confirmerReception(int id);

    LivraisonDTO annulerLivraison(int id, String raison);
}
