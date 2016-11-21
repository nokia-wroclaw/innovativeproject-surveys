import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestMethod, Request } from '@angular/http';
import { RegistrationUser } from '../_models/index';

@Injectable()
export class UserService {
    constructor(private http: Http) { }
	
    getAll() {
        return this.http.get('https://survey-innoproject.herokuapp.com/app/users', this.jwt()).map((response: Response) => response.json());
    }

    getById(id) {
        return this.http.get('https://survey-innoproject.herokuapp.com/app/users/' + id, this.jwt()).map((response: Response) => response.json());
    }

    create(user: RegistrationUser) {
		var header = new Headers();
		header.append("Content-Type", "application/json");
		var options = new RequestOptions({
			method: RequestMethod.Put,
			url: 'http://localhost:9000/app/user/'+user.login,
			headers: header,
			body: JSON.stringify(user),
		})
        return this.http.request(new Request(options));
    }

    update(user) {
        return this.http.put('https://survey-innoproject.herokuapp.com/app/users/' + user.login, user, this.jwt()).map((response: Response) => response.json());
    }

    delete(id) {
        return this.http.delete('https://survey-innoproject.herokuapp.com/app/users/' + id, this.jwt()).map((response: Response) => response.json());
    }

    // private helper methods

    private jwt() {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.token) {
            let headers = new Headers({ 'Authorization': 'Bearer ' + currentUser.token });
            return new RequestOptions({ headers: headers });
        }
    }
}