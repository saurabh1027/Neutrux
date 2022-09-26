import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";

@Component({
    selector: 'app-text-input',
    templateUrl: 'text-input.component.html',
    styleUrls: ['text-input.component.sass']
})
export class TextInputComponent implements OnInit, OnDestroy {

    @Input('title') title:string = ''
    @Input('value') value:string = ''
    @Output('textInputUpdatedEvent') textInputUpdatedEvent= new EventEmitter<string>()
    @Output('cancelTextInputEvent') cancelTextInputEvent:EventEmitter<void> = new EventEmitter()
    @ViewChild('input') input!:ElementRef

    ngOnInit(): void {
        let body:HTMLElement = document.body
        body.classList.add('no-scrolling')
        setTimeout(() => {
            this.input.nativeElement.focus()
        }, 500);
        this.addKeyboardEvent()
    }

    ngOnDestroy(): void {
        let body:HTMLElement = document.body
        body.classList.remove('no-scrolling')
    }

    addKeyboardEvent(){
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if( event.key.toLowerCase() == 'escape' ) {
                this.cancelTextInputEvent.emit()
            }
        })
    }

    onSubmit() {
        this.textInputUpdatedEvent.emit(this.value)
    }

}