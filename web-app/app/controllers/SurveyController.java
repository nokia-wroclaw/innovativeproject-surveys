package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fasterxml.jackson.databind.JsonNode;

import models.Question;
import models.Response;
import models.Survey;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;

public class SurveyController extends Controller {
	

	  
	@Inject MailerClient mailerClient;
	
	// tworzy ankiete i wysyla na podany adres
	public Result SurveyPut(){
		
		JsonNode survey = request().body().asJson();
		if(survey == null) {
			return status(403, "JSON wanted!");
		}
		
		 String name = survey.get("name").asText();
	     String description = survey.get("description").asText();
	     String email = survey.get("email").asText();
	     
	     String question = survey.get("question").asText();
	     String question1 = survey.get("question1").asText();
	     String question2 = survey.get("question2").asText();
	     String question3 = survey.get("question3").asText();
	
	    Survey obiekt = new Survey(name, description, email);
	    
	    obiekt.setIp("");
	    
	    obiekt.save();
		Question q = new Question(question);
		Question q1 = new Question(question1);
		Question q2 = new Question(question2);
		Question q3 = new Question(question3);
		

		q.survey = obiekt;
		q1.survey = obiekt;
		q2.survey = obiekt;
		q3.survey = obiekt;
		
		q.save();
		
		q1.save();
		q2.save();
		q3.save();
		
		
     	obiekt.update();
     	
     	
     	Email email1 = new Email();
		email1.setSubject("Stworzona ankieta");
		email1.setFrom("Surveys <from@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("Została stworzona ankieta na http://localhost:9000/app/surveys/" + obiekt.id + " . " );
		String id =  mailerClient.send(email1);
			
		
		if(id != null){
			
			return status(403, "good");
		}
		
		
		
		return ok("Została stworzona ankieta na http://localhost:9000/app/surveys/" + obiekt.id );
	}
	
	// uzupelnianie ankiety po ip
	public Result fillSurvey(Integer id) {

		JsonNode survey = request().body().asJson();
		//if(survey == null) {
		//	return status(403, "JSON wanted!");
		//}
		

		String ip = request().remoteAddress();
		
		
		List<Response> findIp = Response.find
		        .where()
		        .eq("ip", ip)
		        .eq("survey_id", id)
		        .findList();

		if(findIp.isEmpty()){ // jesli to ip nie wypelnilo jeszcze ankiety 
		
			Survey obiekt = Survey.find.byId(id);
			
			// test bez front-endu
			
			String ans = "odp 1";
	        String ans1 = "odp 2";
	        String ans2 = "odp 3";
	        String ans3 = "odp 4";
	        
	       /* 
	        String ans = survey.get("question").asText();
		     String ans1 = survey.get("question1").asText();
		     String ans2 = survey.get("question2").asText();
		     String ans3 = survey.get("question3").asText(); */
     
			List<Question> items = Question.find
			        .where()
			        .eq("survey_id", id)
			        .findList();
			
			
			 String a = items.get(0).text;
			 Response res1 = new Response(a, ans, ip);
			 res1.save();
			 res1.question = items.get(0);
			 res1.survey = obiekt;
			 res1.update();
			 
			 String b = items.get(1).text;
			 Response res2 = new Response(b, ans1, ip);
			 res2.save();
			 res2.question = items.get(1);
			 res2.survey = obiekt;
			 res2.update();
			 
			 String c = items.get(2).text;
			 Response res3 = new Response(c, ans2, ip);
			 res3.save();
			 res3.question = items.get(2);
			 res3.survey = obiekt;
			 res3.update();
			 
			 String da = items.get(3).text;
			 Response res4 = new Response(da, ans3, ip);
			 res4.save(); 
			 res4.question = items.get(3);
			 res4.survey = obiekt;
			 res4.update();
		}else{ // tutaj jest modyfikacja odp z juz istniejacego ip w bazie dla danej ankiety
	
		
			String ans = "Zmodyfikowana odp inna 1";
	        String ans1 = "Zmodyfikowana odp inna 2";
	        String ans2 = "Zmodyfikowana odp inna 3";
	        String ans3 = "Zmodyfikowana odp inna 4";
	        
	        /* 
	        String ans = survey.get("question").asText();
		     String ans1 = survey.get("question1").asText();
		     String ans2 = survey.get("question2").asText();
		     String ans3 = survey.get("question3").asText(); */
			
	        findIp.get(0).answer = ans;
	        findIp.get(0).update();
	        findIp.get(1).answer = ans1;
	        findIp.get(1).update();
	        findIp.get(2).answer = ans2;
	        findIp.get(2).update();
	        findIp.get(3).answer = ans3;
	        findIp.get(3).update(); 
		}
	
		return ok("Ankieta wypelniona sprawdz: [...]/app/surveys/id");	
		
	}
	
      // wyniki dla danej id ankiety i ip requesta
     public Result resul(Integer id)  {
    	 
    	 String ip = request().remoteAddress();
    	 
    	 // pobieram wyniki dla danego ip i danego id ankiety
    	 
 		List<Response> items = Response.find
 		        .where()
 		        .eq("survey_id", id)
 		        .eq("ip", ip)
 		        .findList();
 		
 		
 		if(items.isEmpty()){
 			
 			return ok("Ankieta nie jest wypelniona. Link do wypelnienia: [...]/app/surveys/id/answer");
 		}
 		
 		
 			//obiekt.save();
 			return ok("Wyniki ankiety \n" +   "Pytanie: " + items.get(0).text + " Odpowiedź: " + items.get(0).answer + "\n"
 					+   "Pytanie: " + items.get(1).text + " Odpowiedź: " + items.get(1).answer + "\n"
 					+   "Pytanie: " + items.get(2).text + " Odpowiedź: " + items.get(2).answer + "\n"
 					+   "Pytanie: " + items.get(3).text + " Odpowiedź: " + items.get(3).answer + "\n");
 		
  
    	 
     }
	
	
}
