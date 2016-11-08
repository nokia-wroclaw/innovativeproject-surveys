package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class Response extends Model {

	
	@Id
	public Integer id;
	public String text;
	public String answer;
	public String ip;

	//public Integer survey_id;
	
	@ManyToOne
	public Question question;
	@ManyToOne
	public Survey survey;

	 
	public static Finder<Integer, Response> find = new Finder<Integer, Response>(Response.class);
	
	public Response(String text, String answer, String ip) {
		this.text = text;
		this.answer = answer;
		this.ip = ip;

		
	}
}
