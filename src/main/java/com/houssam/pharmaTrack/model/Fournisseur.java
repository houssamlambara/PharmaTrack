package com.houssam.pharmaTrack.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fournisseurs")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(unique = true, length = 20)
    private String telephone;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<CommandeFournisseur> commandes = new ArrayList<>();

}
