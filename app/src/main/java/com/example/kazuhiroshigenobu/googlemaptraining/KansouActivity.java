package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
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

//    private DatabaseReference toiletRef;
//    private DatabaseReference reviewsRef;
//    private DatabaseReference toiletReviewRef;
//    private DatabaseReference reviewListRef;
    long originalReviewCount;
    long originalAverageWait;
    String originalAverageStar;
    String originalReviewOne;
    //private Toolbar toolbar;
    //private TextView toolbarTitle;
    Toilet toilet =  new Toilet();
    String newRid = UUID.randomUUID().toString();

    //Boolean toiletWarningLoadedOnce = false;



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

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.kansou_app_bar);
        //toolbarTitle = (TextView) toolbar.findViewById(R.id.kansouAppBarTitle);

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
            //reviewDataUpload();

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
                     Log.i("Do Something","");
                     //true

                } else{
                     Log.i("Do Something","");
                     isThereProblem();
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

    @SuppressWarnings("unchecked")
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

        double newAvStarDouble;
        long newWaitingTime;

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

        String newAvStarString = String.valueOf(roundedAverageStar);

        //So i should update newReviewCount,  newWaitingTime, and newWaitingTime.
        // I used long instead of int but i am not sure its right...
        // it should convert double to int

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
            //String uid = user.getUid();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String uid = user.getUid();



        long timeStamp = System.currentTimeMillis();
        Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));



            String ratingString = String.valueOf(ratingValue);

      //  String ratingValue = String.valueOf(kansouRaitng.getRating());

        String dateString = getDate(timeStamp);
        //String dateString = toDate(timeStamp);

        Map<String, Object> reviewInfoData = new HashMap();





        reviewInfoData.put("available", availableSwitch.isChecked());
        reviewInfoData.put("feedback",String.valueOf(kansouText.getText()));
        reviewInfoData.put("likedCount",0);
        reviewInfoData.put("star", ratingString);
        reviewInfoData.put("tid", toilet.key);
        reviewInfoData.put("time",dateString);
        reviewInfoData.put("timeNumbers", timeStampDouble);
        reviewInfoData.put("uid", uid);
        reviewInfoData.put("waitingtime",waitingSpinner.getSelectedItem().toString());


//        reviewsRef.child(newRid).setValue(new ReviewPost(
//                availableSwitch.isChecked(),
//                String.valueOf(kansouText.getText()),
//                0,
//                ratingValue,
//                toilet.key,
//                dateString,
//                timeStampDouble,
//                uid,
//                waitingSpinner.getSelectedItem().toString()
//        ));


        ///Get from Swift


        Map<String, Object> updateData = new HashMap();

        updateData.put("ReviewInfo/" + newRid, reviewInfoData);
        updateData.put("ReviewList/" + uid + "/" + newRid, true);
        updateData.put("ToiletReview/" + toilet.key + "/" + newRid, true);


        updateData.put("ToiletView/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("ToiletView/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("ToiletView/" + toilet.key + "/reviewCount", newReviewCount);
        updateData.put("ToiletView/" + toilet.key + "/reviewOne", newRid);
        updateData.put("ToiletView/" + toilet.key + "/reviewTwo", originalReviewOne);
            updateData.put("ToiletView/" + toilet.key + "/available", true);


        updateData.put("NoFilter/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("NoFilter/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("NoFilter/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("NoFilter/" + toilet.key + "/available", true);


//
//            updateData.put("ToiletUserList/" + toilet.key + "/averageStar", newAvStarString);
//        updateData.put("ToiletUserList/" + toilet.key + "/averageWait", newWaitingTime);
//        updateData.put("ToiletUserList/" + toilet.key + "/reviewCount", newReviewCount);
//            updateData.put("ToiletView/" + toilet.key + "/available", false);



            updateData.put("UnitOne/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitOne/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitOne/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitOne/" + toilet.key + "/available", true);


            updateData.put("UnitTwo/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitTwo/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitTwo/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitTwo/" + toilet.key + "/available", true);


            updateData.put("UnitThree/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitThree/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitThree/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitThree/" + toilet.key + "/available", true);


            updateData.put("UnitFour/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitFour/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitFour/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitFour/" + toilet.key + "/available", true);


            updateData.put("UnitFive/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitFive/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitFive/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitFive/" + toilet.key + "/available", true);


            updateData.put("UnitSix/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitSix/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitSix/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitSix/" + toilet.key + "/available", true);


            updateData.put("UnitSeven/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitSeven/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitSeven/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitSeven/" + toilet.key + "/available", true);


            updateData.put("UnitEight/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitEight/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitEight/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitEight/" + toilet.key + "/available", true);


            updateData.put("UnitNine/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitNine/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitNine/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitNine/" + toilet.key + "/available", true);


            updateData.put("UnitTen/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitTen/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitTen/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitTen/" + toilet.key + "/available", true);


            updateData.put("UnitEleven/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitEleven/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitEleven/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitEleven/" + toilet.key + "/available", true);


            updateData.put("UnitTwelve/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("UnitTwelve/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("UnitTwelve/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("UnitTwelve/" + toilet.key + "/available", true);



            updateData.put("GroupOne/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("GroupOne/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("GroupOne/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("GroupOne/" + toilet.key + "/available", true);


            updateData.put("GroupTwo/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("GroupTwo/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("GroupTwo/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("GroupTwo/" + toilet.key + "/available", true);


            updateData.put("GroupThree/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("GroupThree/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("GroupThree/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("GroupThree/" + toilet.key + "/available", true);



            updateData.put("HalfOne/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("HalfOne/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("HalfOne/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("HalfOne/" + toilet.key + "/available", true);



            updateData.put("HalfTwo/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("HalfTwo/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("HalfTwo/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("HalfTwo/" + toilet.key + "/available", true);



            updateData.put("AllFilter/" + toilet.key + "/averageStar", newAvStarString);
        updateData.put("AllFilter/" + toilet.key + "/averageWait", newWaitingTime);
        updateData.put("AllFilter/" + toilet.key + "/reviewCount", newReviewCount);
            updateData.put("AllFilter/" + toilet.key + "/available", true);




            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();


            firebaseRef.updateChildren(updateData,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });





    }}

//    private void reviewDataUpload(){
//
////        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
////            //Go to login
////        }
////        else{
////            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
////        }
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String uid = user.getUid();
//
//            Log.i("This is the UID", uid);
//
//
//            long timeStamp = System.currentTimeMillis();
//            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
//
//            String ratingValue = String.valueOf(kansouRaitng.getRating());
//
//            String dateString = getDate(timeStamp);
//            //String dateString = toDate(timeStamp);
//            Log.i("This is the dateString", dateString);
//
//
//            DatabaseReference reviewsRef;
//            reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
//            DatabaseReference toiletReviewRef;
//            toiletReviewRef = FirebaseDatabase.getInstance().getReference().child("ToiletReviews");
//            DatabaseReference reviewListRef;
//            reviewListRef = FirebaseDatabase.getInstance().getReference().child("ReviewList");
//
////            reviewsRef.child(newRid).setValue(new ReviewPost(
////                    availableSwitch.isChecked(),
////                    String.valueOf(kansouText.getText()),
////                    0,
////                    ratingValue,
////                    toilet.key,
////                    dateString,
////                    timeStampDouble,
////                    uid,
////                    waitingSpinner.getSelectedItem().toString()
////            ));
//
//
//            Log.i("before", "toiletReviewRef");
//            Log.i("toilet.key", toilet.key);
//            Log.i("newRid", newRid);
//            toiletReviewRef.child(toilet.key).child(newRid).setValue(true);
//
//
//            Log.i("before", "reviewListRef");
//            reviewListRef.child(uid).child(newRid).setValue(true);
//
//
//        }
//    }

//    private String getDate(long time) {
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//
//        cal.setTimeInMillis(time);
//
//       // String date = DateFormat.format("dd-MM-yyyy", cal).toString();
//        Log.i("TIME121",DateFormat.format("yyyy-MM-dd", cal).toString());
//        return DateFormat.format("yyyy-MM-dd", cal).toString();
//    }

    private void isThereProblem(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("この施設を利用できましたか");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"利用できた", "利用できなかった"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                availableSwitch.setChecked(true);
                                break;
                            case 1:
                                whatIsTheProblem();
                                break;
                        }
                    }
                });
        builder.create().show();



    }

    private void whatIsTheProblem(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("利用できなかった理由を教えてください");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"施設が見つからなかったから","トイレットペーパーがなかったから", "トイレが詰まっていたから" ,"漏水していたから","断水していたから","いいえ、利用することができた"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                problemUploadToDatabase(0);
                                //reviewReportUploadToDatabase("The content of the review is not correct",rid);
                                break;
                            case 1:
                                problemUploadToDatabase(1);

                                //reviewReportUploadToDatabase("The content of the review is not relevent",rid);
                                break;
                            case 2:
                                problemUploadToDatabase(2);
                                //reviewReportUploadToDatabase("The picture of the user is not appropriate",rid);
                                break;
                            case 3:
                                problemUploadToDatabase(3);
                                //reviewReportUploadToDatabase("The name of the user is not appropriate",rid);
                                break;
                            case 4:
                                problemUploadToDatabase(4);

                            case 5:
                                availableSwitch.setChecked(true);
                                break;


                        }
                    }
                });
        builder.create().show();

    }



    private void problemUploadToDatabase(Integer problemInt){

        //toiletWarningsListUpload();




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();

            Map<String, Object> updateInfo = new HashMap();

            Map<String, Object> problemData = new HashMap();

            problemData.put("tid", toilet.key);
            problemData.put("uid", uid);
            problemData.put("time", timeString);
            problemData.put("timeNumbers", timeStampDouble);
            problemData.put("problem", problemInt);


//                    public ToiletProblem(String tid, String uid, String time, Double timeNumbers, String problem) {
//                this.tid = tid;
//                this.uid = uid;
//                this.time = time;
//                this.timeNumbers = timeNumbers;
//                this.problem = problem;
//            }




            //Multi Update June 13





            updateInfo.put("ToiletProblems/" + postId, problemData);
            updateInfo.put("ToiletWarningList/" + toilet.key + "/"+ uid , true);
            updateInfo.put("ToiletView/" + toilet.key + "/available", false);
            updateInfo.put("NoFilter/" + toilet.key + "/available", false);


//
//            updateData.put("ToiletUserList/" + toilet.key + "/averageStar", newAvStarString);
//        updateData.put("ToiletUserList/" + toilet.key + "/averageWait", newWaitingTime);
//        updateData.put("ToiletUserList/" + toilet.key + "/reviewCount", newReviewCount);
//            updateData.put("ToiletView/" + toilet.key + "/available", false);



            updateInfo.put("UnitOne/" + toilet.key + "/available", false);
            updateInfo.put("UnitTwo/" + toilet.key + "/available", false);
            updateInfo.put("UnitThree/" + toilet.key + "/available", false);
            updateInfo.put("UnitFour/" + toilet.key + "/available", false);
            updateInfo.put("UnitFive/" + toilet.key + "/available", false);
            updateInfo.put("UnitSix/" + toilet.key + "/available", false);
            updateInfo.put("UnitSeven/" + toilet.key + "/available", false);
            updateInfo.put("UnitEight/" + toilet.key + "/available", false);
            updateInfo.put("UnitNine/" + toilet.key + "/available", false);
            updateInfo.put("UnitTen/" + toilet.key + "/available", false);
            updateInfo.put("UnitEleven/" + toilet.key + "/available", false);
            updateInfo.put("UnitTwelve/" + toilet.key + "/available", false);
            updateInfo.put("GroupOne/" + toilet.key + "/available", false);
            updateInfo.put("GroupTwo/" + toilet.key + "/available", false);
            updateInfo.put("GroupThree/" + toilet.key + "/available", false);
            updateInfo.put("HalfOne/" + toilet.key + "/available", false);
            updateInfo.put("HalfTwo/" + toilet.key + "/available", false);
            updateInfo.put("AllFilter/" + toilet.key + "/available", false);


            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();



            firebaseRef.updateChildren(updateInfo,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });




//            userWarningsListRef.child(toilet.key).child(uid).setValue(true);


        }


    }


//    ///
//    private void toiletWarningsListUpload(){
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ToiletWarningList");
//        if (user != null){
//            String uid = user.getUid();
//            userWarningsListRef.child(toilet.key).child(uid).setValue(true);
//            //userWarningCount();
//        }
//
//    }

//    private void userWarningCount(){
//        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ToiletWarningList");
//
//
//        userWarningsListRef.child(toilet.key).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                   Log.i("DataSnap 99999", String.valueOf(dataSnapshot));
//
//
//                    //Call Once //Maybe I need boolean filter
//                    Long warningCount = dataSnapshot.getChildrenCount();
//                    userWarningCountUpload(warningCount);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void userWarningCountUpload(Long warningCount){
//        DatabaseReference userWarningsCountRef = FirebaseDatabase.getInstance().getReference().child("ToiletWarningCount");
//
//        userWarningsCountRef.child(toilet.key).setValue(warningCount);
//
//    }


    ///



    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(time);

        // String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        Log.i("TIME121", DateFormat.format("yyyy-MM-dd", cal).toString());
        return DateFormat.format("yyyy-MM-dd", cal).toString();
    }


    private String getHour() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);

        return "-" + String.valueOf(hour) + ":" + String.valueOf(minute);
//        SimpleDateFormat simpleDateFormatArrivals = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        return  simpleDateFormatArrivals;
    }
}


