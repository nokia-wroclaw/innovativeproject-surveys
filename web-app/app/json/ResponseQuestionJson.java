package json;

import models.ResponseChoice;

public class ResponseQuestionJson {
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