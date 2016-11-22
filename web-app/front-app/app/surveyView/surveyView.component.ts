import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { AlertService, SurveyService } from '../_services/index';
import { Survey, Question } from '../_models/index';


@Component({
    templateUrl: 'surveyView.component.html'
})

export class SurveyViewComponent implements OnInit{
	model: any = {};
	answers = [new Question(1, "")];
	id: any;
	survey: any;
	
	loading = false;
	
	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private service: SurveyService,
		private alertService: AlertService
	) {}
	
	ngOnInit() {
		this.id = +this.route.snapshot.params['id'];

		this.service.getSurvey(this.id)
		.subscribe(
			(survey) => {
				console.log(survey);
			},
			error =>{
				this.alertService.error(error.json().message);
			});
	}
	
	answer(){
		this.loading = true;
        this.service.fillSurvey(this.id, this.answers)
            .subscribe(
                data => {
					console.log(JSON.stringify(data));
                    this.alertService.success('Survey filled', true);
                    this.router.navigate(['/']);
                },
                error => {
					console.log(JSON.stringify(error));
                    this.alertService.error(error.json().message);
                    this.loading = false;
                });
	}

}