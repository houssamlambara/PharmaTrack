import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env/environment';
import { CategorieRequestDTO, CategorieResponseDTO } from '../models/categorie.model';

@Injectable({
    providedIn: 'root'
})
export class CategorieService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/categories`;

    getAllCategories(): Observable<{ data: CategorieResponseDTO[] }> {
        return this.http.get<{ data: CategorieResponseDTO[] }>(this.apiUrl);
    }

    createCategory(category: CategorieRequestDTO): Observable<{ data: CategorieResponseDTO }> {
        return this.http.post<{ data: CategorieResponseDTO }>(this.apiUrl, category);
    }

    deleteCategory(id: string): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${id}`);
    }
}
