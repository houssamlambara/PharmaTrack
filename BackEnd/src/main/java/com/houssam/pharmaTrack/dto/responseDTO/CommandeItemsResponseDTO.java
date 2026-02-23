package com.houssam.pharmaTrack.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeItemsResponseDTO {
    private String id;

    // Informations du médicament
    private String medicamentId;
    private String medicamentNom;
    private String medicamentDosage;

    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantTotal; // quantite * prixUnitaire
}
