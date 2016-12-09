import {Component} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {AlertService, SurveyService} from '../_services/index';
import {Survey, User} from '../_models/index';
import {FormGroup, FormArray, Validators, FormControl} from '@angular/forms';


@Component({
    templateUrl: 'surveyView.component.html'
})

export class SurveyViewComponent {
    model: any = {};
    answerForm = new FormGroup({
        answers: new FormArray([], Validators.required)
    });
    id: any;
    survey: Survey = new Survey();    //survey
    currentUser: User;
    oldAnswers = [];

    answered = false;
    loading = false;

    get answers(): FormArray {
        return this.answerForm.get('answers') as FormArray;
    }

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
                    this.service.getUserResult(this.id)
                        .subscribe(
                            (response) => {
                                this.oldAnswers = response;
                                for (let i = 0; i < this.survey.questions.length; i++) {
                                    if (typeof this.oldAnswers === "undefined" || i < this.oldAnswers.length) {
                                        this.answers.push(new FormControl(this.oldAnswers[i].answer));
                                    } else {
                                        this.answers.push(new FormControl(''));
                                    }
                                }
                                if (typeof this.oldAnswers !== "undefined" && this.oldAnswers.length > 0) {
                                    this.answered = true;
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

    answer() {
        this.loading = true;
        let answers = [];
        let i = 1;
        for (let ans of this.answers.value) {
            answers.push({
                id: i,
                answer: ans
            });
            i++;
        }
        console.log(JSON.stringify(answers));
        this.service.fillSurvey(this.id, {Answers: answers})
            .subscribe(
                data => {
                    this.alertService.success('Survey filled', true);
                    this.router.navigate(['/']);
                },
                error => {
                    this.alertService.error(error.message);
                    this.loading = false;
                });
    }

}