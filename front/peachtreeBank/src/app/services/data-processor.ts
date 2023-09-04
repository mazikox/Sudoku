import {Injectable, OnDestroy} from "@angular/core";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {catchError, map, of as observableOf, startWith, Subject, Subscription, switchMap, takeUntil} from "rxjs";
import {MatTableDataSource} from "@angular/material/table";
import {ClientService} from "./client.service";

@Injectable()
export class DataProcessor implements OnDestroy {

  TRANSACTION: number = 1;
  PAYEES: number = 2;
  ACCOUNT: number = 3;
  STATUS: number = -1;

  totalElements: any;
  pageSize: any;
  dataSource: any = [];
  contentData: any;
  sortedBy = '';
  categoryCode = '';
  destroy = new Subject<void>();

  private dataProcessed = new Subject<void>();

  public constructor(public clientService: ClientService) {
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
          return contentData.content;
        }),
        takeUntil(this.destroy)
      )
      .subscribe((contData) => {
        this.contentData = contData;
        this.dataSource = new MatTableDataSource(this.contentData);
        this.dataSource.sort = sort;
        this.dataProcessed.next();
      });
  }

  getTableData$(pageNumber: number) {
    switch (this.STATUS) {
      case this.TRANSACTION: {
        return this.clientService.getApiTransactions('/transactions/get', pageNumber, 10, this.categoryCode, this.sortedBy);
      }
      case this.PAYEES: {
        return this.clientService.getApi('/payees/get', pageNumber, 5, this.sortedBy);
      }
      case this.ACCOUNT: {
        return this.clientService.getApi('/accounts/get', pageNumber, 5, this.sortedBy);
      }
      default: {
        return this.clientService.getApi('/payees/get', pageNumber, 5, this.sortedBy);
      }
    }
  }

  processCategoriesData(){
    return this.clientService.getCategories();
  }

  applySort(event: Event, paginator: MatPaginator, sort: MatSort) {
    this.sortedBy = (event.target as HTMLInputElement).value;
    this.destroy.next();
    this.processTableData(paginator, sort, this.STATUS);
  }

}
