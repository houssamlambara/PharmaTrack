package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;
import com.houssam.pharmaTrack.model.Fournisseur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FournisseurMapper {

    Fournisseur toEntity(FournisseurRequestDTO requestDTO);

    @Mapping(target = "nombreCommandes", expression = "java(fournisseur.getCommandes() != null ? (long) fournisseur.getCommandes().size() : 0L)")
    FournisseurResponseDTO toResponseDTO(Fournisseur fournisseur);

    List<FournisseurResponseDTO> toResponseDTOList(List<Fournisseur> fournisseurs);

    void updateEntityFromDTO(FournisseurRequestDTO requestDTO, @MappingTarget Fournisseur fournisseur);
}

