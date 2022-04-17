import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BlogsComponent } from './index/blogs/blogs.component';
import { HomeComponent } from './index/home/home.component';
import { IndexComponent } from './index/index.component';
import { AuthenticationComponent } from './users/authentication/authentication.component';
import { RegistrationComponent } from './users/registration/registration.component';

const routes: Routes = [
  { path:'', component:IndexComponent, children: [
    { path:'', component: HomeComponent },
    { path:'blogs', component: BlogsComponent },
  ] },
  { path:'authentication', component:AuthenticationComponent },
  { path:'registration', component:RegistrationComponent },
  { path:'**', redirectTo:'',pathMatch:'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
