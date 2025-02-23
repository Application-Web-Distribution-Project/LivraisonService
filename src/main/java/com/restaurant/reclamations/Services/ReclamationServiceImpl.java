package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Reclamation;
import com.restaurant.reclamations.Entities.StatusReclamation;
import com.restaurant.reclamations.Repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationServiceImpl implements ReclamationService {
    @Autowired
    private ReclamationRepository reclamationRepository;

    @Override
    public Reclamation addReclamation(Reclamation reclamation) {
        if (reclamation.getCommande() == null || reclamation.getCommande().getId() == null) {
            throw new IllegalArgumentException("Une commande valide est requise pour la réclamation.");
        }
        return reclamationRepository.save(reclamation);
    }


    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    @Override
    public Reclamation updateReclamation(Long id, StatusReclamation newStatus) {
        Reclamation rec = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
        rec.setStatus(newStatus);
        if (newStatus == StatusReclamation.RESOLU) {
            rec.setDateResolution(LocalDateTime.now());
        }
        return reclamationRepository.save(rec);
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
}
