import { Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";

@Component({
    selector: 'app-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.sass']
})
export class SidenavComponent implements OnInit, OnDestroy {
    @Output('closeSidenav')
    closeSidenav: EventEmitter<void> = new EventEmitter()

    @ViewChild('sidenav')
    sidenav!: ElementRef

    user!:User
    private userSub!:Subscription

    constructor(
        private authService:AuthService
    ) {}

    ngOnInit(): void {
        this.authService.user.subscribe(user=>{
            if(user)
                this.user = user
        })
    }

    onClick(){
        this.sidenav.nativeElement.classList.remove('open')
        this.sidenav.nativeElement.classList.add('close')
        setTimeout(() => {
            this.closeSidenav.emit() 
        }, 450);
    }

    ngOnDestroy(): void {
        
    }

}