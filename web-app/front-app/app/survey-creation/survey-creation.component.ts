import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService, SurveyService} from '../_services/index';
import { Survey, User, Question } from '../_models/index';

@Component({
	templateUrl: 'survey-creation.component.html'
})

export class SurveyCreationComponent {
	model: Survey;
	currentUser: User;
	loading = false;
	id = 1;
	questions = [new Question(1, "")];
	
	constructor(
		private router: Router,
		private surveyService: SurveyService,
		private alertService: AlertService
	) {
		this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
		this.model = {
			name: "",
			description: "",
			email: this.currentUser.email
		}
	}
	
	create() {
		let surveyId;
		this.surveyService.createSurvey(this.model)
			.subscribe(
				(response) => {
					surveyId = response.json().id;
					this.alertService.success("Survey created successful! E-mail with link to your survey was sended. Your survey id is "+surveyId+".", true);
				},
				error => {
					this.alertService.error(error.json().message);
				}
			);
		for(var i; i < this.id; i++ ){
			let que = this.questions[i];
			this.surveyService.addQuestion(que, surveyId)
				.subscribe(
					response => {
						
					},
					error => {
						this.alertService.error(error.message);
					}
				);
		}
	}
	
	addQuestion() {
		this.id++;
		this.questions.push(new Question(this.id, ""));
	}
	
}