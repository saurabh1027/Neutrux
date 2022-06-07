import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { BlogModel } from "../blog.model";
import { BlogsService } from "../blogs.service";

@Component({
    selector: 'app-category',
    templateUrl: 'category.component.html'
})
export class CategoryComponent implements OnInit {
    blogs:BlogModel[] = []
    pageNumber:number = 1
    pageLimit:number = 6
    categoryId:string = ''
    alert:{
        'message': string,
        'isError': boolean
    } | null = null 

    constructor(
        private blogsService:BlogsService,
        private router:Router,
        private readonly route: ActivatedRoute,
    ) {}

    ngOnInit(): void {
        let id = this.route.snapshot.paramMap.get('id')
        if(id) this.categoryId = id 
        this.getBlogsByCategory(this.pageNumber)
    }

    getBlogsByCategory(pageNumber:number) {
        this.blogsService.getBlogsByCategory( this.categoryId, pageNumber, this.pageLimit, true, true)
            .subscribe(data=>{
            if( data.length==0 ) {
                this.pageNumber--
                return
            }
            this.blogs = data
        },error=>{
            if(error.status==400){
                this.alert = {
                    message: '404 - Not Found!',
                    isError: true
                }
            }
        })
    }

    onPrev(pageNumber:number) {
        if(pageNumber==1) return
        this.pageNumber = pageNumber-1
        this.getBlogsByCategory(this.pageNumber)
    }
    
    onNext(pageNumber:number) {
        this.pageNumber = pageNumber+1
        this.getBlogsByCategory(this.pageNumber)
    }

    onClose(){
        if( this.alert?.isError )
            this.router.navigate( ['blogs'] )
        this.alert = null
    }

}