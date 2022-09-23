import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Observable, Subscription } from "rxjs";
import { SharedService } from "src/app/shared/shared.service";
import { CategoryModel } from "../../../category.model";
import { BlogEditorService } from "../blog-editor.service";
import { CanComponentDeactivate } from "../can-deactivate-guard.service";

@Component({
    selector: 'app-blog-details',
    templateUrl: 'blog-details.component.html',
    styleUrls: ['blog-details.component.sass']
})
export class BlogDetailsComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    @Input('title') title!:string
    @Input('thumbnail') thumbnail!:string
    @Input('category') category!:CategoryModel

    @Output('updateThumbnailEvent') updateThumbnailEvent = new EventEmitter<string>()
    @Output('updateTitleEvent') updateTitleEvent = new EventEmitter<string>()
    @Output('updateCategoryEvent') updateCategoryEvent = new EventEmitter<CategoryModel>()

    // @ViewChild('thumbnail') thumbnailElement!:ElementRef
    // @ViewChild('updateThumbnailForm') updateThumbnailForm!:NgForm
    currentDate = new Date()
    selectedThumbnail!:File

    // file upload component
    folderName!:string
    isFileUploadActive:boolean = false

    // text input
    isTitleTextInputActive:boolean = false
    isCategoryTextInputActive:boolean = false
    
    changesMadeSub!:Subscription
    changesSavedSub!:Subscription
    changesMade:boolean = false
    changesSaved:boolean = true

    constructor(
        public blogEditorService:BlogEditorService,
        public sharedService:SharedService
    ) {}

    ngOnInit(): void {
        // this.subscribeCurrentProject()
        this.subscribeChangesVariables()
    }

    ngOnDestroy(): void {
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

    // content loading methods
    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
        } )
        this.changesSavedSub = this.blogEditorService.changesSaved.subscribe( (changesSaved:boolean)=>{
            this.changesSaved = changesSaved
        } )
    }

    // toggleCard(bool:boolean) {
    //     let thumbnail = this.thumbnailElement.nativeElement as HTMLInputElement
    //     if(bool){
    //         thumbnail.classList.add('updating')
    //     }else{
    //         thumbnail.classList.remove('updating')
    //     }
    // }

    // updateThumbnail() {
    //     let thumbnailUrl:string = this.updateThumbnailForm.controls['thumbnail-url'].value
    //     if( thumbnailUrl && thumbnailUrl.length>0 ) {
    //         this.thumbnail = thumbnailUrl
    //         this.toggleCard(false)
    //     } else if(this.selectedThumbnail){
    //         // thumbnail file upload
    //         this.blogEditorService.uploadFile(this.selectedThumbnail)
    //     } else
    //         alert("please select some value!")
    // }

    // changeThumbnail( event:any ) {
    //     if( event && event.target && event.target.files && event.target.files.length>0 )
    //         this.selectedThumbnail = event.target.files[0]
    // }


}