import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../service/data.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private data: DataService) {
  }

  ngOnInit() {
    this.data.currentMessage.subscribe(message => this.username = message)
  }

  connect(username: string) {
    this.data.changeMessage(username);
    this.router.navigate(['/chat/'+username]);
  }

}
