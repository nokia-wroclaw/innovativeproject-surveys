package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
@Entity
public class Question extends Model {

	
	@Id
	public Integer id;
	public String question;

	@ManyToOne
	public Survey survey;
	@OneToMany(mappedBy="question")
	public List<Response> response;
	
	public static Finder<Integer, Question> find = new Finder<Integer, Question>(Question.class);
	
	public Question(String question) {
		this.question = question;	
	}
	
	public void setQuestion(String name){
		this.question = name;
	}
	
	public String getQuestion(){
		return question;
	}
	
}