import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {FormControl, NgForm, Validators} from "@angular/forms";
import {Account, Category, ClientService, Payees} from "../../services/client.service";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {DataProcessor} from "../../services/data-processor";
import {MatPaginator} from "@angular/material/paginator";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.scss']
})
export class AddTransactionComponent implements AfterViewInit {

  name = new FormControl('', [
    Validators.required
  ]);
  address = new FormControl('', [
    Validators.required
  ]);
  required = new FormControl('', [
    Validators.required
  ]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  account: Account[] = [];
  payee: Payees[] = [];
  categories: Category[] = [];
  dataProcess: any;
  accountProcessor: any;
  payeeProcessor: any;


  constructor(clientService: ClientService, private dialog: MatDialog, private router: Router) {
    this.dataProcess = new DataProcessor(clientService);
    this.accountProcessor = new DataProcessor(clientService);
    this.payeeProcessor = new DataProcessor(clientService);
  }

  ngAfterViewInit() {
    this.dataProcess.processCategoriesData().subscribe((data: Category[]) => {
      this.categories = data;
      console.log(this.categories);
    });
    this.fetchAccount();
    this.fetchPayee();
  }

  addTransaction(data: NgForm) {
    console.log(data.value);
    if (data.valid) {
      this.dataProcess.clientService.addTransaction(data.value).subscribe(() => {
          setTimeout(() => {
            this.router.navigate(['/payees']);
          }, 500);
        },
        () => {
          this.openDialog();
        });

    } else {
      this.openDialog();
    }
  }

  fetchAccount() {
    this.accountProcessor.processTableData(this.paginator, this.dataProcess.sort, this.dataProcess.ACCOUNT);
    this.account = this.accountProcessor.dataSource;
    setTimeout(() => {
      this.account = this.accountProcessor.contentData;
    }, 1000);
  }

   fetchPayee() {
    this.payeeProcessor.processTableData(this.paginator, this.dataProcess.sort, this.dataProcess.PAYEES);

    setTimeout(() => {
      console.log(this.payeeProcessor.contentData);
      this.payee = this.payeeProcessor.contentData;
    }, 1000);
  }

  openDialog() {
    this.dialog.open(DialogContentExampleDialog);
  }
}

@Component({
  selector: 'dialog-content-example-dialog',
  templateUrl: 'dialog-content-example-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})

export class DialogContentExampleDialog {
}
