import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddCategorieComponent } from '../../categorie/add-categorie/add-categorie';
import { CategorieService } from '../../categorie/services/categorie.service';
import { CategorieResponseDTO } from '../../categorie/models/categorie.model';

@Component({
  selector: 'app-list-medicament',
  standalone: true,
  imports: [CommonModule, AddCategorieComponent],
  templateUrl: './list-medicament.html',
  styleUrl: './list-medicament.css'
})
export class ListMedicamentComponent implements OnInit {
  isCategoryModalOpen = false;
  categories: CategorieResponseDTO[] = [];
  isLoadingCategories = false;
  errorCategories = '';

  private categorieService = inject(CategorieService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadCategories();
  }

  openCategoryModal() {
    this.isCategoryModalOpen = true;
    this.loadCategories(); // Reload when opening to ensure fresh data
  }

  closeCategoryModal() {
    this.isCategoryModalOpen = false;
  }

  loadCategories() {
    this.isLoadingCategories = true;
    this.errorCategories = '';

    this.categorieService.getAllCategories().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.categories = Array.isArray(data) ? data : [];
        this.isLoadingCategories = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des catégories:', err);
        this.errorCategories = 'Impossible de charger les catégories.';
        this.isLoadingCategories = false;
        this.cdr.detectChanges();
      }
    });
  }

  onCategoryAdded() {
    // Called when the child component <app-add-categorie> emits (categorieAdded)
    this.loadCategories();
  }

  deleteCategory(id: string) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?')) {
      this.categorieService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (err) => {
          console.error('Erreur lors de la suppression:', err);
          alert('Impossible de supprimer cette catégorie car elle est utilisée par des médicaments.');
        }
      });
    }
  }
}

