package com.houssam.pharmaTrack.dto.responseDTO;

import com.houssam.pharmaTrack.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MouvementStockResponseDTO {
    private String id;

    // Informations du médicament
    private String medicamentId;
    private String medicamentNom;

    // Informations de l'utilisateur
    private String userId;
    private String userNom;
    private String userPrenom;

    private MovementType type;
    private Integer quantite;
    private Integer stockAvant; // Stock avant le mouvement
    private Integer stockApres; // Stock après le mouvement
    private String motif;
    private LocalDateTime dateMouvement;
}
