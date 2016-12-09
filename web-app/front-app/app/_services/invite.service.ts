import {Injectable} from '@angular/core';
import {Http, Headers, Response, RequestOptions, RequestMethod, Request} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class InviteService {

    host = "http://localhost:9000/";
    //host = "https://survey-innoproject.herokuapp.com/";

    constructor(private http: Http) {
    }

    invite(email) {
        var headers = new Headers();
        headers.append("Content-Type", "application/json");
        headers.append("Accept", "application/json");
        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host+'app/invitation',
            headers: headers,
            body: JSON.stringify({email: email})
        });
        return this.http.request(new Request(options))
            .map((response: Response) => response.json());
    }
}