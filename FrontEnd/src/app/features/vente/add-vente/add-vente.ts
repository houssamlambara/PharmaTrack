import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { VenteService } from '../services/vente.service';
import { MedicamentService } from '../../medicament/services/medicament.service';
import { CategorieService } from '../../categorie/services/categorie.service';
import { MedicamentResponseDTO } from '../../medicament/models/medicament.model';
import { CategorieResponseDTO } from '../../categorie/models/categorie.model';
import { VenteRequestDTO, VenteItemsRequestDTO } from '../models/vente.model';

interface CartItem {
    medicament: MedicamentResponseDTO;
    quantite: number;
}

@Component({
    selector: 'app-add-vente',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './add-vente.html'
})
export class AddVenteComponent implements OnInit {
    isCartOpen: boolean = true;

    medicaments: MedicamentResponseDTO[] = [];
    categories: CategorieResponseDTO[] = [];

    searchTerm: string = '';
    selectedCategoryId: string | null = null;

    cart: CartItem[] = [];
    methodePaiement: 'ESPECE' | 'CARTE_BANCAIRE' = 'ESPECE';
    clientNom: string = '';

    isLoading = false;
    isProcessing = false;
    successMessage = '';
    errorMessage = '';

    private venteService = inject(VenteService);
    private medicamentService = inject(MedicamentService);
    private categorieService = inject(CategorieService);
    private cdr = inject(ChangeDetectorRef);

    ngOnInit() {
        this.loadCatalog();
    }

    loadCatalog() {
        this.isLoading = true;


        this.categorieService.getAllCategories().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.categories = Array.isArray(data) ? data : [];
                this.cdr.detectChanges();
            }
        });


        this.medicamentService.getAllMedicaments().subscribe({
            next: (res: any) => {
                const data = res.data || res;
                this.medicaments = Array.isArray(data)
                    ? data.filter((m: MedicamentResponseDTO) => m.actif && m.quantiteStock > 0)
                    : [];
                this.isLoading = false;
                this.cdr.detectChanges();
            },
            error: () => {
                this.isLoading = false;
                this.errorMessage = 'Erreur lors du chargement du catalogue.';
                this.cdr.detectChanges();
            }
        });
    }

    get filteredMedicaments(): MedicamentResponseDTO[] {
        return this.medicaments.filter(med => {
            const matchSearch = !this.searchTerm ||
                med.nom.toLowerCase().includes(this.searchTerm.toLowerCase());

            const matchCategory = !this.selectedCategoryId || med.categorieId === this.selectedCategoryId;

            return matchSearch && matchCategory;
        });
    }

    selectCategory(id: string | null) {
        this.selectedCategoryId = id;
    }

    toggleCart() {
        this.isCartOpen = !this.isCartOpen;
    }



    addToCart(med: MedicamentResponseDTO) {
        const existing = this.cart.find(item => item.medicament.id === med.id);
        if (existing) {
            if (existing.quantite < med.quantiteStock) {
                existing.quantite++;
            } else {
                this.showTemporaryError(`Stock maximum atteint pour ${med.nom}`);
            }
        } else {
            this.cart.push({ medicament: med, quantite: 1 });
            this.isCartOpen = true;
        }
    }

    removeFromCart(index: number) {
        this.cart.splice(index, 1);
    }

    updateQuantity(index: number, change: number) {
        const item = this.cart[index];
        const newQty = item.quantite + change;

        if (newQty <= 0) {
            this.removeFromCart(index);
        } else if (newQty > item.medicament.quantiteStock) {
            this.showTemporaryError(`Stock disponible: ${item.medicament.quantiteStock}`);
        } else {
            item.quantite = newQty;
        }
    }

    get cartTotal(): number {
        return this.cart.reduce((total, item) => total + (item.quantite * item.medicament.prixUnitaire), 0);
    }

    get cartItemsCount(): number {
        return this.cart.reduce((count, item) => count + item.quantite, 0);
    }

    showTemporaryError(msg: string) {
        this.errorMessage = msg;
        this.cdr.detectChanges();
        setTimeout(() => {
            this.errorMessage = '';
            this.cdr.detectChanges();
        }, 3000);
    }



    selectPaymentMethod(method: 'ESPECE' | 'CARTE_BANCAIRE') {
        this.methodePaiement = method;
    }

    processSale() {
        if (this.cart.length === 0) return;

        this.isProcessing = true;
        this.errorMessage = '';

        const venteRequest: VenteRequestDTO = {
            clientNom: this.clientNom.trim() || undefined,
            methodePaiement: this.methodePaiement,
            items: this.cart.map(item => ({
                medicamentId: item.medicament.id,
                quantite: item.quantite
            }))
        };

        this.venteService.createVente(venteRequest).subscribe({
            next: () => {
                this.isProcessing = false;
                this.successMessage = 'Vente validée avec succès !';


                this.cart = [];
                this.clientNom = '';
                this.methodePaiement = 'ESPECE';
                this.loadCatalog();

                this.cdr.detectChanges();
                setTimeout(() => {
                    this.successMessage = '';
                    this.cdr.detectChanges();
                }, 3000);
            },
            error: (err) => {
                this.isProcessing = false;
                this.errorMessage = err.error?.message || 'Erreur lors de la validation de la vente.';
                this.cdr.detectChanges();
            }
        });
    }
}
