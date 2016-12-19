package json;

import models.Response;

public class ResponseJson {
    public int id;
    public String answer;

    public ResponseJson(Response answer, int id) {
        this.answer = answer.answer;
        this.id = id;
    }
}