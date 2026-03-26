package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.VenteItemsRequestDTO;
import com.houssam.pharmaTrack.dto.requestDTO.VenteRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.VenteResponseDTO;
import com.houssam.pharmaTrack.enums.PaiementMethode;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.VenteMapper;
import com.houssam.pharmaTrack.model.Medicament;
import com.houssam.pharmaTrack.model.User;
import com.houssam.pharmaTrack.model.Vente;
import com.houssam.pharmaTrack.model.VenteItems;
import com.houssam.pharmaTrack.repository.MedicamentRepository;
import com.houssam.pharmaTrack.repository.UserRepository;
import com.houssam.pharmaTrack.repository.VenteRepository;
import com.houssam.pharmaTrack.service.MouvementStockService;
import com.houssam.pharmaTrack.service.VenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final MedicamentRepository medicamentRepository;
    private final UserRepository userRepository;
    private final VenteMapper venteMapper;
    private final MouvementStockService mouvementStockService;

    @Override
    public VenteResponseDTO create(VenteRequestDTO requestDTO) {
        log.info("Création d'une nouvelle vente");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Vente vente = new Vente();
        vente.setNumeroVente(generateNumeroVente());
        vente.setDateHeure(LocalDateTime.now());
        vente.setPaiementMode(requestDTO.getMethodePaiement());
        vente.setUser(user);
        vente.setRemise(0.0);

        List<VenteItems> items = new ArrayList<>();
        double montantTotal = 0.0;

        for (VenteItemsRequestDTO itemDTO : requestDTO.getItems()) {
            Medicament medicament = medicamentRepository.findById(itemDTO.getMedicamentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + itemDTO.getMedicamentId()));

            if (medicament.getQuantiteStock() < itemDTO.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour " + medicament.getNom() +
                        ". Stock disponible: " + medicament.getQuantiteStock() +
                        ", demandé: " + itemDTO.getQuantite());
            }

            VenteItems item = VenteItems.builder()
                    .medicament(medicament)
                    .quantite(itemDTO.getQuantite())
                    .prixUnitaire(medicament.getPrixUnitaire().doubleValue())
                    .sousTotal(itemDTO.getQuantite() * medicament.getPrixUnitaire().doubleValue())
                    .vente(vente)
                    .build();

            items.add(item);
            montantTotal += item.getSousTotal();

            medicament.setQuantiteStock(medicament.getQuantiteStock() - itemDTO.getQuantite());
            medicamentRepository.save(medicament);

            mouvementStockService.enregistrerSortie(
                    medicament,
                    itemDTO.getQuantite(),
                    "Vente " + vente.getNumeroVente()
            );

            log.info("Stock mis à jour pour {} : {} unités vendues",
                    medicament.getNom(), itemDTO.getQuantite());
        }

        vente.setLignes(items);
        vente.setMontantTotal(montantTotal);
        vente.setMontantFinal(montantTotal - vente.getRemise());

        // Sauvegarder la vente
        Vente savedVente = venteRepository.save(vente);
        log.info("Vente créée avec succès : {} - Montant: {} DH",
                savedVente.getNumeroVente(), savedVente.getMontantFinal());

        return venteMapper.toResponseDTO(savedVente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteResponseDTO> getAll() {
        log.info("Récupération de toutes les ventes");
        List<Vente> ventes = venteRepository.findAllByOrderByDateHeureDesc();
        return venteMapper.toResponseDTOList(ventes);
    }

    @Override
    @Transactional(readOnly = true)
    public VenteResponseDTO getById(String id) {
        log.info("Récupération de la vente avec l'id: {}", id);
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée avec l'id: " + id));
        return venteMapper.toResponseDTO(vente);
    }

    @Override
    @Transactional(readOnly = true)
    public VenteResponseDTO getByNumeroVente(String numeroVente) {
        log.info("Récupération de la vente avec le numéro: {}", numeroVente);
        Vente vente = venteRepository.findByNumeroVente(numeroVente)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée avec le numéro: " + numeroVente));
        return venteMapper.toResponseDTO(vente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteResponseDTO> getByUser(String userId) {
        log.info("Récupération des ventes de l'utilisateur: {}", userId);

        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + userId));

        List<Vente> ventes = venteRepository.findByUser_Id(userId);
        return venteMapper.toResponseDTOList(ventes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteResponseDTO> getByMethodePaiement(PaiementMethode methodePaiement) {
        log.info("Récupération des ventes par méthode de paiement: {}", methodePaiement);
        List<Vente> ventes = venteRepository.findByPaiementMode(methodePaiement);
        return venteMapper.toResponseDTOList(ventes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteResponseDTO> getByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Récupération des ventes entre {} et {}", dateDebut, dateFin);
        List<Vente> ventes = venteRepository.findByDateHeureBetween(dateDebut, dateFin);
        return venteMapper.toResponseDTOList(ventes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteResponseDTO> getVentesAujourdhui() {
        log.info("Récupération des ventes du jour");
        List<Vente> ventes = venteRepository.findVentesAujourdhui();
        return venteMapper.toResponseDTOList(ventes);
    }

    @Override
    public void delete(String id) {
        log.info("Suppression de la vente: {}", id);

        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée avec l'id: " + id));

        log.warn("Suppression d'une vente : {}. Attention, le stock ne sera pas restauré automatiquement.",
                vente.getNumeroVente());

        venteRepository.delete(vente);
        log.info("Vente {} supprimée", id);
    }


    private String generateNumeroVente() {
        LocalDateTime now = LocalDateTime.now();
        String datePrefix = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = venteRepository.count() + 1;
        return "V-" + datePrefix + "-" + String.format("%04d", count);
    }
}
