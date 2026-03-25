package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.FournisseurMapper;
import com.houssam.pharmaTrack.model.Fournisseur;
import com.houssam.pharmaTrack.repository.FournisseurRepository;
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
class FournisseurServiceImplTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @Mock
    private FournisseurMapper fournisseurMapper;

    @InjectMocks
    private FournisseurServiceImpl fournisseurService;

    @Test
    void create_ShouldReturnSavedFournisseur() {
        FournisseurRequestDTO request = new FournisseurRequestDTO();
        request.setNom("Pharma Plus");
        request.setEmail("contact@pharmaplus.com");
        request.setTelephone("0123456789");

        Fournisseur fournisseur = Fournisseur.builder()
                .nom("Pharma Plus")
                .email("contact@pharmaplus.com")
                .telephone("0123456789")
                .build();

        Fournisseur savedFournisseur = Fournisseur.builder()
                .id("1")
                .nom("Pharma Plus")
                .email("contact@pharmaplus.com")
                .telephone("0123456789")
                .build();

        FournisseurResponseDTO response = FournisseurResponseDTO.builder()
                .id("1")
                .nom("Pharma Plus")
                .email("contact@pharmaplus.com")
                .telephone("0123456789")
                .build();

        when(fournisseurRepository.existsByEmail("contact@pharmaplus.com")).thenReturn(false);
        when(fournisseurRepository.existsByTelephone("0123456789")).thenReturn(false);
        when(fournisseurMapper.toEntity(request)).thenReturn(fournisseur);
        when(fournisseurRepository.save(fournisseur)).thenReturn(savedFournisseur);
        when(fournisseurMapper.toResponseDTO(savedFournisseur)).thenReturn(response);


        FournisseurResponseDTO result = fournisseurService.create(request);

        assertNotNull(result);
        assertEquals("Pharma Plus", result.getNom());
        assertEquals("contact@pharmaplus.com", result.getEmail());
        verify(fournisseurRepository).save(fournisseur);
    }

    @Test
    void create_WhenEmailExists_ShouldThrowException() {
        FournisseurRequestDTO request = new FournisseurRequestDTO();
        request.setEmail("existing@email.com");
        request.setTelephone("0987654321");

        when(fournisseurRepository.existsByEmail("existing@email.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> fournisseurService.create(request));
        verify(fournisseurRepository, never()).save(any());
    }

    @Test
    void create_WhenTelephoneExists_ShouldThrowException() {
        FournisseurRequestDTO request = new FournisseurRequestDTO();
        request.setEmail("new@email.com");
        request.setTelephone("0123456789");

        when(fournisseurRepository.existsByEmail("new@email.com")).thenReturn(false);
        when(fournisseurRepository.existsByTelephone("0123456789")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> fournisseurService.create(request));
        verify(fournisseurRepository, never()).save(any());
    }

    @Test
    void getById_WhenExists_ShouldReturnFournisseur() {
        String id = "1";
        Fournisseur fournisseur = Fournisseur.builder().id(id).nom("Pharma Plus").build();
        FournisseurResponseDTO response = FournisseurResponseDTO.builder().id(id).nom("Pharma Plus").build();

        when(fournisseurRepository.findById(id)).thenReturn(Optional.of(fournisseur));
        when(fournisseurMapper.toResponseDTO(fournisseur)).thenReturn(response);

        FournisseurResponseDTO result = fournisseurService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Pharma Plus", result.getNom());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowResourceNotFoundException() {
        String id = "999";
        when(fournisseurRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fournisseurService.getById(id));
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        String id = "1";
        Fournisseur fournisseur = Fournisseur.builder().id(id).build();

        when(fournisseurRepository.findById(id)).thenReturn(Optional.of(fournisseur));
        fournisseurService.delete(id);
        verify(fournisseurRepository).delete(fournisseur);
    }

    @Test
    void toggleActif_ShouldToggleFournisseurStatus() {
        String id = "1";
        Fournisseur fournisseur = Fournisseur.builder().id(id).actif(true).build();
        Fournisseur updatedFournisseur = Fournisseur.builder().id(id).actif(false).build();
        FournisseurResponseDTO response = FournisseurResponseDTO.builder().id(id).build();

        when(fournisseurRepository.findById(id)).thenReturn(Optional.of(fournisseur));
        when(fournisseurRepository.save(any())).thenReturn(updatedFournisseur);
        when(fournisseurMapper.toResponseDTO(updatedFournisseur)).thenReturn(response);

        FournisseurResponseDTO result = fournisseurService.toggleActif(id);

        assertNotNull(result);
        verify(fournisseurRepository).save(any());
    }
}

