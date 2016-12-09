import { Component } from '@angular/core';
import { InviteService } from '../_services/index';
import { AlertService } from '../_services/index';

@Component({
    templateUrl: 'invite.component.html'
})

export class InviteComponent {

    sendingInvite = false;
    model: any = {};

    constructor(private inviteService: InviteService,
                private alertService: AlertService) { }

    invite() {
        this.sendingInvite = true;
        console.log("Sending invitation!");
        this.inviteService.invite(this.model.email)
            .subscribe(
                () => {
                    console.log("Success");
                    this.alertService.success('Invite successful', true);
                    this.sendingInvite = false;
                },
                error => {
                    console.log("Fail");
                    console.log(error);
                    this.alertService.error(error.json().message);
                    this.sendingInvite = false;
                });
    }
}