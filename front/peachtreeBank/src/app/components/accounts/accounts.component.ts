import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {DataProcessor} from "../../services/data-processor";
import {ClientService} from "../../services/client.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent extends DataProcessor implements AfterViewInit{

  displayColumn: string[] = [
    'id',
    'accountNumber',
    'balance',
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(clientService: ClientService) {
    super(clientService);
  }

  ngAfterViewInit() {
    this.processTableData(this.paginator, this.sort, this.ACCOUNT);
  }
}
