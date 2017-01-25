import {Injectable} from '@angular/core';
import {Http, Headers, Response, RequestOptions, RequestMethod, Request} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {Survey, Question} from '../_models/index'
import 'rxjs/add/operator/map'

@Injectable()
export class SurveyService {

    // host = "http://localhost:9000/";
    host = "https://survey-innoproject.herokuapp.com/";

    constructor(private http: Http) {
    }

    getSurvey(id) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/surveys/' + id,
            headers: headers
        });
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    fillSurvey(id, q) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host + 'app/surveys/' + id + '/answer',
            headers: headers,
            body: JSON.stringify(q)
        });
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    createSurvey(survey: Survey) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host + 'app/surveys',
            headers: headers,
            body: JSON.stringify(survey)
        });

        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    addQuestion(question: Question, id: String) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host + 'app/surveys/' + id + '/add',
            headers: headers,
            body: JSON.stringify(question)
        });

        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    getUserResult(id) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/surveys/' + id + '/user/result',
            headers: headers
        })

        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    getAllResult(id) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/surveys/' + id + '/admin/result',
            headers: headers
        })

        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    getUserSurveys() {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/surveys/result/UserList',
            headers: headers
        })
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    getAdminSurveys() {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/surveys/result/AdminList',
            headers: headers
        })
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }

    addMember(member, id){
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");

        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host + 'app/surveys/'+id+'/invitation',
            headers: headers,
            body: JSON.stringify(member)
        })
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }
}