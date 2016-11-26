package controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Question;
import models.Response;
import models.Survey;
import models.SurveyMember;
import models.UserAccount;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;


public class SurveyController extends Controller {

	@Inject
	MailerClient mailerClient;
	
	/**
	 * The method create Survey
	 * 
	 * @return Created Survey
	 */
	public Result SurveyPut() {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String name = surveyJson.get("name").asText();
		String description = surveyJson.get("description").asText();
		String email = surveyJson.get("email").asText();
		String login = session().get("login");

		if (login == null) {
			return status(404, Json.toJson(new Message("You arent logged in")));
		}
		UserAccount ua = UserAccount.find.byId(login);
		
		Survey survey = new Survey(name, description, email);
		survey.adminLogin = ua.login;
		survey.save();

		ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
		for(JsonNode x : allquestion){
			String quest = x.get("question").asText(); 
			Question question = new Question(quest);
			question.survey = survey;
			question.save();
		}	
		
		List<Question> allquestions = Question.find.select("*").where().eq("survey_id", survey.id).findList();
		Question arrayquest[] = new Question[allquestions.size()];
		arrayquest = allquestions.toArray(arrayquest);
		JsonNode surveyJs = Json.toJson(new SurveyJson(survey, arrayquest));
		
		Email email1 = new Email();
		email1.setSubject("Created Survey " + name);
		email1.setFrom("Surveys <from@surveys.com>");
		email1.addTo(email);
		email1.setBodyText(
				"Survey was created on http://localhost:9000/app/surveys/" + survey.id + "/answer . ");
		String id = mailerClient.send(email1);

		if (id == null) {
			return status(404, Json.toJson(new Message("Problem with send Email")));
		}
		return ok(surveyJs);
	}
	
	/**
	 * The method create Survey
	 * 
	 * @return Created Survey
	 */
	public Result SurveyModification(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		
		Survey findSurvey = Survey.find.byId(id);
		
		if(findSurvey == null){
			return status(403, Json.toJson(new Message("Survey doesn't exist.")));
		}
		
		String name = surveyJson.get("name").asText();
		String description = surveyJson.get("description").asText();
		String email = surveyJson.get("email").asText();
		String login = session().get("login");

		if (login == null) {
			return status(404, Json.toJson(new Message("You arent logged in")));
		}
		
		UserAccount ua = UserAccount.find.byId(login);	
		
		if(findSurvey.adminLogin != login){
			return status(403, Json.toJson(new Message("You dont have permission.")));
		}
		Survey survey = Survey.find.byId(id);
		List<Question> allquestions = Question.find.select("*").where().eq("survey_id", survey.id).findList();
		
		int i = 0;
		ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
		for(JsonNode x : allquestion){
			String quest = x.get("question").asText(); 
			allquestions.get(i).question = quest;
			allquestions.get(i).update();	
			i += 1;
		}	
		
		List<Question> allquestions1 = Question.find.select("*").where().eq("survey_id", survey.id).findList();
		Question arrayquest[] = new Question[allquestions.size()];
		arrayquest = allquestions.toArray(arrayquest);
		JsonNode surveyJs = Json.toJson(new SurveyJson(survey, arrayquest));
				
		return ok(surveyJs);
	}
	
	public Result SurveyInvite(Integer id){
		
		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, "JSON wanted!");
		}
		String email = jsNode.findPath("email").textValue();
					
		if (email.equals("")) {
			return status(403, "empty");
		}
		Email email1 = new Email();
		email1.setSubject("The invitation to the survey");
		email1.setFrom("Surveys <from@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("You are invited to participate in the survey"
				+ " http://localhost:9000/app/surveys/" + id + "/answer . ");
		String ret = mailerClient.send(email1);
				
		List<UserAccount> ac = UserAccount.find.select("*").where().eq("email", email).findList();
		
		if(ac.isEmpty()){
			return status(403, Json.toJson(new Message("Not found account!")));
		}		
		if(ret == null){			
			return status(403, Json.toJson(new Message("Problem with send Email")));
		}		
		Survey survey = Survey.find.byId(id);
		SurveyMember sm = new SurveyMember(ac.get(0).login);
		sm.survey = survey;
		sm.save();
		
		return ok(("Correctly sent invitation"));
	}

	/**
	 * The method will fill all Survey answer
	 * 
	 * @return All responses from logged user
	 */
	public Result fillAnswer(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = session().get("login");

		if (login == null) {
			return status(404, Json.toJson(new Message("You arent logged in")));
		}
		Survey survey = Survey.find.byId(id);
		UserAccount userAccount = UserAccount.find.byId(login);

		List<Question> allquestions = Question.find.select("*").where().eq("survey_id", id).findList();

		String answer;
		ArrayNode allanswer = (ArrayNode) surveyJson.withArray("Answers");
		
		int i = 0; 
		for(JsonNode x : allanswer){
			answer = surveyJson.get("answer").asText();
			Response repsponse1 = new Response(answer);
			repsponse1.survey = survey;
			repsponse1.question = allquestions.get(i);
			repsponse1.userAccount = userAccount;
			repsponse1.save();
			i += 1;
		}	
		/*
		for (int i = 0; i < allquestions.size(); i++) {
			answer = surveyJson.get("answer" + i).asText();
			Response repsponse1 = new Response(answer);
			repsponse1.survey = survey;
			repsponse1.question = allquestions.get(i);
			repsponse1.userAccount = userAccount;
			repsponse1.save();
		}*/
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);
		
		return ok(ResponseJs);
	}

	/**
	 * The method return all answers from logged user from selected survey by id
	 * 
	 * @return All responses from logged user
	 */
	public Result getUserResult(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = session().get("login");

		if (login == null) {
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);

		return ok(ResponseJs);
	}

	/**
	 * The method return all answers to Admin user from selected survey by id
	 * 
	 * @return All responses from  users from selected survey
	 */
	public Result getAdminResult(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = session().get("login");

		if (login == null) {
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);

		return ok(ResponseJs);
	}

	
	/**
	 * The method return the surveys list which logged user takes part in
	 * 
	 * @return All userSurveys list from logged user
	 */
	public Result getUserSurveysId(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = session().get("login");

		if (login == null) {
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);

		return ok(ResponseJs);
	}

	
	/**
	 * The method return the surveys list which Admin user created
	 * 
	 * @return All userSurveys list from Admin user
	 */
	public Result getAdminSurveysId(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = session().get("login");

		if (login == null) {
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);

		return ok(ResponseJs);
	}

	class Message {
		public String message;

		public Message(String msg) {
			message = msg;
		}
	}
}

class SurveyJson {
	public Integer id;

	public String name;
	public String description;
	public String email;
	public Question q[];

	public SurveyJson(Survey s, Question q[]) {
		id = s.id;
		name = s.name;
		description = s.description;
		email = s.email;
		this.q = q.clone();
	}
}

