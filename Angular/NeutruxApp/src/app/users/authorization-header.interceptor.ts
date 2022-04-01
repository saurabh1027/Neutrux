import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { exhaustMap, Observable, take } from "rxjs";
import { AuthService } from "./authentication/auth.service";

@Injectable({
    providedIn: 'root'
})
export class AuthorizationHeaderInterceptor {/*implements HttpInterceptor {

    constructor( private authService:AuthService ) {}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return this.authService.user.pipe(
            take(1),
            exhaustMap(user => {
                if( !user )
                    return next.handle(req)
                
                const newRequest = req.clone({
                    headers: new HttpHeaders().set('Authorization', 'Bearer '+user.accessToken)
                })

                return next.handle( newRequest )
            })
        )
    }*/
    
}