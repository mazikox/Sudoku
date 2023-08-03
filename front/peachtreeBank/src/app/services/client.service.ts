import { Injectable, numberAttribute } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private httpClient: HttpClient) {}

  public getApiCategoryCode(url: string, page: number, pageSize: number, categoryCode: string, sortedBy: string): Observable<RootObject> {
    return this.httpClient.get<RootObject>(
      url + '?page=' + page + '&pageSize=' + pageSize + '&categoryCode=' + categoryCode + '&sort=' + sortedBy
    );
  }
  public getApi(url: string, page: number, pageSize: number, sortedBy: string): Observable<RootObject> {
    return this.httpClient.get<RootObject>(
      url + '?page=' + page + '&pageSize=' + pageSize + '&sort=' + sortedBy
    );
  }
}

export interface RootObject {
  content: Content[];
  pageable: Pageable;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: Sort;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface Pageable {
  sort: Sort;
  offset: number;
  pageNumber: number;
  pageSize: number;
  unpaged: boolean;
  paged: boolean;
}

export interface Sort {
  empty: boolean;
  unsorted: boolean;
  sorted: boolean;
}

export interface Content {
  id: number;
  categoryCode: string;
  date: string;
  amount: number;
  currencyCode: string;
  originatorAccountNumber: string;
  counterpartyAccount: string;
  paymentType: string;
  status: string;
  title: string;
}
