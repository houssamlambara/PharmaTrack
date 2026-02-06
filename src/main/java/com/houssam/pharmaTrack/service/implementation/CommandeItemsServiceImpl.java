package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.responseDTO.CommandeItemsResponseDTO;
import com.houssam.pharmaTrack.exception.ResourceNotFoundException;
import com.houssam.pharmaTrack.mapper.CommandeItemsMapper;
import com.houssam.pharmaTrack.model.CommandeItems;
import com.houssam.pharmaTrack.repository.CommandeItemsRepository;
import com.houssam.pharmaTrack.service.CommandeItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommandeItemsServiceImpl implements CommandeItemsService {

    private final CommandeItemsRepository commandeItemsRepository;
    private final CommandeItemsMapper commandeItemsMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommandeItemsResponseDTO> getByCommande(String commandeId) {
        log.info("Récupération des items de la commande: {}", commandeId);
        List<CommandeItems> items = commandeItemsRepository.findByCommandeFournisseur_Id(commandeId);
        return commandeItemsMapper.toResponseDTOList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommandeItemsResponseDTO> getByMedicament(String medicamentId) {
        log.info("Récupération des items du médicament: {}", medicamentId);
        List<CommandeItems> items = commandeItemsRepository.findByMedicament_Id(medicamentId);
        return commandeItemsMapper.toResponseDTOList(items);
    }

    @Override
    public CommandeItemsResponseDTO updateQuantiteRecue(String id, Integer quantiteRecue) {
        log.info("Mise à jour de la quantité reçue de l'item {} à {}", id, quantiteRecue);

        CommandeItems item = commandeItemsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de commande non trouvé avec l'id: " + id));

        if (quantiteRecue < 0) {
            throw new RuntimeException("La quantité reçue ne peut pas être négative");
        }

        if (quantiteRecue > item.getQuantite()) {
            throw new RuntimeException("La quantité reçue ne peut pas être supérieure à la quantité commandée");
        }

        item.setQuantiteRecue(quantiteRecue);
        CommandeItems updatedItem = commandeItemsRepository.save(item);

        log.info("Quantité reçue de l'item {} mise à jour", id);
        return commandeItemsMapper.toResponseDTO(updatedItem);
    }
}
