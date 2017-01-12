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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SimpleSurveyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_survey);


        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);

        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        final Button bSendAnswer = (Button) findViewById(R.id.bSendAnswer);

        Intent intent = getIntent();
        final String j = intent.getStringExtra("json");
        Log.d("JSON", j);

        final JSONObject jsonans = new JSONObject();

        try{

            JSONObject response = new JSONObject(j);

            final String iD = response.get("id").toString();

            Log.d("j", response.get("name").toString());
            tvMsg.setText("Survey name: "+response.get("name").toString());

           JSONArray a = response.getJSONArray("questions");
            Log.d("j", response.getJSONArray("questions").toString());

            for (int i = 0; i < a.length(); ++i) {
                JSONObject rec = a.getJSONObject(i);
                int id = rec.getInt("id");
                String ques = rec.getString("question");


                    // Create LinearLayout
                    LinearLayout ll = new LinearLayout(this);
                    ll.setOrientation(LinearLayout.VERTICAL);

                    // Create TextView
                    TextView qu = new TextView(this);
                    qu.setText(ques);
                    ll.addView(qu);

                    // Create TextView
                    EditText ans = new EditText(this);
                    ans.setText("");
                    ans.setHint("Enter your answer");
                    ans.setId(i);
                    ll.addView(ans);

                    //Add button to LinearLayout defined in XML
                    lm.addView(ll);

                    EditText aa = (EditText) findViewById(i);

                    jsonans.put("answer", aa.getText());
                }


            bSendAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        JSONArray ja = new JSONArray();
                        ja.put(jsonans);

                        JSONObject mainObj = new JSONObject();
                        mainObj.put("Answers", ja);



                        new NukeSSLCerts().nuke();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(1, "https://survey-innoproject.herokuapp.com/app/surveys/"+iD+"/answer", mainObj, new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {

                                try{
                                    if (response.getString("message")!=null) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(response.getString("message"))
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(response.toString())
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                NetworkResponse networkResponse = error.networkResponse;

                                if (networkResponse != null && networkResponse.statusCode == 403) {

                                    String a = new String(networkResponse.data);
                                    try{
                                        JSONObject jsonObj = new JSONObject(a);
                                        String b = jsonObj.getString("message");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(b)
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();

                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        ){

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

                        RequestQueue queue = Volley.newRequestQueue(SimpleSurveyActivity.this);
                        queue.add(stringRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });

        }catch(JSONException e){
                e.printStackTrace();
        }

    }
}