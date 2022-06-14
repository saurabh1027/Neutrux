import { Component, ElementRef, EventEmitter, Input, OnInit, Output, QueryList, ViewChild, ViewChildren } from "@angular/core";
import { BlogElementModel } from "../../../blog.element.model";
import { BlogProjectModel } from "../../blog_project.model";
import { BlogEditorService } from "../blog-editor.service";

@Component({
    selector: 'app-blog-edit-elements',
    templateUrl: 'blog-edit-elements.component.html',
    styleUrls: ['blog-edit-elements.component.sass']
})
export class BlogEditElementsComponent implements OnInit {
    currentProject!:BlogProjectModel
    @ViewChildren('element') draggableElements!:QueryList<ElementRef>
    @ViewChild('editElementsContainer') editElementsContainer!:ElementRef
    @ViewChild('output') outputContainer!:ElementRef

    @Input('selectedElementPosition') selectedElementPosition!:number
    @Output('changeElementPosition') changeElementPosition = new EventEmitter<number>()

    constructor(
        private blogEditorService:BlogEditorService
    ) {}

    ngOnInit(): void {
        this.loadCurrentProject()
        setTimeout(() => {
            this.setTemplateElements()
            this.addEventToContainer()
        }, 1000);
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

    setTemplateElements() {
        this.draggableElements.forEach(item =>{
            let draggableElement = item.nativeElement as HTMLElement

            draggableElement.addEventListener('dragstart', ()=>{
                draggableElement.classList.add('dragging')
            })
            draggableElement.addEventListener('dragend', ()=>{
                draggableElement.classList.remove('dragging')
            })

        })
    }

    selectElement( position:number ) {
        if( this.selectedElementPosition == position ) {
            this.changeElementPosition.emit( 0 )
        } else {
            this.changeElementPosition.emit( position )
        }
    }

    addEventToContainer() {
        let container = this.editElementsContainer.nativeElement as HTMLElement
        
        container.addEventListener('dragover', (event:DragEvent)=>{
            event.preventDefault()

            const afterElement = this.getDragAfterElement(container, event.clientY)
            const draggableElement = document.getElementsByClassName('dragging')[0]
            if(!afterElement)
                container.appendChild(draggableElement)
            else {
                container.insertBefore(draggableElement, afterElement)
            }
            
            this.updateElements()
        })
    }

    updateElements() {
        let oldElements = this.currentProject.elements
        let container = this.editElementsContainer.nativeElement
        let newElements:BlogElementModel[] = []

        let childNodes:NodeList = container.childNodes

        childNodes.forEach( (childNode) => {
            let child = childNode as HTMLElement
            if(child.classList && child.classList.contains('element')) {
                let childPosition = child.attributes.getNamedItem('data-position')?.value
                for( let i=0; i<oldElements.length; i++) {
                    if( oldElements[i].position+'' == childPosition ) {
                        newElements.push( oldElements[i] )
                    }   
                }
            }
        })

        this.currentProject.elements = newElements
        this.blogEditorService.currentProject.next( this.currentProject )
    }

    getDragAfterElement(container:any, y:number) {
        let elements = [...container.querySelectorAll('.element:not(.dragging)')]
        return elements.reduce( (closest, child)=>{
            const box = child.getBoundingClientRect()
            const offset = y - box.top - box.height/2
            if( offset<0 && offset>closest.offset ) {
                return { offset:offset, element:child }
            } else {
                return closest
            }
        }, { offset: Number.NEGATIVE_INFINITY }).element
    }

}