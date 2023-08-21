import {Injectable, numberAttribute} from '@angular/core';
import {HttpClient, HttpClientModule, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private httpClient: HttpClient) {
  }

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

  public addPayee(url: string, body: Payees) {
    return this.httpClient.post(url, body).pipe(catchError(this.handleError));
  }

  public updatePayee(url: string, body: PayeesDto) {
    return this.httpClient.put(url, body);
  }

  public deletePayee(url: string){
    return this.httpClient.delete(url);
  }

  handleError(error: HttpErrorResponse) {
    return throwError(error)
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

export interface Payees {
  name: string;
  address: string;
  accountNumber: number;
}

export interface PayeesDto {
  id: string;
  name: string;
  accountNumber: number;
  address: string;
  active: boolean;
}
