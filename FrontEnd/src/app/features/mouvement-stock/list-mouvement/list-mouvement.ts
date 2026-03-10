import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MouvementStockService } from '../services/mouvement-stock.service';
import { MouvementStockResponseDTO, MovementType } from '../models/mouvement-stock.model';
import { AddMouvementComponent } from '../add-mouvement/add-mouvement';

@Component({
    selector: 'app-list-mouvement',
    standalone: true,
    imports: [CommonModule, FormsModule, AddMouvementComponent],
    providers: [DatePipe],
    templateUrl: './list-mouvement.html'
})
export class ListMouvementComponent implements OnInit {
    isAddPanelOpen = false;
    mouvements: MouvementStockResponseDTO[] = [];
    filteredMouvements: MouvementStockResponseDTO[] = [];

    isLoading = false;
    errorMessage = '';

    selectedType: MovementType | 'TOUS' = 'TOUS';
    searchTerm = '';

    totalEntrees = 0;
    totalSorties = 0;
    totalAjustements = 0;

    private mouvementService = inject(MouvementStockService);
    private cdr = inject(ChangeDetectorRef);
    private datePipe = inject(DatePipe);

    MovementTypeEnum = MovementType;

    ngOnInit() {
        this.loadMouvements();
    }

    loadMouvements() {
        this.isLoading = true;
        this.errorMessage = '';

        this.mouvementService.getAllMouvements().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.mouvements = Array.isArray(data) ? data : [];
                this.calculateStats();
                this.applyFilters();
                this.isLoading = false;
                this.cdr.detectChanges();
            },
            error: () => {
                this.errorMessage = 'Erreur lors du chargement de l\'historique des mouvements.';
                this.isLoading = false;
                this.cdr.detectChanges();
            }
        });
    }

    calculateStats() {
        this.totalEntrees = this.mouvements.filter(m => m.type === MovementType.ENTREE).length;
        this.totalSorties = this.mouvements.filter(m => m.type === MovementType.SORTIE).length;
        this.totalAjustements = this.mouvements.filter(m => m.type === MovementType.AJUSTEMENT).length;
    }

    applyFilters() {
        this.filteredMouvements = this.mouvements.filter(m => {
            const matchType = this.selectedType === 'TOUS' || m.type === this.selectedType;

            const searchTermLower = this.searchTerm.toLowerCase();
            const matchSearch = !this.searchTerm ||
                m.medicamentNom?.toLowerCase().includes(searchTermLower) ||
                m.userNom?.toLowerCase().includes(searchTermLower) ||
                m.userPrenom?.toLowerCase().includes(searchTermLower) ||
                m.motif?.toLowerCase().includes(searchTermLower);

            return matchType && matchSearch;
        });

        this.filteredMouvements.sort((a, b) => {
            const dateA = new Date(a.dateMouvement).getTime();
            const dateB = new Date(b.dateMouvement).getTime();
            return dateB - dateA;
        });
    }

    setFilterType(type: MovementType | 'TOUS') {
        this.selectedType = type;
        this.applyFilters();
    }

    onSearchChange() {
        this.applyFilters();
    }

    getTypeBadgeClasses(type: string): string {
        switch (type) {
            case 'ENTREE':
                return 'bg-emerald-50 text-emerald-700 border-emerald-200';
            case 'SORTIE':
                return 'bg-blue-50 text-blue-700 border-blue-200';
            case 'AJUSTEMENT':
                return 'bg-amber-50 text-amber-700 border-amber-200';
            default:
                return 'bg-slate-50 text-slate-700 border-slate-200';
        }
    }

    getTypeIcon(type: string): string {
        switch (type) {
            case 'ENTREE': return 'arrow_downward';
            case 'SORTIE': return 'arrow_upward';
            case 'AJUSTEMENT': return 'sync_alt';
            default: return 'sync';
        }
    }

    formatDate(dateStr: string): string {
        return this.datePipe.transform(dateStr, 'dd/MM/yyyy HH:mm') || dateStr;
    }
}
