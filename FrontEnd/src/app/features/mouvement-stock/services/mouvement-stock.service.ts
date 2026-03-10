import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { MouvementStockRequestDTO, MouvementStockResponseDTO, MovementType } from '../models/mouvement-stock.model';

@Injectable({
    providedIn: 'root'
})
export class MouvementStockService {
    private apiUrl = `${environment.apiUrl}/mouvements-stock`;
    private http = inject(HttpClient);

    createMouvement(mouvement: MouvementStockRequestDTO): Observable<MouvementStockResponseDTO> {
        return this.http.post<MouvementStockResponseDTO>(this.apiUrl, mouvement);
    }

    getAllMouvements(): Observable<MouvementStockResponseDTO[]> {
        return this.http.get<MouvementStockResponseDTO[]>(this.apiUrl);
    }

    getMouvementById(id: string): Observable<MouvementStockResponseDTO> {
        return this.http.get<MouvementStockResponseDTO>(`${this.apiUrl}/${id}`);
    }

    getMouvementsByMedicament(medicamentId: string): Observable<MouvementStockResponseDTO[]> {
        return this.http.get<MouvementStockResponseDTO[]>(`${this.apiUrl}/medicament/${medicamentId}`);
    }

    getMouvementsByType(type: MovementType): Observable<MouvementStockResponseDTO[]> {
        return this.http.get<MouvementStockResponseDTO[]>(`${this.apiUrl}/type/${type}`);
    }

    getMouvementsByPeriode(dateDebut: string, dateFin: string): Observable<MouvementStockResponseDTO[]> {
        let params = new HttpParams()
            .set('dateDebut', dateDebut)
            .set('dateFin', dateFin);
        return this.http.get<MouvementStockResponseDTO[]>(`${this.apiUrl}/periode`, { params });
    }

    getMouvementsByUser(userId: string): Observable<MouvementStockResponseDTO[]> {
        return this.http.get<MouvementStockResponseDTO[]>(`${this.apiUrl}/user/${userId}`);
    }
}
