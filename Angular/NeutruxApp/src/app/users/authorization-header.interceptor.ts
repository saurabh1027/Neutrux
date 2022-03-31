import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { UsersService } from "./users.service";

@Injectable({
    providedIn: 'root'
})
export class AuthorizationHeaderInterceptor /*implements HttpInterceptor*/ {

    constructor( private usersService:UsersService ) {}

    // intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //     let newRequest = req.clone()
    //     let accessToken = this.usersService.getAccessToken()
        
    //     if( accessToken ){
    //         newRequest = req.clone({
    //             setHeaders: {
    //                 Authorization: 'Bearer '+accessToken
    //             }
    //         })
    //     }

    //     return next.handle( newRequest )
    // }
    
}