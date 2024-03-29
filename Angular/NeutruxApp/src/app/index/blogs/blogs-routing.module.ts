import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "src/app/users/authentication/auth-guard.service";
import { BlogListComponent } from "./blog-list/blog-list.component";
import { BlogEditorComponent } from "./blog-projects/blog-editor/blog-editor.component";
import { CanDeactivateGuard } from "./blog-projects/blog-editor/can-deactivate-guard.service";
import { BlogEditorDashboardComponent } from "./blog-projects/blog-editor-dashboard.component";
import { BlogComponent } from "./blog/blog.component";
import { BlogsComponent } from "./blogs.component";
import { CategoryComponent } from "./category/category.component";

const blogsRoutes : Routes = [
    { path:'', data: { title:'Neutrux | Blogs' }, component: BlogsComponent },
    { 
        path:'editor/dashboard',
        canActivate: [AuthGuard], 
        data: { roles: ['ROLE_EDITOR']}, 
        component: BlogEditorDashboardComponent,
    },
    { 
        path:'editor/projects/:id',
        component: BlogEditorComponent, 
        canActivate: [AuthGuard], 
        canDeactivate: [CanDeactivateGuard],
        data: { roles: ['ROLE_EDITOR']} 
    },
    { 
        path:'editor/blogs/:id',
        component: BlogEditorComponent, 
        canActivate: [AuthGuard], 
        canDeactivate: [CanDeactivateGuard],
        data: { roles: ['ROLE_EDITOR']} 
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