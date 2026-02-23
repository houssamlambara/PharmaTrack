package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.enums.MovementType;
import com.houssam.pharmaTrack.model.MouvementStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, String> {

    // Recherche par médicament
    List<MouvementStock> findByMedicament_Id(String medicamentId);

    // Recherche par type de mouvement
    List<MouvementStock> findByType(MovementType type);

    // Recherche par utilisateur
    List<MouvementStock> findByUser_Id(String userId);

    // Recherche par période
    List<MouvementStock> findByDateHeureBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

    // Tous les mouvements triés par date décroissante
    List<MouvementStock> findAllByOrderByDateHeureDesc();

    // Mouvements d'un médicament triés par date décroissante
    List<MouvementStock> findByMedicament_IdOrderByDateHeureDesc(String medicamentId);
}
