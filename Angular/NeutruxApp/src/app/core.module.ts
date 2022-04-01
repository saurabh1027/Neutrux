import { NgModule } from "@angular/core";
import { AesCryptoService } from "./aes-crypto.service";
import { AuthService } from "./users/authentication/auth.service";
import { UsersService } from "./users/users.service";

@NgModule({
    providers: [
        UsersService,
        AesCryptoService,
        AuthService
    ]
})
export class CoreModule{ }