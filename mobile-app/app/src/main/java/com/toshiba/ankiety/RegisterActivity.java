package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etLastName = (EditText) findViewById(R.id.etLastName);
        final EditText etLogin = (EditText) findViewById(R.id.etLogin);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etPassword2 = (EditText) findViewById(R.id.etPassword2);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String lastName = etLastName.getText().toString();
                final String login = etLogin.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String password2 = etPassword2.getText().toString();

                if(name.equals("")|| lastName.equals("")|| login.equals("")|| email.equals("")|| password.equals("")|| password2.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Complete all gaps")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                }
                else{

                    try {
                        final JSONObject jsonBody = new JSONObject();
                        jsonBody.put("firstName", name);
                        jsonBody.put("lastName", lastName);
                        jsonBody.put("login", login);
                        jsonBody.put("email", email);
                        jsonBody.put("password", password);
                        jsonBody.put("rePassword", password2);

                        new NukeSSLCerts().nuke();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, "https://survey-innoproject.herokuapp.com/app/user/" + login, jsonBody, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try{

                                    if (response.getString("message").equals("Registrated")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("Thank you for registration! Please activate account")
                                                .setNegativeButton("OK", null)
                                                .create()
                                                .show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

                        };

                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(stringRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });
    }
}