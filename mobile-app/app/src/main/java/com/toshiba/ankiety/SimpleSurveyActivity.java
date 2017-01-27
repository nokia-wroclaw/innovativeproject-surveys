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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleSurveyActivity extends AppCompatActivity {

    JSONArray ja = new JSONArray();


    JSONObject mainObj = new JSONObject();

    List<String> AnwserList =  new ArrayList<>();

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



        try{

            JSONObject response = new JSONObject(j);

            final String iD = response.get("id").toString();

            Log.d("j", response.get("name").toString());
            tvMsg.setText("Survey name: " + response.get("name").toString());

           JSONArray a = response.getJSONArray("questions");
            Log.d("j", response.getJSONArray("questions").toString());

            for(int is = 0; is < a.length(); ++is){
                AnwserList.add("false||");
            }

            for ( int i = 0; i < a.length(); ++i) {

                JSONObject rec = a.getJSONObject(i);
                int id = rec.getInt("id");
                String ques = rec.getString("question");
                String type = rec.getString("questionType");



                    // Create LinearLayout
                    LinearLayout ll = new LinearLayout(this);
                    ll.setOrientation(LinearLayout.VERTICAL);

                    // Create TextView
                    TextView qu = new TextView(this);
                    qu.setText(ques);
                    ll.addView(qu);

                if (type.equals("open")){
                        // Create TextView
                        final EditText ans = new EditText(this);
                        ans.setText("");
                        ans.setId(i );
                        ans.setHint("Enter your answer");
                        final String s = ans.getText().toString();
                        ans.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {

                                   //JSONObject jsonans = new JSONObject();
                                    AnwserList.set(ans.getId(),ans.getText().toString());
                                    //jsonans.put("answer", ans.getText());
                                   // jsonans.put("id", ans.getId());
                                   // ja.put(jsonans);

                            }
                        });
                        ll.addView(ans);
                    }
                else{
                    if (type.equals("true/false")){
                        RadioGroup rg = new RadioGroup(this);
                        rg.setId(i);
                        ll.addView(rg);

                                final RadioButton r = new RadioButton(this);
                                r.setText("Yes");
                                r.setId(i );
                        final int rid = r.getId();

                        r.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {

                                    if (r.isChecked()) {
                                       // JSONObject jsonans = new JSONObject();
                                       AnwserList.set(rid,"true");
                                      //  jsonans.put("answer", "true");
                                      //  jsonans.put("id", rid);
                                      //  ja.put(jsonans);
                                    }

                            }
                        });
                        rg.addView(r);

                                final RadioButton r2 = new RadioButton(this);
                                r2.setText("No");
                        r2.setId(i);
                                final int r2id = r2.getId();

                        r2.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {

                                             if(r2.isChecked()){
                                                 //AnwserList.set(ans.getId(),"false");
                                                 AnwserList.set(r2id,"false");
                                             }


                                    }
                                });
                                rg.addView(r2);
                    }
                    else{
                        try{
                            JSONArray possible = rec.getJSONArray("possibleAnswers");
                            final List<String> multiAnwserList =  new ArrayList<>();
                             String joinMultans="";

                            for(int test=0; test < possible.length() ; ++test){
                                multiAnwserList.add("false||");
                                joinMultans = joinMultans + "false||";
                                if(test== (possible.length()-1)) {
                                    //joinMultans = joinMultans + "false||";// String.join("",multiAnwserList);
                                    AnwserList.set(i,joinMultans);
                                }}

                            for (int l = 0; l < possible.length(); ++l) {
                                String possAnsw = possible.getString(l);

                                final int numAns = l;
                                final CheckBox cb = new CheckBox(this);
                                cb.setText(possAnsw);
                                cb.setId(i );


                                final int ansChoiceId = cb.getId();

                                cb.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                       //multiAnwserList.set(ansChoiceId,"True||");
                                        //string ss = joinMultans;
                                       // String joinMultAns = String.join("",multiAnwserList);
                                        int tests = numAns;
                                        String mergeString="";
                                        System.out.println("to num:" + tests);
                                        for(int t2=0; t2 < multiAnwserList.size(); ++t2) {
                                            if(t2!= tests) {
                                                mergeString = mergeString + multiAnwserList.get(t2);
                                            }else{mergeString = mergeString + "true||";multiAnwserList.set(t2,"true||");}

                                        }AnwserList.set(ansChoiceId, mergeString);
                                            //JSONObject jsonans = new JSONObject();
                                           // multiAnwserList.set(cb.getID(),"TRUE||");
                                           // multiAnwserList.get(cb.getID());
                                            //jsonans.putOpt()
                                            //jsonans.put("answer", cb.getText());
                                           // jsonans.put("id", cb.getId());
                                           // ja.put(jsonans);

                                    }

                                });
                                ll.addView(cb);

                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }

                    }

                    }

                    //Add button to LinearLayout defined in XML
                    lm.addView(ll);

            }


            bSendAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                     for(int c=0; c<AnwserList.size();++c){
                                JSONObject jsonans = new JSONObject();
                                jsonans.put("answer", AnwserList.get(c));
                                 jsonans.put("id", c+1);
                                ja.put(jsonans);
                     }
                        mainObj.put("Answers", ja);

                        Log.d("JSONANS", mainObj.toString());


                        new NukeSSLCerts().nuke();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(1, "http://10.0.2.2:9000/app/surveys/" + iD + "/answer", mainObj, new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("message") != null) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(response.getString("message"))
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(response.toString())
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                NetworkResponse networkResponse = error.networkResponse;

                                if (networkResponse != null && networkResponse.statusCode == 403) {

                                    String a = new String(networkResponse.data);
                                    try {
                                        JSONObject jsonObj = new JSONObject(a);
                                        String b = jsonObj.getString("message");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SimpleSurveyActivity.this);
                                        builder.setMessage(b)
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        ) {

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