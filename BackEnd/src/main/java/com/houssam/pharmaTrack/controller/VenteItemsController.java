package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.responseDTO.VenteItemsResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.VenteItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vente-items")
@RequiredArgsConstructor
public class VenteItemsController {

    private final VenteItemsService venteItemsService;

    @GetMapping("/vente/{venteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK', 'VENDEUR')")
    public ResponseEntity<ApiResponse<List<VenteItemsResponseDTO>>> getByVente(
            @PathVariable String venteId) {
        List<VenteItemsResponseDTO> items = venteItemsService.getByVente(venteId);
        return ResponseEntity.ok(new ApiResponse<>("Items de la vente", items));
    }

    @GetMapping("/medicament/{medicamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<List<VenteItemsResponseDTO>>> getByMedicament(
            @PathVariable String medicamentId) {
        List<VenteItemsResponseDTO> items = venteItemsService.getByMedicament(medicamentId);
        return ResponseEntity.ok(new ApiResponse<>("Historique des ventes du médicament", items));
    }
}
