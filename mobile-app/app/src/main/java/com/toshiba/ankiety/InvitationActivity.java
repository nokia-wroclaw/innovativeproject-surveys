package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class InvitationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        final TextView tvMsg = (TextView) findViewById(R.id.tvMsg);
        final TextView etEmail = (TextView) findViewById(R.id.etEmail);
        final Button bInvite = (Button) findViewById(R.id.bInvite);

        String message = "Invitation";
        tvMsg.setText(message);

        bInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = etEmail.getText().toString();

                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("email", mail);
                    final String requestBody = jsonBody.toString();

                    new NukeSSLCerts().nuke();
                    JsonObjectRequest stringRequest = new JsonObjectRequest(1, "https://survey-innoproject.herokuapp.com/app/invitation",jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try{
                                AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
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

                    RequestQueue queue = Volley.newRequestQueue(InvitationActivity.this);
                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }
}