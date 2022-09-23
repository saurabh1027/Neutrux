import { DOCUMENT } from "@angular/common";
import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {
    @ViewChild('navigation') navigation!:ElementRef
    isSidenavOpened:boolean = false
    user!:User

    userSub!:Subscription

    constructor(
        private authService:AuthService,
        @Inject(DOCUMENT) private document:Document,
    ) {}

    ngOnInit(): void {
        this.userSub = this.authService.user.subscribe( user=>{
            if(user) {
                this.user = user
            }
        } )
    }
    
    toggleNavbar(bool:boolean){
        if(bool){
            this.navigation.nativeElement.classList.add('active')
            this.document.body.style.overflow = 'hidden'
        }else{
            this.document.body.style.overflow = 'visible'
            this.navigation.nativeElement.classList.remove('active')
        }
    }

}