import { Routes } from '@angular/router';

export const mouvementStockRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./list-mouvement/list-mouvement').then(m => m.ListMouvementComponent)
    }
];
