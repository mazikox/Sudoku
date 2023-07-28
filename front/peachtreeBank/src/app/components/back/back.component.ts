import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ClientService,} from '../../services/client.service';
import {MatSort} from '@angular/material/sort';
import {MatPaginator,} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Subject, catchError, map, of as observableOf, startWith, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-back',
  templateUrl: './back.component.html',
  styleUrls: ['./back.component.scss'],
})
export class BackComponent implements OnInit, AfterViewInit {
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
  isLoading = false;
  categoryCode = '';
  sortedBy = '';
  destroy = new Subject<void>();


  constructor(private clientService: ClientService) {
  }

  ngOnInit() {
  }

  getTableData$(pageNumber: number) {
    return this.clientService.getApi(pageNumber, 2, this.categoryCode, this.sortedBy);
  }

  ngAfterViewInit() {
    this.displayData();
  }

  applyFilter(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const filterValue = inputElement.value;
    this.categoryCode = filterValue;
    this.destroy.next();
    this.displayData();
  }

  applySort(event: Event) {
    const sortValue = (event.target as HTMLInputElement).value;
    this.sortedBy = sortValue;
    this.destroy.next();
    this.displayData()
  }


  displayData() {
    this.dataSource.paginator = this.paginator;

    this.paginator.page
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoading = true;
          return this.getTableData$(this.paginator.pageIndex).pipe(
            catchError(() => observableOf(null))
          );
        }),
        map((contentData) => {
          if (contentData == null) return [];
          debugger;
          this.totalElements = contentData.totalElements;
          this.pageSize = contentData.size;
          console.log('totaldata' + this.totalElements)
          this.isLoading = false;
          return contentData.content;
        }),
        takeUntil(this.destroy)
      )
      .subscribe((contData) => {
        this.ContentData = contData;
        this.dataSource = new MatTableDataSource(this.ContentData);
        // this.dataSource.sort = this.sort;
      });
  }
}
