package com.houssam.pharmaTrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(unique = true, length = 50)
    private String telephone;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CommandeFournisseur> commandes = new ArrayList<>();

}
