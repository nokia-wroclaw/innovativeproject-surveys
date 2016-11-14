import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService, AuthenticationService } from '../_services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private alertService: AlertService) { }

    ngOnInit() {
        // reset login status
        this.authenticationService.logout();
    }

    login() {
        this.loading = true;
				console.log("LoginComponent.login")
        this.authenticationService.login(this.model.username, this.model.password)
            .subscribe(
                data => {
									console.log("Success");
									localStorage.setItem('currentUser', JSON.stringify({username: this.model.username}));
                  this.router.navigate(['/']);
                },
                error => {
									console.log("Fail");
									console.log(error);
                  this.alertService.error(error);
                  this.loading = false;
                });
				this.loading = false;
    }
}
