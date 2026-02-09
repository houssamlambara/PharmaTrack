package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.MouvementStockRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.MouvementStockResponseDTO;
import com.houssam.pharmaTrack.enums.MovementType;
import com.houssam.pharmaTrack.model.Medicament;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MouvementStockService {

    MouvementStockResponseDTO create(MouvementStockRequestDTO requestDTO);

    void enregistrerEntree(Medicament medicament, Integer quantite, String reference);

    void enregistrerSortie(Medicament medicament, Integer quantite, String reference);

    List<MouvementStockResponseDTO> getAll();

    MouvementStockResponseDTO getById(String id);

    List<MouvementStockResponseDTO> getByMedicament(String medicamentId);

    List<MouvementStockResponseDTO> getByType(MovementType type);

    List<MouvementStockResponseDTO> getByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    List<MouvementStockResponseDTO> getByUser(String userId);
}
