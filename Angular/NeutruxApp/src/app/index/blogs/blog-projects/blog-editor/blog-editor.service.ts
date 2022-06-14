import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { AesCryptoService } from "src/app/aes-crypto.service";
import { BlogElementModel } from "../../blog.element.model";
import { CategoryModel } from "../../category.model";
import { BlogProjectModel } from "../blog_project.model";

@Injectable({
    providedIn: 'root'
})
export class BlogEditorService {

    projects = new BehaviorSubject< BlogProjectModel[] | null >(null)
    currentProject = new BehaviorSubject< BlogProjectModel|null >(null)

    constructor(
        private cryptoService:AesCryptoService,
        private http:HttpClient
    ) {}

    loadBlogElements() : Observable<BlogElementModel[]> {
        return this.http.get<BlogElementModel[]>('./assets/blog_elements.json')
    }

    loadProjects() {
        let encryptedProjectsStr = localStorage.getItem('blog_projects')
        if( !encryptedProjectsStr ){
            this.projects.next( [] )
            this.storeProjects()
            return
        }
        let projectsStr = this.cryptoService.decryptData( encryptedProjectsStr )
        this.projects.next( JSON.parse(projectsStr) )
    }

    storeProjects() {
        let projectsString:string = ''
        let encryptedProjectsString:string = ''
        this.projects.subscribe( projects => {
            projectsString = JSON.stringify( projects )
            encryptedProjectsString = this.cryptoService.encryptData( projectsString )
            localStorage.setItem( 'blog_projects', encryptedProjectsString )
        } )
    }

    uploadThumbnail( thumbnail:File ){
        // enter program for uploading thumbnail on the file server using backend
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

}