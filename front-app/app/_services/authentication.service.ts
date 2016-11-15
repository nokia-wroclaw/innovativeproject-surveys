import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestMethod, Request } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class AuthenticationService {
    constructor(private http: Http) { }
    login(username, password) {
			var headers = new Headers();
			headers.append("Content-Type", "application/json");
			headers.append("Accept", "application/json");
			var options = new RequestOptions({
				method: RequestMethod.Post,
				url: 'http://localhost:9000/app/login',
				headers: headers,
				body: JSON.stringify({ login: username, password: password })
			});
      return this.http.request(new Request(options))
<<<<<<< HEAD
	  
	}  
	
=======
            .map((response: Response) => {
                let user = response.json();
								console.log(JSON.stringify(user));
                if (user /*&& user.token*/) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
            });
    }

>>>>>>> front-app/home-page
    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }
}