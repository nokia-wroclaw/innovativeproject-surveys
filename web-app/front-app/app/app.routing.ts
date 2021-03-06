import {Routes, RouterModule} from '@angular/router';

import { HomeComponent } from './home/index';
import { LoginComponent } from './login/index';
import { RegisterComponent } from './register/index';
import { AuthGuard } from './_guards/index';
import { SurveyViewComponent } from './surveyView/index';
import { SurveyCreationComponent } from './survey-creation/index';
import { InfoComponent } from './info/index'
import { InviteComponent } from './invite/index'
import { SurveyResultComponent } from './survey-result/index'
import { AccountActivationComponent } from './account-activation/index'
import {ResetPasswordComponent} from "./reset-password/reset-password.component";

const appRoutes: Routes = [
    { path: 'home',             component: HomeComponent,               canActivate: [AuthGuard]    },
    { path: 'login',            component: LoginComponent                                           },
    { path: 'register',         component: RegisterComponent                                        },
    { path: 'info',             component: InfoComponent                                            },
    { path: 'invite',           component: InviteComponent                                          },
	{ path: 'surveyView/:id',   component: SurveyViewComponent,         canActivate: [AuthGuard]    },
	{ path: 'surveyCreate',     component: SurveyCreationComponent,     canActivate: [AuthGuard]    },
    { path: 'surveyResult/:id', component: SurveyResultComponent,                                   },
    { path: 'activ/:link',      component: AccountActivationComponent                               },
    {path: 'reset-password',    component: ResetPasswordComponent},
    // otherwise redirect to home
    {path: '**', redirectTo: 'home'}
];

export const routing = RouterModule.forRoot(appRoutes);