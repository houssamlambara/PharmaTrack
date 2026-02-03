package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.enums.PaiementMethode;
import com.houssam.pharmaTrack.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VenteRepository extends JpaRepository<Vente, String> {

    Optional<Vente> findByNumeroVente(String numeroVente);

    boolean existsByNumeroVente(String numeroVente);

    List<Vente> findByUser_Id(String userId);

    List<Vente> findByPaiementMode(PaiementMethode paiementMode);

    List<Vente> findAllByOrderByDateHeureDesc();

    List<Vente> findByDateHeureBetween(LocalDateTime dateDebut, LocalDateTime dateFin);


    @Query("SELECT v FROM Vente v WHERE CAST(v.dateHeure AS DATE) = CURRENT_DATE ORDER BY v.dateHeure DESC")
    List<Vente> findVentesAujourdhui();

    @Query("SELECT SUM(v.montantFinal) FROM Vente v WHERE v.dateHeure BETWEEN :dateDebut AND :dateFin")
    Double getMontantTotalByPeriode(LocalDateTime dateDebut, LocalDateTime dateFin);

    @Query("SELECT COUNT(v) FROM Vente v WHERE v.dateHeure BETWEEN :dateDebut AND :dateFin")
    Long countVentesByPeriode(LocalDateTime dateDebut, LocalDateTime dateFin);
}
