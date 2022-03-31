import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { UsersService } from "../users.service";

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
        private usersService:UsersService,
        private router:Router
    ) { }

    ngOnInit(): void {
        this.usersService.logout()
    }

    onAuthenticationFormSubmit( authenticationForm:NgForm ){
        let email:string = authenticationForm.controls['username'].value
        let password:string = authenticationForm.controls['password'].value

        this.usersService.authenticateUser( email,password ).subscribe(response=>{
            this.alert = {
                message: 'Authenticated Successfully!',
                isError: false
            }    
            authenticationForm.reset()
        },(error:any)=>{
            this.alert = {
                message: error.message,
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