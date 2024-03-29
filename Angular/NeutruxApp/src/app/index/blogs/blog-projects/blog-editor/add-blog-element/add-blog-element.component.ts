import { Component, EventEmitter, OnDestroy, OnInit, Output } from "@angular/core";
import { Subscription } from "rxjs";
import { BlogElementModel } from "../../../blog.element.model";
import { BlogEditorService } from "../blog-editor.service";

@Component({
    selector: 'app-add-blog-element',
    templateUrl: 'add-blog-element.component.html',
    styleUrls: ['add-blog-element.component.sass']
})
export class AddBlogElementComponent implements OnInit, OnDestroy {

    blogElements:BlogElementModel[] = []
    newElement = new BlogElementModel('','','','',0,'')
    blogElementsSub!: Subscription;

    @Output('cancelEvent') cancelEvent = new EventEmitter()
    @Output('addElementSuccessEvent') addElementSuccessEvent = new EventEmitter<BlogElementModel>()

    isLoading = true

    constructor(
        public blogEditorService:BlogEditorService,
    ) {}
    
    ngOnInit(): void {
        let body:HTMLElement = document.body
        body.classList.add('no-scrolling')
        this.loadBlogElements()
        this.addKeyboardEvent()
    }

    ngOnDestroy(): void {
        let body:HTMLElement = document.body
        body.classList.remove('no-scrolling')
        this.blogElementsSub.unsubscribe()
    }

    addKeyboardEvent(){
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if( event.key && event.key.toLowerCase() == 'escape' ) {
                this.cancelEvent.emit()
            }
        })
    }

    loadBlogElements() {
        this.blogElementsSub = this.blogEditorService.loadBlogElements().subscribe( ( blogElements:any )=>{
            this.blogElements = blogElements
            this.isLoading = false
        } )
    }

    selectElement(element:BlogElementModel) {
        if( element.name==this.newElement.name ){
            this.newElement = new BlogElementModel('','','','',0,'')
        } else {
            this.newElement = element
        }
    }

    onSubmit() {
        this.addElementSuccessEvent.emit(this.newElement)
    }

}