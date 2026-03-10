export enum MovementType {
    ENTREE = 'ENTREE',
    SORTIE = 'SORTIE',
    AJUSTEMENT = 'AJUSTEMENT'
}

export interface MouvementStockRequestDTO {
    medicamentId: string;
    quantite: number;
    type: MovementType;
    motif?: string;
}

export interface MouvementStockResponseDTO {
    id: string;
    medicamentId: string;
    medicamentNom: string;
    userId: string;
    userNom: string;
    userPrenom: string;
    type: MovementType;
    quantite: number;
    stockAvant: number;
    stockApres: number;
    motif: string;
    dateMouvement: string;
}
