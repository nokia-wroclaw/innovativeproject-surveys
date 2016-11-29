import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {AlertService, AuthenticationService} from '../_services/index';

@Component({
    templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
    model: any = {};
    loading = false;

    constructor(private router: Router,
                private authenticationService: AuthenticationService,
                private alertService: AlertService) {
    }

    ngOnInit() {
        this.authenticationService.logout();
    }

    login() {
        this.loading = true
        console.log("LoginComponent.login");
        this.authenticationService.login(this.model.username, this.model.password)
            .subscribe(
                data => {
                    this.router.navigate(['/']);
                },
                error => {
                    this.alertService.error(error.json().message);
                    this.loading = false;
                });
    }
}
