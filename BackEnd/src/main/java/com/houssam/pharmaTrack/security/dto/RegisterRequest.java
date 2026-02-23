package com.houssam.pharmaTrack.security.dto;

import com.houssam.pharmaTrack.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 20)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 20)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 3, message = "Minimum 3 caractères")
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;
}