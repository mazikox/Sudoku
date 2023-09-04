import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {DataProcessor} from "../../services/data-processor";
import {ClientService} from "../../services/client.service";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent extends DataProcessor implements AfterViewInit{

  displayColumn: string[] = [
    'id',
    'name',
    'accountNumber',
    'currency',
    'balance',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(clientService: ClientService) {
    super(clientService);
  }

  ngAfterViewInit() {
    this.processTableData(this.paginator, this.sort, this.ACCOUNT);
  }
}
