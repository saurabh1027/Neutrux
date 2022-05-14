import { BlogCommentModel } from "./blog.comment.model";
import { BlogElementModel } from "./blog.element.model";
import { BlogImpressionModel } from "./blog.impression.model";
import { CategoryModel } from "./category.model";

export class BlogModel{

    constructor(
        public blogId:string,
        public title:string,
        public description:string,
        public creationDate:Date,
        public category:CategoryModel,
        public elements:BlogElementModel[],
        public impressions:BlogImpressionModel[],
        public comments:BlogCommentModel[]
    ) {}

}