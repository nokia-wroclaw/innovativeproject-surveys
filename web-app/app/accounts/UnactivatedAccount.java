package accounts;

import java.util.Date;

public class UnactivatedAccount extends UserAccount {
	
	public Date expiredDate;
	public String activationLink;
	
	public UnactivatedAccount(String login, String password,
			String firstName, String lastName, String email,
			String activationLink) {
		
		super(login, password, firstName, lastName, email);
		this.activationLink = activationLink;
		this.expiredDate = new Date(System.currentTimeMillis()+172800000);
	}
	
}
