import { Component, EventEmitter, Output, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MedicamentService } from '../services/medicament.service';
import { CategorieService } from '../../categorie/services/categorie.service';
import { CategorieResponseDTO } from '../../categorie/models/categorie.model';

@Component({
  selector: 'app-add-medicament',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-medicament.html'
})
export class AddMedicamentComponent implements OnInit {
  @Output() medicamentAdded = new EventEmitter<void>();

  medicamentForm: FormGroup;
  categories: CategorieResponseDTO[] = [];

  isLoading = false;
  isLoadingCategories = false;
  errorMessage = '';
  successMessage = '';

  private fb = inject(FormBuilder);
  private medicamentService = inject(MedicamentService);
  private categorieService = inject(CategorieService);
  private cdr = inject(ChangeDetectorRef);

  constructor() {
    this.medicamentForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      codeBarres: ['', [Validators.maxLength(50)]],
      description: ['', [Validators.maxLength(500)]],
      dosage: ['', [Validators.required, Validators.maxLength(50)]],
      forme: ['', [Validators.required, Validators.maxLength(50)]],
      prixUnitaire: ['', [Validators.required, Validators.min(0.01)]],
      categorieId: ['', [Validators.required]],
      seuilAlerte: [10, [Validators.min(1)]],
      dateExpiration: ['']
    });
  }

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.isLoadingCategories = true;
    this.categorieService.getAllCategories().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.categories = Array.isArray(data) ? data : [];
        this.isLoadingCategories = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement des catégories:', err);
        this.isLoadingCategories = false;
      }
    });
  }

  onSubmit() {
    if (this.medicamentForm.invalid) {
      this.medicamentForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.medicamentService.createMedicament(this.medicamentForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Médicament ajouté avec succès !';
        this.medicamentForm.reset({ seuilAlerte: 10 });
        this.medicamentAdded.emit();
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
          this.errorMessage = 'Erreur lors de l\'ajout du médicament.';
        }
        this.cdr.detectChanges();
      }
    });
  }
}
