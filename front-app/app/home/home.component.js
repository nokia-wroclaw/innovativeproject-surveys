"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var router_1 = require('@angular/router');
var index_1 = require('../_services/index');
var index_2 = require('../_services/index');
var HomeComponent = (function () {
    function HomeComponent(userService, inviteService, alertService, router) {
        this.userService = userService;
        this.inviteService = inviteService;
        this.alertService = alertService;
        this.router = router;
        this.sendingInvite = false;
        this.model = {};
        this.surv = {};
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }
    HomeComponent.prototype.ngOnInit = function () {
    };
    HomeComponent.prototype.invite = function () {
        var _this = this;
        this.sendingInvite = true;
        console.log("Sending invitation!");
        this.inviteService.invite(this.model.email)
            .subscribe(function (data) {
            console.log("Success");
            _this.alertService.success('Invite successful', true);
            _this.sendingInvite = false;
        }, function (error) {
            console.log("Fail");
            console.log(error);
            _this.alertService.error(error);
            _this.sendingInvite = false;
        });
    };
    HomeComponent.prototype.surveyGo = function () {
        console.log("here");
        this.router.navigate(['/surveyView', this.surv.id]);
    };
    HomeComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            templateUrl: 'home.component.html'
        }), 
        __metadata('design:paramtypes', [index_1.UserService, index_2.InviteService, index_1.AlertService, router_1.Router])
    ], HomeComponent);
    return HomeComponent;
}());
exports.HomeComponent = HomeComponent;
//# sourceMappingURL=home.component.js.map