import { Component, OnInit } from "@angular/core";
import { BlogModel } from "../blog.model";
import { BlogsService } from "../blogs.service";

@Component({
    selector: 'app-top-blogs',
    templateUrl: './top-blogs.component.html',
    styleUrls: ['./top-blogs.component.sass']
})
export class TopBlogsComponent implements OnInit {
    blogs: BlogModel[] = []

    constructor(
        private blogsService:BlogsService
    ) {}

    ngOnInit(): void {
        this.blogsService.getTrendingBlogs(undefined, undefined, true, true).subscribe(data => {
            console.log(data)
            this.blogs = data
            console.log(this.blogs)
        })
    }



}