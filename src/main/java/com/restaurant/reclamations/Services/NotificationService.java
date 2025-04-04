package com.restaurant.reclamations.Services;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
@Slf4j
public class NotificationService {
    
    private final JavaMailSenderImpl mailSender;
    private final EmailTemplateService emailTemplateService;

    public NotificationService(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port}") int port,
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password,
            EmailTemplateService emailTemplateService) {
        
        this.mailSender = new JavaMailSenderImpl();
        this.emailTemplateService = emailTemplateService;
        
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.connectiontimeout", "5000");
    }
    
    public void sendStatusUpdateEmail(String to, Long reclamationId, String status, String clientName) {
        try {
            log.info("📧 Préparation email pour réclamation #{}", reclamationId);
            
            var template = emailTemplateService.getStatusUpdateTemplate(reclamationId, status, clientName);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Marwaniwael88@gmail.com");
            message.setTo(to);
            message.setSubject(template.subject());
            message.setText(template.message());
            
            log.info("📤 Envoi en cours...");
            mailSender.send(message);
            log.info("✅ Email envoyé avec succès!");
            
        } catch (Exception e) {
            log.error("❌ Erreur d'envoi: {}", e.getMessage());
            throw new RuntimeException("Échec de l'envoi", e);
        }
    }
}
