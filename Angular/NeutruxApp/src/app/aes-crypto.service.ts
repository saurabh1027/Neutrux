import { Injectable } from "@angular/core";
import * as CryptoJS from 'crypto-js';

@Injectable({
    providedIn:'root'
})
export class AesCryptoService{

    private signing_key:string = 'ndsjtvGY3D32EG6D7O67hj88x88HJb89bj9H9nB89k6mB4hjJ1H5N6J8KK8JNj42bB'

    encryptData(data:string):string{
        return CryptoJS.AES.encrypt(data,this.signing_key).toString();
    }

    decryptData(data:any):string{
        return CryptoJS.AES.decrypt(data,this.signing_key).toString(CryptoJS.enc.Utf8);
    }

}