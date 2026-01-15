package com.houssam.pharmaTrack.dto.requestDTO;

import com.houssam.pharmaTrack.enums.PaiementMethode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenteRequestDTO {

    @Size(max = 100, message = "Le nom du client ne peut pas dépasser 100 caractères")
    private String clientNom; // Optionnel

    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaiementMethode methodePaiement;

    @NotEmpty(message = "La vente doit contenir au moins un article")
    @Valid
    private List<VenteItemsRequestDTO> items;
}
