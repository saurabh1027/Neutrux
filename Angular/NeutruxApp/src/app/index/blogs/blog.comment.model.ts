export class BlogCommentModel{

    constructor(
        public commentId:string,
        public content:string,
        public creationDate:Date,
        public userId:string,
        public blogId:string
    ) {}

}