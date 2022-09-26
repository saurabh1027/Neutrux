import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
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
    @ViewChild('searchInput') input!:ElementRef

    isLoading = true

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
        let body:HTMLElement = document.body
        body.classList.add('no-scrolling')
        setTimeout(() => {
            this.input.nativeElement.focus()
        }, 500);
        this.addKeyboardEvent()
    }

    ngOnDestroy(): void {
        let body:HTMLElement = document.body
        body.classList.remove('no-scrolling')
        this.categoriesSub.unsubscribe()
    }

    addKeyboardEvent(){
        document.addEventListener('keydown', (event:KeyboardEvent)=>{
            if( event.key.toLowerCase() == 'escape' ) {
                this.cancelEvent.emit()
            }
        })
    }

    loadCategories() {
        this.categoriesSub = this.blogEditorService.loadCategories( 1, 100000 ).subscribe(data=>{
            this.categories = data
            this.categorySearchResults = this.categories
            this.isLoading = false
        })
    }
    
    filterCategoriesByName() {
        this.categorySearchResults = []
        this.categories.forEach(category => {
            if( category.name.includes( this.searchCategoryName ) )
                this.categorySearchResults.push(category)
        });
        this.isLoading = false
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