import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './index/home/home.component';
import { IndexComponent } from './index/index.component';
import { ToolsComponent } from './index/tools/tools.component';
import { AuthenticationComponent } from './users/authentication/authentication.component';
import { RegistrationComponent } from './users/registration/registration.component';

const routes: Routes = [
  { path:'', component:IndexComponent, children: [
    { path:'', data: { title:'Neutrux | Home' }, component: HomeComponent },
    { 
      path:'blogs', 
      loadChildren: () => import('../app/index/blogs/blogs.module').then(x => x.BlogsModule) 
    },
    { path:'tools', component: ToolsComponent }
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
