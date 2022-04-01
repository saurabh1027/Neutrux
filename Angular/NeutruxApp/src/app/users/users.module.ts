import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { Router, RouterModule } from "@angular/router";
import { SharedModule } from "../shared/shared.module";
import { AuthenticationComponent } from "./authentication/authentication.component";
import { AuthorizationHeaderInterceptor } from "./authorization-header.interceptor";
import { RegistrationComponent } from "./registration/registration.component";

@NgModule({
    declarations:[
        AuthenticationComponent,
        RegistrationComponent,
    ],
    imports:[
        BrowserModule,
        FormsModule,
        HttpClientModule,
        SharedModule,
        RouterModule
    ],
    exports:[
        AuthenticationComponent,
        RegistrationComponent
    ],
    bootstrap:[]
})
export class UsersModule{ }