package com.houssam.pharmaTrack.service.implementation;
import com.houssam.pharmaTrack.dto.requestDTO.MouvementStockRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MouvementStockResponseDTO;
import com.houssam.pharmaTrack.enums.MovementType;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.MouvementStockMapper;
import com.houssam.pharmaTrack.model.Medicament;
import com.houssam.pharmaTrack.model.MouvementStock;
import com.houssam.pharmaTrack.model.User;
import com.houssam.pharmaTrack.repository.MedicamentRepository;
import com.houssam.pharmaTrack.repository.MouvementStockRepository;
import com.houssam.pharmaTrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MouvementStockServiceImpl implements com.houssam.pharmaTrack.service.MouvementStockService {
    private final MouvementStockRepository mouvementStockRepository;
    private final MedicamentRepository medicamentRepository;
    private final UserRepository userRepository;
    private final MouvementStockMapper mouvementStockMapper;

    @Override
    public MouvementStockResponseDTO create(MouvementStockRequestDTO requestDTO) {
        log.info("Création d'un mouvement de stock manuel : type={}, medicamentId={}",
                requestDTO.getType(), requestDTO.getMedicamentId());
        Medicament medicament = medicamentRepository.findById(requestDTO.getMedicamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + requestDTO.getMedicamentId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        if ((requestDTO.getType() == MovementType.PERTE || requestDTO.getType() == MovementType.PEREMPTION)
                && (requestDTO.getMotif() == null || requestDTO.getMotif().trim().isEmpty())) {
            throw new RuntimeException("Le motif est obligatoire pour les mouvements de type " + requestDTO.getType());
        }
        Integer stockAvant = medicament.getQuantiteStock();
        MouvementStock mouvement = MouvementStock.builder()
                .medicament(medicament)
                .type(requestDTO.getType())
                .quantite(requestDTO.getQuantite())
                .dateHeure(LocalDateTime.now())
                .description(requestDTO.getMotif())
                .reference("MANUEL-" + System.currentTimeMillis())
                .user(user)
                .build();
        Integer nouveauStock = calculerNouveauStock(medicament, requestDTO.getType(), requestDTO.getQuantite());
        medicament.setQuantiteStock(nouveauStock);
        medicamentRepository.save(medicament);
        MouvementStock savedMouvement = mouvementStockRepository.save(mouvement);
        log.info("Mouvement de stock créé avec succès : id={}, stock {} -> {}",
                savedMouvement.getId(), stockAvant, nouveauStock);
        MouvementStockResponseDTO response = mouvementStockMapper.toResponseDTO(savedMouvement);
        response.setStockAvant(stockAvant);
        response.setStockApres(nouveauStock);
        return response;
    }

    @Override
    public void enregistrerEntree(Medicament medicament, Integer quantite, String reference) {
        log.info("Enregistrement d'une entrée de stock : medicament={}, quantite={}",
                medicament.getNom(), quantite);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        MouvementStock mouvement = MouvementStock.builder()
                .medicament(medicament)
                .type(MovementType.ENTREE)
                .quantite(quantite)
                .dateHeure(LocalDateTime.now())
                .reference(reference)
                .description("Entrée de stock automatique")
                .user(user)
                .build();
        mouvementStockRepository.save(mouvement);
        log.info("Entrée de stock enregistrée : {}", reference);
    }

    @Override
    public void enregistrerSortie(Medicament medicament, Integer quantite, String reference) {
        log.info("Enregistrement d'une sortie de stock : medicament={}, quantite={}",
                medicament.getNom(), quantite);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        MouvementStock mouvement = MouvementStock.builder()
                .medicament(medicament)
                .type(MovementType.SORTIE)
                .quantite(quantite)
                .dateHeure(LocalDateTime.now())
                .reference(reference)
                .description("Sortie de stock automatique")
                .user(user)
                .build();
        mouvementStockRepository.save(mouvement);
        log.info("Sortie de stock enregistrée : {}", reference);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getAll() {
        log.info("Récupération de tous les mouvements de stock");
        List<MouvementStock> mouvements = mouvementStockRepository.findAllByOrderByDateHeureDesc();
        return mouvements.stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MouvementStockResponseDTO getById(String id) {
        log.info("Récupération du mouvement de stock avec l'id: {}", id);
        MouvementStock mouvement = mouvementStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mouvement de stock non trouvé avec l'id: " + id));
        return buildResponseWithStock(mouvement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getByMedicament(String medicamentId) {
        log.info("Récupération des mouvements du médicament: {}", medicamentId);
        medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + medicamentId));
        List<MouvementStock> mouvements = mouvementStockRepository.findByMedicament_IdOrderByDateHeureDesc(medicamentId);
        return mouvements.stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getByType(MovementType type) {
        log.info("Récupération des mouvements de type: {}", type);
        List<MouvementStock> mouvements = mouvementStockRepository.findByType(type);
        return mouvements.stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        log.info("Récupération des mouvements entre {} et {}", dateDebut, dateFin);
        List<MouvementStock> mouvements = mouvementStockRepository.findByDateHeureBetween(dateDebut, dateFin);
        return mouvements.stream()
                .map(this::buildResponseWithStock)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getByUser(String userId) {
        log.info("Récupération des mouvements de l'utilisateur: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + userId));
        List<MouvementStock> mouvements = mouvementStockRepository.findByUser_Id(userId);
        return mouvements.stream()
                .map(this::buildResponseWithStock)
                .toList();
    }


    private Integer calculerNouveauStock(Medicament medicament, MovementType type, Integer quantite) {
        Integer stockActuel = medicament.getQuantiteStock();
        return switch (type) {
            case ENTREE, ACHAT -> stockActuel + quantite;
            case SORTIE, VENTE, PERTE, PEREMPTION -> {
                Integer nouveauStock = stockActuel - quantite;
                if (nouveauStock < 0) {
                    throw new RuntimeException("Stock insuffisant pour " + medicament.getNom() +
                            ". Stock actuel: " + stockActuel + ", demandé: " + quantite);
                }
                yield nouveauStock;
            }
        };
    }
    /**
     * Construit la réponse DTO avec les informations de stock avant/après
     */
    private MouvementStockResponseDTO buildResponseWithStock(MouvementStock mouvement) {
        MouvementStockResponseDTO response = mouvementStockMapper.toResponseDTO(mouvement);
        response.setStockAvant(null);
        response.setStockApres(null);
        return response;
    }
}
