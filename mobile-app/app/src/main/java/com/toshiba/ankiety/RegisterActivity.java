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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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
                        final String requestBody = jsonBody.toString();

                        new NukeSSLCerts().nuke();
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://survey-innoproject.herokuapp.com/app/user/"+login, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equals("Registrated")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Thank you for registration! Please activate account")
                                            .setNegativeButton("OK", null)
                                            .create()
                                            .show();
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage(response)
                                            .setNegativeButton("OK", null)
                                            .create()
                                            .show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Try again")
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
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