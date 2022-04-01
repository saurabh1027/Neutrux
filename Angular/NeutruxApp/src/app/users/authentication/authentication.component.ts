import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { UsersService } from "../users.service";
import { AuthService } from "./auth.service";

@Component({
    selector:'app-authentication',
    templateUrl:'./authentication.component.html',
    styleUrls: [ './authentication.component.sass' ],
})
export class AuthenticationComponent implements OnInit {
    alert:{
        'message': string,
        'isError': boolean
    } | null = null

    constructor(
        private authService:AuthService,
        private router:Router
    ) { }

    ngOnInit(): void {
        this.authService.logout('authentication')
    }

    onAuthenticationFormSubmit( authenticationForm:NgForm ){
        let email:string = authenticationForm.controls['username'].value
        let password:string = authenticationForm.controls['password'].value

        this.authService.authenticateUser( email,password ).subscribe(response=>{
            this.alert = {
                message: 'Authenticated Successfully!',
                isError: false
            }
            authenticationForm.reset()
        },(errorMessage:any)=>{
            this.alert = {
                message: errorMessage,
                isError: true
            }
        })
        
    }
    
    onClose(){
        if( !this.alert?.isError )
            this.router.navigate( [''] )
        this.alert = null
    }

}