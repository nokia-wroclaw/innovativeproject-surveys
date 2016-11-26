import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule }    from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { AlertComponent } from './_directives/index';
import { BarsComponent } from './bars/index';
import { AuthGuard } from './_guards/index';
import { AlertService, AuthenticationService, UserService, InviteService, SurveyService } from './_services/index';
import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import { SurveyViewComponent } from './surveyView/index'
import { SurveyCreationComponent } from './survey-creation/index'

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpModule,
        routing
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        BarsComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent,
		SurveyViewComponent,
		SurveyCreationComponent
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
		InviteService,
		SurveyService
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }