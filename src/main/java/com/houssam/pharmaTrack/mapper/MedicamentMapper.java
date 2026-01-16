package com.houssam.pharmaTrack.mapper;

import com.houssam.pharmaTrack.dto.requestDTO.MedicamentRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MedicamentResponseDTO;
import com.houssam.pharmaTrack.model.Medicament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicamentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantiteStock", constant = "0")
    @Mapping(target = "actif", constant = "true")
    @Mapping(target = "categorie", ignore = true) // Sera géré dans le service
    @Mapping(target = "mouvements", ignore = true)
    @Mapping(target = "ligneventes", ignore = true)
    @Mapping(target = "ligneCommnde", ignore = true)
    Medicament toEntity(MedicamentRequestDTO requestDTO);

    @Mapping(target = "categorieId", source = "categorie.id")
    @Mapping(target = "categorieNom", source = "categorie.nom")
    @Mapping(target = "enRuptureStock", expression = "java(medicament.getQuantiteStock() <= 0)")
    @Mapping(target = "alerteStock", expression = "java(medicament.getQuantiteStock() <= medicament.getSeuilAlerte())")
    @Mapping(target = "expire", expression = "java(isExpired(medicament.getDateExpiration()))")
    @Mapping(target = "dateCreation", ignore = true) // Pas de date de création dans l'entité actuellement
    MedicamentResponseDTO toResponseDTO(Medicament medicament);

    List<MedicamentResponseDTO> toResponseDTOList(List<Medicament> medicaments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantiteStock", ignore = true) // Ne pas modifier le stock via update simple
    @Mapping(target = "actif", ignore = true) // Ne pas modifier le statut actif via update simple
    @Mapping(target = "categorie", ignore = true) // Sera géré dans le service
    @Mapping(target = "mouvements", ignore = true)
    @Mapping(target = "ligneventes", ignore = true)
    @Mapping(target = "ligneCommnde", ignore = true)
    void updateEntityFromDTO(MedicamentRequestDTO requestDTO, @MappingTarget Medicament medicament);

    // Méthode helper pour vérifier l'expiration
    default boolean isExpired(LocalDate dateExpiration) {
        if (dateExpiration == null) {
            return false;
        }
        return dateExpiration.isBefore(LocalDate.now());
    }
}

