import {Component} from '@angular/core';
import {Survey} from '../_models/index';
import {Router, ActivatedRoute} from '@angular/router'
import {SurveyService, AlertService} from '../_services/index'

@Component({
    templateUrl: 'survey-result.component.html'
})

export class SurveyResultComponent {
	result = {
	    questions: []
    }
    survey = new Survey();
    id: number;
    currentUser: any;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private service: SurveyService,
                private alertService: AlertService) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));

        this.id = +this.route.snapshot.params['id'];

        this.service.getSurvey(this.id)
            .subscribe(
                (survey) => {
                    this.survey = survey;
                    console.log(JSON.stringify(survey));
                    this.service.getAllResult(this.id)
                        .subscribe(
                            (response) => {
                                console.log(JSON.stringify(response));
                                let len = response.length / this.survey.questions.length;
                                let i = 0;
                                let j = 0;
                                for(let q of this.survey.questions){
                                    let res = {
                                        question: q.question,
                                        results: []
                                    };
                                    while(j < len){
                                        res.results.push(response[i].answer);
                                        i++;
                                        j++;
                                    }
                                    j = 0;
                                    this.result.questions.push(res);
                                }
                            },
                            (error) => {
                                this.alertService.error(error.json().message);
                            }
                        );
                },
                error => {
                    this.alertService.error(error.json().message);
                });
    }
}