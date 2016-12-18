package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class ResponseChoice extends Model {
	
	@Id
	public Integer id;	
	public String text;
	@ManyToOne
	public Question question;
	public Boolean isSelected;
	
	public static Finder<Integer, ResponseChoice> find = new Finder<Integer, ResponseChoice>(ResponseChoice.class);

	public ResponseChoice(String answer) {
		this.text = answer;	
	}
	public void setAnswer(String answer){
		this.text = answer;	
	}	
	public String getAnswer(){
		return text;
	}
	public void setIsSelected(Boolean isSelected){
		this.isSelected = isSelected;
	}
	public Boolean getIsSelected(){
		return isSelected;
	}
	public void setQuestion(Question question){
		this.question = question;
	}
	public Question getQuestion(){
		return question;
	}
}
