import { Component, OnDestroy, OnInit } from "@angular/core";
import { ActivatedRoute, Router, UrlSegment } from "@angular/router";
import { Observable, Subscription } from "rxjs";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogElementModel } from "../../blog.element.model";
import { BlogUserModel } from "../../blog_user.model";
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
    user!:User
    elements:BlogElementModel[] = []
    changesMade:boolean = false
    projects:BlogProjectModel[] = []
    errors:string[] = []
    // direction!:string |undefined
    // newElement!:BlogElementModel|undefined
    
    isViewThumbnailActive:boolean = false
    isAddElementActive:boolean = false
    isDescTextInputActive:boolean = false
    isFileUploadActive:boolean = false
    isBlog = false
    isLoadingActive = true
    isValidBlog = false
    isErrorsBlockActive = false
    
    // Subscriptions
    currentProjectSub!:Subscription
    selectedElementPositionSub!:Subscription
    elementsSub!:Subscription
    changesMadeSub!:Subscription
    projectsSub!:Subscription
    userSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService,
        private readonly route:ActivatedRoute,
        private router:Router,
        private authService:AuthService
    ) {}

    // Component life-cycle methods
    ngOnInit(): void {
        let bool = false
        this.route.url.subscribe((urls:UrlSegment[])=>{
            let id = this.route.snapshot.params['id']
            this.subscribeUser()
            this.subscribeElements()
            this.subscribeCurrentProject()
            this.subscribeSelectedElementPosition()
            this.subscribeChangesVariables()
            // this.addArrowKeyEvents()
            if(urls[1].path == 'projects') {
                this.subscribeProjects()
            } else if(urls[1].path=='blogs') {
                this.isBlog = true
                this.subscribeBlogs()
            }
            bool = this.loadCurrentProject(id)
            if(!bool) {
                alert("Couldn't find the project!")
                this.router.navigate(['','blogs','editor','dashboard'])
            }
            this.isLoadingActive = false
            this.validateErrors()
        })
    }
        
    ngOnDestroy(): void {
        this.selectedElementPositionSub.unsubscribe()
        this.changesMadeSub.unsubscribe()
        this.elementsSub.unsubscribe()
        this.currentProjectSub.unsubscribe()
        this.projectsSub.unsubscribe()
        this.userSub.unsubscribe()
        this.blogEditorService.changesMade.next(false)
        this.blogEditorService.currentProject.next(null)
        this.blogEditorService.elements.next([])
        this.blogEditorService.elementActionEmitter.next("")
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if( this.changesMade ) {
            let bool:boolean
            bool = confirm('Do you want to discard the changes?')
            if( bool ) {
                this.blogEditorService.changesMade.next(false)
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

    validateErrors(){
        this.isValidBlog = false
        this.errors = []
        if( !this.currentProject )
            return
        
        // Title
        if(!this.currentProject.title) {
            this.errors.push( "Title of blog cannot be empty" )
        } else if( this.currentProject.title.length<51 || this.currentProject.title.length>140 ) {
            this.errors.push( "Title should be greater than 50 and less than 140 characters!" )
        }

        // Category
        if(!this.currentProject.category.categoryId) {
            this.errors.push( "Please select category for blog" )
        }

        // Description
        if(!this.currentProject.description) {
            this.errors.push( "Description of blog cannot be empty" )
        } else if( this.currentProject.description.length<101 || this.currentProject.description.length>200 ) {
            this.errors.push( "Description should be greater than 100 and less than 200 characters!" )
        }

        // Thumbnail
        if(!this.currentProject.thumbnail)
            this.errors.push( "Please select a thumbnail" )

        // Elements count
        if( this.currentProject.elements.length<3 )
            this.errors.push( "Blog should have at least 3 elements" )

        if(this.errors.length==0) 
            this.isValidBlog = true
    }

    


    // variable subscription methods
    subscribeUser() {
        this.userSub = this.authService.user.subscribe( user=>{
            if(user) {
                this.user = user
            }
        } ) 
    }

    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
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

    subscribeBlogs() {
        this.projectsSub = this.blogEditorService.blogs.subscribe( blogs=>{
            this.projects = blogs
        } )
    }
    
    subscribeSelectedElementPosition() {
        this.selectedElementPositionSub = this.blogEditorService.selectedElementPosition.subscribe( selectedElementPosition=>{
            this.selectedElementPosition = selectedElementPosition
        } )
    }

    // addArrowKeyEvents() {
    //     document.addEventListener('keydown', (event:KeyboardEvent)=>{
    //         switch( event.key ) {
                // don't use save event it causes problems while saving projects
                // case 's':
                //     if( event.ctrlKey ){
                //         event.stopImmediatePropagation()
                //         event.preventDefault()
                //         this.saveCurrentProject()
                //     }
                // break;
    //         }
    //     })
    // }





    // Blog related action events
    
    saveCurrentProject() {
        if(!this.currentProject){
            alert('null current-project')
            return
        }
        let bool = confirm("Do you want to save the changes?")
        if(!bool) return
        this.currentProject.user = new BlogUserModel(
            +this.user.userId,
            this.user.firstname,
            this.user.lastname,
            this.user.email,
            ""
        )
        for( let i=0;i<this.projects.length;i++ ) {
            if( this.currentProject && this.projects[i].projectId==this.currentProject.projectId ) {
                this.projects.splice(i,1)
                this.projects.splice(i,0,this.currentProject)
                break;
            }
        }
        this.blogEditorService.storeProjects(this.projects)
        this.blogEditorService.changesMade.next(false)
        this.blogEditorService.currentProject.next(null)
        this.blogEditorService.elements.next([])
        this.blogEditorService.selectedElementPosition.next(0)
        this.router.navigate(['','blogs','editor','dashboard'])
    }

    discardCurrentProject() {
        let bool = true
        if( this.changesMade ) {
            bool = confirm("Do you want to discard the changes?")
        }
        if(!bool) return
        this.blogEditorService.changesMade.next(false)
        this.blogEditorService.currentProject.next(null)
        this.router.navigate(['','blogs','editor','dashboard'])
    }

    publishCurrentProject() {
        let bool = confirm("Are you sure to publish the blog?")
        if(bool && this.currentProject) {
            this.blogEditorService.publishBlog( this.currentProject ).subscribe( (blogModel:ServerBlogModel)=>{
                if(this.currentProject && this.currentProject.elements ){
                    let elements:BlogElementModel[] = this.currentProject.elements
                    for( let i=0; i<elements.length; i++ ) {
                        this.blogEditorService.postBlogElement( elements[i], blogModel.blogId, this.user.userId ).subscribe((data)=>{
                            
                        },error=>{
                            let invalidFields:{name:string,message:string}[] = error.error.invalidFields
                            if( invalidFields && invalidFields.length>0 ) {
                                alert( invalidFields[0].message )
                            }
                        })
                    }
                    // provide link for blog in here 
                    alert("Blog added successfully!")
                    for( let i=0;i<this.projects.length;i++ ) {
                        if( this.currentProject && this.projects[i].projectId==this.currentProject.projectId ) {
                            this.projects.splice(i,1)
                            break;
                        }
                    }
                    this.blogEditorService.storeProjects(this.projects)
                    this.blogEditorService.changesMade.next(false)
                    this.blogEditorService.currentProject.next(null)
                    this.blogEditorService.elements.next([])
                    this.router.navigate(['','blogs','editor','dashboard'])
                }
            }, error=>{
                let invalidFields:{name:string,message:string}[] = error.error.invalidFields
                if( invalidFields && invalidFields.length>0 ) {
                    alert( invalidFields[0].message )
                }
            } )
        }
    }

    delete() {
        let bool = confirm("Are you sure?")
        if( this.currentProject && bool ) {
            if( !this.isBlog ) {
                for(let i=0;i<this.projects.length;i++) {
                    if(this.projects[i].projectId==this.currentProject.projectId) {
                        this.projects.splice(i,1)
                        this.blogEditorService.storeProjects(this.projects)
                        this.blogEditorService.changesMade.next(false)
                        this.blogEditorService.currentProject.next(null)
                        this.blogEditorService.elements.next([])
                        this.router.navigate(['','blogs','editor','dashboard'])
                        break;
                    }
                }
            } else {
                this.blogEditorService.deleteBlog( this.currentProject.projectId+"", this.user.userId ).subscribe((data:ResponseModel)=>{
                    if(data.status==200) {
                        alert("Blog has been deleted!")
                        this.blogEditorService.changesMade.next(false)
                        this.blogEditorService.currentProject.next(null)
                        this.blogEditorService.elements.next([])
                        this.router.navigate(['','blogs','editor','dashboard'])
                    }
                }, error=>{
                    alert('Failed to delete the blog')
                })
            }
        }
    }

    updateBlog() {
        this.isLoadingActive = true
        if(this.currentProject) {
            this.blogEditorService.updateBlog( this.currentProject ).subscribe( (updateBlogResponseModel:UpdateBlogResponseModel)=>{
                if( this.currentProject && updateBlogResponseModel ){
                    alert("Blog updated successfully!")
                    for( let i=0;i<this.projects.length;i++ ) {
                        if( this.currentProject && this.projects[i].projectId==this.currentProject.projectId ) {
                            this.projects.splice(i,1)
                            break;
                        }
                    }
                    this.blogEditorService.changesMade.next(false)
                    this.blogEditorService.currentProject.next(null)
                    this.blogEditorService.elements.next([])
                    this.router.navigate(['','blogs','editor','dashboard'])
                }
            }, error=>{
                if(error.error && error.error.status==400 && error.error.message) {
                    alert(error.error.message)
                }
            } )

        }
        this.isLoadingActive = false
    }



    // Blog elements related action events
    
    addElement(newElement:BlogElementModel) {
        if( !this.currentProject ) return
        newElement.position = this.elements.length+1
        this.elements.push( newElement )
        this.blogEditorService.elements.next( this.elements )
        this.blogEditorService.selectedElementPosition.next( newElement.position )
        this.blogEditorService.changesMade.next(true)
    }

}


class ResponseModel{
    constructor(
        public date:Date,
        public status:number,
        public type:Object,
        public message:string
    ) {}
}
class UpdateBlogResponseModel {
    constructor(
        
    ) {}
}
class ServerBlogModel{
    constructor( public blogId:string ) {}
}
class ServerBlogElementModel{
    constructor(
        name:string,
        description:string,
        value:string,
        position:number
    ) {}
}