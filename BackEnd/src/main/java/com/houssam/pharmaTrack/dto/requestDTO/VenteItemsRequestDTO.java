package com.houssam.pharmaTrack.dto.requestDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenteItemsRequestDTO {

    @NotNull(message = "Le médicament est obligatoire")
    private String medicamentId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    // Le prix unitaire sera récupéré automatiquement depuis le médicament
    // Pas besoin de le passer dans la requête
}
