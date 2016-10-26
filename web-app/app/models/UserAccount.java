package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class UserAccount extends Model{

	@Id
	public String login;
	private String password;
	public String firstName;
	public String lastName;
	
	
	//public List<String> guestSurvId;
	//public List<String> adminSurvId;
	
	public Date createdTime = new Date();
	public String email;
	public static Finder<String, UserAccount> find = new Finder<String, UserAccount>(UserAccount.class);
	
	public UserAccount(String login, String password,
			String firstName, String lastName, String email) {
		this.login = login;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		
		this.createdTime = new Date();
	}
	
	public boolean checkPassword(String password) {
		return password.equals(this.password);
	}
	
}
