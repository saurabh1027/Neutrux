import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { BehaviorSubject, Observable } from "rxjs";
import { AesCryptoService } from "src/app/aes-crypto.service";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { environment } from "src/environments/environment.prod";
import { BlogElementModel } from "../../blog.element.model";
import { CategoryModel } from "../../category.model";
import { BlogProjectModel } from "../blog_project.model";

@Injectable({
    providedIn: 'root'
})
export class BlogEditorService {

    projects = new BehaviorSubject<BlogProjectModel[]>([])
    blogs = new BehaviorSubject<BlogProjectModel[]>([])
    publishedBlogs = new BehaviorSubject<BlogProjectModel[]>([])
    elements = new BehaviorSubject<BlogElementModel[]>([])
    currentProject = new BehaviorSubject<BlogProjectModel|null>(null)
    selectedElementPosition = new BehaviorSubject<number>(0)
    elementActionEmitter = new BehaviorSubject<string>("")
    changesMade = new BehaviorSubject<boolean>(false)
    isBlogsViewEnabled = new BehaviorSubject<boolean>(false)

    constructor(
        private cryptoService:AesCryptoService,
        private http:HttpClient,
        private router:Router,
        private authService:AuthService
    ) {}

    loadBlogElements() : Observable<BlogElementModel[]> {
        return this.http.get<BlogElementModel[]>('./assets/blog_elements.json')
    }

    loadProjects() {
        let encryptedProjectsStr = localStorage.getItem('blog_projects')
        if( !encryptedProjectsStr ){
            this.storeProjects([])
            return
        }
        let projectsStr = this.cryptoService.decryptData( encryptedProjectsStr )
        let projects:BlogProjectModel[] = JSON.parse(projectsStr)
        let user:User|null = this.authService.user.getValue()
        for( let i=0; i<projects.length; i++ ) {
            if( user && (user.userId != ""+projects[i].user.userId) ) {
                projects.splice(i, 1)
            }
        }
        this.projects.next( projects )
    }

    loadPublishedBlogs( userId:string ){
        return this.http.get<ServerBlogProjectModel[]>( "http://localhost:8010/users/"+userId+"/blogs" )
    }

    storeProjects( projects:BlogProjectModel[] ) {
        let projectsString:string = ''
        let encryptedProjectsString:string = ''
        projectsString = JSON.stringify( projects )
        encryptedProjectsString = this.cryptoService.encryptData( projectsString )
        localStorage.setItem( 'blog_projects', encryptedProjectsString )
        this.loadProjects()
    }

    uploadFile( fileToBeUploaded:File ){
        const formData:FormData = new FormData()
        formData.append( "image", fileToBeUploaded )
        return this.http.post( environment.backendServerUrl+'image-upload', formData, 
            { headers: new HttpHeaders().append('Content-Type', 'multipart/form-data') , responseType:'text' })
    }

    loadCategories( pageNumber:number, pageLimit:number ) :Observable<CategoryModel[]> {
        let params:HttpParams = new HttpParams()
        params = params.append('pageNumber',pageNumber)
        params = params.append('pageLimit',pageLimit)
        return this.http.get<CategoryModel[]>(
            'http://localhost:8010/neutrux-blogs-api/categories/',
            { params:params }
        )
    }

    positionContextMenu(context_menu:HTMLInputElement, event:PointerEvent, container:HTMLInputElement) {
        // IDK why but these two lines are needed
        // If removed then values are not being constant
        context_menu.style.top = ( event.pageY - container.offsetTop ) + 'px'
        context_menu.style.left = event.pageX + 'px'

        let is_bottom_exceeding:boolean = container.offsetHeight < ( context_menu.offsetTop + context_menu.offsetHeight )
        let is_right_exceeding:boolean = container.offsetWidth < ( context_menu.offsetLeft + context_menu.offsetWidth )

        if( is_bottom_exceeding && is_right_exceeding ) {
            // bottom & right exceeding
            context_menu.style.top = ( container.offsetHeight - context_menu.offsetHeight - 5 ) + 'px'
            context_menu.style.left = ( container.offsetWidth - context_menu.offsetWidth - 5 ) + 'px'
        } else if( is_bottom_exceeding ) {
            // bottom exceeding
            context_menu.style.top = ( container.offsetHeight - context_menu.offsetHeight - 5 ) + 'px'
            context_menu.style.left = event.pageX + 'px'
        } else if( is_right_exceeding ) {
            // right exceeding
            context_menu.style.top = ( event.pageY - container.offsetTop ) + 'px'
            context_menu.style.left = ( container.offsetWidth - context_menu.offsetWidth - 5 ) + 'px'
        } else {
            context_menu.style.top = ( event.pageY - container.offsetTop ) + 'px'
            context_menu.style.left = event.pageX + 'px'
        }
        
    }

    publishBlog( currentProject:BlogProjectModel ): Observable<ServerBlogModel> {
        let body:{
            title:String, description: String, userId:String, categoryId:String, thumbnail:String
        } = {
            title: currentProject.title,
            description: currentProject.description,
            userId: currentProject.user.userId+'',
            categoryId: currentProject.category.categoryId,
            thumbnail: currentProject.thumbnail
        }
        return this.http.post<ServerBlogModel>( 'http://localhost:8010/neutrux-blogs-api/blogs/', body )
    }

    postBlogElement( element:BlogElementModel, blogId:string, userId:string ) {
        let serverElement:ServerBlogElementModel
        serverElement = new ServerBlogElementModel(
            element.name, element.description, element.value, element.position
        )
        let params:HttpParams = new HttpParams();
        params = params.append('X-User-ID',userId)
        return this.http.post( "http://localhost:8010/neutrux-blogs-api/blogs/"+blogId+"/elements", serverElement, { params: params } )
    }

    deleteBlog( blogId:string,userId:string ) {
        let params:HttpParams = new HttpParams();
        params = params.append('X-User-ID',userId)
        return this.http.delete<ResponseModel>( 'http://localhost:8010/neutrux-blogs-api/blogs/'+blogId, {params:params} )
    }

    updateBlog( currentProject:BlogProjectModel ){
        let elements: BlogElementModel[] = currentProject.elements
        let body:{
            title:String, description: String, userId:String, categoryId:String, thumbnail:String, elements:BlogElementModel[]
        } = {
            title: currentProject.title,
            description: currentProject.description,
            userId: currentProject.user.userId+'',
            categoryId: currentProject.category.categoryId,
            thumbnail: currentProject.thumbnail,
            elements: elements
        }
        return this.http.put<ServerBlogModel>( 'http://localhost:8010/neutrux-blogs-api/blogs/'+currentProject.projectId, body )
    }

    deleteBlogElementsByBlogId( blogId:string, userId:string ) {
        let params:HttpParams = new HttpParams();
        params = params.append('X-User-ID',userId)
        return this.http.delete( 'http://localhost:8010/neutrux-blogs-api/blogs/'+blogId+'/elements/', {params:params} )
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
class ServerBlogModel{
    constructor( public blogId:string ) {}
}
class ServerBlogElementModel{
    constructor(
        public name:string,
        public description:string,
        public value:string,
        public position:number
    ) {}
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