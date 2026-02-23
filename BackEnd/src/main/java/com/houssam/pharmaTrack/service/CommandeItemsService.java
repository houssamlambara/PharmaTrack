package com.houssam.pharmaTrack.service;

import com.houssam.pharmaTrack.dto.responseDTO.CommandeItemsResponseDTO;

import java.util.List;

public interface CommandeItemsService {

    List<CommandeItemsResponseDTO> getByCommande(String commandeId);

    List<CommandeItemsResponseDTO> getByMedicament(String medicamentId);

    CommandeItemsResponseDTO updateQuantiteRecue(String id, Integer quantiteRecue);
}
