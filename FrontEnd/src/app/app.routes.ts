import { Routes } from '@angular/router';
import { MainLayoutComponent } from './shared/components/layout/main-layout/main-layout';

export const routes: Routes = [
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },

  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.authRoutes)
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.dashboardRoutes)
      },
      {
        path: 'medicaments',
        loadChildren: () => import('./features/medicament/medicament.routes').then(m => m.medicamentRoutes)
      },
      {
        path: 'fournisseurs',
        loadChildren: () => import('./features/fournisseur/fournisseur.routes').then(m => m.fournisseurRoutes)
      },
      {
        path: 'commande-fournisseur',
        loadChildren: () => import('./features/commande-fournisseur/commande-fournisseur.routes').then(m => m.commandeFournisseurRoutes)
      },
      {
        path: 'ventes',
        loadChildren: () => import('./features/vente/vente.routes').then(m => m.venteRoutes)
      },
      {
        path: 'users',
        loadChildren: () => import('./features/user/user.routes').then(m => m.userRoutes)
      }
    ]
  },

  { path: '**', redirectTo: 'auth/login' }
];
