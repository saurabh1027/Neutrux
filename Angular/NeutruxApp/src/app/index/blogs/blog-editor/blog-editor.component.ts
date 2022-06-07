import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "src/app/users/authentication/auth.service";
import { User } from "src/app/users/user.model";

@Component({
    selector: 'app-blog-editor',
    templateUrl: 'blog-editor.component.html',
    styleUrls: ['blog-editor.component.sass']
})
export class BlogEditorComponent implements OnInit {
    user!:User

    constructor(
        private authService:AuthService,
        private router:Router
    ) {}

    ngOnInit(): void {
        this.isAuthenticated()    
    }
    
    isAuthenticated() {
        this.authService.user.subscribe(user=>{
            if(user)
                this.user = user
            else{
                alert("Please login first!")
                this.router.navigate(['','authentication'])
            }
        })
    }

}