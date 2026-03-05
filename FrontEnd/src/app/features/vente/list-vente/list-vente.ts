import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-vente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-vente.html',
  styleUrl: './list-vente.css'
})
export class ListVenteComponent {
  isCartOpen: boolean = true;

  toggleCart() {
    this.isCartOpen = !this.isCartOpen;
  }
}

