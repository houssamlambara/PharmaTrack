import { Component, EventEmitter, Output, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { VenteService } from '../services/vente.service';
import { MedicamentService } from '../../medicament/services/medicament.service';
import { MedicamentResponseDTO } from '../../medicament/models/medicament.model';

@Component({
    selector: 'app-add-vente',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-vente.html'
})
export class AddVenteComponent implements OnInit {
    @Output() venteAdded = new EventEmitter<void>();

    venteForm: FormGroup;
    medicaments: MedicamentResponseDTO[] = [];

    isLoading = false;
    isLoadingMedicaments = false;
    errorMessage = '';
    successMessage = '';

    private fb = inject(FormBuilder);
    private venteService = inject(VenteService);
    private medicamentService = inject(MedicamentService);
    private cdr = inject(ChangeDetectorRef);

    constructor() {
        this.venteForm = this.fb.group({
            clientNom: [''],
            methodePaiement: ['ESPECE', Validators.required],
            items: this.fb.array([])
        });
    }

    get items(): FormArray {
        return this.venteForm.get('items') as FormArray;
    }

    ngOnInit() {
        this.loadMedicaments();
        this.addItem();
    }

    loadMedicaments() {
        this.isLoadingMedicaments = true;
        this.medicamentService.getAllMedicaments().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.medicaments = Array.isArray(data) ? data.filter((m: MedicamentResponseDTO) => m.actif) : [];
                this.isLoadingMedicaments = false;
                this.cdr.detectChanges();
            },
            error: () => {
                this.isLoadingMedicaments = false;
            }
        });
    }

    addItem() {
        const itemGroup = this.fb.group({
            medicamentId: ['', Validators.required],
            quantite: [1, [Validators.required, Validators.min(1)]]
        });
        this.items.push(itemGroup);
    }

    removeItem(index: number) {
        if (this.items.length > 1) {
            this.items.removeAt(index);
        }
    }

    getMedicamentPrix(medicamentId: string): number {
        const med = this.medicaments.find(m => m.id === medicamentId);
        return med ? med.prixUnitaire : 0;
    }

    getMedicamentStock(medicamentId: string): number {
        const med = this.medicaments.find(m => m.id === medicamentId);
        return med ? med.quantiteStock : 0;
    }

    getTotal(): number {
        return this.items.controls.reduce((total, item) => {
            const medId = item.get('medicamentId')?.value;
            const qty = item.get('quantite')?.value || 0;
            const prix = this.getMedicamentPrix(medId);
            return total + (qty * prix);
        }, 0);
    }

    onSubmit() {
        if (this.venteForm.invalid || this.items.length === 0) {
            this.venteForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        this.successMessage = '';

        this.venteService.createVente(this.venteForm.value).subscribe({
            next: () => {
                this.isLoading = false;
                this.successMessage = 'Vente enregistrée avec succès !';
                this.venteForm.reset({ methodePaiement: 'ESPECE', clientNom: '' });
                this.items.clear();
                this.addItem();
                this.venteAdded.emit();
                this.cdr.detectChanges();
                setTimeout(() => {
                    this.successMessage = '';
                    this.cdr.detectChanges();
                }, 3000);
            },
            error: (err) => {
                this.isLoading = false;
                if (err.status === 400) {
                    this.errorMessage = err.error?.message || 'Stock insuffisant ou données invalides.';
                } else if (err.status === 403) {
                    this.errorMessage = 'Accès refusé.';
                } else {
                    this.errorMessage = 'Erreur lors de la vente.';
                }
                this.cdr.detectChanges();
            }
        });
    }
}
