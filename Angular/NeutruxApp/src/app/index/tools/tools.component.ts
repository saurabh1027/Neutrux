import { Component, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";

@Component({
    selector: 'app-tools',
    templateUrl: 'tools.component.html',
    styleUrls: ['tools.component.sass']
})
export class ToolsComponent implements OnInit {
    user!:User
    userSub!:Subscription

    constructor(
        private authService:AuthService
    ) {}

    ngOnInit(): void {
        this.userSub = this.authService.user.subscribe( user=>{
            if(user) this.user = user
        } )
    }

}