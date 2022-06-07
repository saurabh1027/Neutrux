import { Component, OnInit } from "@angular/core";
import { Title } from "@angular/platform-browser";
import { ActivatedRoute } from "@angular/router";
import { BlogModel } from "./blog.model";
import { BlogsService } from "./blogs.service";

@Component({
    selector: 'app-blogs',
    templateUrl: 'blogs.component.html'
})
export class BlogsComponent implements OnInit{
    blogs:BlogModel[] = []
    pageLimit:number = 6
    pageNumber:number = 1

    constructor(
        private blogsService:BlogsService,
        private readonly route: ActivatedRoute,
        private readonly titleService: Title
    ) {}

    ngOnInit(): void {
        this.titleService.setTitle(this.route.snapshot.data['title'])
        this.getBlogs(this.pageNumber)
    }

    getBlogs(pageNumber:number) {
        this.blogsService.getBlogs(pageNumber, this.pageLimit, true, true).subscribe(data=>{
            if( data.length==0 ) {
                this.pageNumber--
                return
            }
            this.blogs = data
        })
    }

    onPrev(pageNumber:number) {
        if(pageNumber==1) return
        this.pageNumber = pageNumber-1
        this.getBlogs(this.pageNumber)
    }
    
    onNext(pageNumber:number) {
        this.pageNumber = pageNumber+1
        this.getBlogs(this.pageNumber)
    }

}