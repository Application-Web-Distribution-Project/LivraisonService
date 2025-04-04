package com.restaurant.reclamations.Services;

import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class EmailTemplateService {
    
    public EmailTemplate getStatusUpdateTemplate(Long reclamationId, String status, String clientName) {
        String emoji = switch (status) {
            case "EN_ATTENTE" -> "⏳";
            case "EN_COURS" -> "🔄";
            case "RESOLU" -> "✅";
            default -> "ℹ️";
        };

        String subject = String.format("%s Mise à jour de votre réclamation #%d - %s", emoji, reclamationId, status);
        String message = String.format("""
            Cher/Chère %s,
            
            Nous vous informons que votre réclamation #%d a été mise à jour vers le statut : %s
            
            %s
            
            %s
            
            Pour toute question, notre équipe support reste à votre disposition :
            📞 Téléphone : +216 XX XXX XXX
            📧 Email : support@legourmet.com
            
            Cordialement,
            L'équipe Le Gourmet
            """,
            clientName,
            reclamationId,
            status,
            getDateInfo(),
            getStatusSpecificMessage(status)
        );

        return new EmailTemplate(subject, message);
    }

    private String getStatusSpecificMessage(String status) {
        return switch (status) {
            case "EN_ATTENTE" -> """
                Notre équipe va examiner votre réclamation dans les plus brefs délais.
                Nous vous tiendrons informé de l'avancement.
                """;
            case "EN_COURS" -> """
                🔄 Votre réclamation est en cours de traitement
                - Un agent a été assigné à votre dossier
                - Nous mettons tout en œuvre pour résoudre rapidement votre problème
                - Vous serez notifié de tout changement
                """;
            case "RESOLU" -> """
                🎉 Votre réclamation a été traitée avec succès !
                
                Nous espérons que la solution apportée vous donne entière satisfaction.
                N'hésitez pas à nous donner votre avis sur la gestion de votre réclamation.
                """;
            default -> "Statut de la réclamation mis à jour.";
        };
    }

    private String getDateInfo() {
        return "Date de mise à jour : " + 
               LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));
    }
}

record EmailTemplate(String subject, String message) {}
