import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BackComponent} from './components/transaction/back.component';
import {PayeesComponent} from "./components/payees/payees.component";
import {AddPayeesComponent} from "./components/add-payees/add-payees.component";
import {AccountsComponent} from "./components/accounts/accounts.component";
import {AddTransactionComponent} from "./components/add-transaction/add-transaction.component";
import {AnalyticsComponent} from "./components/analytics/analytics.component";

const routes: Routes = [
  {
    path: 'transactions',
    component: BackComponent,
  },
  {
    path: 'payees',
    component: PayeesComponent,
  },
  {
    path: 'add-payee',
    component: AddPayeesComponent,
  },
  {
    path: 'accounts',
    component: AccountsComponent,
  },
  {
    path: 'add-transaction',
    component: AddTransactionComponent,
  },
  {
    path: 'analytics',
    component: AnalyticsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
