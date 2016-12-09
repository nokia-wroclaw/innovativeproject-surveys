import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {User} from '../_models/index';
import {UserService, AlertService} from '../_services/index';
import {SurveyService} from '../_services/index';

@Component({
    templateUrl: 'home.component.html'
})

export class HomeComponent {
    currentUser: User;
    model: any = {};
    survey: any = [];
    survey2: any = [];

    constructor(private userService: UserService,
                private service: SurveyService,
                private alertService: AlertService,
                private router: Router) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.showUserSurveys();
        this.showAdminSurveys()
    }

    showUserSurveys() {
        this.service.getUserSurveys()
            .subscribe(
                (survey) => {
                    this.survey = survey;
                },
                error => {
                    this.alertService.error(error.json().message);
                });


    }

    showAdminSurveys() {
        this.service.getAdminSurveys()
            .subscribe(
                (survey2) => {
                    this.survey2 = survey2;
                },
                error => {
                    this.alertService.error(error.json().message);
                });


    }
}