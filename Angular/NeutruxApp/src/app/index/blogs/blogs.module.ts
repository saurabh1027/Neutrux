import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { BlogsComponent } from "./blogs.component";
import { BlogsService } from "./blogs.service";
import { TopBlogsComponent } from "./top-blogs/top-blogs.component";

@NgModule({
    declarations: [
        BlogsComponent,
        TopBlogsComponent
    ],
    imports: [
        HttpClientModule
    ],
    providers: [
        BlogsService
    ]
})
export class BlogsModule{ }