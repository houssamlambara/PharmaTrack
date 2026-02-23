package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.CommandeFournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CommandeFournisseurResponseDTO;
import com.houssam.pharmaTrack.model.CommandeFournisseur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommandeItemsMapper.class})
public interface CommandeFournisseurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroCommande", ignore = true)
    @Mapping(target = "dateCommande", ignore = true)
    @Mapping(target = "commandeStatus", ignore = true)
    @Mapping(target = "montantTotal", ignore = true)
    @Mapping(target = "fournisseur", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lignes", ignore = true)
    CommandeFournisseur toEntity(CommandeFournisseurRequestDTO requestDTO);

    @Mapping(target = "fournisseurId", source = "fournisseur.id")
    @Mapping(target = "fournisseurNom", source = "fournisseur.nom")
    @Mapping(target = "fournisseurTelephone", source = "fournisseur.telephone")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userNom", source = "user.nom")
    @Mapping(target = "userPrenom", source = "user.prenom")
    @Mapping(target = "status", source = "commandeStatus")
    @Mapping(target = "dateReception", ignore = true)
    @Mapping(target = "remarque", ignore = true)
    @Mapping(target = "items", source = "lignes")
    @Mapping(target = "nombreArticles", expression = "java(commandeFournisseur.getLignes() != null ? commandeFournisseur.getLignes().size() : 0)")
    CommandeFournisseurResponseDTO toResponseDTO(CommandeFournisseur commandeFournisseur);

    List<CommandeFournisseurResponseDTO> toResponseDTOList(List<CommandeFournisseur> commandes);
}
