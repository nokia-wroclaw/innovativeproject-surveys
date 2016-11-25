package controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import models.Question;
import models.Response;
import models.Survey;
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
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		UserAccount ua = UserAccount.find.byId(login);

		Survey survey = new Survey(name, description, email, login);
		survey.userAccount = ua;
		survey.save();
		JsonNode surveyJs = Json.toJson(new SurveyJson(survey));

		Email email1 = new Email();
		email1.setSubject("Stworzona ankieta");
		email1.setFrom("Surveys <from@surveys.com>");
		email1.addTo(email);
		email1.setBodyText(
				"Została stworzona ankieta na http://localhost:9000/app/surveys/" + survey.id + "/answer . ");
		String id = mailerClient.send(email1);

		if (id == null) {
			status(404, Json.toJson(new Message("Problem with send Email")));
		}
		return ok(surveyJs);
	}

	/**
	 * The method will add one question to Survey
	 * 
	 * @return The question, which has been added...
	 */
	public Result addQuestion(Integer id) {

		JsonNode surveyJson = request().body().asJson();
		if (surveyJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		// It gives the user who is logged on
		String login = session().get("login");

		if (login == null) {
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		List<Survey> survey1 = Survey.find.select("*").where().eq("user_account_login", login).eq("id", id).findList();

		if (!survey1.get(0).userAccount.login.equals(login)) {
			status(404, Json.toJson(new Message("Dont have permission")));
		}
		Survey survey = Survey.find.byId(id);

		String quest = surveyJson.get("question").asText();
		Question question = new Question(quest);
		question.survey = survey;
		question.save();
		status(200, Json.toJson(new Message("Added question")));

		JsonNode questionJs = Json.toJson(question);

		return ok(questionJs);
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
			status(404, Json.toJson(new Message("You arent logged in")));
		}
		Survey survey = Survey.find.byId(id);
		UserAccount userAccount = UserAccount.find.byId(login);

		List<Question> allquestions = Question.find.select("*").where().eq("survey_id", id).findList();

		String answer;

		for (int i = 0; i < allquestions.size(); i++) {
			answer = surveyJson.get("answer" + i).asText();
			Response repsponse1 = new Response(answer);
			repsponse1.survey = survey;
			repsponse1.question = allquestions.get(i);
			repsponse1.userAccount = userAccount;
		}
		List<Response> allResponse = Response.find.select("*").where().eq("survey_id", id)
				.eq("user_account_login", login).findList();

		JsonNode ResponseJs = Json.toJson(allResponse);
		// This will send recently added question
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

	public SurveyJson(Survey s) {
		id = s.id;
		name = s.name;
		description = s.description;
		email = s.email;
	}
}
