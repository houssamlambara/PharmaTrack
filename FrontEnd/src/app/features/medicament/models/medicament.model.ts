export interface MedicamentResponseDTO {
    id: string;
    nom: string;
    codeBarres: string;
    description: string;
    dosage: string;
    forme: string;
    prixUnitaire: number;
    quantiteStock: number;
    seuilAlerte: number;
    dateExpiration: string;
    actif: boolean;
    dateCreation: string;
    categorieId: string;
    categorieNom: string;
    enRuptureStock: boolean;
    alerteStock: boolean;
    expire: boolean;
}

export interface MedicamentRequestDTO {
    nom: string;
    codeBarres?: string;
    description?: string;
    dosage: string;
    forme: string;
    prixUnitaire: number;
    categorieId: string;
    seuilAlerte?: number;
    dateExpiration?: string;
}
