import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AddCategorieComponent } from '../../categorie/add-categorie/add-categorie';
import { AddMedicamentComponent } from '../add-medicament/add-medicament';
import { CategorieService } from '../../categorie/services/categorie.service';
import { MedicamentService } from '../services/medicament.service';
import { CategorieResponseDTO } from '../../categorie/models/categorie.model';
import { MedicamentResponseDTO } from '../models/medicament.model';

@Component({
  selector: 'app-list-medicament',
  standalone: true,
  imports: [CommonModule, FormsModule, AddCategorieComponent, AddMedicamentComponent],
  templateUrl: './list-medicament.html',
  styleUrl: './list-medicament.css'
})
export class ListMedicamentComponent implements OnInit {
  isCategoryModalOpen = false;
  isAddMedicamentModalOpen = false;

  categories: CategorieResponseDTO[] = [];
  medicaments: MedicamentResponseDTO[] = [];
  filteredMedicaments: MedicamentResponseDTO[] = [];

  totalItems = 0;
  lowStockCount = 0;
  outOfStockCount = 0;
  totalValue = 0;

  searchTerm = '';
  selectedCategorie = '';
  selectedStatus = '';

  isLoadingCategories = false;
  isLoadingMedicaments = false;
  errorCategories = '';
  errorMedicaments = '';

  private categorieService = inject(CategorieService);
  private medicamentService = inject(MedicamentService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.loadCategories();
    this.loadMedicaments();
  }

  loadMedicaments() {
    this.isLoadingMedicaments = true;
    this.errorMedicaments = '';

    this.medicamentService.getAllMedicaments().subscribe({
      next: (res: any) => {
        const data = res.data || res;
        this.medicaments = Array.isArray(data) ? data : [];
        this.applyFilters();
        this.calculateStats();
        this.isLoadingMedicaments = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement médicaments:', err);
        this.errorMedicaments = 'Impossible de charger les médicaments.';
        this.isLoadingMedicaments = false;
        this.cdr.detectChanges();
      }
    });
  }

  calculateStats() {
    this.totalItems = this.medicaments.length;
    this.lowStockCount = this.medicaments.filter(m => m.alerteStock && !m.enRuptureStock).length;
    this.outOfStockCount = this.medicaments.filter(m => m.enRuptureStock).length;
    this.totalValue = this.medicaments.reduce((acc, current) => acc + (current.prixUnitaire * current.quantiteStock), 0);
  }

  applyFilters() {
    this.filteredMedicaments = this.medicaments.filter(m => {
      // Name/Code filter
      const matchesSearch = !this.searchTerm ||
        m.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (m.codeBarres && m.codeBarres.includes(this.searchTerm));

      // Category filter
      const matchesCat = !this.selectedCategorie || m.categorieId === this.selectedCategorie;

      // Status filter
      let matchesStatus = true;
      if (this.selectedStatus === 'In Stock') matchesStatus = !m.enRuptureStock;
      if (this.selectedStatus === 'Low Stock') matchesStatus = m.alerteStock && !m.enRuptureStock;
      if (this.selectedStatus === 'Out of Stock') matchesStatus = m.enRuptureStock;

      return matchesSearch && matchesCat && matchesStatus;
    });
  }

  onSearch(event: any) {
    this.searchTerm = event.target.value;
    this.applyFilters();
  }

  onFilterCategorie(event: any) {
    this.selectedCategorie = event.target.value;
    this.applyFilters();
  }

  onFilterStatus(event: any) {
    this.selectedStatus = event.target.value;
    this.applyFilters();
  }

  toggleActif(medicament: MedicamentResponseDTO) {
    this.medicamentService.toggleActif(medicament.id).subscribe({
      next: () => {
        medicament.actif = !medicament.actif;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur bascule statut:', err);
        alert('Erreur lors de la modification du statut.');
      }
    });
  }

  deleteMedicament(id: string) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce médicament ? Cette action est irréversible.')) {
      this.medicamentService.deleteMedicament(id).subscribe({
        next: () => {
          this.loadMedicaments();
        },
        error: (err) => {
          console.error('Erreur suppression:', err);
          alert('Impossible de supprimer le médicament.');
        }
      });
    }
  }

  openAddMedicamentModal() {
    this.isAddMedicamentModalOpen = true;
  }

  closeAddMedicamentModal() {
    this.isAddMedicamentModalOpen = false;
  }

  onMedicamentAdded() {
    this.loadMedicaments(); // Refresh list automatically
    setTimeout(() => {
      this.closeAddMedicamentModal();
    }, 1500);
  }

  openCategoryModal() {
    this.isCategoryModalOpen = true;
    this.loadCategories(); // Refresh when opening
  }

  closeCategoryModal() {
    this.isCategoryModalOpen = false;
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
        console.error('Erreur chargement catégories:', err);
        this.errorCategories = 'Impossible de charger les catégories.';
        this.isLoadingCategories = false;
        this.cdr.detectChanges();
      }
    });
  }

  onCategoryAdded() {
    this.loadCategories();
  }

  deleteCategory(id: string) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?')) {
      this.categorieService.deleteCategory(id).subscribe({
        next: () => this.loadCategories(),
        error: (err) => alert('Impossible de supprimer. Peut-être est-elle utilisée ?')
      });
    }
  }
}
