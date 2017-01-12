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
        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        new NukeSSLCerts().nuke();



        JsonObjectRequest strinRequest = new JsonObjectRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/"+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("resp", response.toString());

                    try {
                        tvMsg.setText(response.getString("name"));

                        JSONArray array = response.getJSONArray("questions");
                        for (int i = 0; i < array.length(); ++i) {

                            // Create LinearLayout
                            LinearLayout ll = new LinearLayout(ShowSurveyActivity.this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);

                            // Create TextView
                            TextView qu = new TextView(ShowSurveyActivity.this);
                            JSONObject rec = array.getJSONObject(i);
                            qu.setText(rec.getString("question")+"\n");
                            ll.addView(qu);

                            // Create TextView
                            TextView an = new TextView(ShowSurveyActivity.this);
                            an.setText(an.getText()+getAnswer(id, Integer.toString(i)));
                            ll.addView(an);

                            //Add button to LinearLayout defined in XML
                            lm.addView(ll);


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

                params.put("Cookie", preferences.getString("Cookies", ""));
                return params;
            }
        };



        RequestQueue queue = Volley.newRequestQueue(ShowSurveyActivity.this);
        queue.add(strinRequest);


    }

    String q;
    String getAnswer(String id, final String quesId){


        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/"+id+"/admin/result", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d("resp", response.toString());

                try{

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject rec = response.getJSONObject(i);
                        if(quesId.equals(Integer.toString(i)))
                        q=(rec.getString("answer"));
                        Log.d("odp1",q);


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

                params.put("Cookie", preferences.getString("Cookies", ""));
                return params;
            }
        };



        RequestQueue queue = Volley.newRequestQueue(ShowSurveyActivity.this);
        queue.add(stringRequest);

        return q;
    }
}