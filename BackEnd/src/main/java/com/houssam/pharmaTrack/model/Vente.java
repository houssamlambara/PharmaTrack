package com.houssam.pharmaTrack.model;


import com.houssam.pharmaTrack.enums.PaiementMethode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ventes")
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String numeroVente;

    @Column(nullable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

    @Column(nullable = false)
    private Double montantTotal = 0.0;

    @Column(nullable = false)
    private double remise;

    @Column(nullable = false)
    private double montantFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaiementMethode paiementMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VenteItems> lignes = new ArrayList<>();


}
