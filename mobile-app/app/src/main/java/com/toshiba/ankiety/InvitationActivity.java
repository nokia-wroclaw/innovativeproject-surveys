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

                AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
                builder.setMessage("Invitation email sent")
                        .setNegativeButton("OK", null)
                        .create()
                        .show();

                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("mail", mail);
                    final String requestBody = jsonBody.toString();

                    new NukeSSLCerts().nuke();
                    StringRequest stringRequest = new StringRequest(1, "https://survey-innoproject.herokuapp.com/app/invitation", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            etEmail.setText(response);

                                AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
                                builder.setMessage(response)
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
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

                    RequestQueue queue = Volley.newRequestQueue(InvitationActivity.this);
                    queue.add(stringRequest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }
}