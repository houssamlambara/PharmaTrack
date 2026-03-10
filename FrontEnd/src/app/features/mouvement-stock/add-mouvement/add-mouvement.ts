import { Component, Output, EventEmitter, Input, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MouvementStockService } from '../services/mouvement-stock.service';
import { MedicamentService } from '../../medicament/services/medicament.service';
import { MouvementStockRequestDTO, MovementType } from '../models/mouvement-stock.model';

@Component({
    selector: 'app-add-mouvement',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './add-mouvement.html'
})
export class AddMouvementComponent implements OnInit {
    @Input() isOpen = false;
    @Output() closePanel = new EventEmitter<void>();
    @Output() mouvementAdded = new EventEmitter<void>();

    mouvement: MouvementStockRequestDTO = {
        medicamentId: '',
        quantite: 1,
        type: MovementType.AJUSTEMENT,
        motif: ''
    };

    Math = Math;

    medicaments: any[] = [];

    isSubmitting = false;
    errorMessage = '';

    private mouvementService = inject(MouvementStockService);
    private medicamentService = inject(MedicamentService);

    ngOnInit() {
        this.loadMedicaments();
    }

    loadMedicaments() {
        this.medicamentService.getAllMedicaments().subscribe({
            next: (res: any) => {
                this.medicaments = res.data || res;
            }
        });
    }

    close() {
        this.closePanel.emit();
        this.resetForm();
    }

    onSubmit() {
        if (!this.mouvement.medicamentId || !this.mouvement.quantite || !this.mouvement.motif) {
            this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
            return;
        }

        if (this.mouvement.quantite <= 0) {
            this.errorMessage = 'La quantité doit être supérieure à zéro.';
            return;
        }

        this.isSubmitting = true;
        this.errorMessage = '';

        this.mouvementService.createMouvement(this.mouvement).subscribe({
            next: () => {
                this.isSubmitting = false;
                this.mouvementAdded.emit();
                this.close();
            },
            error: (err) => {
                this.errorMessage = err.error?.message || 'Erreur lors de la création de l\'ajustement.';
                this.isSubmitting = false;
            }
        });
    }

    resetForm() {
        this.mouvement = {
            medicamentId: '',
            quantite: 1,
            type: MovementType.AJUSTEMENT,
            motif: ''
        };
        this.errorMessage = '';
    }
}
