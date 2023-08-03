import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {catchError, map, of as observableOf, startWith, Subject, switchMap, takeUntil} from "rxjs";
import {ClientService} from "../../services/client.service";
import {MatTableDataSource} from "@angular/material/table";

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
  ];
  dataSource: any = [];
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalElements: any;
  pageSize: any;
  ContentData: any;
  sortedBy = '';
  destroy = new Subject<void>();


  constructor(private clientService: ClientService) {
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
}

