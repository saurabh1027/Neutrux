import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable, Subscription } from "rxjs";
import { BlogElementModel } from "../../blog.element.model";
import { BlogUserModel } from "../../blog_user.model";
import { CategoryModel } from "../../category.model";
import { BlogProjectModel } from "../blog_project.model";
import { BlogEditorService } from "./blog-editor.service";
import { CanComponentDeactivate } from "./can-deactivate-guard.service";

@Component({
    selector: 'app-blog-editor',
    templateUrl: 'blog-editor.component.html',
    styleUrls: ['blog-editor.component.sass']
})
export class BlogEditorComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    
    currentProject!:BlogProjectModel|null
    selectedElementPosition:number = 0
    // direction!:string |undefined
    // newElement!:BlogElementModel|undefined
    
    // Subscriptions
    currentProjectSub!:Subscription
    selectedElementPositionSub!:Subscription
    elementsSub!:Subscription
    changesMadeSub!:Subscription
    changesSavedSub!:Subscription
    projectsSub!:Subscription

    isAddElementActive:boolean = false
    elements:BlogElementModel[] = []
    changesMade:boolean = false
    changesSaved:boolean = true
    projects:BlogProjectModel[] = []

    constructor(
        public blogEditorService:BlogEditorService,
        private readonly route:ActivatedRoute,
        private router:Router
    ) {}

    // Component life-cycle methods
    ngOnInit(): void {
        let bool = false
        let projectId = this.route.snapshot.params['id']
        this.subscribeElements()
        this.subscribeCurrentProject()
        this.subscribeSelectedElementPosition()
        this.subscribeChangesVariables()
        this.subscribeProjects()
        this.addArrowKeyEvents()
        bool = this.loadCurrentProject(projectId)
        if(!bool) {
            this.router.navigate(['','blogs','projects'])
        }
    }
        
    ngOnDestroy(): void {
        this.selectedElementPositionSub.unsubscribe()
        this.changesMadeSub.unsubscribe()
        this.changesSavedSub.unsubscribe()
        this.elementsSub.unsubscribe()
        this.currentProjectSub.unsubscribe()
        this.projectsSub.unsubscribe()
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if( this.changesMade && !this.changesSaved ) {
            let bool:boolean
            bool = confirm('Do you want to discard the changes?')
            if( bool ) {
                this.blogEditorService.changesMade.next(false)
                this.blogEditorService.changesSaved.next(true)
            }
            return bool
        } else {
            return true
        }
    }

    loadCurrentProject( projectId:string ) {
        let bool = false
        for( let i=0;i<this.projects.length;i++ ) {
            if(this.projects[i].projectId==projectId) {
                this.blogEditorService.currentProject.next( this.projects[i] )
                bool = true
            }
        }
        return bool
    }
    
    // content loading methods
    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
        } )
        this.changesSavedSub = this.blogEditorService.changesSaved.subscribe( (changesSaved:boolean)=>{
            this.changesSaved = changesSaved
        } )
    }

    subscribeElements() {
        this.elementsSub = this.blogEditorService.elements.subscribe( (elements:BlogElementModel[])=>{
            this.elements = elements
        } )
    }

    subscribeCurrentProject() {
        this.currentProjectSub = this.blogEditorService.currentProject.subscribe( currentProject=>{
            if(currentProject) {
                this.currentProject = currentProject
                this.blogEditorService.elements.next( this.currentProject.elements )
            }
        } )
    }

    subscribeProjects() {
        this.projectsSub = this.blogEditorService.projects.subscribe( projects=>{
            this.projects = projects
        } )
    }
    
    subscribeSelectedElementPosition() {
        this.selectedElementPositionSub = this.blogEditorService.selectedElementPosition.subscribe( selectedElementPosition=>{
            this.selectedElementPosition = selectedElementPosition
        } )
    }

    addArrowKeyEvents() {
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            switch( event.key ) {
                // don't use save event it causes problems while saving projects
                // case 's':
                //     if( event.ctrlKey ){
                //         event.stopImmediatePropagation()
                //         event.preventDefault()
                //         this.saveCurrentProject()
                //     }
                // break;
            }
        })
    }

    // Action events
    addElement(newElement:BlogElementModel) {
        if( !this.currentProject ) return
        newElement.position = this.elements.length+1
        this.elements.push( newElement )
        this.blogEditorService.elements.next( this.elements )
        this.blogEditorService.selectedElementPosition.next( newElement.position )
        this.blogEditorService.changesMade.next(true)
        this.blogEditorService.changesSaved.next(false)
    }
    
    saveCurrentProject() {
        for( let i=0;i<this.projects.length;i++ ) {
            if( this.currentProject && this.projects[i].projectId==this.currentProject.projectId ) {
                this.projects.splice(i,1)
                this.projects.splice(i,0,this.currentProject)
                break;
            }
        }
        this.blogEditorService.storeProjects(this.projects)
        this.blogEditorService.changesSaved.next(true)
        this.blogEditorService.changesMade.next(false)
        this.blogEditorService.currentProject.next(null)
        this.blogEditorService.elements.next([])
        this.router.navigate(['','blogs','projects'])
    }

    discardCurrentProject() {
        this.blogEditorService.changesSaved.next(true)
        this.blogEditorService.changesMade.next(false)
        this.blogEditorService.currentProject.next(null)
        this.router.navigate(['','blogs','projects'])
    }

}