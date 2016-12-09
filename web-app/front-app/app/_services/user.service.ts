import {Injectable} from '@angular/core';
import {Http, Headers, Response, RequestOptions, RequestMethod, Request} from '@angular/http';
import {RegistrationUser} from '../_models/index';

@Injectable()
export class UserService {

    host = "http://localhost:9000/"

    constructor(private http: Http) {
    }
    create(user: RegistrationUser) {
        var header = new Headers();
        header.append("Content-Type", "application/json");
        var options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host+'app/user/' + user.login,
            headers: header,
            body: JSON.stringify(user),
        })
        return this.http.request(new Request(options));
    }
}