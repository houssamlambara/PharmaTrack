import { Routes } from '@angular/router';
import { ListMedicamentComponent } from './list-medicament/list-medicament';
import { AddMedicamentComponent } from './add-medicament/add-medicament';

export const medicamentRoutes: Routes = [
  { path: '', component: ListMedicamentComponent }
];

