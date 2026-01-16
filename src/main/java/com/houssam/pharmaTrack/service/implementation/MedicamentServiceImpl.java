package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.MedicamentRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MedicamentResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.MedicamentMapper;
import com.houssam.pharmaTrack.model.Categorie;
import com.houssam.pharmaTrack.model.Medicament;
import com.houssam.pharmaTrack.repository.CategorieRepository;
import com.houssam.pharmaTrack.repository.MedicamentRepository;
import com.houssam.pharmaTrack.service.MedicamentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final CategorieRepository categorieRepository;
    private final MedicamentMapper medicamentMapper;

    @Override
    public MedicamentResponseDTO create(MedicamentRequestDTO requestDTO) {
        log.info("Création d'un nouveau médicament: {}", requestDTO.getNom());

        // Vérifier si le médicament existe déjà
        if (medicamentRepository.existsByNom(requestDTO.getNom())) {
            throw new RuntimeException("Un médicament avec ce nom existe déjà");
        }

        // Vérifier si la catégorie existe
        Categorie categorie = categorieRepository.findById(requestDTO.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + requestDTO.getCategorieId()));

        Medicament medicament = medicamentMapper.toEntity(requestDTO);
        medicament.setCategorie(categorie);

        Medicament savedMedicament = medicamentRepository.save(medicament);
        log.info("Médicament créé avec succès avec l'id: {}", savedMedicament.getId());

        return medicamentMapper.toResponseDTO(savedMedicament);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> getAll() {
        log.info("Récupération de tous les médicaments");
        List<Medicament> medicaments = medicamentRepository.findAll();
        return medicamentMapper.toResponseDTOList(medicaments);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicamentResponseDTO getById(String id) {
        log.info("Récupération du médicament avec l'id: {}", id);
        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + id));
        return medicamentMapper.toResponseDTO(medicament);
    }

    @Override
    public MedicamentResponseDTO update(String id, MedicamentRequestDTO requestDTO) {
        log.info("Mise à jour du médicament avec l'id: {}", id);

        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + id));

        // Vérifier si le nouveau nom existe déjà (sauf si c'est le même)
        if (!medicament.getNom().equals(requestDTO.getNom()) &&
            medicamentRepository.existsByNom(requestDTO.getNom())) {
            throw new RuntimeException("Un médicament avec ce nom existe déjà");
        }

        // Vérifier si la catégorie existe si elle a changé
        if (!medicament.getCategorie().getId().equals(requestDTO.getCategorieId())) {
            Categorie categorie = categorieRepository.findById(requestDTO.getCategorieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + requestDTO.getCategorieId()));
            medicament.setCategorie(categorie);
        }

        medicamentMapper.updateEntityFromDTO(requestDTO, medicament);
        Medicament updatedMedicament = medicamentRepository.save(medicament);

        log.info("Médicament mis à jour avec succès: {}", updatedMedicament.getId());
        return medicamentMapper.toResponseDTO(updatedMedicament);
    }

    @Override
    public void delete(String id) {
        log.info("Suppression du médicament avec l'id: {}", id);

        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + id));

        // Vérifier s'il y a des mouvements de stock
        if (medicament.getMouvements() != null && !medicament.getMouvements().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce médicament car il a des mouvements de stock");
        }

        // Vérifier s'il y a des ventes
        if (medicament.getLigneventes() != null && !medicament.getLigneventes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce médicament car il a été vendu");
        }

        // Vérifier s'il y a des commandes
        if (medicament.getLigneCommnde() != null && !medicament.getLigneCommnde().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce médicament car il est dans des commandes");
        }

        medicamentRepository.delete(medicament);
        log.info("Médicament supprimé avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> searchByNom(String nom) {
        log.info("Recherche de médicaments par nom: {}", nom);
        List<Medicament> medicaments = medicamentRepository.findByNomContainingIgnoreCase(nom);
        return medicamentMapper.toResponseDTOList(medicaments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> getByCategorie(String categorieId) {
        log.info("Récupération des médicaments de la catégorie: {}", categorieId);

        // Vérifier si la catégorie existe
        categorieRepository.findById(categorieId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + categorieId));

        List<Medicament> medicaments = medicamentRepository.findByCategorie_Id(categorieId);
        return medicamentMapper.toResponseDTOList(medicaments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> getLowStockMedicaments() {
        log.info("Récupération des médicaments en alerte de stock");
        List<Medicament> medicaments = medicamentRepository.findLowStockMedicaments();
        return medicamentMapper.toResponseDTOList(medicaments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicamentResponseDTO> getOutOfStockMedicaments() {
        log.info("Récupération des médicaments en rupture de stock");
        List<Medicament> medicaments = medicamentRepository.findOutOfStockMedicaments();
        return medicamentMapper.toResponseDTOList(medicaments);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicamentResponseDTO getByCodeBarres(String codeBarres) {
        log.info("Recherche de médicament par code-barres: {}", codeBarres);
        Medicament medicament = medicamentRepository.findByCodeBarres(codeBarres)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec le code-barres: " + codeBarres));
        return medicamentMapper.toResponseDTO(medicament);
    }

    @Override
    public MedicamentResponseDTO toggleActif(String id) {
        log.info("Basculement du statut actif du médicament: {}", id);

        Medicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médicament non trouvé avec l'id: " + id));

        medicament.setActif(!medicament.isActif());
        Medicament updatedMedicament = medicamentRepository.save(medicament);

        log.info("Statut actif du médicament {} changé à: {}", id, updatedMedicament.isActif());
        return medicamentMapper.toResponseDTO(updatedMedicament);
    }
}

