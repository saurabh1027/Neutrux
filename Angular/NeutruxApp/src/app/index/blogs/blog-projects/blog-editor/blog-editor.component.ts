import { Component, ElementRef, Input, OnInit, QueryList, ViewChild, ViewChildren } from "@angular/core";
import { BlogElementModel } from "../../blog.element.model";
import { CategoryModel } from "../../category.model";
import { BlogProjectModel } from "../blog_project.model";
import { BlogEditorService } from "./blog-editor.service";

@Component({
    selector: 'app-blog-editor',
    templateUrl: 'blog-editor.component.html',
    styleUrls: ['blog-editor.component.sass']
})
export class BlogEditorComponent implements OnInit {
    // @Input('currentProject') currentProject!:BlogProjectModel
    currentProject!:BlogProjectModel

    // EDITOR
    @ViewChild('editor') editor!:ElementRef
    @ViewChild('backdrop') backdrop!:ElementRef
    @ViewChild('element_list') element_list!:ElementRef
    @ViewChild('category_list') category_list!:ElementRef
    @ViewChild('context_menu') context_menu!:ElementRef
    editMode:boolean = true
    
    // CATEGORY
    categories:CategoryModel[] = []
    categorySearchResults:CategoryModel[] = []
    searchCategoryName:string = ''
    selectedCategory:CategoryModel = new CategoryModel('','','')
    
    // BLOG ELEMENT
    blogElements:BlogElementModel[] = []
    selectedElementPosition:number = 0
    @ViewChildren('element') draggableElements!:QueryList<ElementRef>

    constructor(
        private blogEditorService:BlogEditorService
    ) {}

    ngOnInit(): void {
        this.loadCurrentProject()
        this.loadBlogElements()
        
        setTimeout(() => {
            console.log( this.editor.nativeElement.offsetParent )
            this.setContextMenu()
            this.addEventToDraggableElements()
        }, 500);
    }

    loadBlogElements() {
        this.blogEditorService.loadBlogElements().subscribe( ( blogElements:any )=>{
            this.blogElements = blogElements
        } )
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

    addEventToDraggableElements() {
        this.draggableElements.forEach( draggableElement => {
            let element = draggableElement.nativeElement as HTMLElement

            element.addEventListener( 'dragstart', ()=>{
                this.toggleList( false )
                element.classList.add('dragging')
            } )
            element.addEventListener( 'dragend', ()=>{
                // this.currentProject.elements.splice(1, 0, this.blogElements[0])
                this.inserNewElementInContainer()
                element.classList.remove('dragging')
            } )

        } )
    }

    inserNewElementInContainer() {
        
    }

    setContextMenu() {
        let context_menu = this.context_menu.nativeElement as HTMLInputElement
        this.editor.nativeElement.addEventListener( "contextmenu", (event:PointerEvent)=>{
            event.preventDefault()
            context_menu.classList.add('active')
            this.blogEditorService.positionContextMenu(
                context_menu, event, this.editor.nativeElement.offsetParent as HTMLInputElement)
            window.addEventListener('click', ()=>{
                context_menu.classList.remove('active')
            }, false)
        } )
        this.context_menu.nativeElement.addEventListener( "contextmenu", (event:PointerEvent)=>{
            event.preventDefault()
        } )
    }
    
    toggleList( bool:boolean, listName?:string ) {
        if( bool && listName ) {
            this.backdrop.nativeElement.classList.add("active")
            if( listName=='elements' ){
                this.element_list.nativeElement.classList.add("active")
                this.category_list.nativeElement.classList.remove("active")
            } else {
                this.loadCategories()
                this.category_list.nativeElement.classList.add("active")
                this.element_list.nativeElement.classList.remove("active")
            }
        } else {
            this.backdrop.nativeElement.classList.remove("active")
            this.element_list.nativeElement.classList.remove("active")
            this.category_list.nativeElement.classList.remove("active")
        }
    }

    loadCategories() {
        this.blogEditorService.loadCategories( 1, 100000 ).subscribe(data=>{
            this.categories = data
            this.categorySearchResults = this.categories
        })
    }

    filterCategoriesByName() {
        this.categorySearchResults = []
        this.categories.forEach(category => {
            if( category.name.includes( this.searchCategoryName ) )
                this.categorySearchResults.push(category)
        });
    }

    selectCategory(category:CategoryModel) {
        if( category.name==this.selectedCategory.name ){
            this.selectedCategory = new CategoryModel('','','')
        } else {
            this.selectedCategory = category
        }
    }

    changeProjectCategory() {
        this.currentProject.category = this.selectedCategory
        this.selectedCategory = new CategoryModel('', '', '')
        this.toggleList(false)
    }

    deleteElement( elementPosition:number ) {
        let elements:BlogElementModel[] = this.currentProject.elements
        for( let i=0; i<elements.length; i++ ) {
            if( elements[i].position == elementPosition ) {
                elements.splice( i,1 )
            }
        }
        this.currentProject.elements = elements
        this.blogEditorService.currentProject.next( this.currentProject )
        this.selectedElementPosition = 0
    }

}