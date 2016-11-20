package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
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
	
	@OneToMany(mappedBy="survey")
	public List<Question> question;
	@OneToMany(mappedBy="survey")
	public List<Response> response;
	@ManyToOne
	public UserAccount userAccount;

	
	public static Finder<Integer, Survey> find = new Finder<Integer, Survey>(Survey.class);
	
	public Survey(String name, String description, String email) {
		this.name = name;
		this.description = description;
		this.email = email;
	}
	
	public void setId(Integer id){	
		this.id = id;
	}
	
	public void setName(String name){	
		this.name = name;
	}
	
	public void setDescription(String description){	
		this.description = description;
	}
	
	public void setEmail(String email){	
		this.email = email;
	}
	
	public String getName(){	
		return name; 
	}
	
	public String getDescription(){	
		return description;
	}
	
	public String getEmail(){	
		return email;
	}
	
	
}

