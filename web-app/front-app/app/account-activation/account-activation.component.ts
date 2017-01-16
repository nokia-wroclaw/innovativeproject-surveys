import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService, AlertService } from '../_services/index';

@Component({
    template: ""
})

export class AccountActivationComponent implements OnInit{
    constructor(private activatedRoute: ActivatedRoute,
                private router: Router,
                private userService: UserService,
                private  alertService: AlertService){}

    ngOnInit(){
        this.activatedRoute.params.subscribe(params => {
            let link = params['link'];
            this.userService.activate(link).subscribe(
                response => {
                    this.router.navigate(['/']);
                    this.alertService.success(response.message, true);
                },
                error => {
                    error = error.json();
                    this.router.navigate(['/']);
                    this.alertService.error(error.message, true);
                }
            )
        })
    }


}
