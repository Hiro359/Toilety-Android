package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangeUserNameActivity extends AppCompatActivity {

    //TextView userNameTextView;
    EditText userNameTextView;
    Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        layoutSettings();
    }

    private void layoutSettings() {

        Toolbar toolbar;

        toolbar = (Toolbar) findViewById(R.id.app_bar_change_user_name);

        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     goBackToAccountView();
                                                 }
                                             }
        );

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        userNameTextView = (EditText) findViewById(R.id.userNameEditText);


        if (!UserInfo.userName.equals("")) {

            userNameTextView.setText(UserInfo.userName);
        }

        saveButton = (Button) findViewById(R.id.userNameSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkUserNameAction();
            }
        });


    }

    private void goBackToAccountView() {

        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
        finish();


    }

    private void checkUserNameAction() {

        String newUserName = userNameTextView.getText().toString();

        if (newUserName.equals("")) {
            showErrorMessage("Type your name");

        } else if (newUserName.isEmpty()) {
            showErrorMessage("Type your name");

        } else if (newUserName.length() < 3) {
            showErrorMessage("Name Too Short");

        } else {

            saveUserNameAction();
        }


        //Check


    }

    private void showErrorMessage(String errorString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle(errorString);
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"はい"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                break;
                        }
                    }
                });
        builder.create().show();

    }

    private void saveUserNameAction() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            String userId = user.getUid();

            Map<String, Object> childUpdates = new HashMap<>();

            String newName = userNameTextView.getText().toString();

            childUpdates.put("userName", newName);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            userRef.child(userId).updateChildren(childUpdates);

            goBackToAccountView();


        }


    }

}
