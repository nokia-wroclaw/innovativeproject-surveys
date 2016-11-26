import {Component, OnInit} from '@angular/core';
import {AlertService, SurveyService} from '../_services/index';
import {Survey, User, Question} from '../_models/index';
import {FormBuilder, FormGroup, FormArray, FormControl, Validators} from '@angular/forms';

@Component({
    templateUrl: 'survey-creation.component.html'
})

export class SurveyCreationComponent implements OnInit {
    model: Survey;
    currentUser: User;
    loading = false;
    surveyForm = new FormGroup({
        name: new FormControl('', Validators.required),
        description: new FormControl(''),
        email: new FormControl('', Validators.required),
        questions: new FormArray(
            [new FormGroup({
                question: new FormControl('', Validators.required)
            })],
            Validators.required
        )
    });

    constructor(private surveyService: SurveyService,
                private alertService: AlertService,
                private formBuilder: FormBuilder) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.model = {
            name: "",
            description: "",
            email: this.currentUser.email,
            questions: [new Question(1, "")]
        };
    }

    ngOnInit() {
    }

    get questions(): FormArray {
        return this.surveyForm.get('questions') as FormArray;
    }

    create() {
        this.loading = true;
        this.buildSurvey();
        let surveyId;
        this.surveyService.createSurvey(this.model)
            .subscribe(
                (response) => {
                    surveyId = response.json().id;
                    this.alertService.success("Survey created successful! E-mail with link to your survey was sended."
                        + " Your survey id is " + surveyId + ".", true);
                },
                error => {
                    this.alertService.error(error.json().message);
                }
            );
        this.loading = false;

    }

    addQuestion() {
        this.questions.push(new FormGroup({
            question: new FormControl('')
        }))
    }

    removeQuestion(i: number) {
        this.questions.removeAt(i);
    }

    buildSurvey(){
        this.model.name = this.surveyForm.get('name').value;
        this.model.description = this.surveyForm.get('description').value;
        this.model.email = this.surveyForm.get('email').value;
        this.model.questions = [];
        let id = 1;
        for(let ques of this.questions.controls){
            this.model.questions.push({
                id: id,
                question: ques.value
            });
        }
    }
}