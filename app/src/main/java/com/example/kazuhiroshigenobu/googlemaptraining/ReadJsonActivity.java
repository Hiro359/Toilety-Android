package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadJsonActivity extends AppCompatActivity {


    JSONParser parser = new JSONParser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_json);

        Log.i("Read Json Start", "666");

        getData();


    }

    private void getData(){

        Log.i("get Data Called", "666");


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("data");
            //JSONArray m_jArry = obj.getJSONArray("formules");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                //Log.d("Details-->", jo_inside.getString("formule"));
                String name = jo_inside.getString("FIELD2");
                String address = jo_inside.getString("FIELD4");



                if (address.length() > 7){
                    Log.i("address 33333", name);
                    LatLng latLng = getLocationFromAddress(getApplicationContext(),address);
                    Log.i("lat lng 33333", String.valueOf(latLng));


                }








                if (name.contains("ファミリーマート")){
                    Log.i("shit 666", name);
                }



                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("name", name);
                m_li.put("address", address);

                formList.add(m_li);
            }
        } catch (JSONException e) {

            Log.i("Json Error Catch", "666");
            e.printStackTrace();
        }




    }

    public String loadJSONFromAsset() {

        Log.i("loadJson Called", "666");
        String json = null;
        try {

            InputStream is = getAssets().open("new_json_file");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");




        } catch (IOException ex) {
            Log.i("loadJson Error Catch", "666");
            ex.printStackTrace();
            return null;
        }
        return json;

    }

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

            if (address.isEmpty()) {
                Log.i("address Empty 33333",String.valueOf(address));
                return null;
            }


            Log.i("address Inside 33333",String.valueOf(address));



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


//    private void jsonGetData() throws JSONException, IOException {
//
//        //JSONObject is = getAssets().open("new_file");
//        //JSONObject obj = FileReader(getAssets().open(""))
//
//        InputStream inputStream = getAssets().open("") //Read from a file, or a HttpRequest, or whatever.
//        //JSONParser jsonParser = new JSONParser();
//
////        JSONObject jsonObject = (JSONObject)jsonParser.parse(
////                new InputStreamReader(inputStream, "UTF-8"));
////
////        JSONObject jsonObject = (JSONObject)jsonParser.parse(
//
//
//
//
//
//        JSONObject obj = new JSONObject(new InputStreamReader(inputStream, "UTF-8"));
//        String pageName = obj.getJSONObject("pageInfo").getString("pageName");
//
//        JSONArray arr = obj.getJSONArray("posts");
//        for (int i = 0; i < arr.length(); i++)
//        {
//            String post_id = arr.getJSONObject(i).getString("post_id");
//        }
//
//
////        Object obj = parser.parse(new FileReader("something.json"));
////        JsonArray jsonArray = (JsonArray) obj;
////
////        for (JsonElement element : jsonArray) {
////            JsonObject jsonObject = element.getAsJsonObject();
////
////            String usersName = jsonObject.get("name").getAsString();
////            System.out.println("Name of user: " + usersName);
////
////            int usersScore = jsonObject.get("score").getAsInt();
////            System.out.println("Score: " + usersScore);
////        }
//
//
//
//
//    }
}
