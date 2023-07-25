import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  template: `
    <div>{{ responseData }}</div>
  `
})
export class AppComponent {
  responseData: any;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get('localhost:8080/appversion').subscribe(data => {
      this.responseData = data;
      console.log(this.responseData); // wyświetli odpowiedź w konsoli
    });
  }
}