import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../_models/index';
import { UserService, AlertService } from '../_services/index';
import { InviteService } from '../_services/index';

@Component({
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
    currentUser: User;
		model: any = {};
		surv: any = {};

    constructor(private userService: UserService,
								private inviteService: InviteService,
								private alertService: AlertService,
								private router: Router) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }
		
		ngOnInit() {
			
		}


	surveyGo() {
		this.router.navigate(['/surveyView', this.surv.id]);
	}

	createSurveyGo() {
		this.router.navigate(['/surveyCreate']);
	}
}