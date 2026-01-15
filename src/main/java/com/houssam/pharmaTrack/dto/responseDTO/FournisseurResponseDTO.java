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
public class FournisseurResponseDTO {
    private String id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private boolean actif;
    private LocalDateTime dateCreation;
    private Long nombreCommandes; // Nombre total de commandes
}
