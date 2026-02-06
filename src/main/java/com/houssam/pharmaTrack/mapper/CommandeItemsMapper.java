package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.CommandeItemsRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CommandeItemsResponseDTO;
import com.houssam.pharmaTrack.model.CommandeItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommandeItemsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantiteRecue", constant = "0") // Par défaut à 0
    @Mapping(target = "commandeFournisseur", ignore = true) // Sera géré dans le service
    @Mapping(target = "medicament", ignore = true) // Sera géré dans le service
    CommandeItems toEntity(CommandeItemsRequestDTO requestDTO);

    @Mapping(target = "medicamentId", source = "medicament.id")
    @Mapping(target = "medicamentNom", source = "medicament.nom")
    @Mapping(target = "medicamentDosage", source = "medicament.dosage")
    @Mapping(target = "montantTotal", expression = "java(calculateMontantTotal(commandeItems))")
    CommandeItemsResponseDTO toResponseDTO(CommandeItems commandeItems);

    List<CommandeItemsResponseDTO> toResponseDTOList(List<CommandeItems> items);

    // Méthode helper pour calculer le montant total
    default BigDecimal calculateMontantTotal(CommandeItems commandeItems) {
        if (commandeItems.getQuantite() == null || commandeItems.getPrixUnitaire() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(commandeItems.getPrixUnitaire())
                .multiply(BigDecimal.valueOf(commandeItems.getQuantite()));
    }
}
