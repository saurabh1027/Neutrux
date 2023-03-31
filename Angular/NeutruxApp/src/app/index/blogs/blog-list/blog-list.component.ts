import { AfterViewInit, Component, EventEmitter, Input, Output } from "@angular/core";
import { BlogModel } from "../blog.model";

@Component({
    selector: 'app-blog-list',
    templateUrl: 'blog-list.component.html',
    styleUrls: ['blog-list.component.sass']
})
export class BlogListComponent implements AfterViewInit {
    @Output() prev:EventEmitter<number> = new EventEmitter()
    @Output() next:EventEmitter<number> = new EventEmitter()

    @Input('blogs') blogs!:BlogModel[]
    @Input('pageNumber') pageNumber!:number

    isLoading:boolean = true

    ngAfterViewInit(){
        this.isLoading = false
    }

    onNext() {
        this.next.emit(this.pageNumber)
    }
    
    onPrev() {
        this.prev.emit(this.pageNumber)
    }

}