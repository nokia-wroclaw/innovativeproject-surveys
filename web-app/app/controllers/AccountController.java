package controllers;


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

/**Controler managing all account featers.
 * 
 * @author Kamil Malinowski
 *
 */
public class AccountController extends Controller {
	
	@Inject MailerClient mailerClient;
	
	public Result loginPost(){
		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, "JSON wanted!");
		}
		String login = jsNode.findPath("login").textValue();
		String password = jsNode.findPath("password").textValue();
		if (login == null || password == null) {
			return status(403, "Login or password not set.");
		}
		UserAccount ua = UserAccount.find.byId(login);
		if (ua == null) {
			return status(403, "Bad login");
		}
		if (!ua.checkPassword(password)) {
			return status(403, "Bad password");
		}
		session().put("login", login);
		JsonNode jsUser = Json.toJson(ua);
		return ok(jsUser);
	}
	
	public void sendAccEmail (String email, String link, String firstName) {
		Email email1 = new Email();
		email1.setSubject("Rejestracja na surveys");
		email1.setFrom("Surveys <registration@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("Hi "+firstName+"!\n\nOto link aktywacyjny: http://localhost:9000/activ/"+link);
		String id = mailerClient.send(email1);
		
		
		
	}
	
	public Result userPut(String login) {
		
		JsonNode registerJson = request().body().asJson();
		if(registerJson == null) {
			return status(403, "JSON wanted!");
		}
		if (login.equals("")){
			return status(403, "Empty login");
		}
		if(registerJson.get("password") == null){
			return status(403, "Password wanted");
		}
		if(registerJson.get("rePassword") == null){
			return status(403, "Password wanted");
		}
		if(registerJson.get("firstName") == null){
			return status(403, "Password wanted");
		}
		if(registerJson.get("lastName") == null){
			return status(403, "Password wanted");
		}
		if(registerJson.get("email") == null){
			return status(403, "Password wanted");
		}
		
		String password = registerJson.get("password").asText();
		String rePassword = registerJson.get("rePassword").asText();
		String firstName = registerJson.get("firstName").asText();
		String lastName = registerJson.get("lastName").asText();
		String email = registerJson.get("email").asText();
		
		if(password == null || password.equals("")){
			return status(403, "Password wanted");
		}
		
		if(rePassword == null || rePassword.equals("")){
			return status(403, "Password wanted");
		}
		
		if(firstName == null || firstName.equals("")){
			return status(403, "First name wanted");
		}
		
		if(lastName == null || lastName.equals("")){
			return status(403, "Last name wanted");
		}
		
		if(email == null || email.equals("")){
			return status(403, "Email wanted");
		}
		
		@SuppressWarnings("rawtypes")
		UserAccount user = UserAccount.find.byId(login);
		List<UserAccount> userEm = UserAccount.find.where().eq("email", email).findList();
		if(userEm.size() != 0){
			return status(404, "Konto o takim e-mail już istnieje!");
		}
		if(user != null) {
			return status(404, "Ten login już istnieje!");
		}
		if(!password.equals(rePassword)) {
			return status(404, "Hasła do siebie nie pasują!");
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
		UnactivatedAccount newAcc = new UnactivatedAccount(login, password, firstName,
				lastName, email, link);
		
		newAcc.save();
		sendAccEmail(email, link, firstName);
		return ok("Registrated");
	}
	
	public Result invite(){
		
		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, "JSON wanted!");
		}
		String email = jsNode.findPath("email").textValue();
			
		
		if (email.equals("")) {
			return status(403, "empty");
		}
		
		
		Email email1 = new Email();
		email1.setSubject("Rejestracja na surveys");
		email1.setFrom("Surveys <registration@surveys.com>");
		email1.addTo(email);
		email1.setBodyText("Zostałeś zaproszony do http://localhost:9000/register . Kliknij link i zarejestruj swoje konto");
		String id = mailerClient.send(email1);
		
		if(id != null){
			
			return status(200, Json.toJson(new Message("Invitation sended!")));
		}
		
	
		
		session("email", email);

		return ok(("Poprawnie wyslano zaproszenie"));
		
	}

	
	
	public Result activate (String link) {
		UnactivatedAccount ua = UnactivatedAccount.find.byId(link);
		if (ua == null) {
			return ok(activate.render("Zły link aktywacyjny!"));
		} else {
			ua.delete();
			return ok(activate.render("Konto aktywowane!"));
		}
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
		
		return ok(/*register.render("")*/);
	}
}

class Message{
	public String message;
	public Message(String msg) {
		message = msg;
}
}