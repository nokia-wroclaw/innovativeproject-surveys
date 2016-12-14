import {Component} from '@angular/core';
import {AlertService, SurveyService} from '../_services/index';
import {Survey, User, Question} from '../_models/index';
import {FormGroup, FormArray, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
    templateUrl: 'survey-creation.component.html'
})

export class SurveyCreationComponent {
    model: Survey = new Survey();
    currentUser: User;
    loading = false;
    memb = [];

    surveyForm = new FormGroup({
        name: new FormControl('', Validators.required),
        description: new FormControl('', Validators.required),
        questions: new FormArray(
            [new FormGroup({
                type: new FormControl('open', Validators.required),
                question: new FormControl('', Validators.required),
                answers: new FormArray([
                    new FormControl('')
                ], Validators.required)
            })],
            Validators.required
        ),
        members: new FormArray(
            [new FormGroup({
                member: new FormControl('', Validators.required)
            })],
            Validators.required
        )
    });

    constructor(private surveyService: SurveyService,
                private alertService: AlertService,
                private router: Router) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    get questions(): FormArray {
        return this.surveyForm.get('questions') as FormArray;
    }

    get members(): FormArray {
        return this.surveyForm.get('members') as FormArray;
    }

    create() {
        this.loading = true;
        this.buildSurvey();
        this.surveyService.createSurvey(this.model)
            .subscribe(
                (response) => {
                    this.alertService.success("Survey created successful!.", true);
                    for (let memb of this.memb)
                        this.surveyService.addMember(memb, response.id)
                            .subscribe(
                                () => {
                                    this.router.navigate(['/']);
                                },
                                error => {
                                    this.alertService.error(error.json().message);
                                    this.loading = false;
                                }
                            );
                },
                error => {
                    this.alertService.error(error.json().message);
                    this.loading = false;
                }
            );
    }

    addPossibleAnswer(i: number) {
        let answers = this.questions.at(i).get("answers") as FormArray;
        answers.push(new FormControl(''));
    }

    removePossibleAnswer(i: number, j: number) {
        let answers = this.questions.at(i).get("answers") as FormArray;
        answers.removeAt(j);
    }

    addQuestion() {
        this.questions.push(new FormGroup({
            type: new FormControl('open', Validators.required),
            question: new FormControl('', Validators.required),
            answers: new FormArray([
                new FormControl('')
            ], Validators.required)
        }));
        console.log(this.questions.value);
    }

    addMember() {
        this.members.push(new FormGroup({
            member: new FormControl('', Validators.required)
        }));
        console.log(this.questions.value);
    }

    removeQuestion(i: number) {
        this.questions.removeAt(i);
    }

    removeMember(i: number) {
        this.members.removeAt(i);
    }

    buildSurvey() {
        this.model.name = this.surveyForm.get('name').value;
        this.model.description = this.surveyForm.get('description').value;
        this.model.email = this.currentUser.email;
        this.model.questions = [];
        let id = 1;
        for (let ques of this.questions.controls) {
            let possibleAnswers = [];
            if (ques.get('type').value === 'multi') {
                let answers = ques.get('answers') as FormArray;
                for (let answer of answers.controls)
                    possibleAnswers.push({
                        response: answer.value
                    });
            }
            this.model.questions
                .push(new Question(id, ques.get('question').value, ques.get('type').value, possibleAnswers));
            id++;
        }
        console.log(JSON.stringify(this.model));
        for (let mem of this.members.value) {
            console.log("member " + JSON.stringify(mem));
            this.memb.push({email: mem.member});
        }
    }

    isQuestionType(i: number, type: String): boolean {
        return this.questions.at(i).get("type").value == type;
    }
}