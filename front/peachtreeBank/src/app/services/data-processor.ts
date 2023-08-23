import {Injectable, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {catchError, map, of as observableOf, startWith, Subject, switchMap, takeUntil} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {ClientService} from "./client.service";

@Injectable()
export abstract class DataProcessor implements OnDestroy {

  TRANSACTION: number = 1;
  PAYEES: number = 2;
  STATUS: number = -1;

  totalElements: any;
  pageSize: any;
  dataSource: any = [];
  ContentData: any;
  sortedBy = '';
  categoryCode = '';
  destroy = new Subject<void>();

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;


  protected constructor(public clientService: ClientService) {
  }


  public ngOnDestroy(): void {
    this.destroy.next();
    this.destroy.complete();
  }

  processTableData(paginator: MatPaginator, sort: MatSort, status: number) {
    this.STATUS = status;
    paginator.page
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.getTableData$(paginator.pageIndex).pipe(
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
        this.dataSource.sort = sort;
      });
  }

  getTableData$(pageNumber: number) {
    switch (this.STATUS) {
      case this.TRANSACTION: {
        return this.clientService.getApiCategoryCode('/transactions/get', pageNumber, 2, this.categoryCode, this.sortedBy);
      }
      case this.PAYEES: {
        return this.clientService.getApiTransactions('/payees/get', pageNumber, 5, this.sortedBy);
      }
      default: {
        return this.clientService.getApiTransactions('/payees/get', pageNumber, 5, this.sortedBy);
      }
    }
  }

  applySort(event: Event, paginator: MatPaginator, sort: MatSort) {
    this.sortedBy = (event.target as HTMLInputElement).value;
    this.destroy.next();
    this.processTableData(paginator, sort, this.STATUS);
  }

}
