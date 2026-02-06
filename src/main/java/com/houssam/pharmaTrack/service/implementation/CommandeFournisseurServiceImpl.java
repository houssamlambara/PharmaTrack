package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.CommandeFournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.requestDTO.CommandeItemsRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CommandeFournisseurResponseDTO;
import com.houssam.pharmaTrack.enums.CommandeStatus;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.CommandeFournisseurMapper;
import com.houssam.pharmaTrack.model.*;
import com.houssam.pharmaTrack.repository.*;
import com.houssam.pharmaTrack.service.CommandeFournisseurService;
import com.houssam.pharmaTrack.service.MouvementStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final FournisseurRepository fournisseurRepository;
    private final UserRepository userRepository;
    private final MedicamentRepository medicamentRepository;
    private final CommandeFournisseurMapper commandeFournisseurMapper;
    private final MouvementStockService mouvementStockService;

    @Override
    public CommandeFournisseurResponseDTO create(CommandeFournisseurRequestDTO requestDTO) {
        log.info("Création d'une nouvelle commande fournisseur");

        // Vérifier que le fournisseur existe
        Fournisseur fournisseur = fournisseurRepository.findById(requestDTO.getFournisseurId())
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + requestDTO.getFournisseurId()));

        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // Créer la commande
        CommandeFournisseur commande = new CommandeFournisseur();
        commande.setNumeroCommande(generateNumeroCommande());
        commande.setDateCommande(requestDTO.getDateCommande() != null ?
                requestDTO.getDateCommande().toLocalDate() : LocalDate.now());
        commande.setCommandeStatus(CommandeStatus.EN_ATTENTE);
        commande.setFournisseur(fournisseur);
        commande.setUser(user);

        // Créer les items de la commande
        List<CommandeItems> items = new ArrayList<>();
        double montantTotal = 0.0;

        for (CommandeItemsRequestDTO itemDTO : requestDTO.getItems()) {
            Medicament medicament = medicamentRepository.findById(itemDTO.getMedicamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + itemDTO.getMedicamentId()));

            CommandeItems item = new CommandeItems();
            item.setMedicament(medicament);
            item.setQuantite(itemDTO.getQuantite());
            item.setPrixUnitaire(itemDTO.getPrixUnitaire().doubleValue());
            item.setQuantiteRecue(0);
            item.setCommandeFournisseur(commande);

            items.add(item);
            montantTotal += itemDTO.getQuantite() * itemDTO.getPrixUnitaire().doubleValue();
        }

        commande.setLignes(items);
        commande.setMontantTotal(montantTotal);

        // Sauvegarder la commande
        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(commande);
        log.info("Commande fournisseur créée avec succès avec l'id: {}", savedCommande.getId());

        return commandeFournisseurMapper.toResponseDTO(savedCommande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getAll() {
        log.info("Récupération de toutes les commandes fournisseurs");
        List<CommandeFournisseur> commandes = commandeFournisseurRepository.findAllByOrderByDateCommandeDesc();
        return commandeFournisseurMapper.toResponseDTOList(commandes);
    }

    @Override
    @Transactional(readOnly = true)
    public CommandeFournisseurResponseDTO getById(String id) {
        log.info("Récupération de la commande fournisseur avec l'id: {}", id);
        CommandeFournisseur commande = commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande fournisseur non trouvée avec l'id: " + id));
        return commandeFournisseurMapper.toResponseDTO(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public CommandeFournisseurResponseDTO getByNumeroCommande(String numeroCommande) {
        log.info("Récupération de la commande fournisseur avec le numéro: {}", numeroCommande);
        CommandeFournisseur commande = commandeFournisseurRepository.findByNumeroCommande(numeroCommande)
                .orElseThrow(() -> new ResourceNotFoundException("Commande fournisseur non trouvée avec le numéro: " + numeroCommande));
        return commandeFournisseurMapper.toResponseDTO(commande);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getByFournisseur(String fournisseurId) {
        log.info("Récupération des commandes du fournisseur: {}", fournisseurId);

        // Vérifier que le fournisseur existe
        fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + fournisseurId));

        List<CommandeFournisseur> commandes = commandeFournisseurRepository.findByFournisseur_Id(fournisseurId);
        return commandeFournisseurMapper.toResponseDTOList(commandes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getByStatus(CommandeStatus status) {
        log.info("Récupération des commandes avec le statut: {}", status);
        List<CommandeFournisseur> commandes = commandeFournisseurRepository.findByCommandeStatus(status);
        return commandeFournisseurMapper.toResponseDTOList(commandes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        log.info("Récupération des commandes entre {} et {}", dateDebut, dateFin);
        List<CommandeFournisseur> commandes = commandeFournisseurRepository.findByDateCommandeBetween(dateDebut, dateFin);
        return commandeFournisseurMapper.toResponseDTOList(commandes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeFournisseurResponseDTO> getCommandesEnAttente() {
        log.info("Récupération des commandes en attente");
        List<CommandeFournisseur> commandes = commandeFournisseurRepository.findCommandesEnAttente();
        return commandeFournisseurMapper.toResponseDTOList(commandes);
    }

    @Override
    public CommandeFournisseurResponseDTO updateStatus(String id, CommandeStatus status) {
        log.info("Mise à jour du statut de la commande {} à {}", id, status);

        CommandeFournisseur commande = commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande fournisseur non trouvée avec l'id: " + id));

        commande.setCommandeStatus(status);
        CommandeFournisseur updatedCommande = commandeFournisseurRepository.save(commande);

        log.info("Statut de la commande {} mis à jour à {}", id, status);
        return commandeFournisseurMapper.toResponseDTO(updatedCommande);
    }

    @Override
    public CommandeFournisseurResponseDTO receiveCommande(String id) {
        log.info("Réception de la commande: {}", id);

        CommandeFournisseur commande = commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande fournisseur non trouvée avec l'id: " + id));

        if (commande.getCommandeStatus() != CommandeStatus.EN_ATTENTE) {
            throw new RuntimeException("Seules les commandes en attente peuvent être reçues");
        }


        // Mettre à jour le stock pour chaque item
        for (CommandeItems item : commande.getLignes()) {
            Medicament medicament = item.getMedicament();

            // Mettre à jour la quantité en stock
            medicament.setQuantiteStock(medicament.getQuantiteStock() + item.getQuantite());
            medicamentRepository.save(medicament);

            // Enregistrer le mouvement de stock via le service
            mouvementStockService.enregistrerEntree(
                    medicament,
                    item.getQuantite(),
                    "Commande " + commande.getNumeroCommande()
            );

            // Mettre à jour la quantité reçue
            item.setQuantiteRecue(item.getQuantite());
        }

        // Mettre à jour le statut de la commande
        commande.setCommandeStatus(CommandeStatus.LIVREE);
        CommandeFournisseur updatedCommande = commandeFournisseurRepository.save(commande);

        log.info("Commande {} reçue avec succès", id);
        return commandeFournisseurMapper.toResponseDTO(updatedCommande);
    }

    @Override
    public void delete(String id) {
        log.info("Suppression de la commande fournisseur: {}", id);

        CommandeFournisseur commande = commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande fournisseur non trouvée avec l'id: " + id));

        // Vérifier que la commande peut être supprimée (seulement si en attente ou annulée)
        if (commande.getCommandeStatus() == CommandeStatus.LIVREE) {
            throw new RuntimeException("Impossible de supprimer une commande livrée");
        }

        commandeFournisseurRepository.delete(commande);
        log.info("Commande fournisseur {} supprimée avec succès", id);
    }

    // Méthode helper pour générer le numéro de commande
    private String generateNumeroCommande() {
        LocalDate now = LocalDate.now();
        String datePrefix = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = commandeFournisseurRepository.count() + 1;
        return "CMD-" + datePrefix + "-" + String.format("%04d", count);
    }
}
