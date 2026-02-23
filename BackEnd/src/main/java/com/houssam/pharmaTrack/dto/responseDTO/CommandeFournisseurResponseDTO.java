package com.houssam.pharmaTrack.dto.responseDTO;

import com.houssam.pharmaTrack.enums.CommandeStatus;
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
public class CommandeFournisseurResponseDTO {
    private String id;

    // Informations du fournisseur
    private String fournisseurId;
    private String fournisseurNom;
    private String fournisseurTelephone;

    // Informations de l'utilisateur qui a créé la commande
    private String userId;
    private String userNom;
    private String userPrenom;

    private LocalDateTime dateCommande;
    private LocalDateTime dateReception;
    private CommandeStatus status;
    private String remarque;

    // Items de la commande
    private List<CommandeItemsResponseDTO> items;

    // Calculs
    private BigDecimal montantTotal; // Somme des montants des items
    private Integer nombreArticles; // Nombre d'items dans la commande
}
