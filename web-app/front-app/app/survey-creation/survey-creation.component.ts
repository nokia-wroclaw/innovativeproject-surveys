import {Component} from '@angular/core';
import {AlertService, SurveyService, UserService} from '../_services/index';
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
    submitted = false;
    memb = [];
    usernames: string[];
    matchingUsernames: string[][] = [[]];

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
                private userService: UserService,
                private router: Router) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.userService.getAllUsernames().subscribe(
            (response) => {
                this.usernames = response;
            }
        )
    }

    get questions(): FormArray {
        return this.surveyForm.get('questions') as FormArray;
    }

    get members(): FormArray {
        return this.surveyForm.get('members') as FormArray;
    }

    create() {
        this.submitted = true;
        if (!this.surveyForm.valid) {
            return;
        }
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
        this.matchingUsernames.push([]);
    }

    removeQuestion(i: number) {
        this.questions.removeAt(i);
    }

    removeMember(i: number) {
        this.members.removeAt(i);
        this.matchingUsernames.splice(i, 1);
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
            this.memb.push({login: mem.member});
        }
    }

    isQuestionType(i: number, type: String): boolean {
        return this.questions.at(i).get("type").value == type;
    }

    isFormError(formControl: FormControl) {
        return (this.submitted && !formControl.valid);
    }

    findMatching(actual) {
        console.log("findMatching runned!")
        let matching = [];
        for (let user of this.usernames) {
            if (user.includes(actual) && matching.length <= 5) {
                matching.push(user);
            }
        }
        console.log("findMatching returned: " + JSON.stringify(matching));
        return matching;
    }

    updateMatchingUsernames(i: number) {
        let actual = this.members.at(i).get('member').value;
        if (actual.length < 3) {
            this.matchingUsernames[i] = [];
        } else {
            this.matchingUsernames[i] = this.findMatching(actual);
        }
    }

    setUsername(i: number, username: string) {
        let members = this.surveyForm.get('members') as FormArray;
        members.at(i).get('member').setValue(username);
        this.matchingUsernames[i] = [];
    }

}