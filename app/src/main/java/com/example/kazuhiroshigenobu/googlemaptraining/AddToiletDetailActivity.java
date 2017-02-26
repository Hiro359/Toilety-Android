package com.example.kazuhiroshigenobu.googlemaptraining;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddToiletDetailActivity extends AppCompatActivity {


    Spinner japaneseCountSpinner;
    Spinner westernCountSpinner;
    Spinner pppCountSpinner;
    Spinner womenJapaneseCountSpinner;
    Spinner womenWesternCountSpinner;


    ArrayAdapter<CharSequence> adapterJapaneseCount;
    ArrayAdapter<CharSequence> adapterWesternCount;
    ArrayAdapter<CharSequence> adapterPppCount;
    ArrayAdapter<CharSequence> adapterWomenJapaneseCount;
    ArrayAdapter<CharSequence> adapterWomenWesternCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet_detail);
        sppinnerReady();

    }


    private void sppinnerReady(){

        japaneseCountSpinner = (Spinner) findViewById(R.id.japaneseCountSpinner);
        westernCountSpinner = (Spinner) findViewById(R.id.westernCountSpinner);
        pppCountSpinner = (Spinner) findViewById(R.id.peeCountSpinner);
        womenJapaneseCountSpinner = (Spinner) findViewById(R.id.womenJapaneseCountSpinner);
        womenWesternCountSpinner = (Spinner) findViewById(R.id.womenWesternCountSpinner);

        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterPppCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWomenJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterWomenWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);



        adapterJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPppCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWomenJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWomenWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);





        japaneseCountSpinner.setAdapter(adapterJapaneseCount);
        westernCountSpinner.setAdapter(adapterWesternCount);
        pppCountSpinner.setAdapter(adapterPppCount);
        womenJapaneseCountSpinner.setAdapter(adapterWomenJapaneseCount);
        womenWesternCountSpinner.setAdapter(adapterWomenWesternCount);




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


    }

}

