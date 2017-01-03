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
                                        this.pieChartQuestions.push(q.question);
                                        this.pieChartData.push([t, f]);
                                    } else if (q.questionType == 'multi') {
                                        this.barChartLabels.push(q.possibleAnswers);
                                        let posans = [];
                                        for(let b of q.possibleAnswers){
                                            posans.push(0);
                                        }
                                        while (j < len) {
                                            let splitted = response[i].answer.split("||");
                                            let k = 0;
                                            for (let s of splitted) {
                                                if (s == "true") {
                                                    posans[k]++;
                                                }
                                                k++;
                                            }
                                            i++;
                                            j++;
                                        }
                                        j = 0;
                                        console.log(JSON.stringify(posans));
                                        this.barChartData.push([{data: posans, label: 'Answers'}]);
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

    public pieChartLabels:string[] = ['Yes', 'No'];
    public pieChartData = [];
    public pieChartType:string = 'pie';
    public pieChartQuestions = [];

    // events
    public chartClicked(e:any):void {
        console.log(e);
    }

    public chartHovered(e:any):void {
        console.log(e);
    }

    public barChartOptions:any = {
        scaleShowVerticalLines: false,
        responsive: true
    };
    public barChartLabels = [];
    public barChartType:string = 'bar';
    public barChartLegend:boolean = true;
    public barChartQuestions = [];

    public barChartData = [];

}