import { Component, OnInit } from "@angular/core";
import { Title } from "@angular/platform-browser";
import { ActivatedRoute } from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: [ './home.component.sass' ]
})
export class HomeComponent implements OnInit{

    constructor(
        private readonly route: ActivatedRoute,
        private readonly titleService: Title
    ) {}

    ngOnInit(): void {
        this.titleService.setTitle( this.route.snapshot.data['title'] )
    }

}