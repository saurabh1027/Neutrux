import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
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
import { ToolsComponent } from './index/tools/tools.component';
import { SharedModule } from './shared/shared.module';
import { AuthorizationHeaderInterceptor } from './users/authorization-header.interceptor';
import { UsersModule } from './users/users.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    IndexComponent,
    SidenavComponent,
    HomeComponent,
    ToolsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    HttpClientModule,

    // custom modules
    CoreModule,
    UsersModule,
    SharedModule,
    BlogsModule,
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthorizationHeaderInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
