import { DOCUMENT } from "@angular/common";
import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {
    @ViewChild('navigation') navigation!:ElementRef
    isSidenavOpened:boolean = false

    constructor(
        @Inject(DOCUMENT) private document:Document
    ) {}
    
    ngOnInit(): void {
        
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