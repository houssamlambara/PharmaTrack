package com.houssam.pharmaTrack.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentResponseDTO {
    private String id;
    private String nom;
    private String codeBarres;
    private String description;
    private String dosage;
    private String forme;
    private BigDecimal prixUnitaire;
    private Integer quantiteStock;
    private Integer seuilAlerte;
    private LocalDate dateExpiration;
    // private String imageUrl;
    private boolean actif;
    private LocalDateTime dateCreation;

    // Informations de la catégorie
    private String categorieId;
    private String categorieNom;

    // Indicateurs
    private boolean enRuptureStock; // true si quantiteStock <= 0
    private boolean alerteStock; // true si quantiteStock <= seuilAlerte
    private boolean expire; // true si dateExpiration < aujourd'hui
}
