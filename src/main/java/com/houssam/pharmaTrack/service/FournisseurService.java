package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.FournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.FournisseurResponseDTO;

import java.util.List;

public interface FournisseurService {

    FournisseurResponseDTO create(FournisseurRequestDTO requestDTO);

    List<FournisseurResponseDTO> getAll();

    FournisseurResponseDTO getById(String id);

    FournisseurResponseDTO update(String id, FournisseurRequestDTO requestDTO);

    void delete(String id);

    List<FournisseurResponseDTO> searchByNom(String nom);

    List<FournisseurResponseDTO> getActifs();

    FournisseurResponseDTO toggleActif(String id);
}
