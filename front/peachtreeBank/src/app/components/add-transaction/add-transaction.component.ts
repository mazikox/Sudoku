import {AfterViewInit, Component, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormControl, NgForm, Validators} from "@angular/forms";
import {Category, ClientService} from "../../services/client.service";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {DataProcessor} from "../../services/data-processor";
import {MatPaginator} from "@angular/material/paginator";
import {MatButtonModule} from "@angular/material/button";
import {MatSort} from "@angular/material/sort";
import {takeUntil} from "rxjs";

@Component({
  selector: 'app-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.scss'],
})
export class AddTransactionComponent implements AfterViewInit {

  required = new FormControl('', [
    Validators.required,
  ]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  categories: Category[] = [];
  dataProcess: DataProcessor;
  accountProcessor: any;
  payeeProcessor: any;
  selectedAccountNumber: any;
  selectedAccountCurrencyCode: any;
  currencyCode: any;
  isDisabled: boolean = true;


  constructor(clientService: ClientService, private dialog: MatDialog, private router: Router) {
    this.accountProcessor = new DataProcessor(clientService);
    this.payeeProcessor = new DataProcessor(clientService);
    this.dataProcess = new DataProcessor(clientService);
  }

  ngAfterViewInit() {
    this.accountProcessor.processTableData(this.paginator, this.sort, this.dataProcess.ACCOUNT);
    this.payeeProcessor.processTableData(this.paginator, this.sort, this.dataProcess.PAYEES);
    this.dataProcess.processCategoriesData().subscribe((data: Category[]) => {
      this.categories = data;
    });
  }

  addTransaction(data: NgForm) {
    console.log(data.value)
    if (data.valid) {
      this.dataProcess.clientService.addTransaction(data.value).pipe(takeUntil(this.dataProcess.destroy)).subscribe({
        next: () => {
          setTimeout(() => {
            this.router.navigate(['/transactions']);
          }, 500);
        },
        error: () => {
          this.openDialog();
        }
      });
    } else {
      this.openDialog();
    }
  }


  openDialog() {
    this.dialog.open(DialogContentExampleDialog);
  }

  myMethod() {
    this.accountProcessor.contentData.forEach((value: any) => {
      if (value.accountNumber == this.selectedAccountNumber) {
        this.selectedAccountCurrencyCode = value.currency;
      }
    })
  }

  changeDisable() {
    this.isDisabled = false;
  }
}

@Component({
  selector: 'field-require-dialog',
  templateUrl: 'field-require-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})

export class DialogContentExampleDialog {
}
