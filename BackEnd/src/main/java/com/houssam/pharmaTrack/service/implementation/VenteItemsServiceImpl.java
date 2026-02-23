package com.houssam.pharmaTrack.service.implementation;

import com.houssam.pharmaTrack.dto.responseDTO.VenteItemsResponseDTO;
import com.houssam.pharmaTrack.mapper.VenteItemsMapper;
import com.houssam.pharmaTrack.model.VenteItems;
import com.houssam.pharmaTrack.repository.VenteItemsRepository;
import com.houssam.pharmaTrack.service.VenteItemsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VenteItemsServiceImpl implements VenteItemsService {

    private final VenteItemsRepository venteItemsRepository;
    private final VenteItemsMapper venteItemsMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VenteItemsResponseDTO> getByVente(String venteId) {
        log.info("Récupération des items de la vente: {}", venteId);
        List<VenteItems> items = venteItemsRepository.findByVente_Id(venteId);
        return venteItemsMapper.toResponseDTOList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenteItemsResponseDTO> getByMedicament(String medicamentId) {
        log.info("Récupération des items du médicament: {}", medicamentId);
        List<VenteItems> items = venteItemsRepository.findByMedicament_Id(medicamentId);
        return venteItemsMapper.toResponseDTOList(items);
    }
}
