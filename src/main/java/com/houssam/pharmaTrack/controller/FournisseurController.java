package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.FournisseurService;
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
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> create(
            @Valid @RequestBody FournisseurRequestDTO requestDTO) {
        FournisseurResponseDTO response = fournisseurService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Fournisseur créé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> getAll() {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des fournisseurs", fournisseurs));
    }

    @GetMapping("/actifs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> getActifs() {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.getActifs();
        return ResponseEntity.ok(new ApiResponse<>("Liste des fournisseurs actifs", fournisseurs));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> getById(@PathVariable String id) {
        FournisseurResponseDTO response = fournisseurService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur récupéré", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody FournisseurRequestDTO requestDTO) {
        FournisseurResponseDTO response = fournisseurService.update(id, requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur mis à jour avec succès", response));
    }

    @PutMapping("/{id}/toggle-actif")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FournisseurResponseDTO>> toggleActif(@PathVariable String id) {
        FournisseurResponseDTO response = fournisseurService.toggleActif(id);
        return ResponseEntity.ok(new ApiResponse<>("Statut du fournisseur modifié", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        fournisseurService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Fournisseur supprimé avec succès", null));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDTO>>> search(
            @RequestParam String nom) {
        List<FournisseurResponseDTO> fournisseurs = fournisseurService.searchByNom(nom);
        return ResponseEntity.ok(new ApiResponse<>("Résultats de recherche", fournisseurs));
    }
}

