import { BlogUserModel } from "../../blog_user.model";

export class BlogCommentModel{

    constructor(
        public commentId:string,
        public content:string,
        public creationDate:Date,
        public user:BlogUserModel,
        public blogId:string
    ) {}

}