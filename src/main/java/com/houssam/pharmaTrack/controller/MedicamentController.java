package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.MedicamentRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MedicamentResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.MedicamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
@Tag(name = "Médicaments", description = "Gestion complète des médicaments (CRUD, recherche, alertes de stock)")
public class MedicamentController {

    private final MedicamentService medicamentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Créer un médicament", description = "Ajoute un nouveau médicament au catalogue. Accessible par ADMIN et RESPONSABLE_STOCK.")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> create(
            @Valid @RequestBody MedicamentRequestDTO requestDTO) {
        MedicamentResponseDTO response = medicamentService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Médicament créé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Liste tous les médicaments", description = "Retourne la liste complète de tous les médicaments du système")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getAll() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des médicaments", medicaments));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtenir un médicament par ID", description = "Récupère les détails complets d'un médicament spécifique")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> getById(@PathVariable String id) {
        MedicamentResponseDTO response = medicamentService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Médicament récupéré", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Modifier un médicament", description = "Met à jour les informations d'un médicament existant")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody MedicamentRequestDTO requestDTO) {
        MedicamentResponseDTO response = medicamentService.update(id, requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Médicament mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un médicament", description = "Supprime définitivement un médicament. Accessible uniquement par ADMIN.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        medicamentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Médicament supprimé avec succès", null));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher des médicaments", description = "Recherche des médicaments par nom (recherche partielle, insensible à la casse)")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> search(
            @RequestParam String nom) {
        List<MedicamentResponseDTO> medicaments = medicamentService.searchByNom(nom);
        return ResponseEntity.ok(new ApiResponse<>("Résultats de recherche", medicaments));
    }

    @GetMapping("/categorie/{categorieId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Médicaments par catégorie", description = "Récupère tous les médicaments d'une catégorie spécifique")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getByCategorie(
            @PathVariable String categorieId) {
        List<MedicamentResponseDTO> medicaments = medicamentService.getByCategorie(categorieId);
        return ResponseEntity.ok(new ApiResponse<>("Médicaments de la catégorie", medicaments));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Alertes de stock faible", description = "Liste les médicaments dont le stock est inférieur au seuil d'alerte")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getLowStock() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getLowStockMedicaments();
        return ResponseEntity.ok(new ApiResponse<>("Médicaments en alerte de stock", medicaments));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Médicaments en rupture", description = "Liste les médicaments dont le stock est à zéro")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getOutOfStock() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getOutOfStockMedicaments();
        return ResponseEntity.ok(new ApiResponse<>("Médicaments en rupture de stock", medicaments));
    }

    @GetMapping("/code-barres/{codeBarres}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher par code-barres", description = "Trouve un médicament par son code-barres unique")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> getByCodeBarres(
            @PathVariable String codeBarres) {
        MedicamentResponseDTO response = medicamentService.getByCodeBarres(codeBarres);
        return ResponseEntity.ok(new ApiResponse<>("Médicament trouvé", response));
    }

    @PatchMapping("/{id}/toggle-actif")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Activer/Désactiver un médicament", description = "Bascule le statut actif/inactif d'un médicament sans le supprimer")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> toggleActif(@PathVariable String id) {
        MedicamentResponseDTO response = medicamentService.toggleActif(id);
        return ResponseEntity.ok(new ApiResponse<>("Statut du médicament modifié", response));
    }
}

