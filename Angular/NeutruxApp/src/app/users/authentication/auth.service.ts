import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, throwError } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { AesCryptoService } from "src/app/aes-crypto.service";
import { environment } from "src/environments/environment.prod";
import { User } from "../user.model";

export interface AuthResponseData {
    firstname: string,
    lastname: string,
    email: string,
    userId: string,
    expiresIn: number
}

@Injectable()
export class AuthService{
    user = new BehaviorSubject<User|null>(null)
    private tokenExpirationTimer: any;

    constructor(
        private httpClient:HttpClient,
        private aesCryptoService:AesCryptoService,
        private router:Router
    ) { }


    // Sign-In
    authenticateUser(
        email: string,
        password: string
    ){
        return this.httpClient.post<AuthResponseData>(
            environment.backendServerUrl + 'authenticate',
            { 'email': email, 'password': password },
            { observe : 'response' }
        ).pipe(
            catchError( this.handleError ),
            tap( resData=>{
                let token = resData.headers.get('X-Access-Token')
                if( resData && resData.body && token ){
                    let authResponseData:AuthResponseData = resData.body
                    this.handleAuthentication( authResponseData,token )
                }
            } )
        )
    }

    // Sign-Out
    logout( path:string ){
        this.user.next(null)
        this.router.navigate(['',path])
        localStorage.removeItem('userData')

        if( this.tokenExpirationTimer )
            clearTimeout( this.tokenExpirationTimer )

        this.tokenExpirationTimer = null
    }

    autoLogin(){
        let encryptedUserData = localStorage.getItem('userData')
        let userData = ''
        
        if(!encryptedUserData) return
        userData = this.aesCryptoService.decryptData( encryptedUserData )

        if(!userData) return
        const user:{
            userId: string,
            firstname: string,
            lastname: string,
            email: string,
            _accessToken: string,
            _accessTokenExpirationDate: string
        } = JSON.parse( userData )
        
        const loadedUser:User = new User(
            user.userId,
            user.firstname,
            user.lastname,
            user.email,
            user._accessToken,
            new Date( user._accessTokenExpirationDate )
        )

        if( loadedUser.accessToken ) {
            this.user.next( loadedUser )
            const sessionExpiryDuration 
                = new Date(user. _accessTokenExpirationDate).getTime() - new Date().getTime();
            this.autoLogout( sessionExpiryDuration )
        }
    
    }

    autoLogout(sessionExpiryDuration: number) {
        this.tokenExpirationTimer = setTimeout(() => {
            this.logout('authentication');
        }, sessionExpiryDuration);
    }

    private handleAuthentication( authResponseData:AuthResponseData,token:string ) {
        let expirationDate:Date = new Date( +authResponseData.expiresIn )

        const user = new User(
            authResponseData.userId,
            authResponseData.firstname,
            authResponseData.lastname,
            authResponseData.email,
            token,
            expirationDate
        )

        this.user.next( user )
        this.autoLogout( +authResponseData.expiresIn )
        const encryptedUserData = this.aesCryptoService.encryptData( JSON.stringify(user) )

        localStorage.setItem('userData', encryptedUserData )
    }

    private handleError(errorRes: HttpErrorResponse) {
        let errorMessage = 'An unknown error occurred!';
        if (!errorRes.error || !errorRes.error.error) {
            return throwError( errorMessage );
        }
        switch (errorRes.error.message) {
            case 'Unauthorized':
                errorMessage = 'Invalid Credentials!'
                break;
        }
        return throwError( errorMessage );
    }

}