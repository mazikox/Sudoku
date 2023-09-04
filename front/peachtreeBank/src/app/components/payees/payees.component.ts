import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {ClientService} from "../../services/client.service";
import {DialogContentExampleDialog} from "../add-payees/add-payees.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {DataProcessor} from "../../services/data-processor";
import {take, takeUntil} from "rxjs";
import {MatSort} from "@angular/material/sort";

@Component({
  selector: 'app-payees',
  templateUrl: './payees.component.html',
  styleUrls: ['./payees.component.scss']
})
export class PayeesComponent extends DataProcessor implements AfterViewInit {
  displayColumn: string[] = [
    'id',
    'name',
    'address',
    'accountNumber',
    'actions'
  ];

  public editingIndex: number = -1;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(clientService: ClientService, private dialog: MatDialog) {
    super(clientService);
  }


  ngAfterViewInit() {
    this.processTableData(this.paginator, this.sort, this.PAYEES);
  }

  override applySort(event: Event) {
    super.applySort(event, this.paginator, this.sort);
  }

  editRow(index: number) {
    this.editingIndex = index;
  }

  cancelEditRow() {
    this.editingIndex = -1;
  }

  saveRow(element: any) {
    this.clientService.updatePayee('/payees/update/' + element.id, element)
      .pipe(takeUntil(this.destroy))
      .subscribe({
        next: () => {
          this.cancelEditRow();
          this.destroy.next();
          this.processTableData(this.paginator, this.sort, this.PAYEES);
        },
        error: () => {
          this.openDialog();
        }
      });
  }

  deleteRow(element: any) {
    this.clientService.deletePayee('/payees/delete/' + element.id).pipe(takeUntil(this.destroy)).subscribe(
      () => {
        this.cancelEditRow();
        this.destroy.next();
        this.processTableData(this.paginator, this.sort, this.PAYEES);
      }
    );
  }

  openDialog() {
    this.dialog.open(DialogContentExampleDialog);
  }

  openDeleteDialog(element: any) {
    const dialogRef = this.dialog.open(DialogContentConfirm);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteRow(element);
      }
    });
  }
}


@Component({
  selector: 'dialog-content-confirm',
  templateUrl: 'dialog-content-confirm.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})

export class DialogContentConfirm {
}
