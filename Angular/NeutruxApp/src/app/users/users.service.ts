import { Injectable } from "@angular/core"
import { HttpClient } from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment.prod";

@Injectable({
    providedIn: 'root'
})
export class UsersService {

    constructor(
        private http: HttpClient
    ) { }

    registerUser( registerUserModel:any ) : Observable<any> {
        return this.http.post(
            environment.backendServerUrl + 'users/',
            registerUserModel,
            { observe: 'response' }
        ).pipe( 
            catchError((err) => {
                if(err.error)
                    throw err.error
                throw err
            })
        );
    }

}