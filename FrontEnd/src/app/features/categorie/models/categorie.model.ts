export interface CategorieResponseDTO {
    id: string;
    nom: string;
    description: string;
    dateCreation?: string;
    dateModification?: string;
}

export interface CategorieRequestDTO {
    nom: string;
    description?: string;
}
