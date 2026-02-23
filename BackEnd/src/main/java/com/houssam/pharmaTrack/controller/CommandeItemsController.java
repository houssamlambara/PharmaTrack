package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.responseDTO.CommandeItemsResponseDTO;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.CommandeItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commande-items")
@RequiredArgsConstructor
public class CommandeItemsController {

    private final CommandeItemsService commandeItemsService;

    @GetMapping("/commande/{commandeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<List<CommandeItemsResponseDTO>>> getByCommande(
            @PathVariable String commandeId) {
        List<CommandeItemsResponseDTO> items = commandeItemsService.getByCommande(commandeId);
        return ResponseEntity.ok(new ApiResponse<>("Items de la commande", items));
    }

    @GetMapping("/medicament/{medicamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<List<CommandeItemsResponseDTO>>> getByMedicament(
            @PathVariable String medicamentId) {
        List<CommandeItemsResponseDTO> items = commandeItemsService.getByMedicament(medicamentId);
        return ResponseEntity.ok(new ApiResponse<>("Items du médicament", items));
    }

    @PatchMapping("/{id}/quantite-recue")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    public ResponseEntity<ApiResponse<CommandeItemsResponseDTO>> updateQuantiteRecue(
            @PathVariable String id,
            @RequestParam Integer quantiteRecue) {
        CommandeItemsResponseDTO response = commandeItemsService.updateQuantiteRecue(id, quantiteRecue);
        return ResponseEntity.ok(new ApiResponse<>("Quantité reçue mise à jour", response));
    }
}
