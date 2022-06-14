import { Component, OnInit } from "@angular/core";
import { BlogProjectModel } from "../../blog_project.model";
import { BlogEditorService } from "../blog-editor.service";

@Component({
    selector: 'app-blog-elements',
    templateUrl: 'blog-elements.component.html',
    styleUrls: ['blog-elements.component.sass']
})
export class BlogElementsComponent implements OnInit {
    currentProject!:BlogProjectModel

    constructor(
        private blogEditorService:BlogEditorService
    ) {}

    ngOnInit():void {
        this.loadCurrentProject()
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

}