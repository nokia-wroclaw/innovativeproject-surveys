package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

@Entity
public class Survey extends Model {

	@Id
	public Integer id;

	public String name;
	public String admin;
	public String description;
	public String email;

	@OneToMany(mappedBy = "survey")
	public List<Question> question;
	@OneToMany(mappedBy = "survey")
	public List<Response> response;
	@ManyToOne
	public UserAccount userAccount;

	public static Finder<Integer, Survey> find = new Finder<Integer, Survey>(Survey.class);

	public Survey(String name, String description, String email, String admin) {
		this.name = name;
		this.admin = admin;
		this.description = description;
		this.email = email;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getAdmin() {
		return admin;
	}

	public String getDescription() {
		return description;
	}

	public String getEmail() {
		return email;
	}

}

class SurveyJson {
	public Integer id;

	public String name;
	public String admin;
	public String description;
	public String email;

	public SurveyJson(Survey s) {
		id = s.id;
		admin = s.admin;
		name = s.name;
		description = s.description;
		email = s.email;
	}
}
