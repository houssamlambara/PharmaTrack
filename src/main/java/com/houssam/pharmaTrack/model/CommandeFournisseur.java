package com.houssam.pharmaTrack.model;

import com.houssam.pharmaTrack.enums.CommandeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commandes_fournisseurs")
@Builder
public class CommandeFournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String numeroCommande;

    @Column(nullable = false)
    private LocalDate dateCommande = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CommandeStatus commandeStatus;

    @Column(nullable = false)
    private Double montantTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "commandeFournisseur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeItems> lignes = new ArrayList<>();
}
