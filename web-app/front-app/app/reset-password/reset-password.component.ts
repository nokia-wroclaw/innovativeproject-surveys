import {Component} from "@angular/core";
import {FormGroup, FormControl, Validators} from '@angular/forms';
import {UserService, AlertService} from "../_services/index";

@Component({
    templateUrl: "reset-password.component.html"
})

export class ResetPasswordComponent {
    firstForm = new FormGroup({
        login: new FormControl('', Validators.required),
        email: new FormControl('', Validators.required)
    });

    stage: number = 1;
    submitted: boolean = false;
    loading: boolean = false;
    question: string = '';

    constructor(private userService: UserService,
                private alertService: AlertService) {
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
}