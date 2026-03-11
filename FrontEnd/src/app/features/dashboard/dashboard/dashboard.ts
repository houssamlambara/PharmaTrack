import { Component, OnInit, inject, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
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
  imports: [CommonModule, RouterModule, BaseChartDirective],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  caAujourdhui = 0;
  ventesAujourdhui = 0;
  totalMedicaments = 0;
  lowStockItems: any[] = [];
  commandesEnAttente = 0;
  dernieresVentes: any[] = [];
  derniersMouvements: any[] = [];

  isLoading = true;
  showStockNotification = false;

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
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.isLoading = true;

    forkJoin({
      allVentes: this.venteService.getAllVentes(),
      ventesAuj: this.venteService.getVentesAujourdhui(),
      medicaments: this.medicamentService.getAllMedicaments(),
      lowStock: this.medicamentService.getLowStock(),
      commandesEnAttente: this.commandeService.getCommandesEnAttente(),
      mouvements: this.mouvementService.getAllMouvements()
    }).subscribe({
      next: (results: any) => {
        const ventesAujData = results.ventesAuj.data || results.ventesAuj || [];
        const ventesAujArr = Array.isArray(ventesAujData) ? ventesAujData : [];
        this.ventesAujourdhui = ventesAujArr.length;
        this.caAujourdhui = ventesAujArr.reduce((acc: number, v: any) => acc + (v.montantTotal || 0), 0);
        this.dernieresVentes = ventesAujArr.slice(0, 5);

        const allVentesData = results.allVentes.data || results.allVentes || [];
        const allVentesArr = Array.isArray(allVentesData) ? allVentesData : [];
        this.buildWeeklyChart(allVentesArr);

        const medsData = results.medicaments.data || results.medicaments || [];
        this.totalMedicaments = Array.isArray(medsData) ? medsData.length : 0;

        const lowStockData = results.lowStock.data || results.lowStock || [];
        this.lowStockItems = Array.isArray(lowStockData) ? lowStockData.slice(0, 5) : [];

        const cmdData = results.commandesEnAttente.data || results.commandesEnAttente || [];
        this.commandesEnAttente = Array.isArray(cmdData) ? cmdData.length : 0;

        const mouvData = results.mouvements.data || results.mouvements || [];
        const mouvArr = Array.isArray(mouvData) ? mouvData : [];
        this.derniersMouvements = mouvArr
          .sort((a: any, b: any) => new Date(b.dateMouvement).getTime() - new Date(a.dateMouvement).getTime())
          .slice(0, 6);
        this.buildMouvementChart(mouvArr);

        this.isLoading = false;

        // Show notification with slight delay for dramatic effect if there are low stock items
        if (this.lowStockItems.length > 0) {
          setTimeout(() => {
            this.showStockNotification = true;
            this.cdr.detectChanges();
          }, 800);
        }

        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  closeNotification() {
    this.showStockNotification = false;
  }

  buildWeeklyChart(ventes: any[]) {
    const today = new Date();
    const weekData = [0, 0, 0, 0, 0, 0, 0];
    const dayLabels = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];

    ventes.forEach(v => {
      if (!v.dateVente) return;
      const venteDate = new Date(v.dateVente);
      const diffDays = Math.floor((today.getTime() - venteDate.getTime()) / (1000 * 60 * 60 * 24));
      if (diffDays >= 0 && diffDays < 7) {
        const dayIndex = venteDate.getDay();
        weekData[dayIndex] += v.montantTotal || 0;
      }
    });

    const orderedData: number[] = [];
    const orderedLabels: string[] = [];
    const todayDay = today.getDay();
    for (let i = 6; i >= 0; i--) {
      const idx = (todayDay - i + 7) % 7;
      orderedData.push(weekData[idx]);
      orderedLabels.push(dayLabels[idx]);
    }

    this.lineChartData = {
      labels: orderedLabels,
      datasets: [{
        data: orderedData,
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
