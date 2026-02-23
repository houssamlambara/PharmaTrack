package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.model.CommandeItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeItemsRepository extends JpaRepository<CommandeItems, String> {

    // Recherche par commande
    List<CommandeItems> findByCommandeFournisseur_Id(String commandeId);

    // Recherche par médicament
    List<CommandeItems> findByMedicament_Id(String medicamentId);

    // Supprimer tous les items d'une commande
    void deleteByCommandeFournisseur_Id(String commandeId);

    // Quantité totale commandée d'un médicament
    @Query("SELECT SUM(ci.quantite) FROM CommandeItems ci WHERE ci.medicament.id = :medicamentId")
    Integer getTotalQuantiteByMedicament(String medicamentId);
}
