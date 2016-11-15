import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, RequestMethod, Request } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class SurveyService {
	
	constructor(private http: Http) { }
	
	getSurvey(id){
		var headers = new Headers();
			headers.append("Content-Type", "application/json");
			headers.append("Accept", "application/json");
			var options = new RequestOptions({
				method: RequestMethod.Get,
				url: 'http://localhost:9000/app/surveys'+id,
				headers: headers
			});
		return this.http.request(new Request(options))
            .map((response: Response) => {
                let resp = response.json();
								console.log(JSON.stringify(resp));
            });
	}
	
	fillSurvey(id){
		var headers = new Headers();
			headers.append("Content-Type", "application/json");
			headers.append("Accept", "application/json");
			var options = new RequestOptions({
				method: RequestMethod.Get,
				url: 'http://localhost:9000/app/surveys'+id+'/answer',
				headers: headers
			});
		return this.http.request(new Request(options))
            .map((response: Response) => {
                let resp = response.json();
								console.log(JSON.stringify(resp));
            });
	}
}