import { Component, OnDestroy, OnInit } from "@angular/core";
import { Observable, Subscription } from "rxjs";
import { BlogElementModel } from "../../../blog.element.model";
import { BlogEditorService } from "../blog-editor.service";
import { CanComponentDeactivate } from "../can-deactivate-guard.service";

@Component({
    selector: 'app-blog-elements',
    templateUrl: 'blog-elements.component.html',
    styleUrls: ['blog-elements.component.sass']
})
export class BlogElementsComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    elements:BlogElementModel[] = []
    selectedElementPosition:number = 0
    editing:boolean = false

    // text input component
    previousValue:string = ''
    isTextInputActive:boolean = false

    isFileUploadActive:boolean = false

    changesMade:boolean = false
    changesSaved:boolean = true 

    elementsSub!:Subscription
    selectedElementPositionSub!:Subscription
    changesMadeSub!:Subscription
    changesSavedSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService
    ) {}

    ngOnInit(): void {
        this.subscribeElements()
        this.subscribeSelectedElementPosition()
        this.subscribeChangesVariables()
        this.addArrowKeyEvents()
    }

    ngOnDestroy(): void {
        this.elementsSub.unsubscribe()
        this.selectedElementPositionSub.unsubscribe()
        this.blogEditorService.selectedElementPosition.next(0)
        this.changesMadeSub.unsubscribe()
        this.changesSavedSub.unsubscribe()
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

    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
        } )
        this.changesSavedSub = this.blogEditorService.changesSaved.subscribe( (changesSaved:boolean)=>{
            this.changesSaved = changesSaved
        } )
    }

    subscribeSelectedElementPosition() {
        this.selectedElementPositionSub = this.blogEditorService.selectedElementPosition.subscribe( selectedElementPosition=>{
            this.selectedElementPosition = selectedElementPosition
        } )
    }

    subscribeElements() {
        this.elementsSub = this.blogEditorService.elements.subscribe( (elements:BlogElementModel[])=>{
            this.elements = elements
        } )
    }

    selectElement( position:number, event?:PointerEvent|MouseEvent ) {
        if( this.selectedElementPosition == position ) {
            this.blogEditorService.selectedElementPosition.next(0)
        } else {
            this.blogEditorService.selectedElementPosition.next(position)
        }
    }

    edit() {
        let textElements:string[] = ['heading','paragraph']
        // let textElements:string[] = ['heading','paragraph']
        for( let i=0; i<this.elements.length; i++ ) {
            if( this.selectedElementPosition == this.elements[i].position ) {
                if( textElements.includes( this.elements[i].name ) ) {
                    this.previousValue = this.elements[i].value
                    this.isTextInputActive = true
                } else if( this.elements[i].name == 'image' ) {
                    this.isFileUploadActive = true
                }
                return
            }
        }
    }

    onUpdate( value:string ) {
        setTimeout(() => {
            for( let i=0; i<this.elements.length; i++ ) {
                if( this.selectedElementPosition == this.elements[i].position ) {
                    this.elements[i].value = value
                    this.blogEditorService.elements.next( this.elements )
                    this.editing = false
                    this.blogEditorService.changesMade.next(true)
                    this.blogEditorService.changesSaved.next(false)
                    this.addArrowKeyEvents()
                    return
                }
            }
        }, 500);
    }

    addArrowKeyEvents() {
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if(this.elements.length==0) return
            switch(event.key){
                case 'ArrowDown':
                    if( this.selectedElementPosition>0 && this.selectedElementPosition!=this.elements.length ) {
                        if( event.shiftKey ) this.moveElement('down')
                        this.blogEditorService.selectedElementPosition.next( this.selectedElementPosition+1 )
                        if( !event.shiftKey ) this.focusSelectedElement()
                        event.preventDefault()
                    } 
                break;
                case 'ArrowUp':
                    if( this.selectedElementPosition==0 ) {
                        this.blogEditorService.selectedElementPosition.next(1)
                        this.focusSelectedElement()
                        event.preventDefault()
                    }
                    if( this.selectedElementPosition>0 && this.selectedElementPosition!=1 ){
                        if( event.shiftKey ) this.moveElement('up')
                        this.blogEditorService.selectedElementPosition.next( this.selectedElementPosition-1 )
                        if( !event.shiftKey ) this.focusSelectedElement()
                        event.preventDefault()
                    }
                break;
                case 'Delete':
                    if( this.selectedElementPosition>0 ){
                        this.delete()
                        event.preventDefault()
                    }    
                break;
                case 'e':
                    if( this.selectedElementPosition>0 && !this.editing ){
                        this.edit()
                        this.editing = true
                        event.preventDefault()
                    }
                break;
            }
        })
    }

    delete() {
        for( let i=0;i<this.elements.length;i++ ) {
            if( this.elements[i].position == this.selectedElementPosition ) {
                this.elements.splice(i, 1)
                this.blogEditorService.selectedElementPosition.next(0)
                break;
            }
        }
        for( let i=0;i<this.elements.length;i++ ) {
            this.elements[i].position = i+1
        }
        this.blogEditorService.elements.next( this.elements )
        this.blogEditorService.changesMade.next(true)
        this.blogEditorService.changesSaved.next(false)
    }

    focusSelectedElement(){
        setTimeout(() => {
            let element:HTMLInputElement = document.querySelectorAll('.element.selected')[0] as HTMLInputElement
            window.scrollTo({
                top: element.offsetTop-(window.innerHeight/3),
                left: 0,
                behavior: 'smooth'
            })
        }, 300);
    }

    // not working yet
    moveElement( direction:string ) {
        for( let i=0; i<this.elements.length; i++ ) {
            if( this.elements[i].position == this.selectedElementPosition ) {
                if( direction=='up' ) {
                    this.elements.splice(i-1, 0, this.elements[i])
                    this.elements.splice(i+1, 1)
                } else if( direction=='down' ) {
                    this.elements.splice(i+2, 0, this.elements[i])
                    this.elements.splice(i, 1)
                }
                for( let j=1;j<=this.elements.length;j++ ) {
                    this.elements[j].position = j
                }
                this.selectedElementPosition = (direction='up')? i : i+1
                this.blogEditorService.elements.next(this.elements)
                this.blogEditorService.selectedElementPosition.next(this.selectedElementPosition)
                break
            }
        }
    }

}