import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { BlogCommentModel } from "./blog.comment.model";

@Injectable({
    providedIn: 'root'
})
export class CommentsService { 
    
    constructor(
        private http: HttpClient
    ) {}

    addComment( newComment:BlogCommentModel ):Observable<BlogCommentModel>{
        return this.http.post<BlogCommentModel>(
            environment.backendServerUrl+'users/'+newComment.user.userId+'/blogs/'
                +newComment.blogId+'/comments/', 
                { "content": newComment.content }
        );
    }

    deleteComment( commentId:string, userId:string, blogId:string ) {
        return this.http.delete(
            environment.backendServerUrl+'users/'+userId+'/blogs/'+blogId+'/comments/'+commentId
        )
    }

}