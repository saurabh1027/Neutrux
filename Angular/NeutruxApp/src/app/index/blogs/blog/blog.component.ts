import { AfterViewInit, Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogImpressionModel } from "../blog.impression.model";
import { BlogModel } from "../blog.model";
import { BlogsService } from "../blogs.service";

@Component({
    selector: 'app-blog',
    templateUrl: 'blog.component.html',
    styleUrls: ['blog.component.sass']
})
export class BlogComponent implements OnInit, AfterViewInit {
    blog!:BlogModel
    alert:{
        'message': string,
        'isError': boolean
    } | null = null 
    user!:User
    bool:boolean = false
    impression!:BlogImpressionModel
    index:number = 0

    isLoadingActive:boolean = true
    isImpressionBlockActive:boolean = true

    constructor(
        private readonly route:ActivatedRoute,
        private blogsService:BlogsService,
        private router:Router,
        private authService:AuthService
    ) {}

    ngOnInit(): void {
        let blogId = this.route.snapshot.params['id']
        this.authService.user.subscribe(user=>{
            if(user){
                this.user = user
            }
        })
        this.getBlogById( blogId )
    }

    ngAfterViewInit(): void {
        this.isLoadingActive = false
    }

    getBlogById( blogId:number ) {
        this.blogsService.getBlogById( blogId ).subscribe(data=>{
            this.blog = data
            this.sortElements()
            setTimeout(() => {
                if(this.user) {
                    let impressions:BlogImpressionModel[] = this.blog.impressions
                    for( let i=0;i<impressions.length;i++ ){
                        if( impressions[i].blogId==this.blog.blogId && impressions[i].userId==this.user.userId ) {
                            this.impression = impressions[i]
                            this.index = i
                        }
                    }
                }
            }, 1000);
        }, error=>{
            if( error.status==400 ){
                this.alert = {
                    message: '404 - Not Found!',
                    isError: true
                }
            }
        })
    }

    sortElements() {
        this.blog.elements.sort( function (a,b) {
            return a.position-b.position
        } )
    }

    onClose(){
        if( this.alert?.isError )
            this.router.navigate( ['blogs'] )
        this.alert = null
    }

    refreshBlog(blogId:string) {
        this.getBlogById( +blogId )
    }

    toggleCommentSection() {
        this.bool = !this.bool
    }

    onImpression( type:string ){
        if(!this.user) {
            alert("please login")
            return
        }
        this.isImpressionBlockActive = false
        if( this.impression && this.impression.type==type ) {
            // remove impression
            this.blogsService.removeImpressionFromBlog( this.user.userId, this.blog.blogId )
                .subscribe(data=>{
                    let impressions = this.blog.impressions
                    for( let i=0;i<impressions.length;i++ ){
                        if( impressions[i].blogId==this.blog.blogId && impressions[i].userId==this.user.userId ) {
                            impressions.splice(i, 1)
                            this.blog.impressions = impressions
                        }
                    }
                    this.impression = new BlogImpressionModel('','','','')
                    this.isImpressionBlockActive = true
                })
        } else {
            // add or update impression
            this.blogsService.addOrUpdateImpressionToBlog( type, this.user.userId, this.blog.blogId )
                .subscribe(data=>{
                    let impressions = this.blog.impressions
                    for( let i=0;i<impressions.length;i++ ){
                        if( impressions[i].blogId==this.blog.blogId && impressions[i].userId==this.user.userId ) {
                            impressions.splice(i, 1)
                            this.blog.impressions = impressions
                        }
                    }
                    this.impression = data
                    this.blog.impressions.push( this.impression )
                    this.isImpressionBlockActive = true
                }
            )
        }
    }

}