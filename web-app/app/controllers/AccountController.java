package controllers;


import java.util.List;

import models.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.avaje.ebean.Model;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;

/**Controler managing all account featers.
 * 
 * @author Kamil Malinowski
 *
 */
public class AccountController extends Controller {
	
	public Result loginGet(){
		return ok(login.render());
	}
	public Result registerGet(){
		return ok(register.render(""));
	}
	
	public Result loginPost(){
		@SuppressWarnings("deprecation")
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		return ok(logged.render("Zalogowany jako: "+dynamicForm.get("login")));
	}
	
	public Result registerPost() {
		
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String login = dynamicForm.get("login");
		String password = dynamicForm.get("password");
		String rePassword = dynamicForm.get("rePassword");
		String firstName = dynamicForm.get("firstName");
		String lastName = dynamicForm.get("lastName");
		String email = dynamicForm.get("email");
		
		
		
		@SuppressWarnings("rawtypes")
		UserAccount user = UserAccount.find.byId(login);
		if(user != null) {
			return ok(register.render("Ten login już istnieje!"));
		}
		if(!password.equals(rePassword)) {
			return ok(register.render("Hasła nie pasują do siebie!"));
		}
		String link = RandomStringUtils.random(50);
		List<UnactivatedAccount> unactiv = UnactivatedAccount.find.all();
		if (unactiv != null) {
			for (int i = 0; i < unactiv.size(); i++) {
				if (unactiv.get(i).activationLink.equals(link)) {
					i = 0;
					link = RandomStringUtils.random(50);
				}
			}
		}
		UnactivatedAccount newAcc = new UnactivatedAccount(login, password, firstName,
				lastName, email, link);
		
		newAcc.save();
		return ok(logged.render(login+" zarejestrowano! Aktywacja <a href=\"localhost/activ/"+
				"\">link<\\a>"));
	}
}
