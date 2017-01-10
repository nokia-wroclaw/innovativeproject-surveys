package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import cryptography.PasswordCrypt;

import com.avaje.ebean.Model;

@Entity
public class UserAccount extends Model{

	@Id 
	public String login;
	@Column(length=100)
	private String password;
	@Column(length=100)
	public String firstName;
	@Column(length=100)
	public String lastName;

	
	public Date createdTime = new Date();
	public String email;
	public static Finder<String, UserAccount> find = new Finder<String, UserAccount>(UserAccount.class);
	
	public UserAccount(String login, String password,
			String firstName, String lastName, String email) {
		this.login = login;
		try {
			this.password = PasswordCrypt.createPassword(password);
		}catch (Exception e){
			e.printStackTrace();
		}
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		
		this.createdTime = new Date();
	}

	public boolean checkPassword(String password) {
		return PasswordCrypt.checkPassword(password, this.password);
	}
}
