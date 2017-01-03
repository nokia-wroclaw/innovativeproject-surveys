package json;

import json.QuestionJson;
import models.Question;
import models.Survey;
import play.Logger;
import play.libs.Json;

public class SurveyJson {
	public Integer id;

    public String name;
    public String description;
    public String email;
    public QuestionJson questions[];
    
    public SurveyJson(Survey s) {
        id = s.id;
        name = s.name;
        description = s.description;
        email = s.email;
        this.questions = new QuestionJson[s.question.size()];
      
        int i = 0;
        for(Question q : s.question){
            this.questions[i] = new QuestionJson(q);  
            this.questions[i].id = i+1; 
            i++;
        }  
        Logger.info("Send survey:\n" + Json.toJson(this));
    }
}