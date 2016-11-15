import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestMethod, Request } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class InviteService {
    constructor(private http: Http) { }
    invite(email) {
			var headers = new Headers();
			headers.append("Content-Type", "application/json");
			//headers.append("Accept", "application/json");
			var options = new RequestOptions({
				method: RequestMethod.Post,
				url: 'http://localhost:9000/app/invitation',
				headers: headers,
				body: JSON.stringify({ email: email })
			});
      return this.http.request(new Request(options))
            .map((response: Response) => {
                let resp = response.json();
								console.log(JSON.stringify(resp));
            });
    }
}