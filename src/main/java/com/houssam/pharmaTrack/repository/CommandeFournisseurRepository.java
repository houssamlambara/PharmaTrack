package com.houssam.pharmaTrack.repository;

import com.houssam.pharmaTrack.enums.CommandeStatus;
import com.houssam.pharmaTrack.model.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, String> {

    // Recherche par numéro de commande
    Optional<CommandeFournisseur> findByNumeroCommande(String numeroCommande);

    // Vérifier si un numéro de commande existe
    boolean existsByNumeroCommande(String numeroCommande);

    // Recherche par fournisseur
    List<CommandeFournisseur> findByFournisseur_Id(String fournisseurId);

    // Recherche par statut
    List<CommandeFournisseur> findByCommandeStatus(CommandeStatus status);

    // Recherche par fournisseur et statut
    List<CommandeFournisseur> findByFournisseur_IdAndCommandeStatus(String fournisseurId, CommandeStatus status);

    // Recherche par date
    List<CommandeFournisseur> findByDateCommandeBetween(LocalDate dateDebut, LocalDate dateFin);

    // Recherche par utilisateur
    List<CommandeFournisseur> findByUser_Id(String userId);

    // Toutes les commandes triées par date décroissante
    List<CommandeFournisseur> findAllByOrderByDateCommandeDesc();

    // Commandes en attente
    @Query("SELECT c FROM CommandeFournisseur c WHERE c.commandeStatus = 'EN_ATTENTE' ORDER BY c.dateCommande DESC")
    List<CommandeFournisseur> findCommandesEnAttente();

    // Montant total des commandes par statut
    @Query("SELECT SUM(c.montantTotal) FROM CommandeFournisseur c WHERE c.commandeStatus = :status")
    Double getMontantTotalByStatus(CommandeStatus status);
}
