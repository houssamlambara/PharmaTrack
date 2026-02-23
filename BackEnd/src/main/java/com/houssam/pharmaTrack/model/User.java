package com.houssam.pharmaTrack.model;

import com.houssam.pharmaTrack.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable= false, length = 20)
    private String nom;

    @Column(nullable= false, length = 20)
    private String prenom;

    @Column(nullable= false, unique = true, length = 50)
    private String email;

    @Column(nullable= false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable= false)
    private Role role;

    private boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vente> ventes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MouvementStock> mouvements = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommandeFournisseur> commandes = new ArrayList<>();

}
