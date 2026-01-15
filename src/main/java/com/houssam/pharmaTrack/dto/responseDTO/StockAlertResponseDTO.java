package com.houssam.pharmaTrack.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAlertResponseDTO {
    private String medicamentId;
    private String medicamentNom;
    private String medicamentDosage;
    private String categorieNom;
    private Integer quantiteActuelle;
    private Integer seuilAlerte;
    private String typeAlerte; // "RUPTURE", "ALERTE_STOCK", "EXPIRE", "PROCHE_EXPIRATION"
    private String message;
}

