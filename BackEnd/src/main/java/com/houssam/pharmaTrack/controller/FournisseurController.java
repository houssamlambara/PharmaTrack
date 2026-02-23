package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.FournisseurService;
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
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
@Tag(name = "Fournisseurs", description = "Gestion des fournisseurs et partenaires commerciaux")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Créer un fournisseur", description = "Ajoute un nouveau fournisseur au système")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> create(
            @Valid @RequestBody FournisseurRequestDTO requestDTO) {
        FournisseurResponseDTO response = fournisseurService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Fournisseur créé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Liste tous les fournisseurs", description = "Retourne la liste complète de tous les fournisseurs (actifs et inactifs)")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> getAll() {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des fournisseurs", fournisseurs));
    }

    @GetMapping("/actifs")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Fournisseurs actifs", description = "Retourne uniquement les fournisseurs avec le statut actif")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> getActifs() {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.getActifs();
        return ResponseEntity.ok(new ApiResponse<>("Liste des fournisseurs actifs", fournisseurs));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtenir un fournisseur par ID", description = "Récupère les détails d'un fournisseur spécifique")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> getById(@PathVariable String id) {
        FournisseurResponseDTO response = fournisseurService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur récupéré", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Modifier un fournisseur", description = "Met à jour les informations d'un fournisseur existant")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody FournisseurRequestDTO requestDTO) {
        FournisseurResponseDTO response = fournisseurService.update(id, requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur mis à jour avec succès", response));
    }

    @PutMapping("/{id}/toggle-actif")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activer/Désactiver un fournisseur", description = "Bascule le statut actif/inactif d'un fournisseur")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> toggleActif(@PathVariable String id) {
        FournisseurResponseDTO response = fournisseurService.toggleActif(id);
        return ResponseEntity.ok(new ApiResponse<>("Statut du fournisseur modifié", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un fournisseur", description = "Supprime définitivement un fournisseur. Accessible uniquement par ADMIN.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        fournisseurService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur supprimé avec succès", null));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher des fournisseurs", description = "Recherche des fournisseurs par nom (recherche partielle)")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> search(
            @RequestParam String nom) {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.searchByNom(nom);
        return ResponseEntity.ok(new ApiResponse<>("Résultats de recherche", fournisseurs));
    }
}

