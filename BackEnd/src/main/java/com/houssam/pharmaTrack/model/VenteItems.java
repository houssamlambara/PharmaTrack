package com.houssam.pharmaTrack.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ligne_ventes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VenteItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    private Double sousTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "vente_id", nullable = false)
    private Vente vente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "medicament_id", nullable = false)
    private Medicament medicament;

}
