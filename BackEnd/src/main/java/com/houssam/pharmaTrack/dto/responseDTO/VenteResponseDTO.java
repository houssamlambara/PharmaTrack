package com.houssam.pharmaTrack.dto.responseDTO;

import com.houssam.pharmaTrack.enums.PaiementMethode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteResponseDTO {
    private String id;

    // Informations du client
    private String clientNom;

    // Informations du caissier
    private String userId;
    private String userNom;
    private String userPrenom;

    private LocalDateTime dateVente;
    private PaiementMethode methodePaiement;

    // Items de la vente
    private List<VenteItemsResponseDTO> items;

    // Calculs
    private BigDecimal montantTotal; // Somme des montants des items
    private Integer nombreArticles; // Nombre d'items dans la vente
}
