import { Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { BlogProjectModel } from "../../blog_project.model";
import { BlogEditorService } from "../blog-editor.service";

@Component({
    selector: 'app-blog-details',
    templateUrl: 'blog-details.component.html',
    styleUrls: ['blog-details.component.sass']
})
export class BlogDetailsComponent implements OnInit {
    currentProject!:BlogProjectModel
    @Input('editMode') editMode!:boolean
    @ViewChild('thumbnail') thumbnail!:ElementRef
    @ViewChild('updateThumbnailForm') updateThumbnailForm!:NgForm
    selectedThumbnail!:File

    constructor(
        private blogEditorService:BlogEditorService
    ) {}

    ngOnInit(): void {
        this.loadCurrentProject()
    }

    loadCurrentProject() {
        this.blogEditorService.currentProject.subscribe( currentProject=>{
            if(currentProject){
                this.currentProject = currentProject
            } else {
                setTimeout(() => {
                    this.loadCurrentProject()
                }, 500);
            }
        } )
    }

    toggleCard(bool:boolean) {
        let thumbnail = this.thumbnail.nativeElement as HTMLInputElement
        if(bool){
            thumbnail.classList.add('updating')
        }else{
            thumbnail.classList.remove('updating')
        }
    }

    updateThumbnail() {
        console.log( this.updateThumbnailForm )
        let thumbnailUrl:string = this.updateThumbnailForm.controls['thumbnail-url'].value
        if( thumbnailUrl && thumbnailUrl.length>0 ) {
            this.currentProject.thumbnail = thumbnailUrl
            this.toggleCard(false)
        } else if(this.selectedThumbnail){
            console.log(this.selectedThumbnail)
            this.blogEditorService.uploadThumbnail(this.selectedThumbnail)
        } else
            alert("please select some value!")
    }

    changeThumbnail( event:any ) {
        if( event && event.target && event.target.files && event.target.files.length>0 )
            this.selectedThumbnail = event.target.files[0]
    }


}