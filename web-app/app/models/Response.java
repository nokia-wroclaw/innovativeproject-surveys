package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;


@Entity
public class Response extends Model {

	
	@Id
	public Integer id;
	public String answer;
	
	@ManyToOne
	public Question question;
	@ManyToOne
	public Survey survey;
	@ManyToOne
	public UserAccount userAccount;
	
	public static Finder<Integer, Response> find = new Finder<Integer, Response>(Response.class);
	
	public Response(String answer) {
		this.answer = answer;	
	}
	
	public void setQuestion(String name){
		this.answer = name;
	}
	
	public String getQuestion(){
		return answer;
	}
}
