import { Component, EventEmitter, Output, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategorieService } from '../services/categorie.service';

@Component({
    selector: 'app-add-categorie',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-categorie.html'
})
export class AddCategorieComponent {
    @Output() categorieAdded = new EventEmitter<void>();

    categorieForm: FormGroup;
    isLoading = false;
    errorMessage = '';
    successMessage = '';

    private fb = inject(FormBuilder);
    private categorieService = inject(CategorieService);
    private cdr = inject(ChangeDetectorRef);

    constructor() {
        this.categorieForm = this.fb.group({
            nom: ['', [Validators.required, Validators.minLength(2)]],
            description: ['']
        });
    }

    onSubmit() {
        if (this.categorieForm.invalid) {
            this.categorieForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        this.successMessage = '';

        this.categorieService.createCategory(this.categorieForm.value).subscribe({
            next: () => {
                this.isLoading = false;
                this.successMessage = 'Catégorie créée avec succès !';
                this.categorieForm.reset();
                this.categorieAdded.emit(); // Notifie le parent
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
                    this.errorMessage = err.error.message || 'Données invalides.';
                } else {
                    this.errorMessage = 'Erreur lors de la création de la catégorie.';
                }
                this.cdr.detectChanges();
            }
        });
    }
}
