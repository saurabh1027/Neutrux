import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
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

    isLoadingActive:boolean = false

    constructor(
        private authService:AuthService,
        private router:Router
    ) { }

    ngOnInit(): void {
        this.authService.logout('authentication')
    }

    onAuthenticationFormSubmit( authenticationForm:NgForm ){
        this.isLoadingActive = true
        let email:string = authenticationForm.controls['username'].value
        let password:string = authenticationForm.controls['password'].value

        this.authService.authenticateUser( email,password ).subscribe(response=>{
            authenticationForm.reset()
            this.isLoadingActive = false
            this.router.navigate( [''] )
        },(errorMessage:any)=>{
            this.alert = {
                message: errorMessage,
                isError: true
            }
            this.isLoadingActive = false
        })
        
    }
    
    onClose(){
        if( !this.alert?.isError )
            this.router.navigate( [''] )
        this.alert = null
    }

}