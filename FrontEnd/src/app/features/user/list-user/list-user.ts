import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { UserResponseDTO } from '../models/user.model';
import { AddUserComponent } from '../add-user/add-user';

@Component({
  selector: 'app-list-user',
  standalone: true,
  imports: [CommonModule, AddUserComponent],
  templateUrl: './list-user.html'
})
export class ListUserComponent implements OnInit {
  users: UserResponseDTO[] = [];
  isLoadingUsers = true;
  errorUsers = '';

  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  totalEmployees = 0;
  totalAdmins = 0;
  totalCaissiers = 0;
  totalResponsables = 0;

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoadingUsers = true;
    this.authService.getUsers().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.users = Array.isArray(data) ? data : [];
        this.calculateStats();
        this.isLoadingUsers = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.errorUsers = 'Impossible de charger la liste.';
        this.isLoadingUsers = false;
        this.cdr.detectChanges();
      }
    });
  }

  calculateStats() {
    this.totalEmployees = this.users.length;
    this.totalAdmins = this.users.filter(u => u.role === 'ADMIN').length;
    this.totalCaissiers = this.users.filter(u => u.role === 'CAISSIER').length;
    this.totalResponsables = this.users.filter(u => u.role === 'RESPONSABLE_STOCK').length;
  }

  getInitials(user: UserResponseDTO): string {
    const p = user.prenom ? user.prenom.charAt(0).toUpperCase() : '';
    const n = user.nom ? user.nom.charAt(0).toUpperCase() : '';
    return (p + n) || 'U';
  }

  getDisplayName(user: UserResponseDTO): string {
    if (!user.prenom && !user.nom) return 'Utilisateur Inconnu';
    return `${user.prenom || ''} ${user.nom || ''}`.trim();
  }
}
