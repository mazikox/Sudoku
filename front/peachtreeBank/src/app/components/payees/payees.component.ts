import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {ClientService} from "../../services/client.service";
import {DialogContentExampleDialog} from "../add-payees/add-payees.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import {DataProcessor} from "../../services/data-processor";
import {takeUntil} from "rxjs";

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
    'active',
    'actions'
  ];

  public editingIndex: number = -1;
  @ViewChild(MatPaginator) override paginator!: MatPaginator;


  constructor(clientService: ClientService, private dialog: MatDialog) {
    super(clientService);
  }


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
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
      .subscribe(
      () => {
        this.cancelEditRow();
        this.destroy.next();
        this.processTableData(this.paginator, this.sort, this.PAYEES);
      },
      () => {
        this.openDialog();
      }
    );
  }

  deleteRow(element: any) {
    this.clientService.deletePayee('/payees/delete/' + element.id).subscribe(
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
