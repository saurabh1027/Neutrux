import { Component, OnInit, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { RegistrationUserModel } from "../registration.user.model";
import { UsersService } from "../users.service";

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: [ './registration.component.sass' ]
})
export class RegistrationComponent implements OnInit{
    alert:{
        'message': string,
        'isError': boolean
    } | null = null
    
    registrationUserModel: RegistrationUserModel 
        = new RegistrationUserModel('','','','')

    @ViewChild('form') registrationForm!: NgForm

    constructor(
        private usersService:UsersService,
        private router:Router
    ) { }

    ngOnInit(): void {
        this.usersService.logout()
    }

    onRegistrationFormSubmit(){
        
        this.usersService.registerUser( this.registrationUserModel ).subscribe(response=>{
            this.alert = {
                message: 'Registered Successfully!',
                isError: false
            }
            this.registrationForm.reset()
        },error=>{
            this.alert = {
                message: error.message,
                isError: true
            }
        })
        
    }
    
    onClose(){
        if( !this.alert?.isError )
            this.router.navigate( ['authentication'] )
        this.alert = null
    }


} 