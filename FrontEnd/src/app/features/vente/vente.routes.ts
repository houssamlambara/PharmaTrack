import { Routes } from '@angular/router';
import { AddVenteComponent } from './add-vente/add-vente';
import { ListVenteComponent } from './list-vente/list-vente';

export const venteRoutes: Routes = [
  { path: '', component: AddVenteComponent },
  { path: 'history', component: ListVenteComponent }
];
