package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ShowSurveyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        final TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        final TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        final TextView tvAnswers = (TextView) findViewById(R.id.tvAnswers);

/*
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String question = intent.getStringExtra("question");
        final String answers = intent.getStringExtra("answers");

        String message = "Ankieta";
        tvMsg.setText(message+" "+id);
        tvQuestion.setText(question);
        tvAnswers.setText(answers);
*/

    }
}