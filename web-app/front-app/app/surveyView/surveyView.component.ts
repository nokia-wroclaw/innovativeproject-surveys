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
                                        if(this.survey.questions[i].questionType != 'multi')
                                            this.answers.push(new FormControl(this.oldAnswers[i].answer));
                                        else {
                                            let answ = this.oldAnswers[i].answer.split("||");
                                            let contr = new Array<FormControl>();
                                            for (let a of answ) {
                                                contr.push(new FormControl(a));
                                            }
                                            this.answers.push(new FormArray(contr));
                                        }
                                    } else {
                                        if(this.survey.questions[i].questionType != 'multi')
                                            this.answers.push(new FormControl(''));
                                        else {
                                            let contr = new Array<FormControl>();
                                            for (let a of this.survey.questions[i].possibleAnswers) {
                                                contr.push(new FormControl('false'));
                                            }
                                            this.answers.push(new FormArray(contr));
                                        }

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
        for (let ans of this.answers.controls) {
            if(this.survey.questions[i-1].questionType !== "multi")
                answers.push({
                    id: i,
                    answer: ans.value
                });
            else {
                let ansarr = ans as FormArray;
                let ansstr = '';
                for(let a of ansarr.controls){
                    console.log(a.value);
                    ansstr += a.value + '||';
                }
                ansstr = ansstr.slice(0, ansstr.length-2);
                console.log(ansstr);
                answers.push({
                    id: i,
                    answer: ansstr
                });
            }
            i++;
        }
        console.log(JSON.stringify(answers));
        this.service.fillSurvey(this.id, {Answers: answers})
            .subscribe(
                () => {
                    this.alertService.success('Survey filled', true);
                    this.router.navigate(['/']);
                },
                error => {
                    this.alertService.error(error.message);
                    this.loading = false;
                });
    }

}