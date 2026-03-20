import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddVenteComponent } from '../add-vente/add-vente';
import { VenteService } from '../services/vente.service';
import { VenteResponseDTO } from '../models/vente.model';

import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-vente',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './list-vente.html'
})
export class ListVenteComponent implements OnInit {
  isAddModalOpen = false;
  ventes: VenteResponseDTO[] = [];
  filteredVentes: VenteResponseDTO[] = [];

  totalVentes = 0;
  ventesAujourdhui = 0;
  chiffreAffaire = 0;
  caAujourdhui = 0;

  dateDebut: string = '';
  dateFin: string = '';
  currentFilterText: string = 'Toutes les ventes';

  selectedPaiement = '';
  isLoading = false;
  errorMessage = '';

  private venteService = inject(VenteService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadVentes();
  }

  loadVentes() {
    this.isLoading = true;
    this.errorMessage = '';

    this.venteService.getAllVentes().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.ventes = Array.isArray(data) ? data : [];
        this.currentFilterText = 'Toutes les ventes';
        this.applyFilter();
        this.calculateStats();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement ventes:', err);
        this.errorMessage = 'Impossible de charger les ventes.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  calculateStats() {
    this.totalVentes = this.ventes.length;
    this.chiffreAffaire = this.ventes.reduce((acc, v) => acc + v.montantTotal, 0);

    const today = new Date().toISOString().split('T')[0];
    const ventesToday = this.ventes.filter(v => v.dateVente && v.dateVente.startsWith(today));
    this.ventesAujourdhui = ventesToday.length;
    this.caAujourdhui = ventesToday.reduce((acc, v) => acc + v.montantTotal, 0);
  }

  applyFilter() {
    if (!this.selectedPaiement) {
      this.filteredVentes = [...this.ventes];
    } else {
      this.filteredVentes = this.ventes.filter(v => v.methodePaiement === this.selectedPaiement);
    }
  }

  onFilterPaiement(event: any) {
    this.selectedPaiement = event.target.value;
    this.applyFilter();
  }

  loadVentesByPeriod() {
    if (!this.dateDebut && !this.dateFin) {
        this.loadVentes();
        return;
    }
    
    if (!this.dateDebut || !this.dateFin) {
      alert('Veuillez sélectionner les deux dates (début et fin).');
      return;
    }
    
    if (new Date(this.dateDebut) > new Date(this.dateFin)) {
      alert('La date de début doit être antérieure ou égale à la date de fin.');
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const startDateTime = `${this.dateDebut}T00:00:00`;
    const endDateTime = `${this.dateFin}T23:59:59`;

    this.venteService.getVentesByPeriode(startDateTime, endDateTime).subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.ventes = Array.isArray(data) ? data : [];
        this.currentFilterText = `Période: ${new Date(this.dateDebut).toLocaleDateString()} - ${new Date(this.dateFin).toLocaleDateString()}`;
        this.applyFilter();
        this.calculateStats();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement ventes par période:', err);
        this.errorMessage = 'Impossible de charger les ventes de la période.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  setQuickFilter(periode: string) {
    const today = new Date();
    
    if (periode === 'today') {
      const todayStr = this.formatDate(today);
      this.dateDebut = todayStr;
      this.dateFin = todayStr;
      this.loadVentesByPeriod();
    } else if (periode === 'yesterday') {
      const yesterday = new Date(today);
      yesterday.setDate(today.getDate() - 1);
      const yesterdayStr = this.formatDate(yesterday);
      this.dateDebut = yesterdayStr;
      this.dateFin = yesterdayStr;
      this.loadVentesByPeriod();
    } else if (periode === 'week') {
      const lastWeek = new Date(today);
      lastWeek.setDate(today.getDate() - 6);
      this.dateDebut = this.formatDate(lastWeek);
      this.dateFin = this.formatDate(today);
      this.loadVentesByPeriod();
    } else if (periode === 'all') {
      this.dateDebut = '';
      this.dateFin = '';
      this.loadVentes();
    }
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  deleteVente(id: string) {
    if (confirm('Supprimer cette vente ? Attention : le stock ne sera PAS restauré.')) {
      this.venteService.deleteVente(id).subscribe({
        next: () => this.loadVentes(),
        error: () => alert('Impossible de supprimer cette vente.')
      });
    }
  }

  openAddModal() {
    this.isAddModalOpen = true;
  }

  closeAddModal() {
    this.isAddModalOpen = false;
  }

  onVenteAdded() {
    this.loadVentes();
    setTimeout(() => this.closeAddModal(), 1500);
  }

  getPaiementLabel(method: string): string {
    return method === 'ESPECE' ? 'Espèces' : 'Carte Bancaire';
  }

  getPaiementClasses(method: string): string {
    return method === 'ESPECE'
      ? 'bg-green-50 text-green-700 border-green-200'
      : 'bg-violet-50 text-violet-700 border-violet-200';
  }
}
