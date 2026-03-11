import { Component, inject, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { MedicamentService } from '../../../../features/medicament/services/medicament.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './main-layout.html',
  styleUrls: ['./main-layout.css']
})
export class MainLayoutComponent implements OnInit {
  private authService = inject(AuthService);
  private medicamentService = inject(MedicamentService);
  private router = inject(Router);

  currentUser$ = this.authService.currentUser$;
  isProfileMenuOpen = false;
  isNotificationMenuOpen = false;
  lowStockItems: any[] = [];

  ngOnInit() {
    this.medicamentService.getLowStock().subscribe({
      next: (res: any) => {
        this.lowStockItems = res.data || res || [];
      }
    });
  }

  toggleProfileMenu() {
    this.isProfileMenuOpen = !this.isProfileMenuOpen;
    if (this.isProfileMenuOpen) this.isNotificationMenuOpen = false;
  }

  toggleNotificationMenu() {
    this.isNotificationMenuOpen = !this.isNotificationMenuOpen;
    if (this.isNotificationMenuOpen) this.isProfileMenuOpen = false;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  @HostListener('document:click', ['$event'])
  clickout(event: MouseEvent) {
    const target = event.target as HTMLElement | null;
    if (target && !target.closest('.profile-menu-container')) {
      this.isProfileMenuOpen = false;
    }
    if (target && !target.closest('.notification-container')) {
      this.isNotificationMenuOpen = false;
    }
  }
}
