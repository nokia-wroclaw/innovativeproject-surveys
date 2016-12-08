package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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



                    new NukeSSLCerts().nuke();
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/"+id, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Intent intent = new Intent(GetSurveyActivity.this, SimpleSurveyActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("json", response.toString());

                            GetSurveyActivity.this.startActivity(intent);

                            Log.d("json",response.toString());

                            try{
                                AlertDialog.Builder builder = new AlertDialog.Builder(GetSurveyActivity.this);
                                builder.setMessage(response.getString("message"))
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GetSurveyActivity.this);
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

                    RequestQueue queue = Volley.newRequestQueue(GetSurveyActivity.this);
                    queue.add(stringRequest);




            }


        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = etSurveyId.getText().toString();


                Intent showIntent = new Intent(GetSurveyActivity.this, ShowSurveyActivity.class);
                showIntent.putExtra("id", id);
                GetSurveyActivity.this.startActivity(showIntent);

            }

        });


    }
}