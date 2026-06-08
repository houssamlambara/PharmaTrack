import { Component, OnInit, inject, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { Chart, registerables } from 'chart.js';
import { VenteService } from '../../vente/services/vente.service';
import { MedicamentService } from '../../medicament/services/medicament.service';
import { CommandeFournisseurService } from '../../commande-fournisseur/services/commande-fournisseur.service';
import { MouvementStockService } from '../../mouvement-stock/services/mouvement-stock.service';
import { forkJoin } from 'rxjs';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, BaseChartDirective, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  caAujourdhui = 0;
  ventesAujourdhui = 0;
  totalMedicaments = 0;

  dateDebut: string = '';
  dateFin: string = '';
  currentFilterText: string = "Aujourd'hui";
  lowStockItems: any[] = [];
  expiringItems: any[] = [];
  commandesEnAttente = 0;
  dernieresVentes: any[] = [];
  derniersMouvements: any[] = [];

  isLoading = true;
  showStockNotification = false;
  showPeremptionNotification = false;

  private venteService = inject(VenteService);
  private medicamentService = inject(MedicamentService);
  private commandeService = inject(CommandeFournisseurService);
  private mouvementService = inject(MouvementStockService);
  private cdr = inject(ChangeDetectorRef);

  lineChartType = 'line' as const;
  lineChartData: ChartData<'line'> = {
    labels: ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'],
    datasets: [{
      data: [0, 0, 0, 0, 0, 0, 0],
      label: 'Ventes (DH)',
      fill: true,
      backgroundColor: 'rgba(16, 185, 129, 0.12)',
      borderColor: '#10b981',
      borderWidth: 3,
      pointBackgroundColor: '#10b981',
      pointBorderColor: '#ffffff',
      pointBorderWidth: 2,
      pointRadius: 5,
      pointHoverRadius: 8,
      pointHoverBackgroundColor: '#10b981',
      pointHoverBorderColor: '#ffffff',
      pointHoverBorderWidth: 3,
      tension: 0.4
    }]
  };
  lineChartOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#1e293b',
        titleFont: { weight: 'bold', size: 13 },
        bodyFont: { size: 12 },
        padding: 12,
        cornerRadius: 8,
        displayColors: false,
        callbacks: {
          label: (ctx) => `${(ctx.parsed.y ?? 0).toFixed(2)} DH`
        }
      }
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: { font: { weight: 'bold', size: 11 }, color: '#94a3b8' }
      },
      y: {
        grid: { color: 'rgba(148, 163, 184, 0.08)' },
        ticks: {
          font: { size: 11 },
          color: '#94a3b8',
          callback: (value) => value + ' DH'
        },
        beginAtZero: true
      }
    }
  };

  doughnutChartType = 'doughnut' as const;
  doughnutChartData: ChartData<'doughnut'> = {
    labels: ['Entrées', 'Sorties', 'Ajustements'],
    datasets: [{
      data: [0, 0, 0],
      backgroundColor: ['#10b981', '#3b82f6', '#f59e0b'],
      borderColor: '#ffffff',
      borderWidth: 3,
      hoverOffset: 8
    }]
  };
  doughnutChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '70%',
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          padding: 20,
          usePointStyle: true,
          pointStyle: 'circle',
          font: { weight: 'bold', size: 12 }
        }
      },
      tooltip: {
        backgroundColor: '#1e293b',
        titleFont: { weight: 'bold', size: 13 },
        bodyFont: { size: 12 },
        padding: 12,
        cornerRadius: 8
      }
    }
  };

  ngOnInit() {
    this.initFilterToToday();
    this.loadGeneralData();
  }

  initFilterToToday() {
    const today = new Date();
    const todayStr = this.formatDate(today);
    this.dateDebut = todayStr;
    this.dateFin = todayStr;
    this.currentFilterText = "Aujourd'hui";
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  loadGeneralData() {
    this.isLoading = true;

    forkJoin({
      medicaments: this.medicamentService.getAllMedicaments(),
      lowStock: this.medicamentService.getLowStock(),
      expiring: this.medicamentService.getExpiringMedicaments(30),
      commandesEnAttente: this.commandeService.getCommandesEnAttente(),
      mouvements: this.mouvementService.getAllMouvements()
    }).subscribe({
      next: (results: any) => {
        const medsData = results.medicaments.data || results.medicaments || [];
        this.totalMedicaments = Array.isArray(medsData) ? medsData.length : 0;

        const lowStockData = results.lowStock.data || results.lowStock || [];
        this.lowStockItems = Array.isArray(lowStockData) ? lowStockData.slice(0, 5) : [];

        const expiringData = results.expiring?.data || results.expiring || [];
        this.expiringItems = Array.isArray(expiringData) ? expiringData.slice(0, 5) : [];

        const cmdData = results.commandesEnAttente.data || results.commandesEnAttente || [];
        this.commandesEnAttente = Array.isArray(cmdData) ? cmdData.length : 0;

        const mouvData = results.mouvements.data || results.mouvements || [];
        const mouvArr = Array.isArray(mouvData) ? mouvData : [];
        this.derniersMouvements = mouvArr
          .sort((a: any, b: any) => new Date(b.dateMouvement).getTime() - new Date(a.dateMouvement).getTime())
          .slice(0, 6);
        this.buildMouvementChart(mouvArr);

        // Run the sales loader next
        this.loadSalesData();

        // Show notification with slight delay for dramatic effect if there are low stock items
        if (this.lowStockItems.length > 0) {
          setTimeout(() => {
            this.showStockNotification = true;
            this.cdr.detectChanges();
          }, 800);
        }

        if (this.expiringItems.length > 0) {
          setTimeout(() => {
            this.showPeremptionNotification = true;
            this.cdr.detectChanges();
          }, 1200);
        }

      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadSalesData() {
    if (!this.dateDebut || !this.dateFin) { return; }

    this.isLoading = true;
    const startDateTime = `${this.dateDebut}T00:00:00`;
    const endDateTime = `${this.dateFin}T23:59:59`;

    this.venteService.getVentesByPeriode(startDateTime, endDateTime).subscribe({
      next: (res: any) => {
        const ventesArr = Array.isArray(res.data) ? res.data : [];

        this.ventesAujourdhui = ventesArr.length;
        this.caAujourdhui = ventesArr.reduce((acc: number, v: any) => acc + (v.montantTotal || 0), 0);
        this.dernieresVentes = ventesArr.slice(0, 5);

        this.buildDynamicChart(ventesArr, this.dateDebut, this.dateFin);

        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
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
      this.currentFilterText = "Aujourd'hui";
    } else if (periode === 'yesterday') {
      const yesterday = new Date(today);
      yesterday.setDate(today.getDate() - 1);
      const yesterdayStr = this.formatDate(yesterday);
      this.dateDebut = yesterdayStr;
      this.dateFin = yesterdayStr;
      this.currentFilterText = "Hier";
    } else if (periode === 'week') {
      const lastWeek = new Date(today);
      lastWeek.setDate(today.getDate() - 6);
      this.dateDebut = this.formatDate(lastWeek);
      this.dateFin = this.formatDate(today);
      this.currentFilterText = "7 Derniers Jours";
    } else if (periode === 'month') {
      const lastMonth = new Date(today);
      lastMonth.setDate(today.getDate() - 29);
      this.dateDebut = this.formatDate(lastMonth);
      this.dateFin = this.formatDate(today);
      this.currentFilterText = "30 Derniers Jours";
    }

    this.loadSalesData();
  }

  filterCustom() {
    if (!this.dateDebut || !this.dateFin) {
      alert('Veuillez sélectionner les deux dates.');
      return;
    }

    if (new Date(this.dateDebut) > new Date(this.dateFin)) {
      alert('La date de début doit être antérieure à la date de fin.');
      return;
    }

    const start = new Date(this.dateDebut);
    const end = new Date(this.dateFin);
    this.currentFilterText = `Du ${start.toLocaleDateString()} au ${end.toLocaleDateString()}`;
    this.loadSalesData();
  }

  closeNotification() {
    this.showStockNotification = false;
  }

  closePeremptionNotification() {
    this.showPeremptionNotification = false;
  }

  getDaysRemaining(expirationDate: string): number {
    const today = new Date();
    const expDate = new Date(expirationDate);
    const diffTime = expDate.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  buildDynamicChart(ventes: any[], startStr: string, endStr: string) {
    const start = new Date(startStr);
    const end = new Date(endStr);

    // Check if single day to show something interesting, otherwise day-by-day
    const diffTime = end.getTime() - start.getTime();
    let diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    if (diffDays > 30) diffDays = 30; // Truncate at 30 days dynamically for chart clarity

    const labels: string[] = [];
    const data: number[] = [];

    for (let i = 0; i < diffDays; i++) {
        const d = new Date(start);
        d.setDate(d.getDate() + i);
        labels.push(d.toLocaleDateString(undefined, { weekday: 'short', day: 'numeric' }));
        const dayStr = this.formatDate(d);
        const sum = ventes.filter(v => v.dateVente && v.dateVente.startsWith(dayStr)).reduce((s, v) => s + (v.montantTotal || 0), 0);
        data.push(sum);
    }

    this.lineChartData = {
      labels: labels,
      datasets: [{
        data: data,
        label: 'Ventes (DH)',
        fill: true,
        backgroundColor: 'rgba(16, 185, 129, 0.12)',
        borderColor: '#10b981',
        borderWidth: 3,
        pointBackgroundColor: '#10b981',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 8,
        pointHoverBackgroundColor: '#10b981',
        pointHoverBorderColor: '#ffffff',
        pointHoverBorderWidth: 3,
        tension: 0.4
      }]
    };
  }

  buildMouvementChart(mouvements: any[]) {
    const entrees = mouvements.filter(m => m.type === 'ENTREE').length;
    const sorties = mouvements.filter(m => m.type === 'SORTIE').length;
    const ajustements = mouvements.filter(m => m.type === 'AJUSTEMENT').length;

    this.doughnutChartData = {
      labels: ['Entrées', 'Sorties', 'Ajustements'],
      datasets: [{
        data: [entrees, sorties, ajustements],
        backgroundColor: ['#10b981', '#3b82f6', '#f59e0b'],
        borderColor: '#ffffff',
        borderWidth: 3,
        hoverOffset: 8
      }]
    };
  }

  getTypeIcon(type: string): string {
    switch (type) {
      case 'ENTREE': return 'arrow_downward';
      case 'SORTIE': return 'arrow_upward';
      case 'AJUSTEMENT': return 'sync_alt';
      default: return 'sync';
    }
  }

  getTypeColor(type: string): string {
    switch (type) {
      case 'ENTREE': return 'text-emerald-600 bg-emerald-50';
      case 'SORTIE': return 'text-blue-600 bg-blue-50';
      case 'AJUSTEMENT': return 'text-amber-600 bg-amber-50';
      default: return 'text-slate-600 bg-slate-50';
    }
  }
}
