import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {ClientService} from "./services/client.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'peachtreeBank';

  public data: any;

  number$!: Observable<number>;
  balance: any;

  constructor(private clientService: ClientService) {
  }

  ngOnInit(): void {
    this.clientService.getVersion().subscribe((data) => this.data = data);
    this.clientService.getAccountBalance().subscribe((data) => this.balance = data)
  }

  ngAfterViewInit(): void {
  }


}
