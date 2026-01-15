package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, String> {

    Optional<Fournisseur> findByEmail(String email);

    Optional<Fournisseur> findByTelephone(String telephone);

    List<Fournisseur> findByNomContainingIgnoreCase(String nom);

    List<Fournisseur> findByActif(Boolean actif);

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);
}
