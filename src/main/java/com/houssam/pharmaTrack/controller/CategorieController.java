package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.CategorieRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CategorieResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.CategorieService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Catégories", description = "Gestion des catégories de médicaments")
public class CategorieController {

    private final CategorieService categorieService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Créer une catégorie", description = "Crée une nouvelle catégorie de médicaments. Accessible par ADMIN et RESPONSABLE_STOCK.")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> create(
            @Valid @RequestBody CategorieRequestDTO requestDTO) {
        CategorieResponseDTO response = categorieService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Catégorie créée avec succès", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Liste toutes les catégories", description = "Retourne la liste complète de toutes les catégories")
    public ResponseEntity<ApiResponse<List<CategorieResponseDTO>>> getAll() {
        List<CategorieResponseDTO> categories = categorieService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des catégories", categories));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtenir une catégorie par ID", description = "Récupère les détails d'une catégorie spécifique")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> getById(@PathVariable String id) {
        CategorieResponseDTO response = categorieService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Catégorie récupérée", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Modifier une catégorie", description = "Met à jour les informations d'une catégorie existante")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody CategorieRequestDTO requestDTO) {
        CategorieResponseDTO response = categorieService.update(id, requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Catégorie mise à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une catégorie", description = "Supprime définitivement une catégorie. Accessible uniquement par ADMIN.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        categorieService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Catégorie supprimée avec succès", null));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher des catégories", description = "Recherche des catégories par nom (recherche partielle)")
    public ResponseEntity<ApiResponse<List<CategorieResponseDTO>>> search(
            @RequestParam String nom) {
        List<CategorieResponseDTO> categories = categorieService.searchByNom(nom);
        return ResponseEntity.ok(new ApiResponse<>("Résultats de recherche", categories));
    }
}

