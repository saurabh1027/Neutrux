import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { BlogImpressionModel } from "./blog.impression.model";
import { BlogModel } from "./blog.model";

@Injectable({
    providedIn: 'root'
})
export class BlogsService{

    constructor(
        private http: HttpClient
    ) {}

    getBlogs( pageNumber?:number, pageLimit?:number, 
        includeImpressions?:boolean, includeComments?:boolean ) : Observable<BlogModel[]> {
        let params:HttpParams = new HttpParams();
        if(pageNumber) params = params.append('pageNumber', pageNumber)
        if(pageLimit) params = params.append('pageLimit', pageLimit)
        if(includeImpressions) params = params.append('include-impressions', includeImpressions)
        if(includeComments) params = params.append('include-comments', includeComments)

        return this.http.get<BlogModel[]>( environment.backendServerUrl+'blogs', { params:params } )
    }

    getBlogsByCategory( categoryId:string, pageNumber?:number, pageLimit?:number, 
        includeImpressions?:boolean, includeComments?:boolean ) : Observable<BlogModel[]> {
        let params:HttpParams = new HttpParams();
        if(pageNumber) params = params.append('pageNumber', pageNumber)
        if(pageLimit) params = params.append('pageLimit', pageLimit)
        if(includeImpressions) params = params.append('include-impressions', includeImpressions)
        if(includeComments) params = params.append('include-comments', includeComments)

        return this.http.get<BlogModel[]>( 
            environment.backendServerUrl+'blogs/search/c/'+categoryId, 
            { params:params } 
        )
    }

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

    getBlogById( blogId:number ) {
        return this.http.get<BlogModel>( environment.backendServerUrl+'blogs/'+blogId )
    }
    
    addOrUpdateImpressionToBlog( type:string, userId:string, blogId:string ) :Observable<BlogImpressionModel> {
        return this.http.put<BlogImpressionModel>( 
            environment.backendServerUrl+'users/'+userId+'/blogs/'+blogId+'/impressions',
            { 'type': type, 'userId': userId } )
    }

    removeImpressionFromBlog( userId:string, blogId:string ) {
        return this.http.delete( 
            environment.backendServerUrl+'users/'+userId+'/blogs/'+blogId+'/impressions' )
    }

}