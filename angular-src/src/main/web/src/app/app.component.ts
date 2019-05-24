import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {WebsoketService} from "./service/websoket.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Z prime app';

  constructor(private http: HttpClient, private websoketService: WebsoketService) {

  }

  ngOnInit(): void {
    // this.http.get<String>("api/home").subscribe(result => console.log(result));
    // this.http.post("api/create", {"qq":"123"}).subscribe(result => console.log(result));
    // this.websoketService.initializeWebSocketConnection();
  }
  
}
