package com.example.kazuhiroshigenobu.googlemaptraining;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class KansouActivity extends AppCompatActivity {


    Switch availableSwitch;
    Spinner waitingSpinner;
    RatingBar kansouRaitng;
    EditText kansouText;
    Button buttonKansouAdd;
    ArrayAdapter<CharSequence> adapter1;



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



    }

}
