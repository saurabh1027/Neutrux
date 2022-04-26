import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class BlogsService{

    constructor(
        private http: HttpClient
    ) {}

    getTrendingBlogs(pageNumber:number, pageLimit:number) {
        this.http.get( environment.backendServerUrl+'blogs/trending' )
    }
    

}