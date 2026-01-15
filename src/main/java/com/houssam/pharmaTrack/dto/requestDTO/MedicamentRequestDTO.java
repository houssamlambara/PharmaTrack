package com.houssam.pharmaTrack.dto.requestDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentRequestDTO {

    @NotBlank(message = "Le nom du médicament est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @NotBlank(message = "Le dosage est obligatoire")
    @Size(max = 50, message = "Le dosage ne peut pas dépasser 50 caractères")
    private String dosage;

    @NotBlank(message = "La forme est obligatoire")
    @Size(max = 50, message = "La forme ne peut pas dépasser 50 caractères")
    private String forme;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    private BigDecimal prixUnitaire;

    @NotNull(message = "La catégorie est obligatoire")
    private String categorieId;

    @Min(value = 1, message = "Le seuil d'alerte doit être au moins 1")
    private Integer seuilAlerte = 10; // Valeur par défaut

    private LocalDate dateExpiration;
}
