package com.example.kazuhiroshigenobu.googlemaptraining;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddToiletDetailActivity extends AppCompatActivity {


    Spinner japaneseCountSpinner;
    Spinner westernCountSpinner;
    Spinner pppCountSpinner;
    Spinner womenJapaneseCountSpinner;
    Spinner womenWesternCountSpinner;


    Spinner typeSpinner;
    Spinner waitingTimeSpinner;
    Spinner floorSpinner;


    EditText textToiletName;
    EditText textHowToAccess;
    EditText textFeedback;

    Boolean spinnerLoaded = false;



    ArrayAdapter<CharSequence> adapterJapaneseCount;
    ArrayAdapter<CharSequence> adapterWesternCount;
    ArrayAdapter<CharSequence> adapterPppCount;
    ArrayAdapter<CharSequence> adapterWomenJapaneseCount;
    ArrayAdapter<CharSequence> adapterWomenWesternCount;

    ArrayAdapter<CharSequence> adapterType;
    ArrayAdapter<CharSequence> adapterWaitingtime;
    ArrayAdapter<CharSequence> adapterFloor;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet_detail);



//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }


        sppinnerReady();
        textReady();

    }


    private void sppinnerReady(){

        japaneseCountSpinner = (Spinner) findViewById(R.id.japaneseCountSpinner);
        westernCountSpinner = (Spinner) findViewById(R.id.westernCountSpinner);
        pppCountSpinner = (Spinner) findViewById(R.id.peeCountSpinner);
        womenJapaneseCountSpinner = (Spinner) findViewById(R.id.womenJapaneseCountSpinner);
        womenWesternCountSpinner = (Spinner) findViewById(R.id.womenWesternCountSpinner);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        waitingTimeSpinner = (Spinner) findViewById(R.id.spinnerWaitingTime);
        floorSpinner = (Spinner) findViewById(R.id.locationFloorSpinner);


        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterPppCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWomenJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWomenWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);

        adapterType = ArrayAdapter.createFromResource(this,R.array.places_names,android.R.layout.simple_spinner_item);
        adapterWaitingtime = ArrayAdapter.createFromResource(this,R.array.waitingTimeArray,android.R.layout.simple_spinner_item);
        adapterFloor = ArrayAdapter.createFromResource(this,R.array.floorCount,android.R.layout.simple_spinner_item);





        adapterJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPppCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWomenJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWomenWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWaitingtime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFloor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);








        japaneseCountSpinner.setAdapter(adapterJapaneseCount);
        westernCountSpinner.setAdapter(adapterWesternCount);
        pppCountSpinner.setAdapter(adapterPppCount);
        womenJapaneseCountSpinner.setAdapter(adapterWomenJapaneseCount);
        womenWesternCountSpinner.setAdapter(adapterWomenWesternCount);
        typeSpinner.setAdapter(adapterType);
        waitingTimeSpinner.setAdapter(adapterWaitingtime);
        floorSpinner.setAdapter(adapterFloor);

        //...




        japaneseCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);
            }
            @Override
                public void onNothingSelected(AdapterView<?> parent) {

            }
            }

        );

        westernCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(16);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }

        );

        pppCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                 ((TextView) parent.getChildAt(0)).setTextSize(16);
             }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }

        );

        womenJapaneseCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                          ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }

        );

        womenWesternCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                          ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }

        );

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                          ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }

        );

        waitingTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                          ((TextView) parent.getChildAt(0)).setTextSize(16);


                                                          ((TextView) parent.getChildAt(0)).setText("待ち時間  " + parent.getItemAtPosition(position) + "分");



                                                      }
                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }

        );

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                         @Override
                                                         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                                             ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                             ((TextView) parent.getChildAt(0)).setTextSize(16);
//                                                             ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "以上を検索");

                                                             if (!spinnerLoaded){

                                                                // ((TextView) parent.getChildAt(0)).setText(parent.getSelectedItem()));
                                                                 ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(3) + "");
                                                                 spinnerLoaded = true;

                                                             }



                                                         }
                                                         @Override
                                                         public void onNothingSelected(AdapterView<?> parent) {
                                                         }
                                                     }

        );

    }


    private void textReady(){
        textToiletName = (EditText) findViewById(R.id.writeToiletName);
        textHowToAccess = (EditText) findViewById(R.id.inputHowToAccess);
        textFeedback = (EditText) findViewById(R.id.kansou);
//        textToiletName.setInputType(0);
        textToiletName.setTextColor(Color.BLACK);
        textHowToAccess.setTextColor(Color.BLACK);
        textFeedback.setTextColor(Color.BLACK);

        textHowToAccess.setHorizontallyScrolling(false);
        textFeedback.setHorizontallyScrolling(false);

        textHowToAccess.setMaxLines(Integer.MAX_VALUE);
        textFeedback.setMaxLines(Integer.MAX_VALUE);



    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }


}

