package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import cryptography.PasswordCrypt;

import com.avaje.ebean.Model;

@Entity
public class UserAccount extends Model{

	@Id 
	public String login;
	@Column()
	private String password;
	@Column(length=100)
	public String firstName;
	@Column(length=100)
	public String lastName;
    @Column(length=100)
	public String resetQuestion;
	@Column()
    public String resetAnswer;
    @Column()
    public int resetCount;


    @Column()
    private String resetCode;
	public Date createdTime = new Date();
	public String email;
	public static Finder<String, UserAccount> find = new Finder<String, UserAccount>(UserAccount.class);
	
	public UserAccount(String login, String password,
			String firstName, String lastName, String email, String resetQuestion, String resetAnswer) {
		this.login = login;
		try {
			this.password = PasswordCrypt.createPassword(password);
			this.resetAnswer = PasswordCrypt.createPassword(resetAnswer);
		}catch (Exception e){
			e.printStackTrace();
		}
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.resetQuestion = resetQuestion;
		this.createdTime = new Date();
		this.resetCount = 0;
	}

    public void setResetCount(int resetCount) {
        this.resetCount = resetCount;
    }

    public int getResetCount() {
        return resetCount;
    }

    public boolean checkAnswer(String answer){
	    return PasswordCrypt.checkPassword(answer, this.resetAnswer);
    }

	public boolean checkPassword(String password) {
		return PasswordCrypt.checkPassword(password, this.password);
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void changePassword(String newPassword) {
        try {
            this.password = PasswordCrypt.createPassword(newPassword);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
