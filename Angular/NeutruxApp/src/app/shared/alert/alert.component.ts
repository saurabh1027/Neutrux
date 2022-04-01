import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from "@angular/core";

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrls: [ './alert.component.sass' ]
})
export class AlertComponent{
    @Input() alert!:{
        'message': string,
        'isError': boolean
    }
    @Output('close') close = new EventEmitter<void>();
    @ViewChild('closeBtn') btn!:ElementRef

    onClose() {
        this.close.emit();
    }

}