import {Component, OnInit} from '@angular/core';
import {ClientService, RootObject} from "../../services/client.service";


@Component({
  selector: 'app-back',
  templateUrl: './back.component.html',
  styleUrls: ['./back.component.scss']
})
export class BackComponent implements OnInit {

  displayColumn: string[] = ['id', 'categoryCode', 'date', 'amount', 'currencyCode',
    'originatorAccountNumber', 'counterpartyAccount', 'paymentType', 'status', 'title']
  dataSource: any = [];

  constructor(private clientService: ClientService) {
  }

  ngOnInit() {
    this.clientService.getApi().subscribe(value => {
      console.table(value.content)
      this.dataSource = value.content;
    });
  }
}
