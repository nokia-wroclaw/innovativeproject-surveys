import {Component, OnInit} from '@angular/core';
import {AlertService, SurveyService} from '../_services/index';
import {Survey, User, Question} from '../_models/index';
import {FormBuilder, FormGroup, FormArray, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
    templateUrl: 'survey-creation.component.html'
})

export class SurveyCreationComponent {
    model: Survey = new Survey();
    currentUser: User;
    loading = false;
    surveyForm = new FormGroup({
        name: new FormControl('', Validators.required),
        description: new FormControl('', Validators.required),
        questions: new FormArray(
            [new FormGroup({
                question: new FormControl('', Validators.required)
            })],
            Validators.required
        )
    });

    constructor(private surveyService: SurveyService,
                private alertService: AlertService,
                private formBuilder: FormBuilder,
                private router: Router) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    get questions(): FormArray {
        return this.surveyForm.get('questions') as FormArray;
    }

    create() {
        this.loading = true;
        this.buildSurvey();
        this.surveyService.createSurvey(this.model)
            .subscribe(
                (response) => {
                    this.alertService.success("Survey created successful!.", true);
                    this.router.navigate(['/']);
                },
                error => {
                    this.alertService.error(error.json().message);
                    this.loading = false;
                }
            );
    }

    addQuestion() {
        this.questions.push(new FormGroup({
            question: new FormControl('', Validators.required)
        }));
        console.log(this.questions.value);
    }

    removeQuestion(i: number) {
        this.questions.removeAt(i);
    }

    buildSurvey() {
        this.model.name = this.surveyForm.get('name').value;
        this.model.description = this.surveyForm.get('description').value;
        this.model.email = this.currentUser.email;
        this.model.questions = [];
        let id = 1;
        for (let ques of this.questions.value) {
            this.model.questions.push(new Question(id, ques.question));
            id++;
        }
        console.log(JSON.stringify(this.model));
    }
}