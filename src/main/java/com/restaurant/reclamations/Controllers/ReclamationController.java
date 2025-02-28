package com.restaurant.reclamations.Controllers;

import com.restaurant.reclamations.DTO.ReclamationDTO;
import com.restaurant.reclamations.Services.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reclamations")
@RequiredArgsConstructor
public class ReclamationController {

    private final ReclamationService reclamationService;

    // ✅ CORS appliqué uniquement ici
    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<ReclamationDTO> createReclamation(@RequestBody ReclamationDTO reclamationDTO) {
        System.out.println("📥 Réclamation reçue : " + reclamationDTO);
        ReclamationDTO createdReclamation = reclamationService.createReclamation(reclamationDTO);

        // ✅ Ajout des headers CORS dans la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(createdReclamation);
    }

    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping("/{id}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long id) {
        System.out.println("🔍 Requête reçue avec ID : " + id);
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReclamation(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
    }
}
