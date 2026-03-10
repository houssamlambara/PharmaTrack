import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddFournisseurComponent } from '../add-fournisseur/add-fournisseur';
import { FournisseurService } from '../services/fournisseur.service';
import { FournisseurResponseDTO } from '../models/fournisseur.model';

@Component({
    selector: 'app-list-fournisseur',
    standalone: true,
    imports: [CommonModule, AddFournisseurComponent],
    templateUrl: './list-fournisseur.html'
})
export class ListFournisseurComponent implements OnInit {
    isAddModalOpen = false;
    fournisseurs: FournisseurResponseDTO[] = [];
    filteredFournisseurs: FournisseurResponseDTO[] = [];

    isLoading = false;
    errorMessage = '';

    // Stats
    totalFournisseurs = 0;
    actifsCount = 0;
    inactifsCount = 0;

    private fournisseurService = inject(FournisseurService);
    private cdr = inject(ChangeDetectorRef);

    ngOnInit() {
        this.loadFournisseurs();
    }

    openAddModal() {
        this.isAddModalOpen = true;
    }

    closeAddModal() {
        this.isAddModalOpen = false;
    }

    loadFournisseurs() {
        this.isLoading = true;
        this.errorMessage = '';

        this.fournisseurService.getAllFournisseurs().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.fournisseurs = Array.isArray(data) ? data : [];
                this.filteredFournisseurs = [...this.fournisseurs];
                this.calculateStats();
                this.isLoading = false;
                this.cdr.detectChanges();
            },
            error: (err) => {
                console.error('Erreur chargement fournisseurs:', err);
                this.errorMessage = 'Impossible de charger la liste des fournisseurs.';
                this.isLoading = false;
                this.cdr.detectChanges();
            }
        });
    }

    calculateStats() {
        this.totalFournisseurs = this.fournisseurs.length;
        this.actifsCount = this.fournisseurs.filter(f => f.actif).length;
        this.inactifsCount = this.totalFournisseurs - this.actifsCount;
    }

    onFournisseurAdded() {
        this.loadFournisseurs();
    }

    toggleStatus(fournisseur: FournisseurResponseDTO) {
        this.fournisseurService.toggleActif(fournisseur.id).subscribe({
            next: (res: any) => {
                fournisseur.actif = !fournisseur.actif;
                this.calculateStats();
                this.cdr.detectChanges();
            },
            error: (err) => {
                console.error('Erreur toggle statut:', err);
                alert('Impossible de modifier le statut du fournisseur.');
            }
        });
    }

    deleteFournisseur(id: string) {
        if (confirm('Êtes-vous sûr de vouloir supprimer définitivement ce fournisseur ?')) {
            this.fournisseurService.deleteFournisseur(id).subscribe({
                next: () => {
                    this.loadFournisseurs();
                },
                error: (err) => {
                    console.error('Erreur suppression:', err);
                    alert('Impossible de supprimer ce fournisseur (peut-être lié à des commandes).');
                }
            });
        }
    }

    onSearch(event: any) {
        const searchTerm = event.target.value.toLowerCase();
        if (!searchTerm) {
            this.filteredFournisseurs = [...this.fournisseurs];
        } else {
            this.filteredFournisseurs = this.fournisseurs.filter(f =>
                f.nom.toLowerCase().includes(searchTerm) ||
                f.email.toLowerCase().includes(searchTerm)
            );
        }
        this.cdr.detectChanges();
    }
}
