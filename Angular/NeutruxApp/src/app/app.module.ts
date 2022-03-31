import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AesCryptoService } from './aes-crypto.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './index/footer/footer.component';
import { HeaderComponent } from './index/header/header.component';
import { SidenavComponent } from './index/header/sidenav/sidenav.component';
import { IndexComponent } from './index/index.component';
import { SharedModule } from './shared/shared.module';
import { UsersModule } from './users/users.module';
import { UsersService } from './users/users.service';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    IndexComponent,
    SidenavComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    // custom modules
    UsersModule,
    SharedModule,
  ],
  exports: [

  ],
  providers: [UsersService,AesCryptoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
