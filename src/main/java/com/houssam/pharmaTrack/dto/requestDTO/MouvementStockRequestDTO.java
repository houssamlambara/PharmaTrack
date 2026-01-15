package com.houssam.pharmaTrack.dto.requestDTO;

import com.houssam.pharmaTrack.enums.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockRequestDTO {

    @NotNull(message = "Le médicament est obligatoire")
    private String medicamentId;

    @NotNull(message = "Le type de mouvement est obligatoire")
    private MovementType type; // ENTREE, SORTIE, AJUSTEMENT, PEREMPTION

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    @Size(max = 500, message = "Le motif ne peut pas dépasser 500 caractères")
    private String motif; // Obligatoire pour AJUSTEMENT et PEREMPTION
}
