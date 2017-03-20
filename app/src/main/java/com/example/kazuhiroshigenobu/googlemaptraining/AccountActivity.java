package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {


    Button buttonAddToielt;
    Button buttonFavorite;
    Button buttonYouWent;
    Button buttonYouAddd;

    Toolbar toolbar;
    TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

//        toolbar = (Toolbar) findViewById(R.id.app_bar3);
//        toolbar.setTitle("マイページ");
//
//        toolbar.setNavigationContentDescription("戻る");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonAddToielt = (Button) findViewById(R.id.buttonAddToilet);
        buttonFavorite = (Button) findViewById(R.id.buttonFavoriteList);
        buttonYouWent = (Button) findViewById(R.id.buttonToiletYouWent);
        buttonYouAddd = (Button) findViewById(R.id.buttonYouAdded);

        toolbar = (Toolbar) findViewById(R.id.app_bar4);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle4);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
                finish();
            }
        }
        );

        //SetOnClick

        buttonAddToielt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),AddToiletActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
