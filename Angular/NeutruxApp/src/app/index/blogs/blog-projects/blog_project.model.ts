import { BlogElementModel } from "../blog.element.model";
import { BlogUserModel } from "../blog_user.model";
import { CategoryModel } from "../category.model";

export class BlogProjectModel {

    constructor(
        public projectId: string,
        public title:string,
        public description:string,
        public creationDate:Date,
        public thumbnail:string,
        public category:CategoryModel,
        public user:BlogUserModel,
        public elements:BlogElementModel[],
    ) {}

}