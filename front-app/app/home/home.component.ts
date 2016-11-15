import { Component, OnInit } from '@angular/core';

import { User } from '../_models/index';
import { UserService, AlertService } from '../_services/index';
import { InviteService } from '../_services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'home.component.html'
})

export class HomeComponent implements OnInit {
    currentUser: User;
		sendingInvite = false;
		model: any = {};

    constructor(private userService: UserService,
								private inviteService: InviteService,
								private alertService: AlertService) {
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }
		
		ngOnInit() {
			
		}

    invite() {
			this.sendingInvite = true;
			console.log("Sending invitation!");
			this.inviteService.invite(this.model.email)
			    .subscribe(
                data => {
									console.log("Success");
                  this.alertService.success('Invite successful', true);
									this.sendingInvite = false;
                },
                error => {
									console.log("Fail");
									console.log(error);
                  this.alertService.error(error);
									this.sendingInvite = false;
                });
		}
}