import { CommonModule } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { RouterModule } from "@angular/router";
import { SharedModule } from "src/app/shared/shared.module";
import { BlogListComponent } from "./blog-list/blog-list.component";
import { BlogComponent } from "./blog/blog.component";
import { CommentsComponent } from "./blog/comments/comments.component";
import { CommentsService } from "./blog/comments/comments.service";
import { BlogsRoutingModule } from "./blogs-routing.module";
import { BlogsComponent } from "./blogs.component";
import { BlogsService } from "./blogs.service";
import { CategoryComponent } from "./category/category.component";
import { TopBlogsComponent } from "./top-blogs/top-blogs.component";

@NgModule({
    declarations: [
        BlogsComponent,
        TopBlogsComponent,
        BlogListComponent,
        BlogComponent,
        CommentsComponent,
        CategoryComponent
    ],
    imports: [
        // don't import the modules that are already present in AppModule for e.g. BrowserModule, HttpClientModule.
        RouterModule,
        SharedModule,
        FormsModule,
        CommonModule,

        BlogsRoutingModule,
    ],
    providers: [
        BlogsService,
        CommentsService
    ]
})
export class BlogsModule{ }