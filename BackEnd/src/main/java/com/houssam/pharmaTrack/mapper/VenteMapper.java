package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.VenteRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.VenteResponseDTO;
import com.houssam.pharmaTrack.model.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VenteItemsMapper.class})
public interface VenteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroVente", ignore = true) // Sera généré dans le service
    @Mapping(target = "dateHeure", ignore = true) // Sera défini dans le service
    @Mapping(target = "montantTotal", ignore = true) // Sera calculé dans le service
    @Mapping(target = "remise", constant = "0.0") // Par défaut 0
    @Mapping(target = "montantFinal", ignore = true) // Sera calculé dans le service
    @Mapping(target = "paiementMode", source = "methodePaiement")
    @Mapping(target = "user", ignore = true) // Sera géré dans le service
    @Mapping(target = "lignes", ignore = true) // Sera géré dans le service
    Vente toEntity(VenteRequestDTO requestDTO);

    @Mapping(target = "clientNom", ignore = true) // Pas dans le modèle actuel
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userNom", source = "user.nom")
    @Mapping(target = "userPrenom", source = "user.prenom")
    @Mapping(target = "dateVente", source = "dateHeure")
    @Mapping(target = "methodePaiement", source = "paiementMode")
    @Mapping(target = "items", source = "lignes")
    @Mapping(target = "nombreArticles", expression = "java(vente.getLignes() != null ? vente.getLignes().size() : 0)")
    VenteResponseDTO toResponseDTO(Vente vente);

    List<VenteResponseDTO> toResponseDTOList(List<Vente> ventes);
}
