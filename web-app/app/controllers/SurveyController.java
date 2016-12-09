package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.*;
import play.Logger;
import play.libs.Json;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class SurveyController extends Controller {

    @Inject
    MailerClient mailerClient;

    private String host = "https://survey-innoproject.herokuapp.com";

    public Result getSurvey(Integer id) {
        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            }
            return status(404, Json.toJson(new Message("You arent logged in")));
        }
        Logger.info("login " + login);
        UserAccount ua = UserAccount.find.byId(login);

        Survey survey = Survey.find.byId(id);
        if (survey == null) {
            return status(404, Json.toJson(new Message("Survey not find")));
        }
        List<SurveyMember> members = SurveyMember.find.select("*").where().eq("login", login).findList();
        boolean member = false;
        for (SurveyMember mem : members) {
            if (mem.survey.id == survey.id) {
                member = true;
            }
        }
        if (!survey.adminLogin.equals(login) && !member) {
            return status(403, "Don't have permission");
        }
        /*List<Question> questions = Question.find.select("*").where().eq("survey_id", id).findList();
        for(Question q : questions){
			q = Question.find.byId(q.id);
		}*/
        Question[] questionArray = new Question[survey.question.size()];
        questionArray = survey.question.toArray(questionArray);
        for (Question q : survey.question) {
            Logger.info("get Question: " + q.getQuestion());
            Logger.info("get Question id: " + q.id);
        }
        SurveyJson surveyJson = new SurveyJson(survey, questionArray);
        return ok(Json.toJson(surveyJson));
    }

    /**
     * The method create Survey
     *
     * @return Created Survey
     */
    public Result SurveyPost() {

        JsonNode surveyJson = request().body().asJson();
        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }
        Logger.info("login " + login);
        String name = surveyJson.get("name").asText();
        String description = surveyJson.get("description").asText();
        String email = surveyJson.get("email").asText();


        if (login == null) {
            return status(404, Json.toJson(new Message("You arent logged in")));
        }
        UserAccount ua = UserAccount.find.byId(login);

        Survey survey = new Survey(name, description, email);
        survey.adminLogin = ua.login;
        survey.save();

        ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
        Logger.info("allquestions: " + Json.stringify(allquestion));
        for (JsonNode x : allquestion) {
            String quest = x.get("question").asText();
            Logger.info("post question: " + quest);
            Question question = new Question(quest);
            Logger.info("post question in Question: " + question.question);
            question.survey = survey;
            question.save();
        }

        List<Question> allquestions = Question.find.select("*").where().eq("survey_id", survey.id).findList();
        Question arrayquest[] = new Question[allquestions.size()];
        arrayquest = allquestions.toArray(arrayquest);
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey, arrayquest));

        /*Email email1 = new Email();
        email1.setSubject("Created Survey " + name);
        email1.setFrom("Surveys <from@surveys.com>");
        email1.addTo(email);
        email1.setBodyText(
                "Survey was created on "+host+"/app/surveyView/" + survey.id + ". ");
        String id = mailerClient.send(email1);

        if (id == null) {
            return status(404, Json.toJson(new Message("Problem with send Email")));
        }*/
        return ok(surveyJs);
    }

    /**
     * The method create Survey
     *
     * @return Created Survey
     */
    public Result SurveyModification(Integer id) {

        JsonNode surveyJson = request().body().asJson();
        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }

        Survey findSurvey = Survey.find.byId(id);

        if (findSurvey == null) {
            return status(403, Json.toJson(new Message("Survey doesn't exist.")));
        }

        String name = surveyJson.get("name").asText();
        String description = surveyJson.get("description").asText();
        String email = surveyJson.get("email").asText();
        if (login == null) {
            return status(404, Json.toJson(new Message("You arent logged in")));
        }

        UserAccount ua = UserAccount.find.byId(login);

        if (!findSurvey.adminLogin.equals(login)) {
            return status(403, Json.toJson(new Message("You dont have permission.")));
        }
        Survey survey = Survey.find.byId(id);
        List<Question> allquestions = Question.find.select("*").where().eq("survey_id", survey.id).findList();

        int i = 0;
        ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
        for (JsonNode x : allquestion) {
            String quest = x.get("question").asText();
            allquestions.get(i).question = quest;
            allquestions.get(i).update();
            i += 1;
        }

        List<Question> allquestions1 = Question.find.select("*").where().eq("survey_id", survey.id).findList();
        Question arrayquest[] = new Question[allquestions1.size()];
        arrayquest = allquestions1.toArray(arrayquest);
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey, arrayquest));

        return ok(surveyJs);
    }

    public Result SurveyInvite(Integer id) {

        JsonNode jsNode = request().body().asJson();
        if (jsNode == null) {
            return status(403, Json.toJson(new Message("JSON wanted!")));
        }
        String email = jsNode.findPath("email").textValue();
        if (email == null || email.equals("")) {
            return status(403, Json.toJson(new Message("empty")));
        }
        Logger.info("Invite " + email + " to " + id);
        /*Email email1 = new Email();
        email1.setSubject("The invitation to the survey");
        email1.setFrom("Surveys <from@surveys.com>");
        email1.addTo(email);
        email1.setBodyText("You are invited to participate in the survey on "
                + host + "/app/surveysView/"+id+".");
        String ret = mailerClient.send(email1);

        if (ret == null) {
            return status(403, Json.toJson(new Message("Problem with send Email")));
        }*/

        List<UserAccount> ac = UserAccount.find.select("*").where().eq("email", email).findList();

        if (ac.isEmpty()) {
            return status(403, Json.toJson(new Message("Not found account!")));
        }
        Survey survey = Survey.find.byId(id);
        SurveyMember sm = new SurveyMember(ac.get(0).login);
        sm.survey = survey;
        sm.save();

        return ok(Json.toJson(new Message("Correctly sent invitation")));
    }

    /**
     * The method will fill all Survey answer
     *
     * @return All responses from logged user
     */
    public Result fillAnswer(Integer id) {

        JsonNode surveyJson = request().body().asJson();
        if (surveyJson == null) {
            return status(403, Json.toJson(new Message("JSON wanted!")));
        }
        String login = session().get("login");
        String password = session().get("password");
        if (login == null || password == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }
        Survey survey = Survey.find.byId(id);
        UserAccount userAccount = UserAccount.find.byId(login);

        List<Question> allquestions = Question.find.select("*").where().eq("survey_id", id).findList();

        List<Response> response = Response.find.select("*").where().eq("survey_id", id).findList();

        for (int i = 0; i < response.size(); i++) {
            if (!response.get(i).checkUser(password))
                response.remove(i);
        }
        Logger.info("Response size "+response.size());

        String answer;
        ArrayNode allanswer = (ArrayNode) surveyJson.withArray("Answers");

        int i = 0;
        for (JsonNode x : allanswer) {
            answer = x.get("answer").asText();
            Response response1;
            if (response.size() == 0) {
                response1 = new Response(answer);
                response1.survey = survey;
                response1.question = allquestions.get(i);
                response1.save();
                response1.setUser(password);
            } else {
                response1 = response.get(i);
                response1.setAnswer(answer);
            }
            response1.update();
            i += 1;
        }

        return ok(Json.toJson(new Message("survey filled")));
    }

    /**
     * The method return all answers from logged user from selected survey by id
     *
     * @return All responses from logged user
     */

    public Result getUserResult(Integer id) {
        String login = session().get("login");
        String password = session().get("password");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }

        Survey survey = Survey.find.byId(id);
        Logger.info(survey.question.toString());
        List<SurveyMember> surMem = SurveyMember.find.select("*").where().eq("survey_id", id)
                .eq("login", login).findList();

        if (surMem.isEmpty() && !survey.adminLogin.equals(login)) {
            return status(404, Json.toJson(new Message("You arent member")));
        }

        List<Response> allResponse = Response.find
                .select("*")
                .where()
                .eq("survey_id", id)
                .findList();
        for (int i = 0; i < allResponse.size(); i++) {
            if (!allResponse.get(i).checkUser(password))
                allResponse.remove(i);
        }
        List<ResponseJson> result = new ArrayList<ResponseJson>();
        int i = 1;
        for (Response res : allResponse) {
            result.add(new ResponseJson(res, i));
            Logger.info("Get userResult " + i + "result: " + res.answer);
            i++;
        }
        JsonNode ResponseJs = Json.toJson(result);

        return ok(ResponseJs);
    }

    /**
     * The method return all answers to Admin user from selected survey by id
     *
     * @return All responses from  users from selected survey
     */
    public Result getAdminResult(Integer id) {
        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }

        Survey survey = Survey.find.byId(id);
        if (!survey.adminLogin.equals(login)) {
            return status(404, Json.toJson(new Message("You arent admin")));
        }
        List<ResponseJson> response = new ArrayList<ResponseJson>();
        List<Question> allQuestion = Question.find.where().eq("survey_id", id).findList();
        for (Question q : allQuestion) {
            List<Response> allResponse = Response.find.where()
                    .eq("survey_id", id)
                    .eq("question_id", q.id)
                    .findList();
            int i = 0;
            for (Response r : allResponse) {
                response.add(new ResponseJson(r, i));
                i++;
            }
        }


        JsonNode ResponseJs = Json.toJson(response);

        return ok(ResponseJs);
    }


    /**
     * The method return the surveys list which logged user takes part in
     *
     * @return All userSurveys list from logged user
     */
    public Result getUserSurveysId() {

        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }

        List<SurveyMember> surMem = SurveyMember.find.select("*").where().eq("login", login).findList();

        List<SurveyJson> result = new ArrayList<SurveyJson>();
        for (SurveyMember sm : surMem) {
            Survey survey = Survey.find.byId(sm.survey.id);
            result.add(new SurveyJson(survey));
        }

		/*if (surMem.isEmpty()) {
            return status(404, Json.toJson(new Message("You arent member of any survey")));
		}*/

        JsonNode surveyJs = Json.toJson(result);
        return ok(surveyJs);

    }

    /**
     * The method return the surveys list which Admin user created
     *
     * @return All userSurveys list from Admin user
     */
    public Result getAdminSurveysId() {

        String login = session().get("login");
        if (login == null) {
            String session = request().getHeader("PLAY-SESSION");
            if (session != null) {
                login = session;
            } else {
                return status(404, Json.toJson(new Message("You arent logged in")));
            }
        }

        List<Survey> surveylist = Survey.find.select("*").where().eq("adminLogin", login).findList();
        List<SurveyJson> result = new ArrayList<SurveyJson>();
        for (Survey s : surveylist) {
            Survey survey = Survey.find.byId(s.id);
            result.add(new SurveyJson(survey));
        }

		/*if (surveylist.isEmpty()) {
            return status(404, Json.toJson(new Message("You arent Admin of any survey")));
		}*/

        JsonNode surveyJs = Json.toJson(result);
        return ok(surveyJs);

    }

    class Message {
        public String message;

        public Message(String msg) {
            message = msg;
        }
    }
}

class SurveyJson {
    public Integer id;

    public String name;
    public String description;
    public String email;
    public QuestionJson questions[];

    public SurveyJson(Survey s, Question q[]) {
        id = s.id;
        name = s.name;
        description = s.description;
        email = s.email;
        this.questions = new QuestionJson[q.length];
        for (int i = 0; i < q.length; i++) {
            this.questions[i] = new QuestionJson(q[i]);
            this.questions[i].id = i + 1;
        }
        Logger.info("Send survey:\n" + Json.toJson(this));
    }

    public SurveyJson(Survey s) {
        List<Question> q = s.question;
        id = s.id;
        name = s.name;
        description = s.description;
        email = s.email;
        this.questions = new QuestionJson[q.size()];
        for (int i = 0; i < q.size(); i++) {
            this.questions[i] = new QuestionJson(q.get(i));
            this.questions[i].id = i + 1;
        }
        Logger.info("Send survey:\n" + Json.toJson(this));
    }
}

class QuestionJson {
    public int id;
    public String question;

    public QuestionJson(Question q) {
        this.id = q.id;
        this.question = q.question;
    }
}

class ResponseJson {
    public int id;
    public String answer;

    public ResponseJson(Response answer, int id) {
        this.answer = answer.answer;
        this.id = id;
    }
}

