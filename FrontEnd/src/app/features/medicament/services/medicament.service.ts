import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { MedicamentRequestDTO, MedicamentResponseDTO } from '../models/medicament.model';

@Injectable({
    providedIn: 'root'
})
export class MedicamentService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/medicaments`;

    getAllMedicaments(): Observable<{ data: MedicamentResponseDTO[] }> {
        return this.http.get<{ data: MedicamentResponseDTO[] }>(this.apiUrl);
    }

    getMedicamentById(id: string): Observable<{ data: MedicamentResponseDTO }> {
        return this.http.get<{ data: MedicamentResponseDTO }>(`${this.apiUrl}/${id}`);
    }

    createMedicament(medicament: MedicamentRequestDTO): Observable<{ data: MedicamentResponseDTO }> {
        return this.http.post<{ data: MedicamentResponseDTO }>(this.apiUrl, medicament);
    }

    updateMedicament(id: string, medicament: MedicamentRequestDTO): Observable<{ data: MedicamentResponseDTO }> {
        return this.http.put<{ data: MedicamentResponseDTO }>(`${this.apiUrl}/${id}`, medicament);
    }

    deleteMedicament(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }

    searchMedicaments(nom: string): Observable<{ data: MedicamentResponseDTO[] }> {
        let params = new HttpParams().set('nom', nom);
        return this.http.get<{ data: MedicamentResponseDTO[] }>(`${this.apiUrl}/search`, { params });
    }

    getByCategorie(categorieId: string): Observable<{ data: MedicamentResponseDTO[] }> {
        return this.http.get<{ data: MedicamentResponseDTO[] }>(`${this.apiUrl}/categorie/${categorieId}`);
    }

    getLowStock(): Observable<{ data: MedicamentResponseDTO[] }> {
        return this.http.get<{ data: MedicamentResponseDTO[] }>(`${this.apiUrl}/low-stock`);
    }

    getOutOfStock(): Observable<{ data: MedicamentResponseDTO[] }> {
        return this.http.get<{ data: MedicamentResponseDTO[] }>(`${this.apiUrl}/out-of-stock`);
    }

    getByCodeBarres(codeBarres: string): Observable<{ data: MedicamentResponseDTO }> {
        return this.http.get<{ data: MedicamentResponseDTO }>(`${this.apiUrl}/code-barres/${codeBarres}`);
    }

    toggleActif(id: string): Observable<{ data: MedicamentResponseDTO }> {
        return this.http.patch<{ data: MedicamentResponseDTO }>(`${this.apiUrl}/${id}/toggle-actif`, {});
    }
}
