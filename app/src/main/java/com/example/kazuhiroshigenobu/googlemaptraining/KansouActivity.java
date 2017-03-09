package com.example.kazuhiroshigenobu.googlemaptraining;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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




        settingReady();

        originalReviewCount = getIntent().getLongExtra("reviewCount",0);
        originalAverageWait = getIntent().getLongExtra("avereageWait",0);
        originalAverageStar = getIntent().getStringExtra("averageStar");

        kansouFirebaseUpload();






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
        in
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

            int x = ratingValue - originalAvStarDouble;
            double changingValue = x / 10;
            newAvStarDouble = originalAvStarDouble + changingValue;

        } else{
            double y = ratingValue - originalAvStarDouble;
            double changeingValue = y / newReviewCount;
            newAvStarDouble = originalAvStarDouble + changeingValue;

        }












        //double newAvStarDouble = ratingValue +
//        String newAvStar = String.valueOf(ratingValue);














    }

}
