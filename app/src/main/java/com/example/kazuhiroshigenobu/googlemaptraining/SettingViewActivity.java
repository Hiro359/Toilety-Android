package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingViewActivity extends AppCompatActivity {

    Button changeUserNameButton;
    Button changeUserPhotoButton;
    Button aboutThisAppButton;
    Button privacyButton;
    Button helpButton;
    Button logoutButton;

    Toolbar toolbar;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_view);
        toolbar = (Toolbar)findViewById(R.id.settings_view_app_bar);
        toolbarTitle = (TextView)findViewById(R.id.settingsViewToolbarTitle);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),AccountActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );




        changeUserNameButton = (Button)findViewById(R.id.buttonChangeUserName);

        changeUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangeUserNameActivity.class);
                moveToNextActivity(intent);

            }
        });
        changeUserPhotoButton = (Button)findViewById(R.id.buttonChangeUserPhoto);

        changeUserPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangeUserPhotoActivity.class);
                moveToNextActivity(intent);
            }
        });
        aboutThisAppButton  = (Button)findViewById(R.id.buttonAboutThisApp);
        privacyButton = (Button)findViewById(R.id.buttonPrivacyInfo);
        helpButton = (Button)findViewById(R.id.buttonShowHelp);
        logoutButton = (Button)findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }});
    }


    private void moveToNextActivity(Intent intent){
        startActivity(intent);
        finish();
    }

    private void signOut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ログアウトをしますか？");
        builder.setItems(new CharSequence[]
                        {"はい", "いいえ"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                // logout action firebase.....

                                FirebaseAuth.getInstance().signOut();
//                                        ref.unauth(); //End user session
                                Intent intent = new Intent(getApplicationContext(), FirstTimeActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();

    }



}
