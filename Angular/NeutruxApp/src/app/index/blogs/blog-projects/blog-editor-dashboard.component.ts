import { Component, OnDestroy, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { SharedService } from "src/app/shared/shared.service";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogElementModel } from "../blog.element.model";
import { BlogUserModel } from "../blog_user.model";
import { CategoryModel } from "../category.model";
import { BlogEditorService } from "./blog-editor/blog-editor.service";
import { BlogProjectModel } from "./blog_project.model";

@Component({
    selector: 'app-blog-editor-dashboard',
    templateUrl: 'blog-editor-dashboard.component.html',
    styleUrls: ['blog-editor-dashboard.component.sass']
})
export class BlogEditorDashboardComponent implements OnInit, OnDestroy {

    projects:BlogProjectModel[] = []
    blogs:BlogProjectModel[] = []
    user!:User
    isBlogsViewEnabled:boolean = false
    isMenuActive:boolean = false
    isLoadingActive:boolean = true

    projectsSub!:Subscription
    userSub!:Subscription
    isBlogsViewEnabledSub!:Subscription
    blogsSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService,
        public sharedService:SharedService,
        private router:Router,
        private authService:AuthService
    ) {}

    ngOnInit(): void {
        this.subscribeUser()
        this.subscribeProjects()
        this.subscribeIsBlogsViewEnabled()
        this.subscribeBlogs()
        if( this.isBlogsViewEnabled ) {
            this.onClick(true)
            setInterval(()=>{
                this.onClick(true)
            }, (1000*60*5))
        } else {
            this.onClick(false)
            setInterval(()=>{
                this.onClick(false)
            }, (1000*60*5))
        }
    }
    
    ngOnDestroy(): void {
        this.projectsSub.unsubscribe()
        this.userSub.unsubscribe()
        this.isBlogsViewEnabledSub.unsubscribe()
        this.blogsSub.unsubscribe()
        this.blogEditorService.isBlogsViewEnabled.next(false)
    }

    subscribeBlogs() {
        this.blogsSub = this.blogEditorService.blogs.subscribe( blogs=>{
            if(blogs) {
                this.blogs = blogs
            }
        } )
    }

    subscribeUser() {
        this.userSub = this.authService.user.subscribe( user=>{
            if(user) {
                this.user = user
            }
        } ) 
    }

    subscribeIsBlogsViewEnabled(){
        this.isBlogsViewEnabledSub = this.blogEditorService.isBlogsViewEnabled.subscribe( isBlogsViewEnabled=>{
            if(isBlogsViewEnabled) 
                this.isBlogsViewEnabled = isBlogsViewEnabled
        } )
    }

    subscribeProjects() {
        this.projectsSub = this.blogEditorService.projects.subscribe( projects => {
            for( let i=0; i<projects.length; i++ ) {
                for( let j=0; j<projects.length-i-1; j++ ) {
                    if( projects[j].creationDate > projects[j+1].creationDate ) {
                        let temp = projects[j]
                        projects[j] = projects[j+1]
                        projects[j+1] = temp
                    }
                }
            }
            this.projects = projects
        } )
    }

    openNewProject( newProjectId:string ) {
        let newProject:BlogProjectModel = new BlogProjectModel(
            newProjectId,
            'Enter title of your new blog here...', 
            'Enter description of your blog here...', 
            new Date(), 
            'https://neilpatel.com/wp-content/uploads/2017/08/blog.jpg',
            new CategoryModel( '', '', '' ),
            new BlogUserModel(0, '', '', '', ''),
            [
                new BlogElementModel('','heading','','Enter your first heading',1,''),
                new BlogElementModel('','paragraph','','Enter your first paragraph',2,''),
                new BlogElementModel('','heading','','Enter your second heading',3,'')
            ]
        )
        this.projects.push( newProject )
        this.blogEditorService.projects.next( this.projects )
        this.router.navigate(['','blogs','editor','projects',newProjectId])
    }

    onClick(bool:boolean) {
        this.isLoadingActive = true
        this.blogEditorService.isBlogsViewEnabled.next(bool)
        if(bool) {
            this.blogEditorService.loadPublishedBlogs(this.user.userId).subscribe((serverBlogs:ServerBlogProjectModel[])=>{
                let publishedBlogs:BlogProjectModel[] = []
                console.log(serverBlogs)
                for(let i=0;i<serverBlogs.length;i++) {
                    //sorting elements
                    serverBlogs[i].elements.sort( this.compare )
                    let publishedBlog = new BlogProjectModel(
                        serverBlogs[i].blogId,
                        serverBlogs[i].title,
                        serverBlogs[i].description,
                        serverBlogs[i].creationDate,
                        serverBlogs[i].thumbnail,
                        serverBlogs[i].category,
                        new BlogUserModel( +this.user.userId, this.user.firstname, this.user.lastname, this.user.email, '' ),
                        serverBlogs[i].elements
                    );
                    publishedBlogs.push( publishedBlog )
                }
                this.blogEditorService.blogs.next(publishedBlogs)
                this.isLoadingActive = false
            })
        } else {
            this.blogEditorService.loadProjects()
            this.isBlogsViewEnabled = false;
            this.isLoadingActive = false
        }
        this.isMenuActive=false
    }

    compare( a:BlogElementModel, b:BlogElementModel ) {
        if ( a.position < b.position ){
            return -1;
        }
        if ( a.position > b.position ){
            return 1;
        }
        return 0;
    }

    openBlog( projectId:string ){
        this.router.navigate(['','blogs','editor','blogs',projectId])
    }
    
    openProject( projectId:string ){
        this.router.navigate(['','blogs','editor','projects',projectId])
    }

}

class ServerBlogProjectModel{
    constructor(
        public blogId:string,
        public category:CategoryModel,
        public creationDate:Date,
        public description:string,
        public elements:BlogElementModel[],
        public thumbnail:string,
        public title:string
    ) {}
}