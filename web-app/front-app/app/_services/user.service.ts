import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestMethod, Request } from '@angular/http';
import { RegistrationUser } from '../_models/index';

@Injectable()
export class UserService {

    host = "http://localhost:9000/";
    // host = "https://survey-innoproject.herokuapp.com/";

    constructor(private http: Http) {}

    getHeader() : Headers{
        let header = new Headers();
        header.append("Content-Type", "application/json");
        return header;
    }

    create(user: RegistrationUser) {
        let options = new RequestOptions({
            method: RequestMethod.Post,
            url: this.host + 'app/user/' + user.login,
            headers: this.getHeader(),
            body: JSON.stringify(user)
        });
        return this.http.request(new Request(options));
    }

    getAllUsernames() {
        let options = new RequestOptions({
            method: RequestMethod.Get,
            url: this.host + 'app/user/all',
            headers: this.getHeader()
        });
        return this.http.request(new Request(options)).map(
            (response: Response) => response.json()
        );
    }

    activate(link: string) {
        let options = new RequestOptions({
            method: RequestMethod.Put,
            url: this.host + 'app/user/activate/' + link,
            headers: this.getHeader(),
            body: JSON.stringify({})
        });
        return this.http.request(new Request(options)).map(
            (response: Response) => response.json()
        );
    }

    getResetQuestion(body) {
        let options = new RequestOptions({
            method: RequestMethod.Put,
            url: this.host + 'app/user/reset-get',
            headers: this.getHeader(),
            body: JSON.stringify(body)
        });
        return this.http.request(new Request(options)).map(
            (response: Response) => response.json()
        );
    }

    sendAnswer(body) {
        let options = new RequestOptions({
            method: RequestMethod.Put,
            url: this.host + 'app/user/reset-code',
            headers: this.getHeader(),
            body: JSON.stringify(body)
        });
        return this.http.request(new Request(options)).map(
            (response: Response) => response.json()
        );
    }

    resetPassword(body) {
        let options = new RequestOptions({
            method: RequestMethod.Put,
            url: this.host + 'app/user/reset-password',
            headers: this.getHeader(),
            body: JSON.stringify(body)
        });
        return this.http.request(new Request(options)).map(
            (response: Response) => response.json()
        );
    }
}