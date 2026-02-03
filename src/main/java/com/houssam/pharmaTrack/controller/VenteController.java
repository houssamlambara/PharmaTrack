package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.VenteRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.VenteResponseDTO;
import com.houssam.pharmaTrack.enums.PaiementMethode;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.VenteService;
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
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
@Tag(name = "Ventes", description = "Gestion des ventes aux clients (enregistrement, consultation, statistiques)")
public class VenteController {

    private final VenteService venteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    @Operation(summary = "Créer une vente",
               description = "Enregistre une nouvelle vente. Le stock est automatiquement déduit et un mouvement de stock (SORTIE) est créé.")
    public ResponseEntity<ApiResponse<VenteResponseDTO>> create(
            @Valid @RequestBody VenteRequestDTO requestDTO) {
        VenteResponseDTO response = venteService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Vente créée avec succès", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Liste toutes les ventes", description = "Retourne la liste complète de toutes les ventes")
    public ResponseEntity<ApiResponse<List<VenteResponseDTO>>> getAll() {
        List<VenteResponseDTO> ventes = venteService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des ventes", ventes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK', 'VENDEUR')")
    @Operation(summary = "Obtenir une vente par ID", description = "Récupère les détails complets d'une vente spécifique")
    public ResponseEntity<ApiResponse<VenteResponseDTO>> getById(@PathVariable String id) {
        VenteResponseDTO response = venteService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Vente récupérée", response));
    }


    @GetMapping("/numero/{numeroVente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK', 'VENDEUR')")
    @Operation(summary = "Rechercher par numéro de vente", description = "Trouve une vente par son numéro unique (format: V-YYYYMMDD-NNNN)")
    public ResponseEntity<ApiResponse<VenteResponseDTO>> getByNumeroVente(
            @PathVariable String numeroVente) {
        VenteResponseDTO response = venteService.getByNumeroVente(numeroVente);
        return ResponseEntity.ok(new ApiResponse<>("Vente récupérée", response));
    }


    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Ventes par utilisateur", description = "Récupère toutes les ventes effectuées par un utilisateur (caissier) spécifique")
    public ResponseEntity<ApiResponse<List<VenteResponseDTO>>> getByUser(
            @PathVariable String userId) {
        List<VenteResponseDTO> ventes = venteService.getByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("Ventes de l'utilisateur", ventes));
    }


    @GetMapping("/methode-paiement/{methodePaiement}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Ventes par méthode de paiement", description = "Filtre les ventes par mode de paiement (ESPECES, CARTE_BANCAIRE, CHEQUE)")
    public ResponseEntity<ApiResponse<List<VenteResponseDTO>>> getByMethodePaiement(
            @PathVariable PaiementMethode methodePaiement) {
        List<VenteResponseDTO> ventes = venteService.getByMethodePaiement(methodePaiement);
        return ResponseEntity.ok(new ApiResponse<>("Ventes par " + methodePaiement, ventes));
    }


    @GetMapping("/periode")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Ventes par période", description = "Récupère les ventes effectuées entre deux dates/heures")
    public ResponseEntity<ApiResponse<List<VenteResponseDTO>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<VenteResponseDTO> ventes = venteService.getByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(new ApiResponse<>("Ventes de la période", ventes));
    }

    @GetMapping("/aujourdhui")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK', 'VENDEUR')")
    @Operation(summary = "Ventes du jour", description = "Liste toutes les ventes effectuées aujourd'hui")
    public ResponseEntity<ApiResponse<List<VenteResponseDTO>>> getVentesAujourdhui() {
        List<VenteResponseDTO> ventes = venteService.getVentesAujourdhui();
        return ResponseEntity.ok(new ApiResponse<>("Ventes du jour", ventes));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une vente",
               description = "Supprime définitivement une vente. Attention: le stock ne sera pas restauré automatiquement. Accessible uniquement par ADMIN.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        venteService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Vente supprimée avec succès", null));
    }
}
