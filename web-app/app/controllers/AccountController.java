package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cryptography.PasswordCrypt;
import cryptography.RandomVaribles;
import json.AccountJson;
import json.MessageJson;
import json.ResetQuestionAskJson;
import json.ResetQuestionResJson;
import models.*;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.*;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import play.Logger;

import javax.inject.Inject;

/**
 * Controler managing all account featers.
 *
 * @author Kamil Malinowski
 */
public class AccountController extends Controller {

    @Inject
    MailerClient mailerClient;

    private String host = "https://survey-innoproject.herokuapp.com";

    public Result loginPost() {
        JsonNode jsNode = request().body().asJson();
        if (jsNode == null) {
            return status(403, Json.toJson(new MessageJson("JSON wanted!")));
        }
        String login = jsNode.findPath("login").textValue().toLowerCase();
        String password = jsNode.findPath("password").textValue();
        if (login == null || password == null) {
            return status(403,
                    Json.toJson(new MessageJson("Login or password not set.")));
        }
        UserAccount ua = UserAccount.find.byId(login);
        if (ua == null) {
            return status(403, Json.toJson(new MessageJson("Bad login")));
        }
        if (!ua.checkPassword(password)) {
            return status(403, Json.toJson(new MessageJson("Bad password")));
        }
        UnactivatedAccount UnactivatedAcc = UnactivatedAccount.find.byId(login);
        if (UnactivatedAcc != null) {
            return status(403, Json.toJson(new MessageJson("Account not activated.")));
        }
        session().put("login", login);
        session().put("password", password);
        JsonNode jsUser = Json.toJson(new AccountJson(ua));
        return ok(jsUser);
    }

    public void sendAccEmail(String email, String link, String firstName) {
        Email email1 = new Email();
        email1.setSubject("Rejestracja na surveys");
        email1.setFrom("Surveys <registration@surveys.com>");
        email1.addTo(email);
        email1.setBodyText("Hi " + firstName
                + "!\n\nOto link aktywacyjny: " + host + "/activ/"
                + link);
        String id = mailerClient.send(email1);

    }

    public Result userPost(String login) {

        JsonNode registerJson = request().body().asJson();
        if (registerJson == null) {
            return status(403, Json.toJson(new MessageJson("JSON wanted!")));
        }
        if (login.equals("")) {
            return status(403, Json.toJson(new MessageJson("Empty login")));
        } else {
            login = login.toLowerCase();
        }
        if (registerJson.get("password") == null) {
            return status(403, Json.toJson(new MessageJson("Password wanted")));
        }
        if (registerJson.get("rePassword") == null) {
            return status(403, Json.toJson(new MessageJson("rePassword wanted")));
        }
        if (registerJson.get("firstName") == null) {
            return status(403, Json.toJson(new MessageJson("firstName wanted")));
        }
        if (registerJson.get("lastName") == null) {
            return status(403, Json.toJson(new MessageJson("lastName wanted")));
        }
        if (registerJson.get("email") == null) {
            return status(403, Json.toJson(new MessageJson("email wanted")));
        }
        if (registerJson.get("resetQuestion") == null) {
            return status(403, Json.toJson(new MessageJson("email wanted")));
        }
        if (registerJson.get("resetAnswer") == null) {
            return status(403, Json.toJson(new MessageJson("email wanted")));
        }

        String password = registerJson.get("password").asText();
        String rePassword = registerJson.get("rePassword").asText();
        String firstName = registerJson.get("firstName").asText();
        String lastName = registerJson.get("lastName").asText();
        String email = registerJson.get("email").asText();
        String resetQuestion = registerJson.get("resetQuestion").asText();
        String resetAnswer = registerJson.get("resetAnswer").asText();

        if (password == null || password.equals("")) {
            return status(403, Json.toJson(new MessageJson("Password wanted")));
        }

        if (rePassword == null || rePassword.equals("")) {
            return status(403, Json.toJson(new MessageJson("rePassword wanted")));
        }

        if (firstName == null || firstName.equals("")) {
            return status(403, Json.toJson(new MessageJson("First name wanted")));
        }

        if (lastName == null || lastName.equals("")) {
            return status(403, Json.toJson(new MessageJson("Last name wanted")));
        }

        if (email == null || email.equals("")) {
            return status(403, Json.toJson(new MessageJson("Email wanted")));
        }

        if (resetQuestion == null || resetQuestion.equals("")) {
            return status(403, Json.toJson(new MessageJson("Question for password reset wanted!")));
        }

        if (resetAnswer == null || resetAnswer.equals("")) {
            return status(403, Json.toJson(new MessageJson("Question for password reset wanted!")));
        }

        @SuppressWarnings("rawtypes")
        UserAccount user = UserAccount.find.byId(login);
        List<UserAccount> userEm = UserAccount.find.where().eq("email", email)
                .findList();
        if (userEm.size() != 0) {
            return status(403, Json.toJson(new MessageJson(
                    "User with this e-mail already exists!")));
        }
        if (user != null) {
            return status(403, Json.toJson(new MessageJson(
                    "User with this login already exists!")));
        }
        if (!password.equals(rePassword)) {
            return status(403,
                    Json.toJson(new MessageJson("Passwords don't match!")));
        }
        String link = Integer.toHexString(new Random().nextInt(0x1000000));
        List<UnactivatedAccount> unactiv = UnactivatedAccount.find.all();
        if (unactiv != null) {
            for (int i = 0; i < unactiv.size(); i++) {
                if (unactiv.get(i).activationLink.equals(link)) {
                    i = 0;
                    link = Integer.toHexString(new Random().nextInt(0x1000000));
                }
            }
        }
        Logger.info(login+" activation link: "+link);
        UnactivatedAccount newAcc = new UnactivatedAccount(login, password,
                firstName, lastName, email, link, resetQuestion, resetAnswer);

        newAcc.save();
        sendAccEmail(email, link, firstName);
        return ok(Json.toJson(new MessageJson("Registrated")));
    }

    public Result invite() {

        JsonNode jsNode = request().body().asJson();
        if (jsNode == null) {
            return status(403, Json.toJson(new MessageJson("JSON wanted!")));
        }
        String email = jsNode.findPath("email").textValue();

        if (email.equals("")) {
            return status(403, Json.toJson(new MessageJson("Empty email")));
        }

        List<UserAccount> userEm = UserAccount.find.where().eq("email", email)
                .findList();
        if (userEm.size() != 0) {
            return status(403, Json.toJson(new MessageJson(
                    "User with this e-mail already exists!")));
        }

        Email email1 = new Email();
        email1.setSubject("Rejestracja na surveys");
        email1.setFrom("Surveys <registration@surveys.com>");
        email1.addTo(email);
        email1.setBodyText("Zostałeś zaproszony do http://localhost:3000/register . Kliknij link i zarejestruj swoje konto");
        String id = mailerClient.send(email1);

        if (id != null) {

            return status(200, Json.toJson(new MessageJson("Invitation sended!")));
        }

        session("email", email);

        return ok(Json.toJson(new MessageJson("Poprawnie wyslano zaproszenie")));

    }

    public Result activate(String link) {
        List<UnactivatedAccount> ua = UnactivatedAccount.find.all();
        for (UnactivatedAccount a : ua) {
            if (a.activationLink == link) {
                a.delete();
                return ok(Json.toJson(new MessageJson("Konto aktywowane!")));
            }
        }
        return status(404, Json.toJson(new MessageJson("Zły link aktywacyjny!")));

    }

    public Result clean() {
        List<UnactivatedAccount> l = UnactivatedAccount.find.all();
        for (UnactivatedAccount a : l) {
            a.delete();
        }
        List<UserAccount> l1 = UserAccount.find.all();
        for (UserAccount a : l1) {
            a.delete();
        }

        return ok();
    }

    public Result getAll() {
        List<String> allUsersLogins = getUsersLogins(UserAccount.find.all());
        return ok(Json.toJson(allUsersLogins));
    }

    private List<String> getUsersLogins(List<UserAccount> allUsers) {
        List<String> allUsersLogin = new ArrayList<>();
        for (UserAccount user : allUsers) {
            allUsersLogin.add(user.login);
        }
        return allUsersLogin;
    }

    public Result getResetQuestion() {
        ResetQuestionAskJson ask = Json.fromJson(request().body().asJson(), ResetQuestionAskJson.class);
        if (ask == null) {
            return status(403, Json.toJson(new MessageJson("Error in parse JSON")));
        }
        UserAccount user = UserAccount.find.byId(ask.login);
        if (user == null) {
            return status(403, Json.toJson(new MessageJson("Wrong login!")));
        }
        if (!user.email.equals(ask.email)) {
            return status(403, Json.toJson(new MessageJson("E-mail don't match login!")));
        }
        session().put("resetLogin", ask.login);
        return ok(Json.toJson(new ResetQuestionResJson(user.resetQuestion)));
    }

    public Result getResetCode() {
        JsonNode body = request().body().asJson();
        String answer = body.get("answer").asText();
        String login = session().get("resetLogin");

        if (answer == null || answer.equals("") || login == null || login.equals("")) {
            return status(403, Json.toJson(new MessageJson("Error in parse JSON")));
        }

        UserAccount user = UserAccount.find.byId(login);
        if (user == null) {
            return status(403, Json.toJson(new MessageJson("Wrong login!")));
        }
        if (user.getResetCount() > 3) {
            return status(403, Json.toJson(new MessageJson("You tried too many times!")));
        }
        if (!user.checkAnswer(answer)) {
            user.resetCount++;
            user.update();
            return status(403, Json.toJson(new MessageJson("Wrong answer!")));
        }
        user.setResetCount(0);
        user.update();
        String code = RandomVaribles.getRandomString(8);
        try {
            session().put("resetCode", PasswordCrypt.createPassword(code));
        } catch(Exception e) {
            return status(403, Json.toJson(new MessageJson("Crypting error!")));
        }
        Logger.info("code: " + code);
        //sendResetEmail(user.email, code, user.firstName);
        return ok(Json.toJson(new MessageJson("Check your e-mail for coed to password reset")));
    }

    public Result resetPassword() {
        JsonNode body = request().body().asJson();
        String login = session().get("resetLogin");
        String oldCode = session().get("resetCode");
        String code = body.get("code").asText();
        String newPassword = body.get("password").asText();
        String newRePassword = body.get("rePassword").asText();
        if (code == null || code.equals("") || newPassword == null || newPassword.equals("") || login == null) {
            return status(403, Json.toJson(new MessageJson("Every field is required!")));
        }
        if (!newPassword.equals(newRePassword)) {
            return status(403, Json.toJson(new MessageJson("Passwords don't match!")));
        }
        if (oldCode == null || !PasswordCrypt.checkPassword(code, oldCode)){
            return status(403, Json.toJson(new MessageJson("Wrong code!")));
        }
        UserAccount user = UserAccount.find.byId(login);
        user.changePassword(newPassword);
        user.update();
        return ok(Json.toJson(new MessageJson("You successfully reset your password")));
    }

    public void sendResetEmail(String email, String code, String firstName) {
        Email email1 = new Email();
        email1.setSubject("Password reset");
        email1.setFrom("Surveys <no-replay@surveys.com>");
        email1.addTo(email);
        email1.setBodyText("Hi " + firstName
                + "!\n\nHere is your password reset code:" + code +
                ". If you didn't want to get this e-mail, ignore it.");
        String id = mailerClient.send(email1);

    }
}

