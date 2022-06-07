import { RegistrationUserModel } from "src/app/users/registration.user.model";
import { BlogCommentModel } from "./blog/comments/blog.comment.model";
import { BlogElementModel } from "./blog.element.model";
import { BlogImpressionModel } from "./blog.impression.model";
import { BlogUserModel } from "./blog_user.model";
import { CategoryModel } from "./category.model";

export class BlogModel{

    constructor(
        public blogId:string,
        public title:string,
        public description:string,
        public creationDate:Date,
        public thumbnail:string,
        public category:CategoryModel,
        public user:BlogUserModel,
        public elements:BlogElementModel[],
        public impressions:BlogImpressionModel[],
        public comments:BlogCommentModel[]
    ) {}

}