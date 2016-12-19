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
                                for (let q of this.survey.questions) {
                                    if (q.questionType == 'open') {
                                        let res = {
                                            question: q.question,
                                            results: []
                                        };
                                        while (j < len) {
                                            res.results.push(response[i].answer);
                                            i++;
                                            j++;
                                        }
                                        j = 0;
                                        this.result.questions.push(res);
                                    } else if (q.questionType == 'true/false') {
                                        let res = {
                                            question: q.question,
                                            results: []
                                        };
                                        let t = 0, f = 0;
                                        while (j < len) {
                                            if (response[i].answer == "true") {
                                                t++;
                                            } else {
                                                f++;
                                            }
                                            i++;
                                            j++;
                                        }
                                        j = 0;
                                        res.results.push("Yes: " + t);
                                        res.results.push("No: " + f);
                                        this.result.questions.push(res);
                                    } else if (q.questionType == 'multi') {
                                        let res = {
                                            question: q.question,
                                            results: []
                                        };
                                        let posans = [];
                                        for (let p of q.possibleAnswers) {
                                            posans.push({
                                                answer: p,
                                                quantity: 0
                                            });
                                        }
                                        while (j < len) {
                                            let splitted = response[i].answer.split("||");
                                            let k = 0;
                                            for (let s of splitted) {
                                                if (s == "true") {
                                                    posans[k].quantity++;
                                                }
                                                k++;
                                            }
                                            i++;
                                            j++;
                                        }
                                        j = 0;
                                        for (let p of posans) {
                                            res.results.push("Answer: " + p.answer + " Quantity: " + p.quantity);
                                        }
                                        this.result.questions.push(res);
                                    }

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