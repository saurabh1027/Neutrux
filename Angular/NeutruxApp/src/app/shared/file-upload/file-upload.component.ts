import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { BlogEditorService } from "src/app/index/blogs/blog-projects/blog-editor/blog-editor.service";
import { environment } from "src/environments/environment.prod";

@Component({
    selector: 'app-file-upload',
    templateUrl: 'file-upload.component.html',
    styleUrls: ['file-upload.component.sass']
})
export class FileUploadComponent implements OnInit, OnDestroy {
    @Input("folderName") folderName!:string
    @ViewChild("uploadFileForm") uploadFileForm!:NgForm
    @Output("cancelFileUploadEvent") cancelEvent = new EventEmitter()
    @Output("fileUploadSuccessEvent") fileUploadSuccessEvent = new EventEmitter<string>()
    fileToBeUploaded!:File

    constructor(
        private blogEditorService:BlogEditorService 
    ) {}

    ngOnInit(): void {
        let body:HTMLElement = document.body
        body.classList.add('no-scrolling')
        this.addKeyboardEvent()
    }

    ngOnDestroy(): void {
        let body:HTMLElement = document.body
        body.classList.remove('no-scrolling')
    }

    addKeyboardEvent(){
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if( event.key && event.key.toLowerCase() == 'escape' ) {
                this.cancelEvent.emit()
            }
        })
    }

    onFileChange( event:any ) {
        if( event && event.target && event.target.files && event.target.files.length>0 ){
            this.fileToBeUploaded = event.target.files[0]
        }
    }

    onSubmit() {
        let fileUrl:string = this.uploadFileForm.controls['file-url'].value
        if( fileUrl && fileUrl.length>0 ) {
            // update url
            this.fileUploadSuccessEvent.emit(fileUrl)
        } else if(this.fileToBeUploaded){
            // file upload
            this.blogEditorService.uploadFile(this.fileToBeUploaded).subscribe( (path:string)=>{
                fileUrl = path
                this.fileUploadSuccessEvent.emit(environment.assetsUrl + 'blog_pictures/' + fileUrl)
            } )
        }
        this.blogEditorService.changesMade.next(true)
    }


}