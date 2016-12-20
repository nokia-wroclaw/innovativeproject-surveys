package com.toshiba.ankiety;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ShowSurveyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        final TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        final TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        final TextView tvAnswers = (TextView) findViewById(R.id.tvAnswers);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String name = intent.getStringExtra("name");

        tvMsg.setText(name);


        tvAnswers.setText("Answers:\n\n");
        new NukeSSLCerts().nuke();
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/"+id+"/admin/result", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                //tvAnswers.setText(response.toString());
                Log.d("resp", response.toString());

                try {
                    int idmax=0;
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject rec = response.getJSONObject(i);
                        int id = Integer.parseInt(rec.getString("id")) ;
                        if(idmax<id)idmax=id;
                    }
                    //idmax+=1;

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject rec = response.getJSONObject(i);
                        String ans = rec.getString("answer");
                        int id = Integer.parseInt(rec.getString("id")) ;


                        tvAnswers.setText(tvAnswers.getText()+ ans+"\n");
                        if (id==idmax)tvAnswers.setText("\n\n"+tvAnswers.getText()+ "Next question "+"\n\n");

                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }



                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode != 200) {

                    String a = new String(networkResponse.data);
                    try{

                        JSONObject jsonObj = new JSONObject(a);
                        String b = jsonObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowSurveyActivity.this);
                        builder.setMessage(b)
                                .setNegativeButton("OK", null)
                                .create()
                                .show();

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }


            SharedPreferences preferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("PLAY-SESSION", preferences.getString("PLAY-SESSION", ""));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ShowSurveyActivity.this);
        queue.add(stringRequest);

    }
}