export interface UserResponseDTO {
    id: string;
    nom: string;
    prenom: string;
    email: string;
    role: string;
    actif: boolean;
    dateCreation: string;
    nombreVentes?: number;
    nombreCommandes?: number;
    nombreMouvements?: number;
}
