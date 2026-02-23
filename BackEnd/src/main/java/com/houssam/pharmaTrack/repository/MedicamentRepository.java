package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, String> {

    // Recherche par nom
    List<Medicament> findByNomContainingIgnoreCase(String nom);

    // Vérifier si un médicament existe par nom
    boolean existsByNom(String nom);

    // Vérifier si un code-barres existe
    boolean existsByCodeBarres(String codeBarres);

    // Recherche par catégorie
    List<Medicament> findByCategorie_Id(String categorieId);

    // Recherche par code-barres
    Optional<Medicament> findByCodeBarres(String codeBarres);

    // Médicaments actifs uniquement
    List<Medicament> findByActifTrue();

    // Médicaments en alerte de stock (quantité <= seuil)
    @Query("SELECT m FROM Medicament m WHERE m.quantiteStock <= m.seuilAlerte AND m.actif = true")
    List<Medicament> findLowStockMedicaments();

    // Médicaments en rupture de stock
    @Query("SELECT m FROM Medicament m WHERE m.quantiteStock = 0 AND m.actif = true")
    List<Medicament> findOutOfStockMedicaments();

    // Médicaments par catégorie et actifs
    List<Medicament> findByCategorie_IdAndActifTrue(String categorieId);
}
