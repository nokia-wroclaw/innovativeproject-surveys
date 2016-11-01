package controllers;


import java.util.List;
import java.util.Random;

import models.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

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
	
	public Result loginGet(){
		return ok(login.render());
	}
	public Result registerGet(){
		return ok(/*register.render("")*/);
	}
	
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
		session("login", login);
		return ok("Zalogowano");
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
		String password = registerJson.get("login").asText();
		String rePassword = registerJson.get("login").asText();
		String firstName = registerJson.get("login").asText();
		String lastName = registerJson.get("login").asText();
		String email = registerJson.get("login").asText();
		
		@SuppressWarnings("rawtypes")
		UserAccount user = UserAccount.find.byId(login);
		if(user != null) {
			return ok(/*register.render("Ten login już istnieje!")*/);
		}
		if(!password.equals(rePassword)) {
			return ok(/*register.render("Hasła nie pasują do siebie!")*/);
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
		return ok(logged.render(link));
	}
	
	public Result invite(){
		
		JsonNode jsNode = request().body().asJson();
		if (jsNode == null) {
			return status(403, "JSON wanted!");
		}
		String email = jsNode.findPath("mail").textValue();
			
		
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
			
			return status(403, "good");
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
