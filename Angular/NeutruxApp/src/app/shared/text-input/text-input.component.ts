import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";

@Component({
    selector: 'app-text-input',
    templateUrl: 'text-input.component.html',
    styleUrls: ['text-input.component.sass']
})
export class TextInputComponent implements OnInit {

    @Input('value') value:string = ''
    @Output('textInputUpdatedEvent') textInputUpdatedEvent= new EventEmitter<string>()
    @Output('cancelTextInputEvent') cancelTextInputEvent:EventEmitter<void> = new EventEmitter()
    @ViewChild('input') input!:ElementRef

    ngOnInit(): void {
        setTimeout(() => {
            this.input.nativeElement.focus()
        }, 500);
    }

    onSubmit() {
        this.textInputUpdatedEvent.emit(this.value)
    }

}