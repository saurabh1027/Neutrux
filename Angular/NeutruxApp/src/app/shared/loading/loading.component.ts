import { Component, OnDestroy, OnInit } from "@angular/core";

@Component({
    selector: 'app-loading',
    templateUrl: './loading.component.html',
    styleUrls: [ './loading.component.sass' ]
})
export class LoadingComponent implements OnInit, OnDestroy{

    ngOnInit(): void {
        let body:HTMLElement = document.body
        body.classList.add('no-scrolling')
    }

    ngOnDestroy(): void {
        let body:HTMLElement = document.body
        body.classList.remove('no-scrolling')
    }

}