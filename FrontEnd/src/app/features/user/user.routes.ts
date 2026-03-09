import { Routes } from '@angular/router';
import { ListUserComponent } from './list-user/list-user';
import { AddUserComponent } from './add-user/add-user';

export const userRoutes: Routes = [
  { path: '', component: ListUserComponent },
  { path: 'add', component: AddUserComponent }
];

