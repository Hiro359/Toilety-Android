package com.example.kazuhiroshigenobu.googlemaptraining;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DatabaseCreationActivity extends AppCompatActivity {




//    PlacesTaskDataBase placesTask;
//    ParserTaskDataBase parserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_creation);

        Log.i("search called", "3344");


        StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTaskData placesTask = new PlacesTaskData();
        placesTask.execute(sbValue.toString());

//        placesTask = new PlacesTaskDataBase();
//        placesTask.execute();

        //Start with places task database

    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private String downloadUrl(String strUrl) throws IOException {
//
//        Log.i("downloadUrl Called", "3344");
//        String data = "";
//        HttpURLConnection urlConnection;
//        URL url = new URL(strUrl);
//
//        // Creating an http connection to communicate with url
//        urlConnection = (HttpURLConnection) url.openConnection();
//
//        // Connecting to url
//        urlConnection.connect();
//
//        // Reading data from url
//        try (InputStream iStream = urlConnection.getInputStream()) {
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exwhile downloading url", e.toString());
//        } finally {
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//
//    // Fetches all places from GooglePlaces AutoComplete Web Service
//    private class PlacesTaskDataBase extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... place) {
//
//
//            Double mLatitude = -33.856784;
//            Double mLongitude = 151.215318;
//            //Opera House
//
//
//            Log.i("BackGround PlacesTask", "3344");
//            // For storing data from web service
//            String data = "";
//
//            // Obtain browser key from https://code.google.com/apis/console
//
////            Its a different key from maps key April 1
//            //String key = "AIzaSyAH_-R4T3NIHU6nqHnLRiUQriiIcNqEsbc";
//
//            String key = "&key=AIzaSyDj0TpUShX4Jp22p6HT9kiGXCuxjhp6tUo";
//
//
////            String input = "";
////
////            try {
////                input = "input=" + URLEncoder.encode(place[0], "utf-8");
////            } catch (UnsupportedEncodingException e1) {
////                e1.printStackTrace();
////            }
//
////            // place type to be searched
////            String types = "types=geocode";
////
////            // Sensor enabled
////            String sensor = "sensor=false";
////
////            // Building the parameters to the web service
////            String parameters = input + "&" + types + "&" + sensor + "&" + key;
//
//
//
//            String location = "location=" + mLatitude + "," + mLongitude;
//
//            String radius = "&radius=5000";
//
//            String type = "&types=" + "restaurant";
//
//            String sensor = "&sensor=true";
//
//
//
//            Log.i("BackGround PlacesTask22", "3344");
//
//            String parameters = location + radius + type + sensor + key;
//
//
//
////            Log.i(String.valueOf(input), "099");
////            Log.i(String.valueOf(types), "099");
////            Log.i(String.valueOf(sensor), "099");
////            Log.i(String.valueOf(parameters), "099");
//
//            // Output format
//            String output = "json";
//
//
//            Log.i("BackGround PlacesTask33", "3344");
//
//            //location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=YOUR_API_KEY
//
//
//            // Building the url to the web service
//            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
//
//            try {
//                // Fetching the data from we service
//                data = downloadUrl(url);
//                Log.i(String.valueOf(data), "data009");
//                Log.i("BackGround PlacesTask44", "3344");
//
//
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//                Log.i("BackGround PlacesTask55", "3344");
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            Log.i("onPostExecuteCalled", "3344");
//            // Creating ParserTask
//             parserTask = new ParserTaskDataBase();
//
//            // Starting Parsing the JSON string returned by Web Service
//            parserTask.execute(result);
//        }
//    }
//
//    /**
//     * A class to parse the Google Places in JSON format
//     */
//
//    //Commented June 4
//    private class ParserTaskDataBase extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
//
//        JSONObject jObject;
//        String name;
//        String longitude;
//
//        @Override
//        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
//
//            List<HashMap<String, String>> places = null;
//
//            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
//
//            try {
//
//                Log.i("BackGround ParcerTask", "3344");
//                jObject = new JSONObject(jsonData[0]);
//
//
//                Log.i("JSONGET 3344", String.valueOf(jObject));
//
//
//
//
////                String name = jObject.getString("name");
////
////                Log.i("name 3344", name );
//
//
//
//
//
//
//
////                Log.i("THis is the jason1",String.valueOf(jsonData[1]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[2]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[3]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[4]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[5]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[6]));
////                Log.i("THis is the jason1",String.valueOf(jsonData[7]));
////
//
//
//
//                // Getting the parsed data as a List construct
//                places = placeJsonParser.parse(jObject);
//
//
//                Log.i("JSONGET 3344", String.valueOf(places));
//
//                if (places.isEmpty()){
//                    Log.i("Places Empty", "3344");
//
//                    //addressStringSetted = false;
//
//                } else {
//
//                    Log.i("Places Not Empty", "3344");
//                    //addressStringSetted = true;
//                }
//
//
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            }
//
//            return places;
//        }
//
//        @Override
//        protected void onPostExecute(List<HashMap<String, String>> result) {
//
//
//
//
//
//
////            String[] from = new String[]{"description"};
////            int[] to = new int[]{android.R.id.text1};
////
////            // Creating a SimpleAdapter for the AutoCompleteTextView
////            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
//
//            // Setting the adapter
//            //atvPlaces.setAdapter(adapter);
//        }
//    }



    ///Copied from example stock overflow June 4



    public StringBuilder sbMethod() {


        Log.i("sbMethod called", "3344");

        Double mLatitude = -33.856784;
        Double mLongitude = 151.215318;
        //use your current location here
//        double mLatitude = 37.77657;
//        double mLongitude = -122.417506;

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + "restaurant");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyDj0TpUShX4Jp22p6HT9kiGXCuxjhp6tUo");

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }

    private String downloadUrlData(String strUrl) throws IOException {
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
            Log.d("Exception woading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class PlacesTaskData extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrlData(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTaskData parserTask = new ParserTaskData();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }



    private class ParserTaskData extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method

//        private String downloadUrlData(String strUrl) throws IOException {
//            String data = "";
//            InputStream iStream = null;
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(strUrl);
//
//                // Creating an http connection to communicate with url
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                // Connecting to url
//                urlConnection.connect();
//
//                // Reading data from url
//                iStream = urlConnection.getInputStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//                StringBuffer sb = new StringBuffer();
//
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                data = sb.toString();
//
//                br.close();
//
//            } catch (Exception e) {
//                Log.d("Exception whloading url", e.toString());
//            } finally {
//                iStream.close();
//                urlConnection.disconnect();
//            }
//            return data;
//        }



        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
            //mGoogleMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.i("name 3344", name);

                //Log.d("Map 3344", "place: " + name);

                // Getting vicinity
//                String vicinity = hmPlace.get("vicinity");
//
//                LatLng latLng = new LatLng(lat, lng);
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                markerOptions.title(name + " : " + vicinity);
//
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                // Placing a marker on the touched position
                //Marker m = mGoogleMap.addMarker(markerOptions);

            }
        }
    }

    public class Place_JSON {

        /**
         * Receives a JSONObject and returns a list
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                /** Retrieves all the elements in the 'places' array */
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Invoking getPlaces with the array of json object
             * where each json object represent a place
             */
            return getPlaces(jPlaces);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         */
        private HashMap<String, String> getPlace(JSONObject jPlace) {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");

                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }

    ///Copied from example stock overflow June 4





}
