package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.CategorieRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CategorieResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.CategorieMapper;
import com.houssam.pharmaTrack.model.Categorie;
import com.houssam.pharmaTrack.repository.CategorieRepository;
import com.houssam.pharmaTrack.service.CategorieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final CategorieMapper categorieMapper;

    @Override
    public CategorieResponseDTO create(CategorieRequestDTO requestDTO) {
        if (categorieRepository.existsByNom(requestDTO.getNom())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        Categorie categorie = categorieMapper.toEntity(requestDTO);
        Categorie savedCategorie = categorieRepository.save(categorie);

        return categorieMapper.toResponseDTO(savedCategorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorieResponseDTO> getAll() {
        List<Categorie> categories = categorieRepository.findAll();
        return categorieMapper.toResponseDTOList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategorieResponseDTO getById(String id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + id));
        return categorieMapper.toResponseDTO(categorie);
    }

    @Override
    public CategorieResponseDTO update(String id, CategorieRequestDTO requestDTO) {

        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + id));

        // Vérifier si le nouveau nom existe déjà (sauf si c'est le même)
        if (!categorie.getNom().equals(requestDTO.getNom()) &&
            categorieRepository.existsByNom(requestDTO.getNom())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        categorieMapper.updateEntityFromDTO(requestDTO, categorie);
        Categorie updatedCategorie = categorieRepository.save(categorie);

        return categorieMapper.toResponseDTO(updatedCategorie);
    }

    @Override
    public void delete(String id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée avec l'id: " + id));

        // Vérifier s'il y a des médicaments dans cette catégorie
        if (categorie.getMedicaments() != null && !categorie.getMedicaments().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer cette catégorie car elle contient des médicaments");
        }

        categorieRepository.delete(categorie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorieResponseDTO> searchByNom(String nom) {
        List<Categorie> categories = categorieRepository.findByNomContainingIgnoreCase(nom);
        return categorieMapper.toResponseDTOList(categories);
    }
}
