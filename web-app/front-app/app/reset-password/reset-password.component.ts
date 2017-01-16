import {Component} from "@angular/core";
import {FormGroup, FormControl, Validators, AbstractControl} from '@angular/forms';
import {UserService, AlertService} from "../_services/index";
import {Router} from "@angular/router";

@Component({
    templateUrl: "reset-password.component.html"
})

export class ResetPasswordComponent {
    firstForm = new FormGroup({
        login: new FormControl('', Validators.required),
        email: new FormControl('', Validators.required)
    });

    secondForm = new FormGroup({
        answer: new FormControl('', Validators.required)
    });

    thirdForm = new FormGroup({
        code: new FormControl('', Validators.required),
        password: new FormControl('', Validators.compose([
            Validators.required, Validators.minLength(8)
        ])),
        rePassword: new FormControl('', Validators.compose([
            Validators.required, Validators.minLength(8)
        ]))
    })

    stage: number = 1;
    submitted: boolean = false;
    loading: boolean = false;
    question: string = '';

    constructor(private userService: UserService,
                private alertService: AlertService,
                private router: Router) {
    }

    isFormError(formControl: FormControl) {
        return (this.submitted && !formControl.valid);
    }

    getQuestion() {
        this.submitted = true;
        if (!this.firstForm.valid) {
            return;
        }
        this.loading = true;
        this.userService.getResetQuestion(this.firstForm.value).subscribe(
            (data) => {
                this.alertService.success("Answer for your safety question");
                this.stage = 2;
                this.submitted = false;
                this.loading = false;
                this.question = data.question;
                console.log(this.question);
            },
            (error) => {
                error = error.json();
                this.submitted = false;
                this.loading = false;
                this.alertService.error(error.message);
            }
        )
    }

    sendAnswer() {
        this.submitted = true;
        if (!this.secondForm.valid) {
            return;
        }
        this.loading = true;
        this.userService.sendAnswer(this.secondForm.value).subscribe(
            (response) => {
                this.stage = 3;
                this.submitted = false;
                this.loading = false;
                this.alertService.success(response.message);
            },
            (error) => {
                error = error.json();
                this.submitted = false;
                this.loading = false;
                this.alertService.error(error.message);
            }
        )
    }

    resetPassword() {
        this.submitted = true;
        if (!this.thirdForm.valid) {
            return;
        }
        this.loading = true;
        console.log(JSON.stringify(this.thirdForm.value));
        this.userService.resetPassword(this.thirdForm.value).subscribe(
            (data) => {
                this.router.navigate(['/']);
                this.alertService.success(data.message, true);
            },
            (error) => {
                error = error.json();
                this.alertService.error(error.message);
                this.loading = false;
                this.submitted = false;
            }
        )
    }
}