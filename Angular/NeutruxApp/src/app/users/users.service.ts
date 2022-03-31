import { Injectable } from "@angular/core"
import { HttpClient } from "@angular/common/http";
import { map,catchError } from "rxjs/operators";
import { Observable, of } from "rxjs";
import { AesCryptoService } from "../aes-crypto.service";
import { UserModel } from "./user.model";

@Injectable({
    providedIn: 'root'
})
export class UsersService {
    private baseUrl: string = "http://localhost:8010/";
    private user: UserModel|null = null

    constructor(
        private http: HttpClient,
        private aesCryptoService:AesCryptoService
    ) { }

    getAccessToken(){
        return this.aesCryptoService.decryptData( localStorage.getItem('access-token') )
    }

    authenticateUser( email:string,password:string ) : Observable<any> {
        return this.http.post(
            this.baseUrl + 'authenticate',{
                'email':email, 'password':password
            },{ 
                observe: 'response'
            }
        ).pipe( 
            map( response =>{
                const token:string|null = response.headers.get('X-Access-Token')
                const userId:string|null = response.headers.get('X-User-ID')

                if( token && userId ){
                    localStorage.setItem('access-token',this.aesCryptoService.encryptData(token))
                    localStorage.setItem('id',this.aesCryptoService.encryptData(userId))
                }

                if( response.body )
                    this.user = JSON.parse( JSON.stringify( response.body ) )
            })
        );
    }

    registerUser( registerUserModel:any ) : Observable<any> {
        return this.http.post(
            this.baseUrl + 'users/',
            registerUserModel,{
                observe: 'response'
            }
        ).pipe( 
            map( response =>{
                console.log(response)
            }),
            catchError((err) => {
                if(err.error)
                    throw err.error
                throw err
            })
        );
    }

    logout(){
        localStorage.removeItem('access-token')
        localStorage.removeItem('id')
    }

    getAuthenticatedUser(){
        let token = this.getAccessToken()
        let userId = this.aesCryptoService.decryptData( localStorage.getItem('id') )

        if( token && userId ){
            this.getUserById( userId ).subscribe(response=>{
                this.user = response
                return this.user
            },error=>{
                console.log(error)
                return null
            }).unsubscribe()
        }
    }

    getUserById(id: any) : Observable<UserModel> {
        return this.http.get<UserModel>(this.baseUrl + "users/" + id);
    }

}