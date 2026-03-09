import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-add-user',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './add-user.html'
})
export class AddUserComponent {
    registerForm: FormGroup;
    isLoading = false;
    errorMessage = '';
    successMessage = '';

    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private router = inject(Router);

    constructor() {
        this.registerForm = this.fb.group({
            nom: ['', [Validators.required, Validators.minLength(2)]],
            prenom: ['', [Validators.required, Validators.minLength(2)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(4)]],
            role: ['PHARMACIEN', [Validators.required]] // Par défaut
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
            next: (res) => {
                this.isLoading = false;
                this.successMessage = 'Utilisateur créé avec succès !';
                this.registerForm.reset({ role: 'PHARMACIEN' });

                // Optionnel : rediriger vers la liste des utilisateurs après 2 secondes
                // setTimeout(() => this.router.navigate(['/users']), 2000);
            },
            error: (err) => {
                this.isLoading = false;
                console.error('Erreur lors de la création:', err);
                if (err.status === 403) {
                    this.errorMessage = 'Accès refusé : Seul un Administrateur peut créer des utilisateurs.';
                } else {
                    this.errorMessage = 'Une erreur est survenue lors de la création de l\'utilisateur.';
                }
            }
        });
    }
}
