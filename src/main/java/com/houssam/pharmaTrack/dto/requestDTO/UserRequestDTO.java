package com.houssam.pharmaTrack.dto.requestDTO;

import com.houssam.pharmaTrack.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 20, message = "Le nom doit contenir entre 2 et 20 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 20, message = "Le prénom doit contenir entre 2 et 20 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    private Role role; // Modifiable uniquement par ADMIN
}
