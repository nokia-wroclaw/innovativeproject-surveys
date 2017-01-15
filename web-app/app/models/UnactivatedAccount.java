package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class UnactivatedAccount extends Model {
	
	@Id 
	public String login;
	
	public UserAccount ua;
	public String activationLink;
	public Date expiredDate;
	
	
	public static Finder<String, UnactivatedAccount> find =
			new Finder<String, UnactivatedAccount>(UnactivatedAccount.class);
	
	public UnactivatedAccount(String login, String password,
			String firstName, String lastName, String email,
			String activationLink, String resetQuestion, String resetAnswer) {
		
		ua = new UserAccount(login, password, firstName, lastName, email, resetQuestion, resetAnswer);
		ua.save();
		this.activationLink = activationLink;
		this.expiredDate = new Date(System.currentTimeMillis()+172800000);
		this.login = login;
	}
	
}
