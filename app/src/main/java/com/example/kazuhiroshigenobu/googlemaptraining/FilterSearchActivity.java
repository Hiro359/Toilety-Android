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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FilterSearchActivity extends AppCompatActivity {

    Button button;
    Toolbar toolbar;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;
    ArrayAdapter<CharSequence> adapter4;

    Switch japaneseSwitch;
    Switch westernSwitch;
    Switch onlyFemaleSwitch;
    Switch unisexSwitch;
    Switch washletSwitch;
    Switch warmSeatSwitch;
    Switch omutuSwitch;
    Switch milkSwitch;
    Switch makeroomSwitch;
    Switch baggageSpaceSwitch;
    Switch wheelChairSwitch;
    Switch ostomateSwitch;
    Switch availableSwitch;

    Filter filter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        button = (Button) findViewById(R.id.startSearchButton);
        toolbar = (Toolbar) findViewById(R.id.app_bar2);
        toolbar.setTitle("条件検索");

        toolbar.setNavigationContentDescription("戻る");
//        toolbar.setNavigationIcon(R.drawable.earth);
        // I can set an image like X for the left side

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("OKAY???","OKAY???12345");



        //Commented at 9am 24th


//        getSupportActionBar().setTitle("TITLE");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilterSearchActivity.this, "Button 2222liCked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(FilterSearchActivity.this, "Button CliCked?????", Toast.LENGTH_SHORT).show();

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                filter.japaneseFilter = false;
                filter.westernFilter = false;
                filter.onlyFemaleFilter = false;
                filter.unisexFilter = false;
                filter.washletFilter = false;
                filter.warmSearFilter = false;
                filter.omutuFilter = false;
                filter.milkspaceFilter = false;
                filter.makeroomFilter = false;
                filter.baggageSpaceFilter = false;
                filter.wheelchairFilter = false;
                filter.ostomateFilter = false;
                filter.availableFilter = false;

                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
                finish();
             }
        }
        );

        spinnersReady();
        switchReady();
        Log.i("JAP98789",String.valueOf(filter.japaneseFilter));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.filter2, menu);
        //Commented for adding below code at 5pm
        //getMenuInflater().inflate(R.menu.filter,amvMenu.getMenu());


//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menufile);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.filter){
            //Search clicked
            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
            finish();


            return  true;

        } else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

//        if id == R.id.
            return super.onOptionsItemSelected(item);
        }
    }


    private void switchReady(){
        japaneseSwitch = (Switch) findViewById(R.id.japaneseSwich);
        westernSwitch = (Switch) findViewById(R.id.westernSwitch);
        onlyFemaleSwitch = (Switch) findViewById(R.id.onlyFemaleSwitch);
        unisexSwitch = (Switch) findViewById(R.id.unisexSwitch);
        washletSwitch = (Switch) findViewById(R.id.washletSwitch);
        warmSeatSwitch = (Switch) findViewById(R.id.warmSeatSwitch);
        omutuSwitch = (Switch) findViewById(R.id.omutuSwitch);
        milkSwitch = (Switch) findViewById(R.id.milkSwitch);
        makeroomSwitch = (Switch) findViewById(R.id.makeroomSwitch);
        baggageSpaceSwitch = (Switch) findViewById(R.id.baggageSwitch);
        wheelChairSwitch = (Switch) findViewById(R.id.wheelChairSwitch);
        ostomateSwitch = (Switch) findViewById(R.id.ostomateSwitch);
        availableSwitch = (Switch) findViewById(R.id.availableSwitch);


//        filter.japaneseFilter = false;
        //THis caused an error

        japaneseSwitch.setChecked(filter.japaneseFilter);
        westernSwitch.setChecked(filter.westernFilter);
        onlyFemaleSwitch.setChecked(filter.onlyFemaleFilter);
        unisexSwitch.setChecked(filter.unisexFilter);
        washletSwitch.setChecked(filter.washletFilter);
        warmSeatSwitch.setChecked(filter.warmSearFilter);
        omutuSwitch.setChecked(filter.omutuFilter);
        milkSwitch.setChecked(filter.milkspaceFilter);
        makeroomSwitch.setChecked(filter.makeroomFilter);
        baggageSpaceSwitch.setChecked(filter.baggageSpaceFilter);
        wheelChairSwitch.setChecked(filter.wheelchairFilter);
        ostomateSwitch.setChecked(filter.ostomateFilter);
        availableSwitch.setChecked(filter.availableFilter);


        japaneseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true){
                    filter.japaneseFilter = true;
                } else{
                    filter.japaneseFilter = false;
                }
            }
        });

        westernSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "western", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.westernFilter = true;
                } else{
                    filter.westernFilter = false;
                }
            }
        });

        onlyFemaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "onlyFemaleSwitch", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.onlyFemaleFilter = true;
                } else{
                    filter.onlyFemaleFilter = false;
                }
            }
        });

        unisexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "unisexSwitch", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.unisexFilter = true;
                } else{
                    filter.unisexFilter  = false;
                }
            }
        });

        washletSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, " washletSwitch", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.washletFilter = true;
                } else{
                    filter.washletFilter = false;
                }
            }
        });

        warmSeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "warmSeatSwitch", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.warmSearFilter = true;
                } else{
                    filter.warmSearFilter = false;
                }
            }
        });

        omutuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "omutuSwitch", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.omutuFilter = true;
                } else{
                    filter.omutuFilter = false;
                }
            }
        });

        milkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.milkspaceFilter = true;
                } else{
                    filter.milkspaceFilter = false;
                }
            }
        });
        makeroomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.makeroomFilter = true;
                } else{
                    filter.makeroomFilter = false;
                }
            }
        });

        baggageSpaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.baggageSpaceFilter = true;
                } else{
                    filter.baggageSpaceFilter = false;
                }
            }
        });

        wheelChairSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.wheelchairFilter = true;
                } else{
                    filter.wheelchairFilter = false;
                }
            }
        });

        ostomateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.ostomateFilter = true;
                } else{
                    filter.ostomateFilter = false;
                }
            }
        });

        availableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(FilterSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                if (isChecked == true){
                    filter.availableFilter = true;
                } else{
                    filter.availableFilter = false;
                }
            }
        });



//



    }

    private void spinnersReady(){
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        adapter1 = ArrayAdapter.createFromResource(this,R.array.distance_names,android.R.layout.simple_spinner_item);
        adapter2 = ArrayAdapter.createFromResource(this,R.array.order_names,android.R.layout.simple_spinner_item);
        adapter3 = ArrayAdapter.createFromResource(this,R.array.places_names,android.R.layout.simple_spinner_item);
        adapter4 = ArrayAdapter.createFromResource(this,R.array.star_numbers,android.R.layout.simple_spinner_item);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "のトイレを検索" );


//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setText(String.valueOf(parent.getItemAtPosition(position)));
                //Commented 8pm 23th

//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setText(String.valueOf(parent.getItemAtPosition(position)));


//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "以上を検索");


//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

//    private void startFilterSearch(){
//        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//        startActivity(intent);
//
//
//    }



//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        Toast.makeText(this, "SwitchChanged", Toast.LENGTH_SHORT).show();
//    }
}
