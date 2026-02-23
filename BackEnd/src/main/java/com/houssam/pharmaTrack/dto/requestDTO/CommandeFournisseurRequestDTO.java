package com.houssam.pharmaTrack.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeFournisseurRequestDTO {

    @NotNull(message = "Le fournisseur est obligatoire")
    private String fournisseurId;

    private LocalDateTime dateCommande; // Si null, sera défini à now()

    @NotEmpty(message = "La commande doit contenir au moins un article")
    @Valid
    private List<CommandeItemsRequestDTO> items;

    private String remarque;
}
