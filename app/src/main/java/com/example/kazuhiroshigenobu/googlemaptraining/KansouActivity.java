package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import com.google.firebase.database.DatabaseReference;

public class KansouActivity extends AppCompatActivity {


    Switch availableSwitch;
    Spinner waitingSpinner;
    RatingBar kansouRaitng;
    EditText kansouText;
    Button buttonKansouAdd;
    ArrayAdapter<CharSequence> adapter1;

    private DatabaseReference toiletRef;
    private DatabaseReference reviewsRef;
    long originalReviewCount;
    long originalAverageWait;
    String originalAverageStar;
    private Toolbar toolbar;
    private TextView toolbarTitle;

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

        final String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        final Double toiletLat = getIntent().getDoubleExtra("toiletLatitude",0);
        final Double toiletLon = getIntent().getDoubleExtra("toiletLongitude",0);

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
                        intent.putExtra("EXTRA_SESSION_ID", key);
                        intent.putExtra("toiletLatitude",toiletLat);
                        intent.putExtra("toiletLongitude",toiletLon);
                        startActivity(intent);
                        finish();
                    }
                }
        );




        settingReady();

        originalReviewCount = getIntent().getLongExtra("reviewCount",0);
        originalAverageWait = getIntent().getLongExtra("avereageWait",0);
        originalAverageStar = getIntent().getStringExtra("averageStar");

        kansouFirebaseUpload();






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
            ///////////////////////// 1pm 25th Feb
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

    private void kansouFirebaseUpload(){



        Log.i("originalAverageStar",originalAverageStar);
        Log.i("originalAverageWait",String.valueOf(originalAverageWait));
        Log.i("originalReviewCount",String.valueOf(originalReviewCount));


        double originalAvStarDouble = Double.parseDouble(originalAverageStar);
        double ratingValue = kansouRaitng.getRating();
        double newAvStarDouble = 3.0;

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

            //int x = ratingValue - originalAvStarDouble;
            //double changingValue = x / 10;
            //newAvStarDouble = originalAvStarDouble + changingValue;

        } else{
            double y = ratingValue - originalAvStarDouble;
            double changeingValue = y / newReviewCount;
            newAvStarDouble = originalAvStarDouble + changeingValue;

        }












        //double newAvStarDouble = ratingValue +
//        String newAvStar = String.valueOf(ratingValue);














    }

}
