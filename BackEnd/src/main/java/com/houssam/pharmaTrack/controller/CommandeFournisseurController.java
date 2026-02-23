package com.houssam.pharmaTrack.controller;

import com.houssam.pharmaTrack.dto.requestDTO.CommandeFournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CommandeFournisseurResponseDTO;
import com.houssam.pharmaTrack.enums.CommandeStatus;
import com.houssam.pharmaTrack.response.ApiResponse;
import com.houssam.pharmaTrack.service.CommandeFournisseurService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commandes-fournisseurs")
@RequiredArgsConstructor
@Tag(name = "Commandes Fournisseurs", description = "Gestion des commandes passées aux fournisseurs (création, réception, suivi)")
public class CommandeFournisseurController {

    private final CommandeFournisseurService commandeFournisseurService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Créer une commande fournisseur",
               description = "Crée une nouvelle commande à un fournisseur. Le stock n'est pas encore modifié à cette étape.")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> create(
            @Valid @RequestBody CommandeFournisseurRequestDTO requestDTO) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Commande fournisseur créée avec succès", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Liste toutes les commandes", description = "Retourne toutes les commandes fournisseurs (tous statuts confondus)")
    public ResponseEntity<ApiResponse<List<CommandeFournisseurResponseDTO>>> getAll() {
        List<CommandeFournisseurResponseDTO> commandes = commandeFournisseurService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("Liste des commandes fournisseurs", commandes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Obtenir une commande par ID", description = "Récupère les détails complets d'une commande fournisseur")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> getById(@PathVariable String id) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande fournisseur récupérée", response));
    }

    @GetMapping("/numero/{numeroCommande}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Rechercher par numéro de commande", description = "Trouve une commande par son numéro unique (format: CMD-YYYYMMDD-NNNN)")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> getByNumeroCommande(
            @PathVariable String numeroCommande) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.getByNumeroCommande(numeroCommande);
        return ResponseEntity.ok(new ApiResponse<>("Commande fournisseur récupérée", response));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Commandes par fournisseur", description = "Récupère toutes les commandes d'un fournisseur spécifique")
    public ResponseEntity<ApiResponse<List<CommandeFournisseurResponseDTO>>> getByFournisseur(
            @PathVariable String fournisseurId) {
        List<CommandeFournisseurResponseDTO> commandes = commandeFournisseurService.getByFournisseur(fournisseurId);
        return ResponseEntity.ok(new ApiResponse<>("Commandes du fournisseur", commandes));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Commandes par statut", description = "Filtre les commandes par statut (EN_ATTENTE, LIVREE, ANNULEE)")
    public ResponseEntity<ApiResponse<List<CommandeFournisseurResponseDTO>>> getByStatus(
            @PathVariable CommandeStatus status) {
        List<CommandeFournisseurResponseDTO> commandes = commandeFournisseurService.getByStatus(status);
        return ResponseEntity.ok(new ApiResponse<>("Commandes avec le statut " + status, commandes));
    }

    @GetMapping("/en-attente")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Commandes en attente", description = "Liste les commandes qui n'ont pas encore été réceptionnées")
    public ResponseEntity<ApiResponse<List<CommandeFournisseurResponseDTO>>> getCommandesEnAttente() {
        List<CommandeFournisseurResponseDTO> commandes = commandeFournisseurService.getCommandesEnAttente();
        return ResponseEntity.ok(new ApiResponse<>("Commandes en attente", commandes));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Commandes par période", description = "Récupère les commandes passées entre deux dates")
    public ResponseEntity<ApiResponse<List<CommandeFournisseurResponseDTO>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<CommandeFournisseurResponseDTO> commandes = commandeFournisseurService.getByDateRange(dateDebut, dateFin);
        return ResponseEntity.ok(new ApiResponse<>("Commandes entre " + dateDebut + " et " + dateFin, commandes));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Changer le statut d'une commande", description = "Modifie manuellement le statut d'une commande")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> updateStatus(
            @PathVariable String id,
            @RequestParam CommandeStatus status) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>("Statut de la commande mis à jour à " + status, response));
    }


    @PostMapping("/{id}/recevoir")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Réceptionner une commande",
               description = "Marque une commande comme reçue. Cette action met automatiquement à jour le stock des médicaments et crée les mouvements de stock (ENTREE).")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> receiveCommande(@PathVariable String id) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.receiveCommande(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande reçue avec succès", response));
    }

    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_STOCK')")
    @Operation(summary = "Annuler une commande", description = "Passe le statut de la commande à ANNULEE")
    public ResponseEntity<ApiResponse<CommandeFournisseurResponseDTO>> cancelCommande(@PathVariable String id) {
        CommandeFournisseurResponseDTO response = commandeFournisseurService.updateStatus(id, CommandeStatus.ANNULEE);
        return ResponseEntity.ok(new ApiResponse<>("Commande annulée avec succès", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une commande", description = "Supprime définitivement une commande. Impossible si déjà livrée. Accessible uniquement par ADMIN.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        commandeFournisseurService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Commande fournisseur supprimée avec succès", null));
    }
}
