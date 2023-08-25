import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {BackComponent} from './components/transaction/back.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatTableModule} from '@angular/material/table';
import {MatInputModule} from '@angular/material/input';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {PayeesComponent} from './components/payees/payees.component';
import {AddPayeesComponent} from './components/add-payees/add-payees.component';
import {FormsModule} from "@angular/forms";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogModule} from '@angular/material/dialog';
import {MatTooltipModule} from "@angular/material/tooltip";
import {AccountsComponent} from './components/accounts/accounts.component';
import { AddTransactionComponent } from './components/add-transaction/add-transaction.component';
import {MatSelectModule} from "@angular/material/select";


@NgModule({
  declarations: [AppComponent, BackComponent, PayeesComponent, AddPayeesComponent, AccountsComponent, AddTransactionComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatInputModule,
    MatPaginatorModule,
    MatSortModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    MatTooltipModule,
    MatSelectModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
