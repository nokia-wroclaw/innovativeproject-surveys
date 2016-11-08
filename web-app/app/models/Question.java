package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
@Entity
public class Question extends Model {

	
	@Id
	public Integer id;
	public String text;
	//public String answer;
	

	//public Integer survey_id;
	
	@ManyToOne
	public Survey survey;
	@OneToMany(mappedBy="question")
	public List<Response> response;

	 
	public static Finder<Integer, Question> find = new Finder<Integer, Question>(Question.class);
	
	public Question(String text) {
		this.text = text;
		//this.answer = answer;
		

		
	}
	
}
