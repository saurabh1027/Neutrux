import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthService } from "../users/authentication/auth.service";
import { User } from "../users/user.model";

@Component({
    selector: 'app-index',
    templateUrl: './index.component.html',
    styleUrls: ['./index.component.sass']
})
export class IndexComponent implements OnInit, OnDestroy{
    isSidenavOpened: boolean = false;
    user: User|null = null
    private userSub!:Subscription 

    constructor(
        private authService:AuthService
    ) { }

    ngOnInit(): void {
        this.userSub = this.authService.user.subscribe(user=>{
            this.user = user
        })
    }

    toggleSidenav(){
        this.isSidenavOpened = !this.isSidenavOpened
    }

    ngOnDestroy(): void {
        this.userSub.unsubscribe()
    }

}