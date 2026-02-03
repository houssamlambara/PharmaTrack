package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.requestDTO.VenteRequestDTO;
import com.houssam.pharmaTrack.dto.responseDTO.VenteResponseDTO;
import com.houssam.pharmaTrack.enums.PaiementMethode;

import java.time.LocalDateTime;
import java.util.List;

public interface VenteService {

    VenteResponseDTO create(VenteRequestDTO requestDTO);

    List<VenteResponseDTO> getAll();

    VenteResponseDTO getById(String id);

    VenteResponseDTO getByNumeroVente(String numeroVente);

    List<VenteResponseDTO> getByUser(String userId);

    List<VenteResponseDTO> getByMethodePaiement(PaiementMethode methodePaiement);

    List<VenteResponseDTO> getByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin);

    List<VenteResponseDTO> getVentesAujourdhui();

    void delete(String id);
}
