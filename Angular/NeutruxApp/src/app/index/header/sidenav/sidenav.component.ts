import { Component, ElementRef, EventEmitter, OnInit, Output, ViewChild } from "@angular/core";
import { UsersService } from "src/app/users/users.service";

@Component({
    selector: 'app-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.sass']
})
export class SidenavComponent implements OnInit {
    @Output('closeSidenav')
    closeSidenav: EventEmitter<void> = new EventEmitter()
    @ViewChild('sidenav')
    sidenav!: ElementRef

    constructor(
        private usersService:UsersService
    ) {}

    ngOnInit(): void {
        // console.log( this.usersService.getAuthenticatedUser() )
    }

    onClick(){
        this.sidenav.nativeElement.classList.remove('open')
        this.sidenav.nativeElement.classList.add('close')
        setTimeout(() => {
            this.closeSidenav.emit() 
        }, 450);
    }
}