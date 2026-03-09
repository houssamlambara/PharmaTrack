import { Component, EventEmitter, Output, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-add-user',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './add-user.html'
})
export class AddUserComponent {
    @Output() userAdded = new EventEmitter<void>();

    registerForm: FormGroup;
    isLoading = false;
    errorMessage = '';
    successMessage = '';

    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private cdr = inject(ChangeDetectorRef);

    constructor() {
        this.registerForm = this.fb.group({
            nom: ['', [Validators.required, Validators.minLength(2)]],
            prenom: ['', [Validators.required, Validators.minLength(2)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(3)]],
            role: ['CAISSIER', [Validators.required]]
        });
    }

    onSubmit() {
        if (this.registerForm.invalid) {
            this.registerForm.markAllAsTouched();
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        this.successMessage = '';

        this.authService.register(this.registerForm.value).subscribe({
            next: () => {
                this.isLoading = false;
                this.successMessage = 'Utilisateur créé avec succès !';
                this.registerForm.reset({ role: 'CAISSIER' });
                this.userAdded.emit();
                this.cdr.detectChanges();
                setTimeout(() => {
                    this.successMessage = '';
                    this.cdr.detectChanges();
                }, 3000);
            },
            error: (err) => {
                this.isLoading = false;
                if (err.status === 403) {
                    this.errorMessage = 'Accès refusé : Seul un Admin peut créer des utilisateurs.';
                } else {
                    this.errorMessage = 'Erreur lors de la création.';
                }
                this.cdr.detectChanges();
            }
        });
    }
}
