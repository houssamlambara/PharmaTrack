import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { CommandeFournisseurRequestDTO, CommandeFournisseurResponseDTO } from '../models/commande.model';

@Injectable({
    providedIn: 'root'
})
export class CommandeFournisseurService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/commandes-fournisseurs`;

    getAllCommandes(): Observable<{ data: CommandeFournisseurResponseDTO[] }> {
        return this.http.get<{ data: CommandeFournisseurResponseDTO[] }>(this.apiUrl);
    }

    getCommandeById(id: string): Observable<{ data: CommandeFournisseurResponseDTO }> {
        return this.http.get<{ data: CommandeFournisseurResponseDTO }>(`${this.apiUrl}/${id}`);
    }

    createCommande(commande: CommandeFournisseurRequestDTO): Observable<{ data: CommandeFournisseurResponseDTO }> {
        return this.http.post<{ data: CommandeFournisseurResponseDTO }>(this.apiUrl, commande);
    }

    getByFournisseur(fournisseurId: string): Observable<{ data: CommandeFournisseurResponseDTO[] }> {
        return this.http.get<{ data: CommandeFournisseurResponseDTO[] }>(`${this.apiUrl}/fournisseur/${fournisseurId}`);
    }

    getByStatus(status: string): Observable<{ data: CommandeFournisseurResponseDTO[] }> {
        return this.http.get<{ data: CommandeFournisseurResponseDTO[] }>(`${this.apiUrl}/status/${status}`);
    }

    getCommandesEnAttente(): Observable<{ data: CommandeFournisseurResponseDTO[] }> {
        return this.http.get<{ data: CommandeFournisseurResponseDTO[] }>(`${this.apiUrl}/en-attente`);
    }

    receiveCommande(id: string): Observable<{ data: CommandeFournisseurResponseDTO }> {
        return this.http.post<{ data: CommandeFournisseurResponseDTO }>(`${this.apiUrl}/${id}/recevoir`, {});
    }

    cancelCommande(id: string): Observable<{ data: CommandeFournisseurResponseDTO }> {
        return this.http.put<{ data: CommandeFournisseurResponseDTO }>(`${this.apiUrl}/${id}/annuler`, {});
    }

    deleteCommande(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }
}
