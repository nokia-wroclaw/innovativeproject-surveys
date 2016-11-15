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
var http_1 = require('@angular/http');
require('rxjs/add/operator/map');
var SurveyService = (function () {
    function SurveyService(http) {
        this.http = http;
    }
    SurveyService.prototype.getSurvey = function (id) {
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new http_1.RequestOptions({
            method: http_1.RequestMethod.Get,
            url: 'http://localhost:9000/app/surveys' + id,
            headers: headers
        });
        return this.http.request(new http_1.Request(options))
            .map(function (response) {
            var resp = response.json();
            console.log(JSON.stringify(resp));
        });
    };
    SurveyService.prototype.fillSurvey = function (id) {
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new http_1.RequestOptions({
            method: http_1.RequestMethod.Get,
            url: 'http://localhost:9000/app/surveys' + id + '/answer',
            headers: headers
        });
        return this.http.request(new http_1.Request(options))
            .map(function (response) {
            var resp = response.json();
            console.log(JSON.stringify(resp));
        });
    };
    SurveyService.prototype.createSurvey = function (survey) {
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new http_1.RequestOptions({
            method: http_1.RequestMethod.Post,
            url: 'http://localhost:9000/app/surveys',
            headers: headers,
            body: JSON.stringify(survey)
        });
        return this.http.request(new http_1.Request(options))
            .map(function (response) {
            var resp = response.json();
            console.log(JSON.stringify(resp));
        });
    };
    SurveyService.prototype.getResult = function (id) {
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new http_1.RequestOptions({
            method: http_1.RequestMethod.Get,
            url: 'http://localhost:9000/app/surveys/' + id + '/result',
            headers: headers
        });
        return this.http.request(new http_1.Request(options))
            .map(function (response) {
            var resp = response.json();
            console.log(JSON.stringify(resp));
        });
    };
    SurveyService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], SurveyService);
    return SurveyService;
}());
exports.SurveyService = SurveyService;
//# sourceMappingURL=survey.service.js.map