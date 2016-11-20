package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.RegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);


        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                try {
                    final JSONObject jsonBody = new JSONObject();
                    jsonBody.put("login", username);
                    jsonBody.put("password", password);
                    final String requestBody = jsonBody.toString();

                    new NukeSSLCerts().nuke();
                    StringRequest stringRequest = new StringRequest(1, "https://survey-innoproject.herokuapp.com/app/login", new Response.Listener<String>() {


                        @Override
                        public void onResponse(String response) {
                            etUsername.setText("");
                            etPassword.setText("");

                            if (response.equals("Zalogowano")) {
                                Intent userAreaIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                userAreaIntent.putExtra("login", username);
                                LoginActivity.this.startActivity(userAreaIntent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(response)
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Try again")
                                    .setNegativeButton("OK", null)
                                    .create()
                                    .show();

                        }
                    }


                    ){
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            try {
                                String j = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "charset=utf-8"));
                                //String i = response.headers.get("PLAY-SESSION");
                                return Response.success(j,
                                        HttpHeaderParser.parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                return Response.error(new ParseError(e));
                            }
                        }




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




                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }




        });



    }



}
