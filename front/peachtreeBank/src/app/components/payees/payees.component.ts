import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {catchError, map, of as observableOf, startWith, Subject, switchMap, takeUntil} from "rxjs";
import {ClientService} from "../../services/client.service";
import {MatTableDataSource} from "@angular/material/table";
import {HttpErrorResponse} from "@angular/common/http";
import {DialogContentExampleDialog} from "../add-payees/add-payees.component";
import {MatDialog, MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-payees',
  templateUrl: './payees.component.html',
  styleUrls: ['./payees.component.scss']
})
export class PayeesComponent implements AfterViewInit {
  displayColumn: string[] = [
    'id',
    'name',
    'address',
    'accountNumber',
    'active',
    'actions'
  ];
  dataSource: any = [];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalElements: any;
  pageSize: any;
  ContentData: any;
  sortedBy = '';
  destroy = new Subject<void>();
  public editingIndex: number = -1;


  constructor(private clientService: ClientService, private dialog: MatDialog) {
  }


  getTableData$(pageNumber: number) {
    return this.clientService.getApi('/payees/get', pageNumber, 5, this.sortedBy);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.displayData();
  }

  applySort(event: Event) {
    this.sortedBy = (event.target as HTMLInputElement).value;
    this.destroy.next();
    this.displayData();
  }

  displayData() {
    this.paginator.page
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.getTableData$(this.paginator.pageIndex).pipe(
            catchError(() => observableOf(null))
          );
        }),
        map((contentData) => {
          if (contentData == null) return [];
          this.totalElements = contentData.totalElements;
          this.pageSize = contentData.size;
          console.log('totaldata' + this.totalElements);
          return contentData.content;
        }),
        takeUntil(this.destroy)
      )
      .subscribe((contData) => {
        this.ContentData = contData;
        this.dataSource = new MatTableDataSource(this.ContentData);
        this.dataSource.sort = this.sort;
      });
  }

  editRow(index: number) {
    this.editingIndex = index;
  }

  saveRow(element: any) {
    this.clientService.updatePayee('/payees/update/' + element.id, element).subscribe(
      () => {
        this.editingIndex = -1;
        this.destroy.next();
        this.displayData();
      },
      (error: HttpErrorResponse) => {
        this.openDialog();
      }
    );
  }

  cancelEdit(index: number) {
    this.editingIndex = -1;
  }

  deleteRow(element: any) {
    this.clientService.deletePayee('/payees/delete/' + element.id).subscribe(
      () => {
        this.editingIndex = -1;
        this.destroy.next();
        this.displayData();
      }
    );
  }

  openDialog() {
    this.dialog.open(DialogContentExampleDialog);
  }

  openDeleteDialog(element: any) {
    const dialogRef = this.dialog.open(DialogContentConfirm);
    dialogRef.afterClosed().subscribe(result => {
      console.log(result)
      if (result) {
        this.deleteRow(element);
      }
    });
  }

  protected readonly open = open;
}


@Component({
  selector: 'dialog-content-confirm',
  templateUrl: 'dialog-content-confirm.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})

export class DialogContentConfirm {
}
