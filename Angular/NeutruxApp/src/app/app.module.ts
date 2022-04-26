import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core.module';
import { BlogsModule } from './index/blogs/blogs.module';
import { FooterComponent } from './index/footer/footer.component';
import { HeaderComponent } from './index/header/header.component';
import { SidenavComponent } from './index/header/sidenav/sidenav.component';
import { HomeComponent } from './index/home/home.component';
import { IndexComponent } from './index/index.component';
import { SharedModule } from './shared/shared.module';
import { UsersModule } from './users/users.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    IndexComponent,
    SidenavComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    // custom modules
    CoreModule,
    UsersModule,
    SharedModule,
    BlogsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
