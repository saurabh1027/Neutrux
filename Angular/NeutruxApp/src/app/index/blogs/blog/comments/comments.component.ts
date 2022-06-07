import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { NgForm } from "@angular/forms";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";
import { BlogUserModel } from "../../blog_user.model";
import { BlogCommentModel } from "./blog.comment.model";
import { CommentsService } from "./comments.service";

@Component({
    selector: 'app-comments',
    templateUrl: 'comments.component.html',
    styleUrls: ['comments.component.sass']
})
export class CommentsComponent implements OnInit {
    @Input('comments') comments!:BlogCommentModel[]
    @Input('blogId') blogId!:string 
    @Input('bool') bool!:boolean 
    @ViewChild('AddCommentForm') addCommentForm !:NgForm
    @Output('onDelete') onDelete:EventEmitter<string> = new EventEmitter()
    @Output('onCloseCommentSection') onCloseCommentSection:EventEmitter<void> = new EventEmitter()
    user!:User

    constructor(
        private authService:AuthService,
        private commentsService:CommentsService
    ) {}

    ngOnInit(): void {
        this.authService.user.subscribe(user=>{
            if(user)
                this.user = user
        })
    }

    addComment() {
        let content:string = this.addCommentForm.controls['new-comment'].value

        if(!this.user){
            alert("please login!")
            return
        }

        let user:BlogUserModel = new BlogUserModel(
            +this.user.userId,
            this.user.firstname,
            this.user.lastname,
            this.user.email,
            ''
        )
        let newComment:BlogCommentModel = new BlogCommentModel(
            '', content, new Date(), user, this.blogId
        )
        this.commentsService.addComment( newComment ).subscribe(data=>{
            newComment = data
            this.comments.push(newComment)
            this.addCommentForm.controls['new-comment'].setValue('')
        })
    }

    deleteCommentById( commentId:string ) {
        this.commentsService.deleteComment( commentId, this.user.userId, this.blogId ).subscribe(data=>{
            this.onDelete.emit( this.blogId )
        })
    }

    closeCommentSection() {
        this.onCloseCommentSection.emit()
    }

}