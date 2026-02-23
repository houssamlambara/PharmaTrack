package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.CategorieRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CategorieResponseDTO;

import java.util.List;

public interface CategorieService {

    CategorieResponseDTO create(CategorieRequestDTO requestDTO);

    List<CategorieResponseDTO> getAll();

    CategorieResponseDTO getById(String id);

    CategorieResponseDTO update(String id, CategorieRequestDTO requestDTO);

    void delete(String id);

    List<CategorieResponseDTO> searchByNom(String nom);
}
