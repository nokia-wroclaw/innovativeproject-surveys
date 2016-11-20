package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	
	@OneToMany
	public List<Response> response;	
	@OneToMany
	public List<Survey> survey;
	
	
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
	
	public void setResponse(List<Response> response){
		this.response = response;
	}
	
	public List<Response> getResponse(){
		return response;
	}
	
	public void setSurveyList(List<Survey> survey){
		this.survey = survey;
	}
	
	public List<Survey> getSurveyList(){
		return survey;
	}
	
}
