import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  template: '<div>{{ data }}</div>'
})
export class AppComponent implements OnInit {
  title = 'peachtreeBank';

  public data: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getMethod();
  }

  public getMethod() {
     this.http.get('http://localhost:8080/appversion', {responseType: 'text'}).subscribe((data) => {
      this.data = data;
      console.log(data);
     });
  }

  
}
