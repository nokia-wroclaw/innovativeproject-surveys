package json;

import models.Question;
import models.ResponseChoice;
import play.Logger;

public class QuestionJson {
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