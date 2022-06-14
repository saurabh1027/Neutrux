import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "src/app/users/authentication/auth-guard.service";
import { BlogListComponent } from "./blog-list/blog-list.component";
import { BlogProjectsComponent } from "./blog-projects/blog-projects.component";
import { BlogComponent } from "./blog/blog.component";
import { BlogsComponent } from "./blogs.component";
import { CategoryComponent } from "./category/category.component";

const blogsRoutes : Routes = [
    { path:'', data: { title:'Neutrux | Blogs' }, component: BlogsComponent },
    { 
        path:'editor',
        canActivate: [AuthGuard], 
        data: { roles: ['ROLE_EDITOR']}, 
        component: BlogProjectsComponent
    },
    { path:':id', component: BlogComponent },
    { path:'categories/:id', component: CategoryComponent, children:[
        { path:'blogs', component:BlogListComponent }
    ] },
]

@NgModule({
    imports: [
        RouterModule.forChild( blogsRoutes )
    ], 
    exports: [RouterModule]
})
export class BlogsRoutingModule {}