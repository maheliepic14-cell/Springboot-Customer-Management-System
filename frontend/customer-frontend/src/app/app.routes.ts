import { Routes } from '@angular/router';
import {CustomerListComponent} from './pages/customer-list/customer-list.component';

export const routes: Routes = [
  {
    path: "customers",
    component: CustomerListComponent
  },
  {
    path: "",
    redirectTo: "customers",
    //Empty route matches the entire empty path
    pathMatch: "full"
  }
];
