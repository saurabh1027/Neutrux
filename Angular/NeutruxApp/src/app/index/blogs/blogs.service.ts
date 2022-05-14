import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { BlogModel } from "./blog.model";

@Injectable({
    providedIn: 'root'
})
export class BlogsService{

    constructor(
        private http: HttpClient
    ) {}

    // it is returning normal blogs instead of trending blogs
    getTrendingBlogs(pageNumber?:number, pageLimit?:number, includeImpressions?:boolean, 
        includeComments?:boolean
    ) : Observable<BlogModel[]> {
        let params:HttpParams = new HttpParams();
        if(pageNumber) params = params.append('pageNumber', pageNumber)
        if(pageLimit) params = params.append('pageLimit', pageLimit)
        if(includeImpressions) params = params.append('include-impressions', includeImpressions)
        if(includeComments) params = params.append('include-comments', includeComments)

        return this.http.get<BlogModel[]>( environment.backendServerUrl+'blogs/trending', { params:params } )
    }
    

}