package com.houssam.pharmaTrack.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteStatisticsDTO {
    private LocalDate date;
    private Long nombreVentes;
    private BigDecimal chiffreAffaire;
    private Integer nombreArticlesVendus;
    private BigDecimal ticketMoyen; // CA / nombre de ventes
}

