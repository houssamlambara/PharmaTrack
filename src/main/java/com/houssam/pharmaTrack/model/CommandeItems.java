package com.houssam.pharmaTrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ligne_commandes")
public class CommandeItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    private Integer quantiteRecue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "commande_id", nullable = false)
    private CommandeFournisseur commandeFournisseur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicament medicament;

}
