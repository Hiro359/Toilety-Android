package com.example.kazuhiroshigenobu.googlemaptraining;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FilterSearchActivity extends AppCompatActivity {

    Spinner spinner1;
    ArrayAdapter<CharSequence> adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        adapter1 = ArrayAdapter.createFromResource(this,R.array.distance_names,android.R.layout.simple_spinner_item);
        //Changed (this,R.array.distance_names,android.R.layout.simple_spinner_item)

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "のトイレを検索" );


                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
