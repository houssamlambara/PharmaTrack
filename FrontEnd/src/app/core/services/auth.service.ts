import { Injectable, inject, PLATFORM_ID, Optional } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../env/environment';

export interface AuthResponse {
    accessToken: string;
    nom: string;
    prenom: string;
    role: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);
    private platformId = inject(PLATFORM_ID);
    private tokenKey = 'pharmatrack_auth_token';

    // Manage current user state Reactively
    private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor() {
        this.loadUserFromToken();
    }

    login(credentials: any): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, credentials)
            .pipe(
                tap(response => {
                    this.setToken(response.accessToken);
                    this.currentUserSubject.next({
                        accessToken: response.accessToken,
                        nom: response.nom,
                        prenom: response.prenom,
                        role: response.role
                    });
                })
            );
    }

    register(userData: any): Observable<any> {
        return this.http.post<any>(`${environment.apiUrl}/auth/register`, userData);
    }

    logout(): void {
        if (isPlatformBrowser(this.platformId)) {
            localStorage.removeItem(this.tokenKey);
        }
        this.currentUserSubject.next(null);
    }

    getToken(): string | null {
        if (isPlatformBrowser(this.platformId)) {
            return localStorage.getItem(this.tokenKey);
        }
        return null;
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    private setToken(token: string): void {
        if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem(this.tokenKey, token);
        }
    }

    private loadUserFromToken(): void {
        const token = this.getToken();
        if (token) {
            try {
                // Simple base64 decoding to get payload
                const payload = JSON.parse(atob(token.split('.')[1]));
                this.currentUserSubject.next({
                    accessToken: token,
                    nom: payload.nom || '',
                    prenom: payload.prenom || '',
                    role: payload.role || 'ADMIN'
                });
            } catch (e) {
                this.logout();
            }
        }
    }
}
