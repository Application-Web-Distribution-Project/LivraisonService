package com.restaurant.reclamations.Services;

import com.restaurant.reclamations.Entities.Commande;
import com.restaurant.reclamations.Repositories.CommandeRepository;
import com.restaurant.reclamations.Repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommandeServiceImpl implements CommandeService {
    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Commande addCommande(Commande commande) {
        if (commande.getMenu() == null || commande.getMenu().getId() == null) {
            throw new IllegalArgumentException("Un menu valide est requis pour la commande.");
        }

        // Vérifier si le menu existe en base de données
        if (!menuRepository.existsById(commande.getMenu().getId())) {
            throw new IllegalArgumentException("Le menu sélectionné n'existe pas.");
        }

        commande.setDateCommande(LocalDateTime.now());
        return commandeRepository.save(commande);
    }


    @Override
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @Override
    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    @Override
    public List<Commande> getCommandesByUserId(Long userId) {
        return commandeRepository.findByUserId(userId);
    }

    @Override
    public Commande updateCommande(Long id, Commande commandeDetails) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        commande.setDateCommande(commandeDetails.getDateCommande());
        commande.setUser(commandeDetails.getUser());

        return commandeRepository.save(commande);
    }

    @Override
    public void deleteCommande(Long id) {
        commandeRepository.deleteById(id);
    }
}
