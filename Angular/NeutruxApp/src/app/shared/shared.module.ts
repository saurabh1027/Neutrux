import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AlertComponent } from "./alert/alert.component";
import { FileUploadComponent } from "./file-upload/file-upload.component";
import { TextInputComponent } from "./text-input/text-input.component";

@NgModule({
    declarations: [
        AlertComponent,
        FileUploadComponent,
        TextInputComponent
    ],
    imports: [
        CommonModule,
        FormsModule
    ],
    exports: [
        AlertComponent,
        FileUploadComponent,
        TextInputComponent
    ]
})
export class SharedModule{ }