import { Component, OnDestroy, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { Subscription } from "rxjs";
import { SharedService } from "src/app/shared/shared.service";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogUserModel } from "../blog_user.model";
import { CategoryModel } from "../category.model";
import { BlogEditorService } from "./blog-editor/blog-editor.service";
import { BlogProjectModel } from "./blog_project.model";

@Component({
    selector: 'app-blog-projects',
    templateUrl: 'blog-projects.component.html',
    styleUrls: ['blog-projects.component.sass']
})
export class BlogProjectsComponent implements OnInit, OnDestroy {
    projects:BlogProjectModel[] = []
    projectsSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService,
        public sharedService:SharedService,
        private router:Router
    ) {}

    ngOnInit(): void {
        this.subscribeProjects()
        this.blogEditorService.loadProjects()
        // setInterval(()=>{
        //     this.blogEditorService.loadProjects()
        // }, 60000)
    }
    
    ngOnDestroy(): void {
        this.projectsSub.unsubscribe()
    }

    subscribeProjects() {
        this.projectsSub = this.blogEditorService.projects.subscribe( projects => {
            this.projects = projects
        } )
    }

    deleteProject( projectId:string ) {
        let bool = confirm("Are you sure?")
        if( bool ) {
            for(let i=0;i<this.projects.length;i++) {
                if(this.projects[i].projectId==projectId) {
                    this.projects.splice(i,1)
                    // this.blogEditorService.projects.next(this.projects)
                    this.blogEditorService.storeProjects(this.projects)
                    break;
                }
            }
        }
    }

    openNewProject( newProjectId:string ) {
        let newProject:BlogProjectModel = new BlogProjectModel(
            newProjectId,
             'Enter title of the blog here..'+newProjectId, 
            '', new Date(), 
            'https://neilpatel.com/wp-content/uploads/2017/08/blog.jpg',
            new CategoryModel( '', '', '' ),
            new BlogUserModel(0, '', '', '', ''),
            []
        )
        this.projects.push( newProject )
        this.blogEditorService.projects.next( this.projects )
        this.router.navigate(['','blogs','editor',newProjectId])
    }

}