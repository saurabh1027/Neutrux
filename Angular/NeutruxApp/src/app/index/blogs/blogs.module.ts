import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { SharedModule } from "src/app/shared/shared.module";
import { BlogEditorService } from "./blog-projects/blog-editor/blog-editor.service";
import { BlogListComponent } from "./blog-list/blog-list.component";
import { BlogProjectsComponent } from "./blog-projects/blog-projects.component";
import { BlogComponent } from "./blog/blog.component";
import { CommentsComponent } from "./blog/comments/comments.component";
import { CommentsService } from "./blog/comments/comments.service";
import { BlogsRoutingModule } from "./blogs-routing.module";
import { BlogsComponent } from "./blogs.component";
import { BlogsService } from "./blogs.service";
import { CategoryComponent } from "./category/category.component";
import { TopBlogsComponent } from "./top-blogs/top-blogs.component";
import { BlogEditorComponent } from "./blog-projects/blog-editor/blog-editor.component";
import { BlogDetailsComponent } from "./blog-projects/blog-editor/blog-details/blog-details.component";
import { BlogElementsComponent } from "./blog-projects/blog-editor/blog-elements/blog-elements.component";
import { SelectCategoryComponent } from "./blog-projects/blog-editor/select-category/select-category.component";
import { AddBlogElementComponent } from "./blog-projects/blog-editor/add-blog-element/add-blog-element.component";

@NgModule({
    declarations: [
        BlogsComponent,
        TopBlogsComponent,
        BlogListComponent,
        BlogComponent,
        CommentsComponent,
        CategoryComponent,
        BlogProjectsComponent,
        BlogEditorComponent,
        BlogDetailsComponent,
        BlogElementsComponent,
        SelectCategoryComponent,
        AddBlogElementComponent
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
        CommentsService,
        BlogEditorService
    ]
})
export class BlogsModule{ }