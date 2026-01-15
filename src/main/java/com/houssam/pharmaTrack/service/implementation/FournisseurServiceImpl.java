package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.FournisseurMapper;
import com.houssam.pharmaTrack.model.Fournisseur;
import com.houssam.pharmaTrack.repository.FournisseurRepository;
import com.houssam.pharmaTrack.service.FournisseurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    @Override
    public FournisseurResponseDTO create(FournisseurRequestDTO requestDTO) {
        // Vérifier si l'email existe déjà
        if (fournisseurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Un fournisseur avec cet email existe déjà");
        }

        // Vérifier si le téléphone existe déjà
        if (fournisseurRepository.existsByTelephone(requestDTO.getTelephone())) {
            throw new RuntimeException("Un fournisseur avec ce téléphone existe déjà");
        }

        Fournisseur fournisseur = fournisseurMapper.toEntity(requestDTO);
        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);

        return fournisseurMapper.toResponseDTO(savedFournisseur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FournisseurResponseDTO> getAll() {
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        return fournisseurMapper.toResponseDTOList(fournisseurs);
    }

    @Override
    @Transactional(readOnly = true)
    public FournisseurResponseDTO getById(String id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + id));
        return fournisseurMapper.toResponseDTO(fournisseur);
    }

    @Override
    public FournisseurResponseDTO update(String id, FournisseurRequestDTO requestDTO) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + id));

        // Vérifier si le nouvel email existe déjà (sauf si c'est le même)
        if (!fournisseur.getEmail().equals(requestDTO.getEmail()) &&
            fournisseurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Un fournisseur avec cet email existe déjà");
        }

        // Vérifier si le nouveau téléphone existe déjà (sauf si c'est le même)
        if (!fournisseur.getTelephone().equals(requestDTO.getTelephone()) &&
            fournisseurRepository.existsByTelephone(requestDTO.getTelephone())) {
            throw new RuntimeException("Un fournisseur avec ce téléphone existe déjà");
        }

        fournisseurMapper.updateEntityFromDTO(requestDTO, fournisseur);
        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);

        return fournisseurMapper.toResponseDTO(updatedFournisseur);
    }

    @Override
    public void delete(String id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + id));

        // Vérifier s'il y a des commandes pour ce fournisseur
        if (fournisseur.getCommandes() != null && !fournisseur.getCommandes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer ce fournisseur car il a des commandes associées");
        }

        fournisseurRepository.delete(fournisseur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FournisseurResponseDTO> searchByNom(String nom) {
        List<Fournisseur> fournisseurs = fournisseurRepository.findByNomContainingIgnoreCase(nom);
        return fournisseurMapper.toResponseDTOList(fournisseurs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FournisseurResponseDTO> getActifs() {
        List<Fournisseur> fournisseurs = fournisseurRepository.findByActif(true);
        return fournisseurMapper.toResponseDTOList(fournisseurs);
    }

    @Override
    public FournisseurResponseDTO toggleActif(String id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'id: " + id));

        fournisseur.setActif(!fournisseur.getActif());
        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);

        return fournisseurMapper.toResponseDTO(updatedFournisseur);
    }
}
