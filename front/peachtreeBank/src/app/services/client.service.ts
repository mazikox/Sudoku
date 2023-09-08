import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, pipe, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private httpClient: HttpClient) {
  }

  public getApiTransactions(url: string, page: number, pageSize: number, categoryCode: string, sortedBy: string): Observable<RootObject> {
    return this.httpClient.get<RootObject>(
      `${url}?page=${page}&pageSize=${pageSize}&categoryCode=${categoryCode}&sort=${sortedBy}`
    );
  }

  public getApi(url: string, page: number, pageSize: number, sortedBy: string): Observable<RootObject> {
    return this.httpClient.get<RootObject>(
      `${url}?page=${page}&pageSize=${pageSize}&sort=${sortedBy}`
    );
  }

  public getCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>('/categories/all');
  }

  public addPayee(body: Payees) {
    return this.httpClient.post("/payees/add", body).pipe(catchError(this.handleError));
  }

  public addTransaction(body: Payees) {
    return this.httpClient.post("/transactions/add", body).pipe(catchError(this.handleError));
  }

  public updatePayee(url: string, body: PayeesDto) {
    return this.httpClient.put(url, body);
  }

  public deletePayee(url: string) {
    return this.httpClient.delete(url);
  }

  public getAccountBalance() {
    return this.httpClient.get("accounts/countBalance");
  }

  public getVersion() {
    return this.httpClient.get('/appversion', {responseType: 'text'});
  }

  public getAnalytics(dateFrom: number, dateTo: number){
    return this.httpClient.get(`/transactions/groupBy?dateFrom=${dateFrom}&dateTo=${dateTo}`)
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
}


export interface Transaction {
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

export interface Analytics {
  categoryCode: Category
  count: number
  minAmount: number
  maxAmount: number
  avgAmount: number
  sum: number;
}


export interface Category {
  categoryCodeId: number;
  name: string;
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

export interface Account {
  id: number;
  name: string;
  accountNumber: string;
  currency: string;
  balance: number;
}
