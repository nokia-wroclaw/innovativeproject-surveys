import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService, SurveyService} from '../_services/index';
import { Survey, User } from '../_models/index';

@Component({
	moduleId: module.id,
	templateUrl: 'survey-creation.component.html'
})

export class SurveyCreationComponent {
	model: Survey;
	currentUser: User;
	loading = false;
	
	constructor(
		private router: Router,
		private surveyService: SurveyService,
		private alertService: AlertService
	) {
		this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
		this.model = {
			name: "",
			description: "",
			email: this.currentUser.email,
			question: "",
			question1: "",
			question2: "",
			question3: ""
		}
	}
	
	create() {
		this.surveyService.createSurvey(this.model)
			.subscribe(
				data => {
					this.alertService.success("Survey created successful! E-mail with link to your survey was sended.", true);
				},
				error => {
					console.log(JSON.stringify(error));
					this.alertService.error(error);
				}
			);
	}
	
}