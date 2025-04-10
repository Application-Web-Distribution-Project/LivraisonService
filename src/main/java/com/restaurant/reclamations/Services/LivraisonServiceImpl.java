package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Clients.CommandeClient;
import com.restaurant.reclamations.Clients.UserClient;
import com.restaurant.reclamations.DTO.LivraisonDTO;
import com.restaurant.reclamations.Entities.Livraison;
import com.restaurant.reclamations.Entities.Status;
import com.restaurant.reclamations.Entities.TrackingInfoDTO;
import com.restaurant.reclamations.Repositories.LivraisonRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

// Add these imports
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivraisonServiceImpl implements LivraisonService {

    private final UserClient userClient;
    private final CommandeClient commandeClient;
    private final LivraisonRepository livraisonRepository;

    @Override
    public LivraisonDTO createLivraison(LivraisonDTO livraisonDTO) {
        System.out.println("📩 Nouvelle livraison reçue : " + livraisonDTO);

        if (livraisonDTO.getCommandeId() == 0) {
            throw new RuntimeException("CommandeId invalide !");
        }

        // Sauvegarde en base de données
        Livraison livraison = new Livraison();

        livraison.setLivreurId((long) livraisonDTO.getLivreurId());
        livraison.setCommandeId(livraisonDTO.getCommandeId());
        livraison.setStatus(livraisonDTO.getStatus() != null ? livraisonDTO.getStatus() : Status.EN_ATTENTE);
        livraison.setAdresseLivraison(livraisonDTO.getAdresseLivraison());
        livraison.setDateHeureCommande(livraisonDTO.getDateHeureCommande() != null ? livraisonDTO.getDateHeureCommande() : LocalDateTime.now());
        livraison.setLatitude(livraisonDTO.getLatitude());
        livraison.setLongitude(livraisonDTO.getLongitude());
        livraison.setConfirmeParClient(livraisonDTO.getConfirmeParClient());
        livraison.setAnnulee(livraisonDTO.getAnnulee());
        livraison.setRaisonAnnulation(livraisonDTO.getRaisonAnnulation());
        livraison.setDateLivraison(livraisonDTO.getDateLivraison());

        Livraison savedLivraison = livraisonRepository.save(livraison);

        return new LivraisonDTO(savedLivraison);
    }

    @Override
    public LivraisonDTO updateLivraisonStatus(int id, Status status) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée avec ID: " + id));

        livraison.setStatus(status);
        livraison = livraisonRepository.save(livraison);

        return new LivraisonDTO(livraison);
    }

    @Override
    public List<LivraisonDTO> getAllLivraisons() {
        return livraisonRepository.findAll().stream()
                .map(LivraisonDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public LivraisonDTO getLivraisonById(int id) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée avec ID: " + id));

        LivraisonDTO dto = new LivraisonDTO(livraison);

        return dto;
    }

    @Override
    public List<LivraisonDTO> getLivraisonsByStatus(Status status) {
        return livraisonRepository.findByStatus(status).stream()
                .map(LivraisonDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLivraison(int id) {
        if (livraisonRepository.existsById((long) id)) {
            livraisonRepository.deleteById((long) id);
        }
    }
    @Override
    public void updatePosition(int id, double lat, double lng) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));
        livraison.setLatitude(lat);
        livraison.setLongitude(lng);
        livraisonRepository.save(livraison);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon de la Terre en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance en km
    }

    private String calculateEstimatedArrival(Livraison livraison) {
        double latitude = livraison.getLatitude();
        double longitude = livraison.getLongitude();
        String adresseLivraison = livraison.getAdresseLivraison();

        // Simulation d’un point fixe pour l’adresse du client (ex: latitude/longitude du restaurant)
        double destinationLat = 36.8000; // Exemple : Tunis
        double destinationLng = 10.1800;

        double distance = haversine(latitude, longitude, destinationLat, destinationLng);

        // Suppose que le livreur roule à 40 km/h
        double estimatedTimeInHours = distance / 40.0;
        int estimatedMinutes = (int) (estimatedTimeInHours * 60);

        return estimatedMinutes + " minutes";
    }

    public TrackingInfoDTO getTrackingInfo(int id) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        return new TrackingInfoDTO(
                livraison.getLatitude(),
                livraison.getLongitude(),
                livraison.getStatus(),
                calculateEstimatedArrival(livraison)
        );
    }

    private LivraisonDTO mapToDTO(Livraison livraison) {
        LivraisonDTO dto = new LivraisonDTO();
        dto.setId(livraison.getId());
        dto.setCommandeId(livraison.getCommandeId());
        dto.setAdresseLivraison(livraison.getAdresseLivraison());
        dto.setStatus(livraison.getStatus());
        dto.setLivreurId(Math.toIntExact(livraison.getLivreurId()));
        dto.setLatitude(livraison.getLatitude());
        dto.setLongitude(livraison.getLongitude());
        dto.setDateLivraison(livraison.getDateLivraison());
        return dto;
    }

    @Override
    public LivraisonDTO assignLivreur(int id, Long livreurId) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setLivreurId((long) Math.toIntExact(livreurId));
        livraisonRepository.save(livraison);
        return mapToDTO(livraison);
    }

    @Override
    public List<LivraisonDTO> getLivraisonsBetweenDates(LocalDate start, LocalDate end) {
        return livraisonRepository
                .findByDateLivraisonBetween(start.atStartOfDay(), end.atTime(23, 59))
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void confirmerReception(int id) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setConfirmeParClient(true);
        livraisonRepository.save(livraison);
    }

    @Override
    public LivraisonDTO annulerLivraison(int id, String raison) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setAnnulee(true);
        livraison.setRaisonAnnulation(raison);
        livraison.setStatus(Status.ANNULEE); // Si tu as ce statut
        livraisonRepository.save(livraison);
        return mapToDTO(livraison);
    }


}
