export interface CommandeItemsRequestDTO {
    medicamentId: string;
    quantite: number;
    prixUnitaire: number;
}

export interface CommandeItemsResponseDTO {
    id: string;
    medicamentId: string;
    medicamentNom: string;
    medicamentDosage: string;
    quantite: number;
    prixUnitaire: number;
    montantTotal: number;
}

export interface CommandeFournisseurRequestDTO {
    fournisseurId: string;
    remarque?: string;
    items: CommandeItemsRequestDTO[];
}

export interface CommandeFournisseurResponseDTO {
    id: string;
    fournisseurId: string;
    fournisseurNom: string;
    fournisseurTelephone: string;
    userId: string;
    userNom: string;
    userPrenom: string;
    dateCommande: string;
    dateReception: string;
    status: 'EN_ATTENTE' | 'LIVREE' | 'ANNULEE';
    remarque: string;
    items: CommandeItemsResponseDTO[];
    montantTotal: number;
    nombreArticles: number;
}
