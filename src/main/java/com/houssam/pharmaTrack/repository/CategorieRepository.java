package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, String> {

    Optional<Categorie> findByNom(String nom);

    List<Categorie> findByNomContainingIgnoreCase(String nom);

    boolean existsByNom(String nom);
}
