import { Component, EventEmitter, Output, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FournisseurService } from '../services/fournisseur.service';

@Component({
  selector: 'app-add-fournisseur',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-fournisseur.html'
})
export class AddFournisseurComponent {
  @Output() fournisseurAdded = new EventEmitter<void>();

  fournisseurForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  private fb = inject(FormBuilder);
  private fournisseurService = inject(FournisseurService);
  private cdr = inject(ChangeDetectorRef);

  constructor() {
    this.fournisseurForm = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\s\./0-9]*$/)]],
      adresse: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  onSubmit() {
    if (this.fournisseurForm.invalid) {
      this.fournisseurForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.fournisseurService.createFournisseur(this.fournisseurForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Fournisseur ajouté avec succès !';
        this.fournisseurForm.reset();
        this.fournisseurAdded.emit(); // Notifier le parent
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
          this.errorMessage = 'Erreur lors de l\'ajout du fournisseur.';
        }
        this.cdr.detectChanges();
      }
    });
  }
}
