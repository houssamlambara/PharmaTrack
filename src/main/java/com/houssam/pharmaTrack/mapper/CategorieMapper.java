package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.CategorieRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CategorieResponseDTO;
import com.houssam.pharmaTrack.model.Categorie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategorieMapper {

    Categorie toEntity(CategorieRequestDTO requestDTO);

    @Mapping(target = "nombreMedicaments", expression = "java(categorie.getMedicaments() != null ? (long) categorie.getMedicaments().size() : 0L)")
    CategorieResponseDTO toResponseDTO(Categorie categorie);

    List<CategorieResponseDTO> toResponseDTOList(List<Categorie> categories);

    void updateEntityFromDTO(CategorieRequestDTO requestDTO, @MappingTarget Categorie categorie);
}

