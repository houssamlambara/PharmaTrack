import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { VenteRequestDTO, VenteResponseDTO } from '../models/vente.model';

@Injectable({
    providedIn: 'root'
})
export class VenteService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/ventes`;

    getAllVentes(): Observable<{ data: VenteResponseDTO[] }> {
        return this.http.get<{ data: VenteResponseDTO[] }>(this.apiUrl);
    }

    getVenteById(id: string): Observable<{ data: VenteResponseDTO }> {
        return this.http.get<{ data: VenteResponseDTO }>(`${this.apiUrl}/${id}`);
    }

    createVente(vente: VenteRequestDTO): Observable<{ data: VenteResponseDTO }> {
        return this.http.post<{ data: VenteResponseDTO }>(this.apiUrl, vente);
    }

    getVentesAujourdhui(): Observable<{ data: VenteResponseDTO[] }> {
        return this.http.get<{ data: VenteResponseDTO[] }>(`${this.apiUrl}/aujourdhui`);
    }

    getByMethodePaiement(methode: string): Observable<{ data: VenteResponseDTO[] }> {
        return this.http.get<{ data: VenteResponseDTO[] }>(`${this.apiUrl}/methode-paiement/${methode}`);
    }

    getByUser(userId: string): Observable<{ data: VenteResponseDTO[] }> {
        return this.http.get<{ data: VenteResponseDTO[] }>(`${this.apiUrl}/user/${userId}`);
    }

    deleteVente(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }
}
