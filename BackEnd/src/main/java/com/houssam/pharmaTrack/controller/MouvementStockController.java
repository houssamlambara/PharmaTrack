package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.MouvementStockRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MouvementStockResponseDTO;
import com.houssam.pharmaTrack.enums.MovementType;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.MouvementStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mouvements-stock")
@RequiredArgsConstructor
@Tag(name = "Mouvements de Stock", description = "Traçabilité complète des entrées, sorties et ajustements de stock")
@SecurityRequirement(name = "bearerAuth")
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Créer un mouvement manuel",
               description = "Enregistre un mouvement de stock manuel (généralement pour ajustement). Les mouvements ENTREE et SORTIE sont créés automatiquement lors des réceptions et ventes.")
    public ResponseEntity<ApiResponse<MouvementStockResponseDTO>> create(
            @Valid @RequestBody MouvementStockRequestDTO requestDTO) {
        MouvementStockResponseDTO response = mouvementStockService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Mouvement de stock créé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Liste tous les mouvements", description = "Retourne l'historique complet de tous les mouvements de stock")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getAll() {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des mouvements de stock", mouvements));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Obtenir un mouvement par ID", description = "Récupère les détails d'un mouvement spécifique")
    public ResponseEntity<ApiResponse<MouvementStockResponseDTO>> getById(@PathVariable String id) {
        MouvementStockResponseDTO response = mouvementStockService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Mouvement de stock récupéré", response));
    }


    @GetMapping("/medicament/{medicamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Historique par médicament", description = "Consulte l'historique complet des mouvements d'un médicament spécifique")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getByMedicament(
            @PathVariable String medicamentId) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getByMedicament(medicamentId);
        return ResponseEntity.ok(new ApiResponse<>("Historique du médicament", mouvements));
    }


    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Mouvements par type", description = "Filtre les mouvements par type (ENTREE, SORTIE, AJUSTEMENT)")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getByType(
            @PathVariable MovementType type) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getByType(type);
        return ResponseEntity.ok(new ApiResponse<>("Mouvements de type " + type, mouvements));
    }


    @GetMapping("/periode")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Mouvements par période", description = "Récupère les mouvements effectués entre deux dates/heures")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(new ApiResponse<>("Mouvements de la période", mouvements));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Mouvements par utilisateur", description = "Liste les mouvements effectués par un utilisateur spécifique")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getByUser(
            @PathVariable String userId) {
        List<MouvementStockResponseDTO> mouvements = mouvementStockService.getByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("Mouvements de l'utilisateur", mouvements));
    }
}
