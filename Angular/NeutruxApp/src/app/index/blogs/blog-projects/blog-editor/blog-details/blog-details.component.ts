import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Observable, Subscription } from "rxjs";
import { SharedService } from "src/app/shared/shared.service";
import { CategoryModel } from "../../../category.model";
import { BlogEditorService } from "../blog-editor.service";
import { CanComponentDeactivate } from "../can-deactivate-guard.service";

@Component({
    selector: 'app-blog-details',
    templateUrl: 'blog-details.component.html',
    styleUrls: ['blog-details.component.sass']
})
export class BlogDetailsComponent implements OnInit, OnDestroy, CanComponentDeactivate {
    @Input('title') title!:string
    @Input('category') category!:CategoryModel

    @Output('updateTitleEvent') updateTitleEvent = new EventEmitter<string>()
    @Output('updateCategoryEvent') updateCategoryEvent = new EventEmitter<CategoryModel>()

    currentDate = new Date()

    // text input
    isTitleTextInputActive:boolean = false
    isCategoryTextInputActive:boolean = false
    
    changesMade:boolean = false
    changesMadeSub!:Subscription

    constructor(
        public blogEditorService:BlogEditorService,
        public sharedService:SharedService
    ) {}

    ngOnInit(): void {
        this.subscribeChangesVariables()
    }

    ngOnDestroy(): void {
        this.changesMadeSub.unsubscribe()
    }

    canDeactivate(): boolean | Observable<boolean> | Promise<boolean> {
        if( this.changesMade ) {
            let bool:boolean
            bool = confirm('Do you want to discard the changes?')
            if( bool ) {
                this.blogEditorService.changesMade.next(false)
            }
            return bool
        } else {
            return true
        }
    }

    // content loading methods
    subscribeChangesVariables() {
        this.changesMadeSub = this.blogEditorService.changesMade.subscribe( (changesMade:boolean)=>{
            this.changesMade = changesMade
        } )
    }

}