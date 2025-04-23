package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Clients.CommandeClient;
import com.restaurant.reclamations.Clients.UserClient;
import com.restaurant.reclamations.DTO.CommandeDTO;
import com.restaurant.reclamations.DTO.LivraisonDTO;
import com.restaurant.reclamations.DTO.UserDTO;
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

import feign.FeignException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
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
        log.info("📩 Nouvelle livraison reçue : {}", livraisonDTO);

        if (livraisonDTO.getCommandeId() == null || livraisonDTO.getCommandeId().isEmpty()) {
            throw new RuntimeException("CommandeId invalide !");
        }

        // Sauvegarde en base de données
        Livraison livraison = new Livraison();

        // Assignation des détails de la livraison
        livraison.setCommandeId(livraisonDTO.getCommandeId());
        livraison.setUserId(livraisonDTO.getUserId());
        livraison.setLivreurId(livraisonDTO.getLivreurId()); // Peut être null à la création
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

        return enrichLivraisonWithDetails(new LivraisonDTO(savedLivraison));
    }

    @Override
    public LivraisonDTO updateLivraisonStatus(int id, Status status) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée avec ID: " + id));

        livraison.setStatus(status);
        livraison = livraisonRepository.save(livraison);

        return enrichLivraisonWithDetails(new LivraisonDTO(livraison));
    }

    @Override
    public List<LivraisonDTO> getAllLivraisons() {
        return livraisonRepository.findAll().stream()
                .map(LivraisonDTO::new)
                .map(this::enrichLivraisonWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    public LivraisonDTO getLivraisonById(int id) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée avec ID: " + id));

        LivraisonDTO dto = new LivraisonDTO(livraison);
        return enrichLivraisonWithDetails(dto);
    }

    /**
     * Enrichit un DTO de livraison avec les informations client, livreur et commande
     */
    private LivraisonDTO enrichLivraisonWithDetails(LivraisonDTO dto) {
        try {
            // Récupérer les détails du client (personne qui a passé la commande)
            if (dto.getUserId() != null && !dto.getUserId().isEmpty()) {
                log.info("🔍 [Feign] Récupération des infos du client avec ID: {}", dto.getUserId());
                try {
                    UserDTO client = userClient.getUserById(dto.getUserId());
                    log.info("✅ [Feign] Client récupéré : {}", client);
                    dto.setClient(client);
                } catch (FeignException e) {
                    log.error("❌ [Feign] Erreur lors de la récupération du client: {}", e.getMessage());
                }
            }
            
            // Récupérer les détails du livreur si un livreur est assigné
            if (dto.getLivreurId() != null && !dto.getLivreurId().isEmpty()) {
                log.info("🔍 [Feign] Récupération des infos du livreur avec ID: {}", dto.getLivreurId());
                try {
                    UserDTO livreur = userClient.getUserById(dto.getLivreurId());
                    log.info("✅ [Feign] Livreur récupéré : {}", livreur);
                    
                    // Vérifier que l'utilisateur récupéré a bien le rôle de livreur
                    if (livreur != null && "livreur".equalsIgnoreCase(livreur.getRole())) {
                        dto.setLivreur(livreur);
                    } else {
                        log.warn("⚠️ L'utilisateur avec ID {} n'a pas le rôle de livreur", dto.getLivreurId());
                    }
                } catch (FeignException e) {
                    log.error("❌ [Feign] Erreur lors de la récupération du livreur: {}", e.getMessage());
                }
            }
            
            // Récupérer les détails de la commande
            if (dto.getCommandeId() != null && !dto.getCommandeId().isEmpty()) {
                log.info("🔍 [Feign] Récupération des infos commande avec ID: {}", dto.getCommandeId());
                
                String authToken = extractAuthorizationToken();
                try {
                    CommandeDTO commande;
                    if (authToken != null) {
                        log.debug("Token d'autorisation trouvé, transfert au service commandes");
                        commande = commandeClient.getCommandeByIdWithAuth(
                            dto.getCommandeId(),
                            "livraisons-service",
                            "Bearer " + authToken
                        );
                    } else {
                        log.debug("Aucun token d'autorisation disponible, utilisation de l'ID client uniquement");
                        commande = commandeClient.getCommandeByIdBasic(
                            dto.getCommandeId(),
                            "livraisons-service"
                        );
                    }
                    log.info("✅ [Feign] Commande récupérée avec ID: {}", dto.getCommandeId());
                    dto.setCommande(commande);
                } catch (FeignException e) {
                    log.error("❌ [Feign] Erreur lors de la récupération de la commande: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'enrichissement des données de livraison: {}", e.getMessage());
        }
        
        return dto;
    }
    
    /**
     * Extrait le token JWT de l'en-tête Authorization de la requête actuelle
     * @return le token JWT sans le préfixe "Bearer ", ou null si non trouvé
     */
    private String extractAuthorizationToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader("Authorization");
                
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    return authHeader.substring(7); // Supprime le préfixe "Bearer "
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction du token d'autorisation: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<LivraisonDTO> getLivraisonsByStatus(Status status) {
        return livraisonRepository.findByStatus(status).stream()
                .map(LivraisonDTO::new)
                .map(this::enrichLivraisonWithDetails)
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
        double dLon = Math.toRadians(lat2 - lon1);
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

        // Simulation d'un point fixe pour l'adresse du client (ex: latitude/longitude du restaurant)
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
        LivraisonDTO dto = new LivraisonDTO(livraison);
        return enrichLivraisonWithDetails(dto);
    }

    @Override
    public LivraisonDTO assignLivreur(int id, Long livreurId) {
        Livraison livraison = livraisonRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        if (livreurId == null) {
            throw new RuntimeException("ID du livreur invalide");
        }
        
        // Convertir le Long en String pour MongoDB
        String livreurIdStr = livreurId.toString();
        
        try {
            // Vérifier que l'utilisateur existe et a le rôle de livreur
            UserDTO livreur = userClient.getUserById(livreurIdStr);
            
            if (livreur == null) {
                throw new RuntimeException("Livreur introuvable avec ID: " + livreurIdStr);
            }
            
            if (!"livreur".equalsIgnoreCase(livreur.getRole())) {
                throw new RuntimeException("L'utilisateur avec ID " + livreurIdStr + " n'a pas le rôle de livreur");
            }
            
            // Assigner le livreur à la livraison
            livraison.setLivreurId(livreurIdStr);
            livraisonRepository.save(livraison);
            
            log.info("✅ Livreur avec ID {} assigné à la livraison {}", livreurIdStr, id);
            return mapToDTO(livraison);
            
        } catch (FeignException e) {
            log.error("❌ Erreur lors de la vérification du livreur: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'assignation du livreur: " + e.getMessage());
        }
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
