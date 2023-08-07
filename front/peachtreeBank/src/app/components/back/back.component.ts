import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ClientService,} from '../../services/client.service';
import {MatSort} from '@angular/material/sort';
import {MatPaginator,} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {catchError, map, of as observableOf, startWith, Subject, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-back',
  templateUrl: './back.component.html',
  styleUrls: ['./back.component.scss'],
})
export class BackComponent implements AfterViewInit {
  displayColumn: string[] = [
    'id',
    'categoryCode',
    'date',
    'amount',
    'currencyCode',
    'originatorAccountNumber',
    'counterpartyAccount',
    'paymentType',
    'status',
    'title',
  ];
  dataSource: any = [];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalElements: any;
  pageSize: any;
  ContentData: any;
  categoryCode = '';
  sortedBy = '';
  destroy = new Subject<void>();


  constructor(private clientService: ClientService) {
  }

  getTableData$(pageNumber: number) {
    return this.clientService.getApiCategoryCode('/transactions/get', pageNumber, 2, this.categoryCode, this.sortedBy);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.displayData();
  }

  applyFilter(event: Event) {
    this.categoryCode = (event.target as HTMLInputElement).value;
    this.destroy.next();
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
          console.log('totaldata' + this.totalElements)
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
}
