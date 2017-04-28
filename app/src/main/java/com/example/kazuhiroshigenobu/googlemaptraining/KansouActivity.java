package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class KansouActivity extends AppCompatActivity {


    Switch availableSwitch;
    Spinner waitingSpinner;
    RatingBar kansouRaitng;
    EditText kansouText;
    Button buttonKansouAdd;
    ArrayAdapter<CharSequence> adapter1;

    private DatabaseReference toiletRef;
    private DatabaseReference reviewsRef;
    private DatabaseReference toiletReviewRef;
    private DatabaseReference reviewListRef;
    long originalReviewCount;
    long originalAverageWait;
    String originalAverageStar;
    String originalReviewOne;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    Toilet toilet =  new Toilet();
    String newRid = UUID.randomUUID().toString();


    //private Toilet toilet = new Toilet();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kansou);
        availableSwitch = (Switch) findViewById(R.id.switchAvailableCheck);
        waitingSpinner = (Spinner) findViewById(R.id.kansouWaitingTime);
        kansouRaitng = (RatingBar) findViewById(R.id.kansouStarRateBar);
        kansouText = (EditText) findViewById(R.id.kansouText);
        buttonKansouAdd = (Button) findViewById(R.id.buttonAddKansou);

        adapter1 = ArrayAdapter.createFromResource(this,R.array.waitingTimeArray,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waitingSpinner.setAdapter(adapter1);

        toolbar = (Toolbar) findViewById(R.id.kansou_app_bar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.kansouAppBarTitle);

        toilet.key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.latitude = getIntent().getDoubleExtra("toiletLatitude",0);
        toilet.longitude = getIntent().getDoubleExtra("toiletLongitude",0);
        toilet.reviewOne = getIntent().getStringExtra("reviewOne");



        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", toilet.key);
                        intent.putExtra("toiletLatitude",toilet.latitude);
                        intent.putExtra("toiletLongitude",toilet.longitude);
                        startActivity(intent);
                        finish();
                    }
                }
        );


        settingReady();

        originalReviewCount = getIntent().getIntExtra("reviewCount",0);
        originalAverageWait = getIntent().getIntExtra("avereageWait",0);
        originalAverageStar = getIntent().getStringExtra("averageStar");
        originalReviewOne = getIntent().getStringExtra("reviewOne");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.kansou_view_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.postKansou) {
            Toast.makeText(this, "Hey Did you post Kansou??", Toast.LENGTH_SHORT).show();

            newAvStarAvWaitReviewCountUpload();
            reviewDataUpload();

            Intent intent = new Intent(getApplicationContext(),DetailViewActivity.class);
            intent.putExtra("EXTRA_SESSION_ID", toilet.key);
            intent.putExtra("toiletLatitude",toilet.latitude);
            intent.putExtra("toiletLongitude",toilet.longitude);
            startActivity(intent);
            finish();


            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private void settingReady(){

        availableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     //true

                } else{
                   //false
                }
            }
        });


        waitingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

                ((TextView) parent.getChildAt(0)).setText("待ち時間  " + parent.getItemAtPosition(position) + "分");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kansouText.setHint("Hint (Placeholder) created programmatically");
        kansouText.setMaxLines(Integer.MAX_VALUE);
        kansouText.setHorizontallyScrolling(false);

        availableSwitch.setChecked(true);





    }

    private void newAvStarAvWaitReviewCountUpload(){
        //get averageStar, averageWait, and reviewCount so that we could calculate values after user posts kansou

        Log.i("originalAverageStar",originalAverageStar);
        Log.i("originalAverageWait",String.valueOf(originalAverageWait));
        Log.i("originalReviewCount",String.valueOf(originalReviewCount));


        double originalAvStarDouble = Double.parseDouble(originalAverageStar);
        int originalWatingTime = Integer.parseInt(String.valueOf(originalAverageWait));

        // i think i dont need to convert int to int anyway..
        double ratingValue = kansouRaitng.getRating();
        int waitingUserInputValue = Integer.parseInt(waitingSpinner.getSelectedItem().toString());

        double newAvStarDouble = 3.0;
        long newWaitingTime = 3;

        long newReviewCount = originalReviewCount + 1;

        //star
        if (newReviewCount > 9){


            double x = ratingValue - originalAvStarDouble;
            double changingValue = x / 10;
            newAvStarDouble = originalAvStarDouble + changingValue;



        } else{

            double y = ratingValue - originalAvStarDouble;
            double changeingValue = y / newReviewCount;
            newAvStarDouble = originalAvStarDouble + changeingValue;

        }
        //wait

        if (newReviewCount > 4){
            long x = waitingUserInputValue - originalWatingTime;
            long changingWaitValue = x / 5;
            newWaitingTime = originalWatingTime + changingWaitValue;


        } else{
            long y = waitingUserInputValue - originalWatingTime;
            long changingWaitValue = y / newReviewCount;
            newWaitingTime = originalWatingTime + changingWaitValue;

        }


        double roundedAverageStar = (double) Math.round(newAvStarDouble * 10) / 10;


        //So i should update newReviewCount,  newWaitingTime, and newWaitingTime.
        // I used long instead of int but i am not sure its right...
        // it should convert double to int

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
        DatabaseReference updateToiletRef = toiletRef.child(toilet.key);


        String newAvStarString = String.valueOf(roundedAverageStar);

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("reviewCount",newReviewCount);
        childUpdates.put("averageStar",newAvStarString);
        childUpdates.put("averageWait",newWaitingTime);
        childUpdates.put("reviewOne", newRid);
        childUpdates.put("reviewTwo", originalReviewOne);



        //childUpdates.put("editedBy",uid);

        //We dont need to updata editedBy uid....


        updateToiletRef.updateChildren(childUpdates);
    }

    private void reviewDataUpload(){

//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            //Go to login
//        }
//        else{
//            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.i("This is the UID", uid);


        long timeStamp = System.currentTimeMillis();
        Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));

        String ratingValue = String.valueOf(kansouRaitng.getRating());

        String dateString = getDate(timeStamp);
        //String dateString = toDate(timeStamp);
        Log.i("This is the dateString",dateString);


        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
        toiletReviewRef = FirebaseDatabase.getInstance().getReference().child("ToiletReviews");
        reviewListRef = FirebaseDatabase.getInstance().getReference().child("ReviewList");

        reviewsRef.child(newRid).setValue(new ReviewPost(
                availableSwitch.isChecked(),
                String.valueOf(kansouText.getText()),
                0,
                ratingValue,
                toilet.key,
                dateString,
                timeStampDouble,
                uid,
                waitingSpinner.getSelectedItem().toString()
        ));


        Log.i("before", "toiletReviewRef");
        Log.i("toilet.key", toilet.key);
        Log.i("newRid", newRid);
        toiletReviewRef.child(toilet.key).child(newRid).setValue(true);


        Log.i("before", "reviewListRef");
        reviewListRef.child(uid).child(newRid).setValue(true);



    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(time);

       // String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        Log.i("TIME121",DateFormat.format("yyyy-MM-dd", cal).toString());
        return DateFormat.format("yyyy-MM-dd", cal).toString();
    }
}


