package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import controllers.SurveyController.Message;
import models.*;
import json.*;
import play.Logger;
import play.libs.Json;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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

        Survey survey = Survey.find.byId(id);
        Logger.info("Survey GET:");
        Logger.info("Question 0 id: "+survey.question.get(0).id);
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
        
        SurveyJson surveyJson = new SurveyJson(survey);
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
 
        UserAccount ua = UserAccount.find.byId(login);

        Survey survey = new Survey(name, description, email);
        survey.adminLogin = ua.login;
        survey.save();
        
        ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
        
        int i = 1;
        Logger.info("allquestions: " + Json.stringify(allquestion));
        for (JsonNode x : allquestion) {
            String quest = x.get("question").asText();
            String questType = x.get("questionType").asText();
            Logger.info("post question: " + quest);
            Question question = new Question(quest);
            Logger.info("post question in Question: " + question.question);
            question.survey = survey;
            question.setQuestionType(questType);
            question.save();
            
            if(questType.equals("open")){
                i += 1;
            }else if(questType.equals("true/false")){
                i += 1;
            }else if(questType.equals("multi")){
          
            	 ArrayNode questionResponse = (ArrayNode) x.withArray("possibleAnswers");
            	          	 
            	  for (JsonNode x1 : questionResponse) {
            		  String questResponse = x1.get("response").asText();
            		  ResponseChoice res = new ResponseChoice(questResponse);
            		  res.setIsSelected(false);
            		  res.setQuestion(question);
            		  res.save();
            		  question.responseChoice.add(res); 
            	  } 
            	 question.update();
            	 i += 1;
            }
            survey.question.add(question);
            survey.update();
        }
        
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey));
     
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
        
        findSurvey.setName(name);
        findSurvey.setDescription(description);
        findSurvey.setEmail(email);
        findSurvey.update();

        if (!findSurvey.adminLogin.equals(login)) {
            return status(403, Json.toJson(new Message("You dont have permission.")));
        }
        Survey survey = Survey.find.byId(id);
        List<Question> allquestions = Question.find.select("*").where().eq("survey_id", survey.id).findList();
       
        int i = 0;
        int temp2 = 0; 
        ArrayNode allquestion = (ArrayNode) surveyJson.withArray("questions");
        for (JsonNode x : allquestion) {
            String quest = x.get("question").asText();
            String questType = x.get("questionType").asText();
            allquestions.get(i).setQuestion(quest);
            allquestions.get(i).setQuestionType(questType);
            allquestions.get(i).update();        
            temp2 = 0;
            if(questType.equals("multi")){
            	
             List<ResponseChoice> allquestions2 = ResponseChoice.find.select("*").where().eq("question_id",allquestions.get(i).id).findList();
           	 ArrayNode questionResponse = (ArrayNode) x.withArray("possibleAnswers");
           	          	 
           	  for (JsonNode x1 : questionResponse) {
           		  
           		 if(questionResponse.size() <= allquestions2.size()){
           		  String questResponse = x1.get("response").asText();
           		  allquestions2.get(temp2).setAnswer(questResponse);;
           		  allquestions2.get(temp2).setIsSelected(false);
           		  allquestions2.get(temp2).update();
           		  temp2++;
           		 }
           		  //Add new possible answer to database
           		  if(questionResponse.size() > allquestions2.size()){
           			  
           		  String questResponse = x1.get("response").asText();
           		  ResponseChoice res = new ResponseChoice(questResponse);
           		  res.setIsSelected(false);
           		  res.setQuestion(allquestions.get(i));
           		  res.save();
           		  allquestions.get(i).responseChoice.add(res); 
           		  allquestions.get(i).update();         	 
           		  }
           	  } 
        }
            i += 1;
        }
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey));

        return ok(surveyJs);
    }

    public Result SurveyInvite(Integer id) {

        JsonNode jsNode = request().body().asJson();
        if (jsNode == null) {
            return status(403, Json.toJson(new Message("JSON wanted!")));
        }
        String login = jsNode.findPath("login").textValue();
        if (login == null || login.equals("")) {
            return status(403, Json.toJson(new Message("empty")));
        }
        Logger.info("Invite " + login + " to " + id);

        UserAccount ac = UserAccount.find.byId(login);

        if (ac == null) {
            return status(403, Json.toJson(new Message("Not found account!")));
        }
        Survey survey = Survey.find.byId(id);
        SurveyMember sm = new SurveyMember(login);
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

        for (Iterator<Response> iterator = response.iterator(); iterator.hasNext();) {
            Response current = iterator.next();
            Logger.info(current.getAnswer());
            if (!current.checkUser(password, login)) {
                iterator.remove();
                Logger.info("removed");
            }
        }
        Logger.info("Response "+response.size());

        String answer;
        ArrayNode allanswer = (ArrayNode) surveyJson.withArray("Answers");

        int i = 0;
        for (JsonNode x : allanswer) {
            answer = x.get("answer").asText();
            Response response1;
            if (response.size() <= i) {
                response1 = new Response(answer);
                response1.survey = survey;
                response1.question = allquestions.get(i);
                response1.save();
                response1.setUser(password, login);
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
        Logger.info("Response size: "+allResponse.size());
        for (Iterator<Response> iterator = allResponse.iterator(); iterator.hasNext();) {
            Response current = iterator.next();
            Logger.info(current.getAnswer());
            if (!current.checkUser(password, login)) {
                iterator.remove();
                Logger.info("removed");
            }
        }
        Logger.info("Response size: "+allResponse.size());
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

