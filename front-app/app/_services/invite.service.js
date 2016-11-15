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
var InviteService = (function () {
    function InviteService(http) {
        this.http = http;
    }
    InviteService.prototype.invite = function (email) {
        var headers = new http_1.Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new http_1.RequestOptions({
            method: http_1.RequestMethod.Post,
            url: 'http://localhost:9000/app/invitation',
            headers: headers,
            body: JSON.stringify({ email: email })
        });
        return this.http.request(new http_1.Request(options))
            .map(function (response) {
            var resp = response.json();
            console.log(JSON.stringify(resp));
        });
    };
    InviteService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], InviteService);
    return InviteService;
}());
exports.InviteService = InviteService;
//# sourceMappingURL=invite.service.js.map