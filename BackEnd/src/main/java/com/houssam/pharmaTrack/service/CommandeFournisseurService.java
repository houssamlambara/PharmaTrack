package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.CommandeFournisseurRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.CommandeFournisseurResponseDTO;
import com.houssam.pharmaTrack.enums.CommandeStatus;

import java.time.LocalDate;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurResponseDTO create(CommandeFournisseurRequestDTO requestDTO);

    List<CommandeFournisseurResponseDTO> getAll();

    CommandeFournisseurResponseDTO getById(String id);

    CommandeFournisseurResponseDTO getByNumeroCommande(String numeroCommande);

    List<CommandeFournisseurResponseDTO> getByFournisseur(String fournisseurId);

    List<CommandeFournisseurResponseDTO> getByStatus(CommandeStatus status);

    List<CommandeFournisseurResponseDTO> getByDateRange(LocalDate dateDebut, LocalDate dateFin);

    List<CommandeFournisseurResponseDTO> getCommandesEnAttente();

    CommandeFournisseurResponseDTO updateStatus(String id, CommandeStatus status);

    CommandeFournisseurResponseDTO receiveCommande(String id);

    void delete(String id);
}
