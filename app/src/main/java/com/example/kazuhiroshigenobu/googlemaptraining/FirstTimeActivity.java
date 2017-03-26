package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstTimeActivity extends AppCompatActivity {

    Button skipButton;
    Button loginButton;
    Button signUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        skipButton = (Button)findViewById(R.id.buttonForSkip);
        loginButton =  (Button)findViewById(R.id.buttonForLogin);
        signUpButton = (Button)findViewById(R.id.buttonForSingUp);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartLoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartSignUpActivity.class);
                startActivity(intent);
                finish();


            }
        });




    }
}
