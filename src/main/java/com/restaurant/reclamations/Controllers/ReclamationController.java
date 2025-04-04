package com.restaurant.reclamations.Controllers;

import com.restaurant.reclamations.DTO.ReclamationDTO;
import com.restaurant.reclamations.Services.ReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.Map;
import com.restaurant.reclamations.Entities.StatusReclamation;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/reclamations")  
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReclamationController {

    private final ReclamationService reclamationService;

    @PostMapping
    public ResponseEntity<ReclamationDTO> createReclamation(
            @RequestBody ReclamationDTO reclamationDTO) {
        try {
            System.out.println("📝 Creating reclamation: " + reclamationDTO);
            ReclamationDTO created = reclamationService.createReclamation(reclamationDTO);
            System.out.println("✅ Successfully created reclamation: " + created);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            System.err.println("❌ Creation error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        try {
            System.out.println("📋 Getting all reclamations");
            List<ReclamationDTO> reclamations = reclamationService.getAllReclamations();
            System.out.println("✅ Found " + reclamations.size() + " reclamations");
            return ResponseEntity.ok(reclamations);
        } catch (Exception e) {
            System.err.println("❌ Error getting reclamations: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamationDTO> getReclamationById(@PathVariable Long id) {
        System.out.println("🔍 Récupération réclamation ID: " + id);
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable Long id) {
        try {
            reclamationService.deleteReclamation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReclamationDTO>> searchReclamations(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) StatusReclamation status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Page<ReclamationDTO> results = reclamationService.searchReclamations(
                keyword, status, startDate, endDate, 
                PageRequest.of(page, size));
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.err.println("❌ Search error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReclamationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam("newStatus") StatusReclamation newStatus, // Changed from status to newStatus
            @RequestParam(required = false) String comment) {
        try {
            System.out.println("📝 Updating status for reclamation " + id + " to " + newStatus);
            ReclamationDTO updated = reclamationService.updateStatus(id, newStatus, comment);
            System.out.println("✅ Status updated successfully");
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("❌ Update status error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<StatusReclamation, Long>> getStats() {
        return ResponseEntity.ok(reclamationService.getReclamationStats());
    }
}