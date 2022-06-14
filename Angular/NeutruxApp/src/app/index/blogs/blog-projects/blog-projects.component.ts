import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogEditorService } from "./blog-editor/blog-editor.service";
import { BlogUserModel } from "../blog_user.model";
import { CategoryModel } from "../category.model";
import { BlogProjectModel } from "./blog_project.model";
import { BlogElementModel } from "../blog.element.model";

@Component({
    selector: 'app-blog-projects',
    templateUrl: 'blog-projects.component.html',
    styleUrls: ['blog-projects.component.sass']
})
export class BlogProjectsComponent implements OnInit {
    user!:User
    projects:BlogProjectModel[] = []
    currentProject!:BlogProjectModel

    constructor(
        private blogEditorService:BlogEditorService,
        private authService:AuthService,
    ) {}

    ngOnInit(): void {
        this.loadCurrentProject()
        this.blogEditorService.loadProjects()
        this.loadProjects()

        this.authService.user.subscribe(user=>{
            if(user) this.user = user
        })

        this.openNewProject()
    }

    loadCurrentProject() {
        this.blogEditorService.currentProject.subscribe( currentProject=>{
            if(currentProject){
                this.currentProject = currentProject
            } else {
                setTimeout(() => {
                    this.loadCurrentProject()
                }, 500);
            }
        } )
    }
    
    loadProjects() {
        this.blogEditorService.projects.subscribe( projects => {
            if(projects) 
                this.projects = projects
            else 
                setTimeout(() => {
                    this.loadProjects()
                }, 500);
        } )
    }

    openNewProject() {

        let newProject:BlogProjectModel = new BlogProjectModel(
            ( this.projects.length + 1 )+'',
            'Hello this is the title I have wrote', 
            '', new Date(), 
            'https://neilpatel.com/wp-content/uploads/2017/08/blog.jpg',
            new CategoryModel( '', '', '' ),
            new BlogUserModel(0, '', '', '', ''),
            [ 
                new BlogElementModel(
                    '', 'heading', '', 'Hello this is the 1st heading I have wrote',
                    1, ''
                ),
                new BlogElementModel(
                    '', 'paragraph', '', 'Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote ',
                    2, ''
                ),
                new BlogElementModel(
                    '', 'heading', '', 'Hello this is the 4th heading I have wrote',
                    4, ''
                ),
                new BlogElementModel(
                    '', 'paragraph', '', 'Hello this is the 6th heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote ',
                    6, ''
                ),
                new BlogElementModel(
                    '', 'heading', '', 'Hello this is the 5th heading I have wrote',
                    5, ''
                ),
                new BlogElementModel(
                    '', 'paragraph', '', 'Hello this is the 3rd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote Hello this is the 2nd heading I have wrote ',
                    3, ''
                ),
                new BlogElementModel(
                    '', 'heading', '', 'Hello this is the 7th heading I have wrote',
                    7, ''
                ),
            ]
        )

        this.blogEditorService.currentProject.next( newProject )
        this.projects.push(this.currentProject)
        this.blogEditorService.projects.next( this.projects )
    }

}