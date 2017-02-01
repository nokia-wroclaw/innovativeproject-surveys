package com.toshiba.ankiety;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpStatus;

import android.content.Context;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES_NAME = "myPreferences";
    private static final String PREFERENCES_TEXT_FIELD = "textField";

    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Context context;
        context = getApplicationContext();
        preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);


        //SharedPreferences preferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

        if(preferences.getString("login", "").equals("logout")){
            Intent userAreaIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
            userAreaIntent.putExtra("login", preferences.getString("login", ""));


            LoginActivity.this.startActivity(userAreaIntent);
        }
        else{
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

                        new NukeSSLCerts().nuke();

                        JsonObjectRequest stringRequest = new JsonObjectRequest(1, "https://survey-innoproject.herokuapp.com/app/login", jsonBody, new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {
                                etUsername.setText("");
                                etPassword.setText("");

                                try{
                                    if (response.getString("login")!=null) {
                                        Intent userAreaIntent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                        userAreaIntent.putExtra("login", username);
                                        SharedPreferences.Editor preferencesEditor = preferences.edit();

                                        preferencesEditor.putString("login", username);

                                        LoginActivity.this.startActivity(userAreaIntent);
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                            @Override
                            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                                if (response.headers.containsKey("Set-Cookie")) {
                                    String cookie = response.headers.get("Set-Cookie");
                                    if (cookie.length() > 0) {
                                        String[] splitCookie = cookie.split(";");
                                        String[] splitSessionId = splitCookie[0].split("=");
                                        Log.d("cookie", splitSessionId[1] + "=" + splitSessionId[2]);

                                        SharedPreferences.Editor preferencesEditor = preferences.edit();
                                        preferencesEditor.putString("cookie", "PLAY-SESSION="+ splitSessionId[1]+"="+splitSessionId[2]);
                                        preferencesEditor.putString("PLAY-SESSION", username);
                                        preferencesEditor.putString("Cookies", splitCookie[0]);
                                        Log.d("COOKIES", cookie);
                                        preferencesEditor.commit();
                                    }
                                }

                                return super.parseNetworkResponse(response);
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
}
