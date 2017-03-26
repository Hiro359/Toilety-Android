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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartSignUpActivity extends AppCompatActivity {


   // private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference databaseReference;
    EditText loginEmailText;
    EditText loginPasswordText;
    EditText userNameText;

    Button startLoginButton;
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_sign_up);



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
        userNameText = (EditText)findViewById(R.id.writeLoginUsername);
        startLoginButton = (Button) findViewById(R.id.buttonForStartingLogin);

        userNameText.setHint("User Name");
        loginEmailText.setHint("Email");
        loginPasswordText.setHint("Password");

        startLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemtRegister();
            }
        });
    }

    private void attemtRegister() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        loginEmailText.setError(null);
        loginPasswordText.setError(null);

        // Store values at the time of the login attempt.

        final String email = loginEmailText.getText().toString();
        final String password = loginPasswordText.getText().toString();
        final String userName = userNameText.getText().toString();


        //////I use the static email and password/....

//        final String email = "kazushige12343@gmail.com";
//        final String password = "kazu34565";


        boolean cancel = false;
        View focusView = null;









        ///

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



//            //Copy
//            firebase.auth().createUserWithEmailAndPassword(email, password).then(function(user) {
//                // [END createwithemail]
//                // callSomeFunction(); Optional
//                // var user = firebase.auth().currentUser;
//                user.updateProfile({
//                        displayName: username
//                }).then(function() {
//                    // Update successful.
//                }, function(error) {
//                    // An error happened.
//                });
//            }, function(error) {
//                // Handle Errors here.
//                var errorCode = error.code;
//                var errorMessage = error.message;
//                // [START_EXCLUDE]
//                if (errorCode == 'auth/weak-password') {
//                    alert('The password is too weak.');
//                } else {
//                    console.error(error);
//                }
//                // [END_EXCLUDE]
//            });
//

            //Copy

            // mAuthTask = new UserLoginTask(email, password);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch(com.google.firebase.auth.FirebaseAuthWeakPasswordException e) {
                                    showProgress(false);
                                    loginPasswordText.setError(getString(R.string.error_weak_password));
                                    Log.i("error",getString(R.string.error_weak_password));
                                    loginPasswordText.requestFocus();
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    showProgress(false);
                                    loginEmailText.setError(getString(R.string.error_invalid_email));
                                    loginEmailText.requestFocus();
                                    Log.i("error",getString(R.string.error_invalid_email));
                                } catch(FirebaseAuthUserCollisionException e) {
                                    showProgress(false);
                                    loginEmailText.setError(getString(R.string.error_user_exists));
                                    loginEmailText.requestFocus();
                                    Log.i("error",getString(R.string.error_user_exists));

                                } catch(Exception e) {
                                    Log.i("error",e.getMessage());
                                }

                                Log.i("HEre", "Here");
                                //User is successfully registered and logged in

                            } else if (task.isSuccessful()){

                                    FirebaseUser user = task.getResult().getUser();
                                    String newUserName = user.getUid();
                                    Log.i("Uid","onComplete: uid=" + user.getUid());

                                    //Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                    //Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                                    //finish();



                                        firebaseUpdate(newUserName, userName, email, password);

                                }
                                //startActivity(intent);

                        }
                    }
            );
            //mAuthTask.execute((Void) null);
        }
    }


    private void firebaseUpdate(String userID, String userName, String email, String password) {

        databaseReference = FirebaseDatabase.getInstance().getReference();


        //For just Development




        User user = new User(userName, password, "https://firebasestorage.googleapis.com/v0/b/problemsolving-299e4.appspot.com/o/images%2Fdefault%20picture.png?alt=media&token=b407a188-5a9d-4b0f-8b43-3bf6c2060573", email, 0, 0, 0);

//        FirebaseUser userID = firebaseAuth.getCurrentUser();
//        String userid = userID.getUid();


        Log.i("User","onComplete: uid=" + user +"FIreCalled21");

//        databaseReference.child("HUHU").setValue(user);
//        databaseReference.child("JUJU").setValue("JUHU");

        databaseReference.child("users").child(userID).setValue(user);




        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        finish();
        startActivity(intent);


        //should be changed puch() to getUserId......
        //March 25



        //DatabaseReference userRef = mDatabase.child("users").push(user);


        //userRef.setValue(new Post(email,password,"https://firebasestorage.googleapis.com/v0/b/problemsolving-299e4.appspot.com/o/images%2Fdefault%20picture.png?alt=media&token=b407a188-5a9d-4b0f-8b43-3bf6c2060573",email,0,0,0));


        //Auto id should be added
        //private EditText mPasswordView;


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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
