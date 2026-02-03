package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.responseDTO.VenteItemsResponseDTO;

import java.util.List;

public interface VenteItemsService {

    List<VenteItemsResponseDTO> getByVente(String venteId);
    List<VenteItemsResponseDTO> getByMedicament(String medicamentId);
}
