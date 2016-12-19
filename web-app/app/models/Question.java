package models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class Question extends Model implements Serializable  {

	
	@Id
	public Integer id;
	public String question;
	public String questionType;
	
	@ManyToOne
	public Survey survey;
	@OneToMany(mappedBy="question")
	public List<Response> response;
	@OneToMany(mappedBy="question")
	public List<ResponseChoice> responseChoice;
	
	public static Finder<Integer, Question> find = new Finder<Integer, Question>(Question.class);
	
	public Question(String question) {
		this.question = question;
	}
	
	public void setAnswers(List<ResponseChoice> name){
		this.responseChoice = name;
	}
	
	public List<ResponseChoice> getAnswers(){
		return responseChoice;
	}
	
	public void setQuestion(String name){
		this.question = name;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public void setQuestionType(String questionType){
		this.questionType = questionType;
	}
	
	public String getQuestionType(){
		return questionType;
	}

	
}
