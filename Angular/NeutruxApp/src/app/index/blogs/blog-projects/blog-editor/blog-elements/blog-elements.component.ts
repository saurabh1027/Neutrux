import { Component, OnDestroy, OnInit } from "@angular/core";
import { Observable, Subscription } from "rxjs";
import { BlogElementModel } from "../../../blog.element.model";
import { BlogProjectModel } from "../../blog_project.model";
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
    currentProject!:BlogProjectModel

    // text input component
    previousValue:string = ''
    isTextInputActive:boolean = false
    isFileUploadActive:boolean = false
    changesMade:boolean = false

    elementsSub!:Subscription
    selectedElementPositionSub!:Subscription
    changesMadeSub!:Subscription
    elementActionEmitterSub!:Subscription
    currentProjectSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService
    ) {}

    ngOnInit(): void {
        this.subscribeElements()
        this.subscribeSelectedElementPosition()
        this.subscribeChangesVariables()
        this.subscribeElementActionEmitter()
        this.addArrowKeyEvents()
    }

    ngOnDestroy(): void {
        this.elementsSub.unsubscribe()
        this.selectedElementPositionSub.unsubscribe()
        this.blogEditorService.selectedElementPosition.next(0)
        this.changesMadeSub.unsubscribe()
        this.elementActionEmitterSub.unsubscribe()
        if( this.currentProjectSub ) this.currentProjectSub.unsubscribe()
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

    // Subscription methods
    subscribeElementActionEmitter() {
        this.elementActionEmitterSub = this.blogEditorService.elementActionEmitter.subscribe((actionKey:string)=>{
            switch(actionKey) {
                case 'e':
                    this.edit()
                break;
                case 'd':
                    this.delete()
                break;
                case 'mu':
                    this.moveElement('up')
                break;
                case 'md':
                    this.moveElement('down')
                break;
            }
        })
    }

    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
        } )
    }

    subscribeSelectedElementPosition() {
        this.selectedElementPositionSub = this.blogEditorService.selectedElementPosition.subscribe( selectedElementPosition=>{
            this.selectedElementPosition = selectedElementPosition
        } )
    }

    subscribeElements() {
        this.elementsSub = this.blogEditorService.elements.subscribe( (elements:BlogElementModel[])=>{
            for( let i=0; i<elements.length; i++ ) {
                for( let j=0; j<elements.length-i-1; j++ ) {
                    if( elements[j].position > elements[j+1].position ) {
                        let temp = elements[j]
                        elements[j] = elements[j+1]
                        elements[j+1] = temp
                    }
                }
            }
            this.elements = elements
        } )
    }

    subscribeCurrentProject() {
        this.currentProjectSub = this.blogEditorService.currentProject.subscribe(currentProject=>{
            if( currentProject ) this.currentProject = currentProject
        })
    }
    // Subscription methods - end

    // action methods
    selectElement( position:number ) {
        if( this.selectedElementPosition == position ) {
            this.blogEditorService.selectedElementPosition.next(0)
        } else {
            this.blogEditorService.selectedElementPosition.next(position)
        }
    }

    edit() {
        let textElements:string[] = ['heading','paragraph']
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
                    // this.addArrowKeyEvents()
                    return
                }
            }
        }, 500);
    }

    delete() {
        let bool = confirm('Are you sure?')
        if(bool){
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
        }
    }

    // not working yet
    moveElement( direction:string ) {
        let newElements = []
        let element!:BlogElementModel;
        let index=0;
        if( this.selectedElementPosition==0 ) {
            alert("Selct an element first!")
            return
        }
        for( let i=0; i<this.elements.length; i++ ) {
            if( this.elements[i].position != this.selectedElementPosition ) {
                newElements.push(this.elements[i])
            } else {
                element = this.elements[i]
                index = i;
            }
        }
        if(direction=='up') {
            if(this.selectedElementPosition==1) {
                newElements.splice(0,0,element)
            } else {
                newElements.splice( index-1,0,element )
                this.selectedElementPosition--
            }
        } else {
            if(this.selectedElementPosition==this.elements.length) {
                newElements.push(element)
            } else {
                newElements.splice( index+1,0,element )
                this.selectedElementPosition++
            }
        }
        for( let i=0; i<newElements.length; i++ ) {
            newElements[i].position = i+1
        }
        this.blogEditorService.elements.next( newElements )
        this.blogEditorService.selectedElementPosition.next( this.selectedElementPosition )
        if( this.currentProject ) {
            this.currentProject.elements = newElements
            this.blogEditorService.currentProject.next(this.currentProject)
        }
        this.focusSelectedElement()
        this.blogEditorService.changesMade.next( true )
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

    addArrowKeyEvents() {
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if(this.elements.length==0) return
            switch(event.key){
                case 'ArrowDown':
                    if( event.shiftKey ) this.moveElement('down')
                    if( !event.shiftKey ) {
                        let pos = this.selectedElementPosition
                        if( pos == this.elements.length ) {
                            this.blogEditorService.selectedElementPosition.next( 1 )
                        } else {
                            this.blogEditorService.selectedElementPosition.next( pos+1 )
                        }
                        this.focusSelectedElement()
                    }
                    event.preventDefault()
                break;
                case 'ArrowUp':
                    if( event.shiftKey ) this.moveElement('up')
                    if( !event.shiftKey ) {
                        let pos = this.selectedElementPosition
                        if( pos==0 ) {
                            this.blogEditorService.selectedElementPosition.next(this.elements.length)
                        } else if( pos == 1 ) {
                            this.blogEditorService.selectedElementPosition.next( this.elements.length )
                        } else {
                            this.blogEditorService.selectedElementPosition.next( pos-1 )
                        }
                        this.focusSelectedElement()
                    }
                    event.preventDefault()
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
                // case ' ':
                //     if(this.selectedElementPosition!=0) {
                //         this.blogEditorService.selectedElementPosition.next(0)
                //     }
                // break;
            }
        })
    }

}