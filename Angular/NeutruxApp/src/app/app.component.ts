import { Component, OnInit } from '@angular/core';
import { AuthService } from './users/authentication/auth.service';
import { UsersService } from './users/users.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {

  constructor(
    private authService:AuthService
  ) { }

  ngOnInit(): void {
    this.authService.autoLogin()
  }

}