import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from "@angular/core";
import { Subscription } from "rxjs";
import { CategoryModel } from "../../../category.model";
import { BlogEditorService } from "../blog-editor.service";

@Component({
    selector: 'app-select-category',
    templateUrl: 'select-category.component.html',
    styleUrls: ['select-category.component.sass']
})
export class SelectCategoryComponent implements OnInit, OnDestroy {
    @Input('selectedCategory') selectedCategory !: CategoryModel
    @Output('changeCategoryEvent') changeCategoryEvent = new EventEmitter<CategoryModel>()
    @Output('cancelEvent') cancelEvent = new EventEmitter()

    // Category
    categories:CategoryModel[] = []
    categorySearchResults:CategoryModel[] = []
    searchCategoryName:string = ''

    // Subscriptions
    categoriesSub!: Subscription;

    constructor(
        public blogEditorService:BlogEditorService,
    ) {}

    ngOnInit(): void {
        this.loadCategories()
    }

    ngOnDestroy(): void {
        this.categoriesSub.unsubscribe()
    }

    loadCategories() {
        this.categoriesSub = this.blogEditorService.loadCategories( 1, 100000 ).subscribe(data=>{
            this.categories = data
            this.categorySearchResults = this.categories
        })
    }
    
    filterCategoriesByName() {
        this.categorySearchResults = []
        this.categories.forEach(category => {
            if( category.name.includes( this.searchCategoryName ) )
                this.categorySearchResults.push(category)
        });
    }

    selectCategory(category:CategoryModel) {
        if( category.name==this.selectedCategory.name ){
            this.selectedCategory = new CategoryModel('','','')
        } else {
            this.selectedCategory = category
        }
    }

    onSubmit() {
        this.changeCategoryEvent.emit(this.selectedCategory)
    }
}