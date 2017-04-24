package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {


    Button buttonAddToielt;
    Button buttonFavorite;
    Button buttonYouWent;
    Button buttonYouAddd;

    Toolbar toolbar;
    TextView toolbarTitle;
    TextView accountNameText;
    TextView likeCountText;
    TextView favoriteCountText;
    TextView helpedCountText;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;
    Boolean userAlreadyLogin = false;



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


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {



            Log.i("Already Loged in", "");
            userAlreadyLogin = true;
            String userID = firebaseAuth.getCurrentUser().getUid();
            getUserInfo(userID);

        }

        buttonAddToielt = (Button) findViewById(R.id.buttonAddToilet);
        buttonFavorite = (Button) findViewById(R.id.buttonFavoriteList);
        buttonYouWent = (Button) findViewById(R.id.buttonToiletYouWent);
        buttonYouAddd = (Button) findViewById(R.id.buttonYouAdded);
        accountNameText = (TextView) findViewById(R.id.userAccountName);
        likeCountText = (TextView) findViewById(R.id.likeCount);
        favoriteCountText =  (TextView) findViewById(R.id.favoriteCount);
        helpedCountText = (TextView) findViewById(R.id.helepedCount);


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
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), AddToiletActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), FavotiteListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonYouWent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), UserWentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonYouAddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), CommentListView.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.accountbuttonmenu, menu);
        //Commented for adding below code at 5pm
        //getMenuInflater().inflate(R.menu.filter,amvMenu.getMenu());


//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menufile);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        if (id == R.id.settingButton) {

            if (!userAlreadyLogin){

                loginPlease();
                return true;
            } else {

                Toast.makeText(this, "Hey Did you Click Setting??", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SettingViewActivity.class);
                startActivity(intent);
                finish();
                return true;
            }


        }  else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

           return super.onOptionsItemSelected(item);
        }
    }

    private void loginPlease(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ログインをしますか？");
        builder.setItems(new CharSequence[]
                        {"はい", "いいえ"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
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
    private void getUserInfo(String userID){
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        //addValueEventListener(new ValueEventListener() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.child("userName").getValue();
                Long likeNumber = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long favoriteNumber = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long helpedNumber = (Long) dataSnapshot.child("totalHelpedCount").getValue();


//                String likeString = Long.toString(likeNumber);
//                String favoString = Long.toString(favoriteNumber);
//                String helpString = Long.toString(helpedNumber);
//
//                //String likeString = likeNumber.toString();
////                String favoString = favoriteNumber.toString();
////                String helpString = helpedNumber.toString();

                String likeString = String.valueOf(likeNumber);
                String favoString = String.valueOf(favoriteNumber);
                String helpString = String.valueOf(helpedNumber);

                Log.i("likeString", likeString);
                Log.i("favoString", favoString);
                Log.i("helpString", helpString);





                accountNameText.setText(userName);
                likeCountText.setText(likeString);
                favoriteCountText.setText(favoString);
                helpedCountText.setText(helpString);




                 Toast.makeText(AccountActivity.this, userName, Toast.LENGTH_SHORT).show();
                 Log.i("USERNAME", userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
