import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddCommandeComponent } from '../add-commande/add-commande';
import { CommandeFournisseurService } from '../services/commande-fournisseur.service';
import { CommandeFournisseurResponseDTO } from '../models/commande.model';

@Component({
    selector: 'app-list-commande',
    standalone: true,
    imports: [CommonModule, AddCommandeComponent],
    templateUrl: './list-commande.html'
})
export class ListCommandeComponent implements OnInit {
    isAddModalOpen = false;
    commandes: CommandeFournisseurResponseDTO[] = [];
    filteredCommandes: CommandeFournisseurResponseDTO[] = [];

    totalCommandes = 0;
    enAttenteCount = 0;
    livreesCount = 0;
    annuleesCount = 0;

    selectedStatus = '';
    isLoading = false;
    errorMessage = '';

    private commandeService = inject(CommandeFournisseurService);
    private cdr = inject(ChangeDetectorRef);

    ngOnInit() {
        this.loadCommandes();
    }

    loadCommandes() {
        this.isLoading = true;
        this.errorMessage = '';

        this.commandeService.getAllCommandes().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.commandes = Array.isArray(data) ? data : [];
                this.applyFilter();
                this.calculateStats();
                this.isLoading = false;
                this.cdr.detectChanges();
            },
            error: (err) => {
                console.error('Erreur chargement commandes:', err);
                this.errorMessage = 'Impossible de charger les commandes.';
                this.isLoading = false;
                this.cdr.detectChanges();
            }
        });
    }

    calculateStats() {
        this.totalCommandes = this.commandes.length;
        this.enAttenteCount = this.commandes.filter(c => c.status === 'EN_ATTENTE').length;
        this.livreesCount = this.commandes.filter(c => c.status === 'LIVREE').length;
        this.annuleesCount = this.commandes.filter(c => c.status === 'ANNULEE').length;
    }

    applyFilter() {
        if (!this.selectedStatus) {
            this.filteredCommandes = [...this.commandes];
        } else {
            this.filteredCommandes = this.commandes.filter(c => c.status === this.selectedStatus);
        }
    }

    onFilterStatus(event: any) {
        this.selectedStatus = event.target.value;
        this.applyFilter();
    }

    receiveCommande(id: string) {
        if (confirm('Confirmer la réception de cette commande ? Le stock des médicaments sera mis à jour automatiquement.')) {
            this.commandeService.receiveCommande(id).subscribe({
                next: () => this.loadCommandes(),
                error: (err) => {
                    console.error('Erreur réception:', err);
                    alert('Erreur lors de la réception.');
                }
            });
        }
    }

    cancelCommande(id: string) {
        if (confirm('Êtes-vous sûr de vouloir annuler cette commande ?')) {
            this.commandeService.cancelCommande(id).subscribe({
                next: () => this.loadCommandes(),
                error: (err) => {
                    console.error('Erreur annulation:', err);
                    alert('Erreur lors de l\'annulation.');
                }
            });
        }
    }

    deleteCommande(id: string) {
        if (confirm('Supprimer définitivement cette commande ?')) {
            this.commandeService.deleteCommande(id).subscribe({
                next: () => this.loadCommandes(),
                error: (err) => {
                    console.error('Erreur suppression:', err);
                    alert('Impossible de supprimer cette commande.');
                }
            });
        }
    }

    openAddModal() {
        this.isAddModalOpen = true;
    }

    closeAddModal() {
        this.isAddModalOpen = false;
    }

    onCommandeAdded() {
        this.loadCommandes();
        setTimeout(() => this.closeAddModal(), 1500);
    }

    getStatusLabel(status: string): string {
        switch (status) {
            case 'EN_ATTENTE': return 'En attente';
            case 'LIVREE': return 'Livrée';
            case 'ANNULEE': return 'Annulée';
            default: return status;
        }
    }

    getStatusClasses(status: string): string {
        switch (status) {
            case 'EN_ATTENTE': return 'bg-amber-50 text-amber-700 border-amber-200';
            case 'LIVREE': return 'bg-emerald-50 text-emerald-700 border-emerald-200';
            case 'ANNULEE': return 'bg-red-50 text-red-600 border-red-200';
            default: return 'bg-slate-50 text-slate-600 border-slate-200';
        }
    }
}
