package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class UnactivatedAccount extends Model {
	
	public Date expiredDate;
	@Id
	public String activationLink;
	public UserAccount ua;
	
	public static Finder<String, UnactivatedAccount> find =
			new Finder<String, UnactivatedAccount>(UnactivatedAccount.class);
	
	public UnactivatedAccount(String login, String password,
			String firstName, String lastName, String email,
			String activationLink) {
		
		ua = new UserAccount(login, password, firstName, lastName, email);
		ua.save();
		this.activationLink = activationLink;
		this.expiredDate = new Date(System.currentTimeMillis()+172800000);
	}
	
}
