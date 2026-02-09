package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.MouvementStockRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MouvementStockResponseDTO;
import com.houssam.pharmaTrack.model.MouvementStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MouvementStockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateHeure", ignore = true) // Sera défini dans le service
    @Mapping(target = "medicament", ignore = true) // Sera géré dans le service
    @Mapping(target = "user", ignore = true) // Sera géré dans le service
    @Mapping(target = "description", source = "motif")
    @Mapping(target = "reference", ignore = true) // Sera défini dans le service
    MouvementStock toEntity(MouvementStockRequestDTO requestDTO);

    @Mapping(target = "medicamentId", source = "medicament.id")
    @Mapping(target = "medicamentNom", source = "medicament.nom")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userNom", source = "user.nom")
    @Mapping(target = "userPrenom", source = "user.prenom")
    @Mapping(target = "motif", source = "description")
    @Mapping(target = "dateMouvement", source = "dateHeure")
    @Mapping(target = "stockAvant", ignore = true) // Sera calculé dans le service
    @Mapping(target = "stockApres", ignore = true) // Sera calculé dans le service
    MouvementStockResponseDTO toResponseDTO(MouvementStock mouvementStock);

    List<MouvementStockResponseDTO> toResponseDTOList(List<MouvementStock> mouvements);
}
