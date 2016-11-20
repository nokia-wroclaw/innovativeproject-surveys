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

public class GetSurveyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_survey);

        final TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        final TextView etSurveyId = (TextView) findViewById(R.id.etSurveyId);
        final Button bComplete = (Button) findViewById(R.id.bComplete);
        final Button bShow = (Button) findViewById(R.id.bShow);


        bComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = etSurveyId.getText().toString();
                String question ="question";

                Intent registerIntent = new Intent(GetSurveyActivity.this, SimpleSurveyActivity.class);
                registerIntent.putExtra("id", id);
                registerIntent.putExtra("question", question);
                GetSurveyActivity.this.startActivity(registerIntent);

/*
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", id);
                    final String requestBody = jsonBody.toString();

                    new NukeSSLCerts().nuke();
                    StringRequest stringRequest = new StringRequest(2, "https://survey-innoproject.herokuapp.com/surveys/"+id, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(GetSurveyActivity.this);
                            builder.setMessage(response)
                                    .setNegativeButton("OK", null)
                                    .create()
                                    .show();

                            String question="";

                            //to do
                            //odpowiedz z serwera
                            //json z arkuszem ankiety


                            Intent registerIntent = new Intent(GetSurveyActivity.this, SimpleSurveyActivity.class);
                            registerIntent.putExtra("id", id);
                            registerIntent.putExtra("question", question);
                            GetSurveyActivity.this.startActivity(registerIntent);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return String.format("application/json; charset=utf-8");
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                        requestBody, "utf-8");
                                return null;
                            }
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(GetSurveyActivity.this);
                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
 */
            }


        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = etSurveyId.getText().toString();


                Intent showIntent = new Intent(GetSurveyActivity.this, ShowSurveyActivity.class);
                showIntent.putExtra("id", id);
                GetSurveyActivity.this.startActivity(showIntent);
/*
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", id);
                    final String requestBody = jsonBody.toString();

                    new NukeSSLCerts().nuke();
                    StringRequest stringRequest = new StringRequest(2, "https://survey-innoproject.herokuapp.com/surveys/"+id+"result", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(GetSurveyActivity.this);
                            builder.setMessage(response)
                                    .setNegativeButton("OK", null)
                                    .create()
                                    .show();

                            String question="";
                            String answers="";



                            //to do
                            //odpowiedz z serwera
                            //json z arkuszem ankiety


                            Intent showIntent = new Intent(GetSurveyActivity.this, ShowSurveyActivity.class);
                            showIntent.putExtra("id", id);
                            showIntent.putExtra("question", question);
                            showIntent.putExtra("answers", answers);
                            GetSurveyActivity.this.startActivity(showIntent);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return String.format("application/json; charset=utf-8");
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                        requestBody, "utf-8");
                                return null;
                            }
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(GetSurveyActivity.this);
                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
            }

        });


    }
}