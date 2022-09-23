import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { AesCryptoService } from "src/app/aes-crypto.service";
import { User } from "../user.model";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    
    constructor(
        private authService:AuthService,
        private router:Router,
        private cryptoService:AesCryptoService
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot
    ) : boolean | Observable<boolean> | Promise<boolean> {
        let is_authorized : boolean = false;
        let encryptedUserStr = localStorage.getItem('userData');
        if( !encryptedUserStr ){
            alert("Please log in first!")
            this.router.navigate( ['','authentication'] )
            return false;
        }
        let user:User = JSON.parse( this.cryptoService.decryptData(encryptedUserStr) )
        
        if( route.data['roles'] ){
            is_authorized = this.isAuthorized( route.data['roles'], user.roles )
        }
        if(is_authorized) {
            return true
        } else {
            alert("Unauthorized access!")
            this.router.navigate( ['','tools'] )
            return false
        }
    }

    isAuthorized( authorizedRoles:string[], currentUserRoles:string[] ) {
        for(let i=0; i<authorizedRoles.length; i++) {
            for(let j=0; j<currentUserRoles.length; j++) {
                if( authorizedRoles[i].trim() == currentUserRoles[j].trim() )
                    return true
            }
        }
        return false
    }
    

}