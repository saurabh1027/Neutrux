import { Component } from "@angular/core";

@Component({
    selector: 'app-index',
    templateUrl: './index.component.html',
    styleUrls: ['./index.component.sass']
})
export class IndexComponent{
    isSidenavOpened: boolean = false;

    toggleSidenav(){
        this.isSidenavOpened = !this.isSidenavOpened
    }


}