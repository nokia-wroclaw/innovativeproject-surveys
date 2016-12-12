package com.toshiba.ankiety;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        TextView tvInviteLink = (TextView) findViewById(R.id.tvInviteLink);
        Button bGo = (Button) findViewById(R.id.bGo);

        Intent intent = getIntent();
        String name = intent.getStringExtra("login");


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

        /*bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaActivity.this, GetSurveyActivity.class);
                UserAreaActivity.this.startActivity(intent);
            }
        });
        */
    }
}
