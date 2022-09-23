import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { BlogEditorService } from "src/app/index/blogs/blog-projects/blog-editor/blog-editor.service";

@Component({
    selector: 'app-file-upload',
    templateUrl: 'file-upload.component.html',
    styleUrls: ['file-upload.component.sass']
})
export class FileUploadComponent{
    @Input("folderName") folderName!:string
    @ViewChild("uploadFileForm") uploadFileForm!:NgForm
    @Output("cancelFileUploadEvent") cancelFileUploadEvent = new EventEmitter()
    @Output("fileUploadSuccessEvent") fileUploadSuccessEvent = new EventEmitter<string>()
    fileToBeUploaded!:File
    // fileUrl!:string

    constructor(
        private blogEditorService:BlogEditorService 
    ) {}

    onFileChange( event:any ) {
        if( event && event.target && event.target.files && event.target.files.length>0 )
            this.fileToBeUploaded = event.target.files[0]
    }

    onSubmit() {
        let fileUrl:string = this.uploadFileForm.controls['file-url'].value
        if( fileUrl && fileUrl.length>0 ) {
            // update url
            this.fileUploadSuccessEvent.emit(fileUrl)
        } else if(this.fileToBeUploaded){
            // file upload
            this.blogEditorService.uploadFile(this.fileToBeUploaded)
            // emit file url after uploading on file server
            this.fileUploadSuccessEvent.emit('')
        }
    }


}