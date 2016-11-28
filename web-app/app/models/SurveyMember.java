package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class SurveyMember extends Model{
	
	@Id
	Integer id;
	@ManyToOne
	@JsonManagedReference
	public Survey survey;
	public String login;

	
	public static Finder<Integer, SurveyMember> find = new Finder<Integer, SurveyMember>(SurveyMember.class);
	
	public SurveyMember(String login) {
		this.login = login;
	}
}
