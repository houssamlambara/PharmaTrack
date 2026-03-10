import { Component, EventEmitter, Output, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommandeFournisseurService } from '../services/commande-fournisseur.service';
import { FournisseurService } from '../../fournisseur/services/fournisseur.service';
import { MedicamentService } from '../../medicament/services/medicament.service';
import { FournisseurResponseDTO } from '../../fournisseur/models/fournisseur.model';
import { MedicamentResponseDTO } from '../../medicament/models/medicament.model';

@Component({
    selector: 'app-add-commande',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-commande.html'
})
export class AddCommandeComponent implements OnInit {
    @Output() commandeAdded = new EventEmitter<void>();

    commandeForm: FormGroup;
    fournisseurs: FournisseurResponseDTO[] = [];
    medicaments: MedicamentResponseDTO[] = [];

    isLoading = false;
    isLoadingFournisseurs = false;
    isLoadingMedicaments = false;
    errorMessage = '';
    successMessage = '';

    private fb = inject(FormBuilder);
    private commandeService = inject(CommandeFournisseurService);
    private fournisseurService = inject(FournisseurService);
    private medicamentService = inject(MedicamentService);
    private cdr = inject(ChangeDetectorRef);

    constructor() {
        this.commandeForm = this.fb.group({
            fournisseurId: ['', Validators.required],
            remarque: [''],
            items: this.fb.array([])
        });
    }

    get items(): FormArray {
        return this.commandeForm.get('items') as FormArray;
    }

    ngOnInit() {
        this.loadFournisseurs();
        this.loadMedicaments();
        this.addItem();
    }

    loadFournisseurs() {
        this.isLoadingFournisseurs = true;
        this.fournisseurService.getAllFournisseurs().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.fournisseurs = Array.isArray(data) ? data.filter((f: FournisseurResponseDTO) => f.actif) : [];
                this.isLoadingFournisseurs = false;
                this.cdr.detectChanges();
            },
            error: () => {
                this.isLoadingFournisseurs = false;
            }
        });
    }

    loadMedicaments() {
        this.isLoadingMedicaments = true;
        this.medicamentService.getAllMedicaments().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.medicaments = Array.isArray(data) ? data : [];
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
            quantite: [1, [Validators.required, Validators.min(1)]],
            prixUnitaire: ['', [Validators.required, Validators.min(0.01)]]
        });
        this.items.push(itemGroup);
    }

    removeItem(index: number) {
        if (this.items.length > 1) {
            this.items.removeAt(index);
        }
    }

    onMedicamentSelected(index: number) {
        const itemGroup = this.items.at(index);
        const medicamentId = itemGroup.get('medicamentId')?.value;
        const med = this.medicaments.find(m => m.id === medicamentId);
        if (med) {
            itemGroup.get('prixUnitaire')?.setValue(med.prixUnitaire);
        }
    }

    getTotal(): number {
        return this.items.controls.reduce((total, item) => {
            const qty = item.get('quantite')?.value || 0;
            const price = item.get('prixUnitaire')?.value || 0;
            return total + (qty * price);
        }, 0);
    }

    onSubmit() {
        if (this.commandeForm.invalid || this.items.length === 0) {
            this.commandeForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        this.successMessage = '';

        this.commandeService.createCommande(this.commandeForm.value).subscribe({
            next: () => {
                this.isLoading = false;
                this.successMessage = 'Commande créée avec succès !';
                this.commandeForm.reset();
                this.items.clear();
                this.addItem();
                this.commandeAdded.emit();
                this.cdr.detectChanges();
                setTimeout(() => {
                    this.successMessage = '';
                    this.cdr.detectChanges();
                }, 3000);
            },
            error: (err) => {
                this.isLoading = false;
                if (err.status === 403) {
                    this.errorMessage = 'Accès refusé.';
                } else if (err.status === 400) {
                    this.errorMessage = err.error?.message || 'Données invalides.';
                } else {
                    this.errorMessage = 'Erreur lors de la création de la commande.';
                }
                this.cdr.detectChanges();
            }
        });
    }
}
