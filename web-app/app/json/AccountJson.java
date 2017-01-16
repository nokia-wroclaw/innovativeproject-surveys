package json;

import models.UserAccount;

/**
 * Created by FantyG on 2017-01-15.
 */
public class AccountJson {
    public String login;
    public String email;
    public String firstName;
    public String lastName;

    public AccountJson(UserAccount ua) {
        login = ua.login;
        email = ua.email;
        firstName = ua.firstName;
        lastName = ua.lastName;
    }
}
