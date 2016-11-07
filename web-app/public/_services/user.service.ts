import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';

@Injectable()
export class UserService {
    constructor(private http: Http) { }

    getAll() {
        return this.http.get('/app/users', this.jwt()).map((response: Response) => response.json());
    }

    getById(login) {
        return this.http.get('/app/users/' + login, this.jwt()).map((response: Response) => response.json());
    }

    create(user) {
        return this.http.post('/app/users/' + user.login, user, this.jwt()).map((response: Response) => response.json());
    }

    update(user) {
        return this.http.put('/app/users/' + user.login, user, this.jwt()).map((response: Response) => response.json());
    }

    delete(login) {
        return this.http.delete('/api/users/' + login, this.jwt()).map((response: Response) => response.json());
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