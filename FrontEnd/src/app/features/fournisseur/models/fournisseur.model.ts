export interface FournisseurResponseDTO {
    id: string;
    nom: string;
    email: string;
    telephone: string;
    adresse: string;
    actif: boolean;
    dateCreation?: string;
    dateModification?: string;
}

export interface FournisseurRequestDTO {
    nom: string;
    email: string;
    telephone: string;
    adresse: string;
}
