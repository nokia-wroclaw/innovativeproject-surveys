package com.toshiba.ankiety;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserAreaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        TextView tvInviteLink = (TextView) findViewById(R.id.tvInviteLink);
        final TextView tvInfo = (TextView) findViewById(R.id.tvInfo);
        final TextView tvInfo2 = (TextView) findViewById(R.id.tvInfo2);

        Intent intent = getIntent();
        String name = intent.getStringExtra("login");

        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);
        final LinearLayout lm2 = (LinearLayout) findViewById(R.id.linearMain2);

        final Button bLogout = (Button) findViewById(R.id.bLogout);

        SharedPreferences preferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        preferences.getString("cookie", "");

        TextView tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);

        // Display user details
        String message = name + "! Welcome to survey app with anonimity ";
        tvWelcomeMsg.setText(message);

        tvInviteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invitationIntent = new Intent(UserAreaActivity.this, InvitationActivity.class);
                UserAreaActivity.this.startActivity(invitationIntent);
            }
        });


            new NukeSSLCerts().nuke();

            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/result/AdminList", null, new Response.Listener<JSONArray>() {


                @Override
                public void onResponse(JSONArray response) {

                    try{
                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject rec = response.getJSONObject(i);
                            final int id = rec.getInt("id");
                            final String name = rec.getString("name");

                            tvInfo.setText("You can manage:");

                            // Create LinearLayout
                            LinearLayout ll = new LinearLayout(UserAreaActivity.this);
                            ll.setOrientation(LinearLayout.HORIZONTAL);

                            // Create TextView
                            TextView qu = new TextView(UserAreaActivity.this);
                            qu.setText(name);
                            ll.addView(qu);

                            // Create button
                            Button ans = new Button(UserAreaActivity.this);
                            ans.setText("");

                            ans.setHint("Results");
                            ans.setId(i);
                            ans.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent invitationIntent = new Intent(UserAreaActivity.this, ShowSurveyActivity.class);
                                    invitationIntent.putExtra("id", Integer.toString(id));
                                    invitationIntent.putExtra("name", name);
                                    UserAreaActivity.this.startActivity(invitationIntent);
                                }
                            });

                            ll.addView(ans);

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

                    if (networkResponse != null && networkResponse.statusCode == 403) {

                        String a = new String(networkResponse.data);
                        try{
                            JSONObject jsonObj = new JSONObject(a);
                            String b = jsonObj.getString("message");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
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

                    params.put("PLAY_SESSION", preferences.getString("username", ""));
                    return params;
                }


            };



        JsonArrayRequest strinRequest = new JsonArrayRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/result/UserList", null, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                try{
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject rec = response.getJSONObject(i);
                        final int id = rec.getInt("id");
                        final String name = rec.getString("name");
                        // ...
                        tvInfo2.setText("You can answer:");
                        Log.d("aaaaa", response.toString());


                        // Create LinearLayout
                        LinearLayout ll = new LinearLayout(UserAreaActivity.this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);


                        // Create TextView
                        TextView qu = new TextView(UserAreaActivity.this);
                        qu.setText(name);
                        ll.addView(qu);

                        // Create button
                        Button ans = new Button(UserAreaActivity.this);
                        ans.setText("");

                        ans.setHint("Fill/View");
                        ans.setId(i);
                        ans.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new NukeSSLCerts().nuke();
                                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, "https://survey-innoproject.herokuapp.com/app/surveys/"+id, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Intent intent = new Intent(UserAreaActivity.this, SimpleSurveyActivity.class);
                                        intent.putExtra("id", id);
                                        intent.putExtra("json", response.toString());

                                        UserAreaActivity.this.startActivity(intent);

                                        Log.d("json",response.toString());

                                        try{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
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
                                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
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

                                        params.put("Set-Cookie", preferences.getString("cookie", ""));
                                        return params;
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                                queue.add(stringRequest);

                            }

                        });

                        ll.addView(ans);

                        //Add button to LinearLayout defined in XML
                        lm2.addView(ll);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
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

                params.put("PLAY-SESSION", preferences.getString("PLAY-SESSION", ""));
                return params;
            }


        };


        RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        queue.add(strinRequest);
        queue.add(stringRequest);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                UserAreaActivity.this.startActivity(intent);
            }
        });
    }
}
