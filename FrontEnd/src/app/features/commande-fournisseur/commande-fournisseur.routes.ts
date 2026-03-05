import { Routes } from '@angular/router';
import { ListCommandeComponent } from './list-commande/list-commande';
import { AddCommandeComponent } from './add-commande/add-commande';

export const commandeFournisseurRoutes: Routes = [
    {
        path: '',
        children: [
            { path: 'list', component: ListCommandeComponent },
            { path: 'add', component: AddCommandeComponent },
            { path: '', redirectTo: 'list', pathMatch: 'full' }
        ]
    }
];
