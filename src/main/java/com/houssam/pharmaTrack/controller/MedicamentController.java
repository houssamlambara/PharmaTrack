package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.MedicamentRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MedicamentResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.MedicamentService;
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
public class MedicamentController {

    private final MedicamentService medicamentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> create(
            @Valid @RequestBody MedicamentRequestDTO requestDTO) {
        MedicamentResponseDTO response = medicamentService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Médicament créé avec succès", response));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getAll() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des médicaments", medicaments));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> getById(@PathVariable String id) {
        MedicamentResponseDTO response = medicamentService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Médicament récupéré", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody MedicamentRequestDTO requestDTO) {
        MedicamentResponseDTO response = medicamentService.update(id, requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Médicament mis à jour avec succès", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        medicamentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Médicament supprimé avec succès", null));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> search(
            @RequestParam String nom) {
        List<MedicamentResponseDTO> medicaments = medicamentService.searchByNom(nom);
        return ResponseEntity.ok(new ApiResponse<>("Résultats de recherche", medicaments));
    }

    @GetMapping("/categorie/{categorieId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getByCategorie(
            @PathVariable String categorieId) {
        List<MedicamentResponseDTO> medicaments = medicamentService.getByCategorie(categorieId);
        return ResponseEntity.ok(new ApiResponse<>("Médicaments de la catégorie", medicaments));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getLowStock() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getLowStockMedicaments();
        return ResponseEntity.ok(new ApiResponse<>("Médicaments en alerte de stock", medicaments));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<List<MedicamentResponseDTO>>> getOutOfStock() {
        List<MedicamentResponseDTO> medicaments = medicamentService.getOutOfStockMedicaments();
        return ResponseEntity.ok(new ApiResponse<>("Médicaments en rupture de stock", medicaments));
    }

    @GetMapping("/code-barres/{codeBarres}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> getByCodeBarres(
            @PathVariable String codeBarres) {
        MedicamentResponseDTO response = medicamentService.getByCodeBarres(codeBarres);
        return ResponseEntity.ok(new ApiResponse<>("Médicament trouvé", response));
    }

    @PatchMapping("/{id}/toggle-actif")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<MedicamentResponseDTO>> toggleActif(@PathVariable String id) {
        MedicamentResponseDTO response = medicamentService.toggleActif(id);
        return ResponseEntity.ok(new ApiResponse<>("Statut du médicament modifié", response));
    }
}

