export interface VenteItemsRequestDTO {
    medicamentId: string;
    quantite: number;
}

export interface VenteItemsResponseDTO {
    id: string;
    medicamentId: string;
    medicamentNom: string;
    medicamentDosage: string;
    quantite: number;
    prixUnitaire: number;
    montantTotal: number;
}

export interface VenteRequestDTO {
    clientNom?: string;
    methodePaiement: 'ESPECE' | 'CARTE_BANCAIRE';
    items: VenteItemsRequestDTO[];
}

export interface VenteResponseDTO {
    id: string;
    clientNom: string;
    userId: string;
    userNom: string;
    userPrenom: string;
    dateVente: string;
    methodePaiement: 'ESPECE' | 'CARTE_BANCAIRE';
    items: VenteItemsResponseDTO[];
    montantTotal: number;
    nombreArticles: number;
}
