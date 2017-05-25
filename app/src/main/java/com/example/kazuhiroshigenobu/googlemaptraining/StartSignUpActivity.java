package com.example.kazuhiroshigenobu.googlemaptraining;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StartSignUpActivity extends AppCompatActivity {


   // private FirebaseAuth.AuthStateListener mAuthListener;

//    private DatabaseReference databaseReference;
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

                                } else {
                                showProgress(false);
                                messageUnSuccessful();
                            }
                                //startActivity(intent);

                        }
                    }
            );
            //mAuthTask.execute((Void) null);
        }
    }


    @SuppressWarnings("unchecked")
    private void firebaseUpdate(String userID, String userName, String email, String password) {

        //DatabaseReference databaseReference;
        //databaseReference = FirebaseDatabase.getInstance().getReference();


        //For just Development

        Map<String, Object> userPublicData = new HashMap();

        userPublicData.put("userPhoto", "");
        userPublicData.put("userName", userName);
        userPublicData.put("totalFavoriteCount", 0);
        userPublicData.put("totalHelpedCount", 0);
        userPublicData.put("totalLikedCount", 0);


        Map<String, Object> userPrivateData = new HashMap();
        userPrivateData.put("userEmail", email);
        userPrivateData.put("password", password);

        Map<String, Object> updateData = new HashMap();
        updateData.put("Users/" + userID, userPublicData);
        updateData.put("UserPrivateInfo/" + userID, userPrivateData);

        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();


        firebaseRef.updateChildren(updateData,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


            }
        });





        // User user = new User(userName, password, "", email, 0, 0, 0);

//        FirebaseUser userID = firebaseAuth.getCurrentUser();
//        String userid = userID.getUid();


        //Log.i("User","onComplete: uid=" + user +"FIreCalled21");

//        databaseReference.child("HUHU").setValue(user);
//        databaseReference.child("JUJU").setValue("JUHU");

        //databaseReference.child("Users").child(userID).setValue(user);




        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        finish();
        startActivity(intent);


        //should be changed puch() to getUserId......
        //March 25



        //DatabaseReference userRef = mDatabase.child("Users").push(user);


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

    private void messageUnSuccessful()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("You could not login");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"はい"},
                new DialogInterface.OnClickListener()

                {
                    public void onClick (DialogInterface dialog,int which){
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
