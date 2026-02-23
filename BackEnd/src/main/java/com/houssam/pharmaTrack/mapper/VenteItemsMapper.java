package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.VenteItemsRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.VenteItemsResponseDTO;
import com.houssam.pharmaTrack.model.VenteItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VenteItemsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prixUnitaire", ignore = true) // Sera récupéré du médicament
    @Mapping(target = "sousTotal", ignore = true) // Sera calculé dans le service
    @Mapping(target = "vente", ignore = true) // Sera géré dans le service
    @Mapping(target = "medicament", ignore = true) // Sera géré dans le service
    VenteItems toEntity(VenteItemsRequestDTO requestDTO);

    @Mapping(target = "medicamentId", source = "medicament.id")
    @Mapping(target = "medicamentNom", source = "medicament.nom")
    @Mapping(target = "medicamentDosage", source = "medicament.dosage")
    @Mapping(target = "montantTotal", expression = "java(calculateMontantTotal(venteItems))")
    VenteItemsResponseDTO toResponseDTO(VenteItems venteItems);

    List<VenteItemsResponseDTO> toResponseDTOList(List<VenteItems> items);

    // Méthode helper pour calculer le montant total
    default BigDecimal calculateMontantTotal(VenteItems venteItems) {
        if (venteItems.getQuantite() == null || venteItems.getPrixUnitaire() == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(venteItems.getPrixUnitaire())
                .multiply(BigDecimal.valueOf(venteItems.getQuantite()));
    }
}
