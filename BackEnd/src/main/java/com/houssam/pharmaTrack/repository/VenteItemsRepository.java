package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.model.VenteItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteItemsRepository extends JpaRepository<VenteItems, String> {

    // Recherche par vente
    List<VenteItems> findByVente_Id(String venteId);

    // Recherche par médicament
    List<VenteItems> findByMedicament_Id(String medicamentId);

    // Supprimer tous les items d'une vente
    void deleteByVente_Id(String venteId);

    // Quantité totale vendue d'un médicament
    @Query("SELECT SUM(vi.quantite) FROM VenteItems vi WHERE vi.medicament.id = :medicamentId")
    Integer getTotalQuantiteVendueByMedicament(String medicamentId);

    // Médicaments les plus vendus
    @Query("SELECT vi.medicament.id, vi.medicament.nom, SUM(vi.quantite) as total " +
           "FROM VenteItems vi GROUP BY vi.medicament.id, vi.medicament.nom ORDER BY total DESC")
    List<Object[]> getMedicamentsPlusVendus();
}
