package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;

/**
 * Controler managing all account featers.
 * 
 * @author Kamil Malinowski
 *
 */
public class AccountController extends Controller {

	@Inject
	MailerClient mailerClient;

	private String host = "https://survey-innoproject.herokuapp.com";

	public Result loginPost() {
		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String login = jsNode.findPath("login").textValue();
		String password = jsNode.findPath("password").textValue();
		if (login == null || password == null) {
			return status(403,
					Json.toJson(new Message("Login or password not set.")));
		}
		UserAccount ua = UserAccount.find.byId(login);
		if (ua == null) {
			return status(403, Json.toJson(new Message("Bad login")));
		}
		if (!ua.checkPassword(password)) {
			return status(403, Json.toJson(new Message("Bad password")));
		}
		UnactivatedAccount UnactivatedAcc = UnactivatedAccount.find.byId(login);
		if (UnactivatedAcc != null) {
			return status(403, Json.toJson(new Message("Account not activated.")));
		}
		session().put("login", login);
		session().put("password", password);
		JsonNode jsUser = Json.toJson(new AccountJson(ua));
		return ok(jsUser);
	}

	public void sendAccEmail(String email, String link, String firstName) {
		Email email1 = new Email();
		email1.setSubject("Rejestracja na surveys");
		email1.setFrom("Surveys <registration@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("Hi " + firstName
				+ "!\n\nOto link aktywacyjny: "+host+"/activ/"
				+ link);
		String id = mailerClient.send(email1);

	}

	public Result userPost(String login) {

		JsonNode registerJson = request().body().asJson();
		if (registerJson == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		if (login.equals("")) {
			return status(403, Json.toJson(new Message("Empty login")));
		}
		if (registerJson.get("password") == null) {
			return status(403, Json.toJson(new Message("Password wanted")));
		}
		if (registerJson.get("rePassword") == null) {
			return status(403, Json.toJson(new Message("rePassword wanted")));
		}
		if (registerJson.get("firstName") == null) {
			return status(403, Json.toJson(new Message("firstName wanted")));
		}
		if (registerJson.get("lastName") == null) {
			return status(403, Json.toJson(new Message("lastName wanted")));
		}
		if (registerJson.get("email") == null) {
			return status(403, Json.toJson(new Message("email wanted")));
		}

		String password = registerJson.get("password").asText();
		String rePassword = registerJson.get("rePassword").asText();
		String firstName = registerJson.get("firstName").asText();
		String lastName = registerJson.get("lastName").asText();
		String email = registerJson.get("email").asText();

		if (password == null || password.equals("")) {
			return status(403, Json.toJson(new Message("Password wanted")));
		}

		if (rePassword == null || rePassword.equals("")) {
			return status(403, Json.toJson(new Message("rePassword wanted")));
		}

		if (firstName == null || firstName.equals("")) {
			return status(403, Json.toJson(new Message("First name wanted")));
		}

		if (lastName == null || lastName.equals("")) {
			return status(403, Json.toJson(new Message("Last name wanted")));
		}

		if (email == null || email.equals("")) {
			return status(403, Json.toJson(new Message("Email wanted")));
		}

		@SuppressWarnings("rawtypes")
		UserAccount user = UserAccount.find.byId(login);
		List<UserAccount> userEm = UserAccount.find.where().eq("email", email)
				.findList();
		if (userEm.size() != 0) {
			return status(403, Json.toJson(new Message(
					"User with this e-mail already exists!")));
		}
		if (user != null) {
			return status(403, Json.toJson(new Message(
					"User with this login already exists!")));
		}
		if (!password.equals(rePassword)) {
			return status(403,
					Json.toJson(new Message("Passwords don't match!")));
		}
		String link = Integer.toHexString(new Random().nextInt(0x1000000));
		List<UnactivatedAccount> unactiv = UnactivatedAccount.find.all();
		if (unactiv != null) {
			for (int i = 0; i < unactiv.size(); i++) {
				if (unactiv.get(i).activationLink.equals(link)) {
					i = 0;
					link = Integer.toHexString(new Random().nextInt(0x1000000));
				}
			}
		}
		UnactivatedAccount newAcc = new UnactivatedAccount(login, password,
				firstName, lastName, email, link);

		newAcc.save();
		sendAccEmail(email, link, firstName);
		return ok(Json.toJson(new Message("Registrated")));
	}

	public Result invite() {

		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, Json.toJson(new Message("JSON wanted!")));
		}
		String email = jsNode.findPath("email").textValue();

		if (email.equals("")) {
			return status(403, Json.toJson(new Message("Empty email")));
		}

		List<UserAccount> userEm = UserAccount.find.where().eq("email", email)
				.findList();
		if (userEm.size() != 0) {
			return status(403, Json.toJson(new Message(
					"User with this e-mail already exists!")));
		}

		Email email1 = new Email();
		email1.setSubject("Rejestracja na surveys");
		email1.setFrom("Surveys <registration@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("Zostałeś zaproszony do http://localhost:3000/register . Kliknij link i zarejestruj swoje konto");
		String id = mailerClient.send(email1);

		if (id != null) {

			return status(200, Json.toJson(new Message("Invitation sended!")));
		}

		session("email", email);

		return ok(Json.toJson(new Message("Poprawnie wyslano zaproszenie")));

	}

	public Result activate(String link) {
		 List<UnactivatedAccount> ua = UnactivatedAccount.find.all();
		 for (UnactivatedAccount a : ua) {
		  if(a.activationLink == link){
			 a.delete();
			 return ok(Json.toJson(new Message("Konto aktywowane!")));
		  	}
		  }
			return status(404, Json.toJson(new Message("Zły link aktywacyjny!")));

	}

	public Result clean() {
		List<UnactivatedAccount> l = UnactivatedAccount.find.all();
		for (UnactivatedAccount a : l) {
			a.delete();
		}
		List<UserAccount> l1 = UserAccount.find.all();
		for (UserAccount a : l1) {
			a.delete();
		}

		return ok();
	}

	public Result getAll(){
		List<String> allUsersLogins = getUsersLogins(UserAccount.find.all());
		return ok(Json.toJson(allUsersLogins));
	}

	private List<String> getUsersLogins(List<UserAccount> allUsers){
		List<String> allUsersLogin = new ArrayList<>();
		for(UserAccount user : allUsers){
			allUsersLogin.add(user.login);
		}
		return allUsersLogin;
	}
}

class Message {
	public String message;

	public Message(String msg) {
		message = msg;
	}
}

class AccountJson {
	public String login;
	public String email;
	public String firstName;
	public String lastName;

	public AccountJson(UserAccount ua) {
		login = ua.login;
		email = ua.email;
		firstName = ua.firstName;
		lastName = ua.lastName;
	}
}