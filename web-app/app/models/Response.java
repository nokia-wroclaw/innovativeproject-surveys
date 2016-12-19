package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.avaje.ebean.Model;
import cryptography.PasswordCrypt;

@Entity
public class Response extends Model {

	
	@Id
	public Integer id;
	public String answer;
	
	@ManyToOne
	public Question question;
	@ManyToOne
	public Survey survey;
	@Column(length=1000)
	public String keyToUser;
	
	public static Finder<Integer, Response> find = new Finder<Integer, Response>(Response.class);
	
	public Response(String answer) {
		this.answer = answer;
	}
	
	public void setAnswer(String name){
		this.answer = name;
	}

	public String getAnswer(){
		return this.answer;
	}

	public void setUser(String userPassword){
		try{
			this.keyToUser = PasswordCrypt.createPassword(userPassword+this.id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean checkUser(String password){
		return PasswordCrypt.checkPassword(password+this.id, this.keyToUser);
	}
}
