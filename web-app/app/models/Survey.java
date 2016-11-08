package models;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;


@Entity
public class Survey extends Model{

	@Id
	public Integer id;
	
	public String name;
	public String description;
	public String email;
	public String ip;
	
	
	@OneToMany(mappedBy="survey")
	public List<Question> question;
	@OneToMany(mappedBy="survey")
	public List<Response> response;

	
	public static Finder<Integer, Survey> find = new Finder<Integer, Survey>(Survey.class);
	
	public Survey(String name, String description, String email) {
		this.name = name;
		this.description = description;
		this.email = email;
		this.ip = "";
		
	}
	
	public void setId(Integer id){	
		this.id = id;
	}
	
	public void setIp(String Ip){
		this.ip = Ip;
	}
	
	
}
