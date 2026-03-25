package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.CategorieRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CategorieResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.CategorieMapper;
import com.houssam.pharmaTrack.model.Categorie;
import com.houssam.pharmaTrack.repository.CategorieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorieServiceImplTest {

    @Mock
    private CategorieRepository categorieRepository;

    @Mock
    private CategorieMapper categorieMapper;

    @InjectMocks
    private CategorieServiceImpl categorieService;

    @Test
    void create_ShouldReturnSavedCategorie() {
        CategorieRequestDTO request = new CategorieRequestDTO();
        request.setNom("Antibiotiques");
        request.setDescription("Desc");
        
        Categorie categorie = Categorie.builder()
                .nom("Antibiotiques")
                .description("Desc")
                .build();
        
        Categorie savedCategorie = Categorie.builder()
                .id("1")
                .nom("Antibiotiques")
                .description("Desc")
                .build();
        
        CategorieResponseDTO response = CategorieResponseDTO.builder()
                .id("1")
                .nom("Antibiotiques")
                .description("Desc")
                .build();

        when(categorieRepository.existsByNom("Antibiotiques")).thenReturn(false);
        when(categorieMapper.toEntity(request)).thenReturn(categorie);
        when(categorieRepository.save(categorie)).thenReturn(savedCategorie);
        when(categorieMapper.toResponseDTO(savedCategorie)).thenReturn(response);

        CategorieResponseDTO result = categorieService.create(request);

        assertNotNull(result);
        assertEquals("Antibiotiques", result.getNom());
        verify(categorieRepository).save(categorie);
    }

    @Test
    void create_WhenNameExists_ShouldThrowException() {
        CategorieRequestDTO request = new CategorieRequestDTO();
        request.setNom("Existante");

        when(categorieRepository.existsByNom("Existante")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> categorieService.create(request));
        verify(categorieRepository, never()).save(any());
    }

    @Test
    void getById_WhenExists_ShouldReturnCategorie() {
        String id = "1";
        Categorie categorie = Categorie.builder().id(id).nom("Test").build();
        CategorieResponseDTO response = CategorieResponseDTO.builder().id(id).nom("Test").build();

        when(categorieRepository.findById(id)).thenReturn(Optional.of(categorie));
        when(categorieMapper.toResponseDTO(categorie)).thenReturn(response);

        CategorieResponseDTO result = categorieService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        String id = "999";
        when(categorieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categorieService.getById(id));
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        String id = "1";
        Categorie categorie = Categorie.builder().id(id).build();

        when(categorieRepository.findById(id)).thenReturn(Optional.of(categorie));

        categorieService.delete(id);

        verify(categorieRepository).delete(categorie);
    }
}

