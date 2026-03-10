import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { FournisseurRequestDTO, FournisseurResponseDTO } from '../models/fournisseur.model';

@Injectable({
    providedIn: 'root'
})
export class FournisseurService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/fournisseurs`;

    getAllFournisseurs(): Observable<{ data: FournisseurResponseDTO[] }> {
        return this.http.get<{ data: FournisseurResponseDTO[] }>(this.apiUrl);
    }

    getActifs(): Observable<{ data: FournisseurResponseDTO[] }> {
        return this.http.get<{ data: FournisseurResponseDTO[] }>(`${this.apiUrl}/actifs`);
    }

    getFournisseurById(id: string): Observable<{ data: FournisseurResponseDTO }> {
        return this.http.get<{ data: FournisseurResponseDTO }>(`${this.apiUrl}/${id}`);
    }

    createFournisseur(fournisseur: FournisseurRequestDTO): Observable<{ data: FournisseurResponseDTO }> {
        return this.http.post<{ data: FournisseurResponseDTO }>(this.apiUrl, fournisseur);
    }

    updateFournisseur(id: string, fournisseur: FournisseurRequestDTO): Observable<{ data: FournisseurResponseDTO }> {
        return this.http.put<{ data: FournisseurResponseDTO }>(`${this.apiUrl}/${id}`, fournisseur);
    }

    toggleActif(id: string): Observable<{ data: FournisseurResponseDTO }> {
        return this.http.put<{ data: FournisseurResponseDTO }>(`${this.apiUrl}/${id}/toggle-actif`, {});
    }

    deleteFournisseur(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }

    searchFournisseurs(nom: string): Observable<{ data: FournisseurResponseDTO[] }> {
        let params = new HttpParams().set('nom', nom);
        return this.http.get<{ data: FournisseurResponseDTO[] }>(`${this.apiUrl}/search`, { params });
    }
}
