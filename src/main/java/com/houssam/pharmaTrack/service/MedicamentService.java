package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.MedicamentRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MedicamentResponseDTO;

import java.util.List;

public interface MedicamentService {

    MedicamentResponseDTO create(MedicamentRequestDTO requestDTO);

    List<MedicamentResponseDTO> getAll();

    MedicamentResponseDTO getById(String id);

    MedicamentResponseDTO update(String id, MedicamentRequestDTO requestDTO);

    void delete(String id);

    List<MedicamentResponseDTO> searchByNom(String nom);

    List<MedicamentResponseDTO> getByCategorie(String categorieId);

    List<MedicamentResponseDTO> getLowStockMedicaments();

    List<MedicamentResponseDTO> getOutOfStockMedicaments();

    MedicamentResponseDTO getByCodeBarres(String codeBarres);

    MedicamentResponseDTO toggleActif(String id);
}
