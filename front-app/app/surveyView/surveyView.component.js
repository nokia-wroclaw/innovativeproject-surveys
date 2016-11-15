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
var SurveyViewComponent = (function () {
    function SurveyViewComponent(route, router, service, alertService) {
        this.route = route;
        this.router = router;
        this.service = service;
        this.alertService = alertService;
        this.model = {};
        this.loading = false;
    }
    SurveyViewComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.id = +this.route.snapshot.params['id'];
        this.service.getSurvey(this.id)
            .subscribe(function (survey) { return _this.survey = survey; }, function (error) {
            _this.alertService.error(error);
        });
    };
    SurveyViewComponent.prototype.answer = function () {
        var _this = this;
        this.loading = true;
        this.service.fillSurvey(this.id)
            .subscribe(function (data) {
            console.log(JSON.stringify(data));
            _this.alertService.success('Survey filled', true);
            _this.router.navigate(['/']);
        }, function (error) {
            console.log(JSON.stringify(error));
            _this.alertService.error(error);
            _this.loading = false;
        });
    };
    SurveyViewComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            templateUrl: 'surveyView.component.html'
        }), 
        __metadata('design:paramtypes', [router_1.ActivatedRoute, router_1.Router, index_1.SurveyService, index_1.AlertService])
    ], SurveyViewComponent);
    return SurveyViewComponent;
}());
exports.SurveyViewComponent = SurveyViewComponent;
//# sourceMappingURL=surveyView.component.js.map