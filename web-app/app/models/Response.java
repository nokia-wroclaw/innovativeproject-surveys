package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Response extends Model {

	
	@Id
	public Integer id;
	public String answer;
	
	@ManyToOne
	@JsonManagedReference
	public Question question;
	@ManyToOne
	@JsonManagedReference
	public Survey survey;
	@ManyToOne
	@JsonManagedReference
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
