package com.houssam.pharmaTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "medicaments")

public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(unique = true, length = 50)
    private String codeBarres;

    @Column(nullable = false, length = 50)
    private double prixUnitaire;

    @Column(nullable = false)
    private Integer quantiteStock = 0;

    @Column(nullable = false)
    private LocalDate dateExpiration;

    @Column(length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private Integer seuilAlerte;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<MouvementStock> mouvements = new ArrayList<>();

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<VenteItems> ligneventes = new ArrayList<>();

    @OneToMany(mappedBy = "medicament", cascade = CascadeType.ALL)
    private List<CommandeItems> ligneCommnde = new ArrayList<>();


}
