import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddVenteComponent } from '../add-vente/add-vente';
import { VenteService } from '../services/vente.service';
import { VenteResponseDTO } from '../models/vente.model';

import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-list-vente',
  standalone: true,
  imports: [CommonModule, RouterModule],
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
