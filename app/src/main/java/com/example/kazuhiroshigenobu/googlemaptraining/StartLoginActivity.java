package com.example.kazuhiroshigenobu.googlemaptraining;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartLoginActivity extends AppCompatActivity {

    EditText loginEmailText;
    EditText loginPasswordText;
    Button startLoginButton;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login);

        toolbar = (Toolbar) findViewById(R.id.defaultappbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }


        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FirstTimeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        loginEmailText = (EditText) findViewById(R.id.writeLoginEmail);
        loginPasswordText = (EditText) findViewById(R.id.writeLoginPassword);
        startLoginButton = (Button) findViewById(R.id.buttonForStartingLogin);

        loginEmailText.setHint("Email");
        loginPasswordText.setHint("Password");

        startLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        // Reset errors.
        loginEmailText.setError(null);
        loginPasswordText.setError(null);

        // Store values at the time of the login attempt.
//        final String email = mEmailView.getText().toString();
//
//        final String password = mPasswordView.getText().toString();

//        final String email = "kazushige12343@gmail.com";
//        final String password = "kazu34565";

        final String email = "kazukazu133@gmail.com";
        final String password = "change123";



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            loginPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = loginPasswordText;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            loginEmailText.setError(getString(R.string.error_field_required));
            focusView = loginEmailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            loginEmailText.setError(getString(R.string.error_invalid_email));
            focusView = loginEmailText;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            // mAuthTask = new UserLoginTask(email, password);
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);


                        finish();
                        startActivity(intent);


                    } else {


                    }


                }
            });
            // mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

//    **
//            * Shows the progress UI and hides the login form.
//    */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
