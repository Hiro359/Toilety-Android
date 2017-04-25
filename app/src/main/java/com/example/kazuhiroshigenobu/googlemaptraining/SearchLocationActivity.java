package com.example.kazuhiroshigenobu.googlemaptraining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

public class SearchLocationActivity extends AppCompatActivity {
    //changed AppCompatActivity to Activity

    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    Button buttonSearchLocationFromAddress;
    Toolbar toolbar;
    TextView toolbarTitle;

    Boolean addressStringSetted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);

        toolbar = (Toolbar) findViewById(R.id.search_location_app_bar_layout);
        toolbarTitle = (TextView) findViewById(R.id.search_location_toolbar_title);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );


        buttonSearchLocationFromAddress = (Button)findViewById(R.id.buttonSearchLatLngFromAddress);
        buttonSearchLocationFromAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Is this the add 765 AA","");
                Log.i("Is this the add 765??",String.valueOf(atvPlaces.getText()));



                if (addressStringSetted) {

                    Log.i("Address 765 in seton", String.valueOf(atvPlaces.getText()));

                    LatLng getLatLng = getLocationFromAddress(getApplicationContext(), String.valueOf(atvPlaces.getText()));
                    startSearchUsingNewLatLng(getLatLng);
                }


            }
        });

        atvPlaces.setThreshold(1);

        //Changed 1 to 2 April 2

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
                Log.i("onTextChangeCalled", "099");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

//        atvPlaces.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                atvPlaces.showDropDown();
//                Log.i("setTouchListenerCalled", "099");
//                return false;
//            }
//        });

         //Commented for finding causes of error of Index 0 April 2

//        String locationString = .getPlace().geometry.location
//        String place = autocomplete.getPlace();
//
//        Double lat = place.geometry.location.lat(),
//                lng = place.geometry.location.lng();
//
//// Then do whatever you want with them
//
//        console.log(lat);
//        console.log(lng);
//
//        console.warn('Warning: I didn\'t test this code!');
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exwhile downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {

            Log.i("doInBackGroundCalled", "765");
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console

//            Its a different key from maps key April 1
            //String key = "AIzaSyAH_-R4T3NIHU6nqHnLRiUQriiIcNqEsbc";

            String key = "key=AIzaSyDj0TpUShX4Jp22p6HT9kiGXCuxjhp6tUo";


            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            Log.i(String.valueOf(input), "099");
            Log.i(String.valueOf(types), "099");
            Log.i(String.valueOf(sensor), "099");
            Log.i(String.valueOf(parameters), "099");

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
                Log.i(String.valueOf(data), "data009");


            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("onPostExecuteCalled", "765");
            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {

                Log.i("doInBackground J ob", "765");
                jObject = new JSONObject(jsonData[0]);
                Log.i("JSONGET090", String.valueOf(jObject));
//                Log.i("THis is the jason1",String.valueOf(jsonData[1]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[2]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[3]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[4]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[5]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[6]));
//                Log.i("THis is the jason1",String.valueOf(jsonData[7]));
//



                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);


                Log.i("JSONGET 765", String.valueOf(places));

                if (places.isEmpty()){
                    Log.i("Places Empty", "765");

                    addressStringSetted = false;

                } else {

                    Log.i("Places Not Empty", "765");
                    addressStringSetted = true;
                }








            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter){

            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();

            String addressText = String.valueOf(atvPlaces.getText());
            Log.i("addressText111",addressText);

                Log.i("Address", String.valueOf(atvPlaces.getText()));
                LatLng getLatLng = getLocationFromAddress(getApplicationContext(), String.valueOf(atvPlaces.getText()));

                startSearchUsingNewLatLng(getLatLng);


            return  true;

        } else {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

            return super.onOptionsItemSelected(item);
        }
    }

    private void startSearchUsingNewLatLng(LatLng newLatLng) {

        if (newLatLng != null) {
            Log.i(" newLatLng 765","");
            //get the correct latlng
            //start avtivity with the latlng
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            UserInfo.userSelectedLatLng = newLatLng;
            UserInfo.userSelectedLocation = true;

            startActivity(intent);
            finish();
        } else {

            Log.i(" newLatLng 765 what?","");

            Toast.makeText(this, "What the fuck??", Toast.LENGTH_SHORT).show();
            atvPlaces.setError("目的地が入力されていません");


            //display something for letting the user know its not the right address...

           // showSearchAgain();
            //dont


            //Write something on Display

        }
    }



    // I have not used it April 1
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        Log.i(" strAddress 765",strAddress);
        //crash before..
        //It didnt give me anything..

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i("THIS P1ONE", String.valueOf(p1));


            } catch (IOException ex) {

                Log.i("THIS ERROR765", "GIVE ME");
                ex.printStackTrace();
            }




        return p1;
    }

}







