package accounts;

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
	
	
	public Date createdTime;
	
	public UserAccount(String login, String password,
			String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		
		this.createdTime = new Date();
		
	}
	
	public boolean checkPassword(String password) {
		return password.equals(this.password);
	}
	
}