package com.houssam.pharmaTrack.dto.responseDTO;

import com.houssam.pharmaTrack.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private boolean actif;
    private LocalDateTime dateCreation;
    
    // Statistiques (optionnelles)
    private Long nombreVentes;
    private Long nombreCommandes;
    private Long nombreMouvements;
}
