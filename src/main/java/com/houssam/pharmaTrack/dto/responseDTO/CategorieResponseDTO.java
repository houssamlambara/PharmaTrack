package com.houssam.pharmaTrack.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorieResponseDTO {
    private String id;
    private String nom;
    private String description;
    private LocalDateTime dateCreation;
    private Long nombreMedicaments; // Nombre de médicaments dans cette catégorie
}
