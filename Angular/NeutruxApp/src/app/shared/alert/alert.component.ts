import { AfterViewChecked, AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";

@Component({
    selector: 'app-alert',
    templateUrl: './alert.component.html',
    styleUrls: [ './alert.component.sass' ]
})
export class AlertComponent implements AfterViewChecked {
    @Input() alert!:{
        'message': string,
        'isError': boolean
    }
    @Output('close') close = new EventEmitter<void>();
    @ViewChild('closeBtn') btn!:ElementRef
    
    ngAfterViewChecked(): void {
        this.btn.nativeElement.focus()
    }
    
    onClose() {
        this.close.emit();
    }

}