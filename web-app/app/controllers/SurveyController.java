package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import controllers.SurveyController.Message;
import models.*;
import play.Logger;
import play.libs.Json;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SurveyController extends Controller {

    @Inject
    MailerClient mailerClient;

    private String host = "https://survey-innoproject.herokuapp.com";

    public Result re(){

    	String name = "Ankieta1";
        String description = "Nic";
        String email = "mail";
 

        Survey survey = new Survey(name, description, email);
        survey.adminLogin = "Bartek";
        survey.save();
        List<ResponseChoice> list = new LinkedList<ResponseChoice>();
        
        int i = 1;
            String quest = "Pytanie1";
            String questType = "multi";
            Question question = new Question(quest);
            question.survey = survey;
            question.setQuestionType(questType);
            question.save();
            
            if(questType.equals("open")){
              
            }else if(questType.equals("true/false")){
            
            }else if(questType.equals("multi")){
            	          	 
            		  String questResponse = "odp1";
            		  ResponseChoice res = new ResponseChoice(questResponse);
            		  res.setIsSelected(false);
            		  res.setQuestion(question);
            		  res.save();
            		  question.responseChoice.add(res);
            		//  list.add(res);
            		  
            		  String questResponse2 = "odp2";
            		  ResponseChoice res2 = new ResponseChoice(questResponse2);
            		  res2.setIsSelected(false);
            		  res2.setQuestion(question);
            		  res2.save();
            		  question.responseChoice.add(res2);
            		 // list.add(res2); 
            		  
            		 // question.setAnswers(list);
            		  question.update();
            		  
            		//  survey.save();
            	        
            	        survey.question.add(question);
            	        survey.update();
            	  }   	  
   
        
        List<Question> allquestions = Question.find.select("*").where().eq("survey_id", 1).findList();
        Question arrayquest[] = new Question[allquestions.size()];
        arrayquest = allquestions.toArray(arrayquest);
        
        //ResponseChoice arrayResponse[] = new ResponseChoice[list.size()];
       // arrayResponse = list.toArray(arrayResponse);
        
     
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey));
       
    	
    	return ok(surveyJs);
    }
    
    public Result re3(){
    	
    	 Survey survey = Survey.find.byId(1);
        

         List<Question> allquestions = Question.find.select("*").where().eq("survey_id", 1).findList();

        

         String answer = "true||false||false";
 
                 Response reponse1 = new Response(answer);
                 reponse1.setAnswer(answer);
                 reponse1.survey = survey;
                 reponse1.question = allquestions.get(0);
                 reponse1.keyToUser = "dfs";
                 reponse1.save();
                 
                 survey.response.add(reponse1);
             
     

                 JsonNode surveyJs = Json.toJson(new SurveyJson(survey));	
             	return ok(surveyJs);
    }
    
    public Result re2(){
    	
    	Question quest = Question.find.byId(1);
    	Survey survey = Survey.find.byId(1);
    	
    	  JsonNode surveyJs = Json.toJson(new SurveyJson(survey));
          
      	
      	return ok(surveyJs);
    }
    
    public Result re4(){
    List<ResponseJson> response = new ArrayList<ResponseJson>();
    List<Question> allQuestion = Question.find.where().eq("survey_id", 1).findList();
    for (Question q : allQuestion) {
        List<Response> allResponse = Response.find.where()
                .eq("survey_id", 1)
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
       // UserAccount ua = UserAccount.find.byId(login);

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
        JsonNode surveyJs = Json.toJson(new SurveyJson(survey/*, arrayquest*/));

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
    public ResponseJson responseQuestions[];
    
    public SurveyJson(Survey s/*, Question q[], ResponseChoice r[]*/) {
        id = s.id;
        name = s.name;
        description = s.description;
        email = s.email;
        this.questions = new QuestionJson[s.question.size()];
        this.responseQuestions = new ResponseJson[s.response.size()];
        //this.responseQuestions = new ResponseQuestionJson[r.length];
        int i = 0;
        for(Question q : s.question){
            this.questions[i] = new QuestionJson(q);  
            i++;
        }
        
        int i2 = 0;
        for(Response q2 : s.response){
            this.responseQuestions[i2] = new ResponseJson(q2,i2);  
            i2++;
        }
        
        
        Logger.info("Send survey:\n" + Json.toJson(this));
    }
    
 
}

class QuestionJson {
    public int id;
    public String question;
    public String questionType;
    public String[] possibleAnswers;
    
    public QuestionJson(Question q) {
        q = Question.find.byId(q.id);
        this.id = q.id;
        this.question = q.question;
        this.questionType = q.questionType;
        if(this.questionType.equals("multi")) {
        	   Logger.info("1: " + this.questionType.equals("multi")); 
            this.possibleAnswers = new String[q.responseChoice.size()];
            Logger.info("2: " + q.responseChoice.size()); 
            int i = 0;
            for(ResponseChoice rc : q.responseChoice){
                this.possibleAnswers[i] = rc.text;
                i++;
            }
        }
    }
}

class ResponseQuestionJson {
    public int id;
    public String response;
    public Boolean isSelected;
    public int questionId;
    
    public ResponseQuestionJson(ResponseChoice q) {
        this.id = q.id;
        this.response = q.text;
        this.isSelected = q.isSelected;
        this.questionId = q.question.id;
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
