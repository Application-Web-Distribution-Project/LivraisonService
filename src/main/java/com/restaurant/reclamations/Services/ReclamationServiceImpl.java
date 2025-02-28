package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.DTO.ReclamationDTO;
import com.restaurant.reclamations.DTO.UserDTO;
import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Repositories.ReclamationRepository;
import com.restaurant.reclamations.Clients.UserClient;
import com.restaurant.reclamations.Clients.CommandeClient;
import feign.FeignException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserClient userClient;
    private final CommandeClient commandeClient;

    @Override
    public ReclamationDTO createReclamation(ReclamationDTO reclamationDTO) {
        System.out.println("📩 Nouvelle réclamation reçue : " + reclamationDTO);

        if (reclamationDTO.getUserId() == null || reclamationDTO.getUserId() == 0) {
            throw new RuntimeException("UserId invalide !");
        }
        if (reclamationDTO.getCommandeId() == null || reclamationDTO.getCommandeId() == 0) {
            throw new RuntimeException("CommandeId invalide !");
        }

        // Sauvegarde en base de données
        Reclamation reclamation = new Reclamation();
        reclamation.setUserId(reclamationDTO.getUserId());
        reclamation.setCommandeId(reclamationDTO.getCommandeId());
        reclamation.setDescription(reclamationDTO.getDescription());
        reclamation.setStatus(reclamationDTO.getStatus());

        reclamation = reclamationRepository.save(reclamation);

        // 🔍 Récupération de l'utilisateur depuis le mock server
        try {
            UserDTO user = userClient.getUserById(reclamationDTO.getUserId());

        } catch (FeignException e) {
            System.err.println("❌ [Feign] Erreur lors de l'appel à UserClient : " + e.getMessage());
        }

        return new ReclamationDTO(reclamation);
    }

    @Override
    public List<ReclamationDTO> getAllReclamations() {
        return reclamationRepository.findAll().stream()
                .map(ReclamationDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ReclamationDTO getReclamationById(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        ReclamationDTO dto = new ReclamationDTO(reclamation);

        try {
            System.out.println("🔍 [Feign] Récupération des infos user...");
            UserDTO user = userClient.getUserById(reclamation.getUserId());
            System.out.println("✅ [Feign] Utilisateur récupéré : " + user);
            dto.setUser(user);
        } catch (FeignException e) {
            System.err.println("❌ [Feign] Erreur lors de l'appel à UserClient : " + e.getMessage());
            dto.setUser(new UserDTO()); // Définit un objet vide pour éviter une erreur
        }

        return dto;
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
}
