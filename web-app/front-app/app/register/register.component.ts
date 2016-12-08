import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService, UserService } from '../_services/index';

@Component({
    templateUrl: 'register.component.html'
})

export class RegisterComponent {
    model: any = {};
    loading = false;
	
    constructor(
        private router: Router,
        private userService: UserService,
        private alertService: AlertService) {	}

    register() {
        this.loading = true;
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
}
