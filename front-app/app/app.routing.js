"use strict";
var router_1 = require('@angular/router');
var index_1 = require('./home/index');
var index_2 = require('./login/index');
var index_3 = require('./register/index');
var index_4 = require('./_guards/index');
var index_5 = require('./surveyView/index');
var index_6 = require('./survey-creation/index');
var appRoutes = [
    { path: '', component: index_1.HomeComponent, canActivate: [index_4.AuthGuard] },
    { path: 'login', component: index_2.LoginComponent },
    { path: 'register', component: index_3.RegisterComponent },
    { path: 'surveyView/:id', component: index_5.SurveyViewComponent },
    { path: 'create-survey', component: index_6.SurveyCreationComponent },
    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];
exports.routing = router_1.RouterModule.forRoot(appRoutes);
//# sourceMappingURL=app.routing.js.map