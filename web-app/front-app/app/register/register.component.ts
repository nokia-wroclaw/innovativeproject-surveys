import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import { AlertService, UserService } from '../_services/index';
import {formArrayNameProvider} from "@angular/forms/src/directives/reactive_directives/form_group_name";

@Component({
    templateUrl: 'register.component.html'
})

export class RegisterComponent {
    model: any = {};
    loading = false;
    submitted = false;
    form = new FormGroup({
        login: new FormControl('', Validators.required),
        firstName: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required),
        rePassword: new FormControl('', Validators.required),
        email: new FormControl('', Validators.required),
        resetQuestion: new FormControl('', Validators.required),
        resetAnswer: new FormControl('', Validators.required)
    });
    questions = [
        "What is your mother's maiden name?",
        "What is your hobby?",
        "What was your first pet name?"
    ];
	
    constructor(
        private router: Router,
        private userService: UserService,
        private alertService: AlertService) {	}

    register() {

        this.submitted = true;
        if(!this.form.valid){
            return;
        }
        this.model = this.form.value;
        this.loading = true;
        console.log(JSON.stringify(this.model));
        this.userService.create(this.model)
            .subscribe(
                data => {
					console.log(JSON.stringify(data));
                    this.alertService.success('Registration successful! Check your email to activate the account', true);
                    this.router.navigate(['/login']);
                },
                error => {
					console.log(JSON.stringify(error));
                    this.alertService.error(error.json().message);
                    this.loading = false;
                });
    }

    isFormError(formControl: FormControl) {
        return (this.submitted && !formControl.valid);
    }
}
