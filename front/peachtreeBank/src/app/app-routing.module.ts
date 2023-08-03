import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BackComponent} from './components/back/back.component';
import {PayeesComponent} from "./components/payees/payees.component";

const routes: Routes = [
  {
    path: 'transactions',
    component: BackComponent,
  },
  {
    path: 'payees',
    component: PayeesComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
