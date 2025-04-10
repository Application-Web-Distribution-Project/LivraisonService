package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Clients.CommandeClient;
import com.restaurant.reclamations.Clients.UserClient;
import com.restaurant.reclamations.DTO.CommandeDTO;
import com.restaurant.reclamations.DTO.LivraisonDTO;
import com.restaurant.reclamations.DTO.UserDTO;
import com.restaurant.reclamations.Entities.Livraison;
import com.restaurant.reclamations.Entities.Status;
import com.restaurant.reclamations.Repositories.LivraisonRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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
        livraison.setCommandeId(livraisonDTO.getCommandeId());
        livraison.setAdresseLivraison(livraisonDTO.getAdresseLivraison());
        livraison.setStatus(Status.EN_ATTENTE);  // Statut initial
        livraison.setDateHeureCommande(LocalDateTime.now());  // Date de création
        livraison.setLivreurId(0);  // Livreurs non assignés initialement (0 ou null)

        livraison = livraisonRepository.save(livraison);

        return new LivraisonDTO(livraison);
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

        try {
            System.out.println("🔍 [Feign] Récupération des infos de la commande...");
            // Convert int to Long for the method call
            CommandeDTO commande = commandeClient.getCommandeById(Long.valueOf(livraison.getCommandeId()));
            System.out.println("✅ [Feign] Commande récupérée : " + commande);
            dto.setCommande(commande);

            // Si la commande contient un userID, on peut récupérer les infos du client
            try {
                // Assuming there's a getUserId method in CommandeDTO
                Long userId = commande.getUserId(); // Get the correct user ID field
                UserDTO user = userClient.getUserById(userId);
                System.out.println("✅ [Feign] Utilisateur récupéré : " + user);
                dto.setUser(user);
            } catch (FeignException e) {
                System.err.println("❌ [Feign] Erreur lors de l'appel à UserClient : " + e.getMessage());
                dto.setUser(new UserDTO()); // Définit un objet vide pour éviter une erreur
            }

        } catch (FeignException e) {
            System.err.println("❌ [Feign] Erreur lors de l'appel à CommandeClient : " + e.getMessage());
            dto.setCommande(new CommandeDTO()); // Définit un objet vide pour éviter une erreur
        }

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
}
