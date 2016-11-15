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
var SurveyCreationComponent = (function () {
    function SurveyCreationComponent(router, surveyService, alertService) {
        this.router = router;
        this.surveyService = surveyService;
        this.alertService = alertService;
        this.loading = false;
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.model = {
            name: "",
            description: "",
            email: this.currentUser.email,
            question: "",
            question1: "",
            question2: "",
            question3: ""
        };
    }
    SurveyCreationComponent.prototype.create = function () {
        var _this = this;
        this.surveyService.createSurvey(this.model)
            .subscribe(function (data) {
            _this.alertService.success("Survey created successful! E-mail with link to your survey was sended.", true);
        }, function (error) {
            console.log(JSON.stringify(error));
            _this.alertService.error(error);
        });
    };
    SurveyCreationComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            templateUrl: 'survey-creation.component.html'
        }), 
        __metadata('design:paramtypes', [router_1.Router, index_1.SurveyService, index_1.AlertService])
    ], SurveyCreationComponent);
    return SurveyCreationComponent;
}());
exports.SurveyCreationComponent = SurveyCreationComponent;
//# sourceMappingURL=survey-creation.component.js.map