import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-list-commande',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './list-commande.html',
    styleUrls: ['./list-commande.css']
})
export class ListCommandeComponent {
}
