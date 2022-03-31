import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AlertComponent } from "./alert/alert.component";

@NgModule({
    declarations: [
        AlertComponent
    ],
    imports: [
        CommonModule,
        FormsModule
    ],
    exports: [
        AlertComponent
    ]
})
export class SharedModule{ }