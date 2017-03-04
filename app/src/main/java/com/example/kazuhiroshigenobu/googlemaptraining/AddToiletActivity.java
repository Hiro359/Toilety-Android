package com.example.kazuhiroshigenobu.googlemaptraining;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.os.StrictMode;


//Trying to remove strict network but other pople say its not the right way to do that 4th March

public class AddToiletActivity extends FragmentActivity implements OnMapReadyCallback {





    private GoogleMap mMap;
    LocationManager locationManager;

    LocationListener locationListener;


    private Filter filter = new Filter();
    private AddLocations addLocations = new AddLocations();

    private ToiletListAdapter adapter;


    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayoutManager mLinearLayoutManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private ActionMenuView amvMenu;

    private Button buttonLocationSend;
    private LayoutInflater layoutInflater;

    private RequestQueue requestQueue;



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("Permission","Permission111");
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission","Permission222");
                    mMap.setMyLocationEnabled(true);

                    //mapUserCenterZoon();

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("Permission","Permission333");
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context;
        setContentView(R.layout.activity_add_toilet);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Should be changed to the exact file name

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addToiletMap);
        mapFragment.getMapAsync(this);

        Toast.makeText(this, "is this working", Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);



        buttonLocationSend = (Button) findViewById(R.id.buttonLocationSend);
        buttonLocationSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Bring It On!!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(v.getContext(),LoginActivity.class);


               // Intent intent = new Intent(v.getContext(),AddToiletDetailActivity.class);

                /////////

                startActivity(intent);
                finish();
            }
        });



//        LinearLayout item = (LinearLayout) findViewById(R.id.overlayLayout);
//        View child = getLayoutInflater().inflate(R.layout.activity_add_toilet, null);
//        item.addView(child);


//
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        toolbar.setNavigationIcon(R.drawable.earth);

        Log.i("JAP98789000",String.valueOf(Filter.japaneseFilter));



        //amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
//        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return onOptionsItemSelected(menuItem);
//            }
//        });

//        getActionBar().setIcon(R.drawable.earth);
       // setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
//                                                 @Override
//                                                 public void onClick(View v) {
//                                                     Log.i("YouareThebest", "11");
//
//                                                     Intent intent = new Intent(v.getContext(),FilterSearchActivity.class);
//                                                     startActivity(intent);
//                                                     finish();
//
//
////                v.startActivity(SettingsActivity.class);
//
////                Intent intent = new Intent(SettingsActivity.class);
//                                                     //  startActivity(intent);
//
//                                                 }
//                                             }
//        );


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("FireAuth","onAuthStateChanged:signed_in:" + user.getUid());
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("FireAuth","onAuthStateChanged:signed_out");
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    private void buttonInflation(){

//        LinearLayout view = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.addingtoiletbutton, null);
//// or LinearLayout buttonView = (LinearLayout)this.getLayoutInflater().inflate(R.layout.my_button, null);
//        Button myButton = (Button) view.findViewById(R.id.buttonForAddingToilet);
//        view.removeView(myButton);
//
//        LinearLayout mainView = (LinearLayout)this.findViewById(R.id.mainLayout);
//        mainView.addView(myButton);



//       / = (Button) findViewById(R.id.buttonLocationSend);
       // View view = inflater.inflate(R.layout.addingtoiletbutton, container, false);
        //buttonLocationSend = (Button) view.findViewById(R.id.buttonForAddingToilet);
//        buttonLocationSend.setVisibility(View.VISIBLE);

//        buttonLocationSend.isShown();


//        View view = LayoutInflater.from(this).inflate(R.layout.addingtoiletbutton);
//        Button button = (Button) view.findViewById(R.id.myButton);
//        ....
//        aGroupView.addView(button);


//        LinearLayout view = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.addingtoiletbutton, null);
//
//        Button myButton = (Button) view.findViewById(R.id.buttonForAddingToilet);
//        view.removeView(myButton);
//
//        LinearLayout mainView = (LinearLayout)this.findViewById(R.id.addToiletMap);
//        mainView.addView(myButton);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.filter, menu);
        //Commented for adding below code at 5pm
        //getMenuInflater().inflate(R.menu.filter,amvMenu.getMenu());


//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menufile);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        Log.i("R.menu.account",String.valueOf(R.id.account));
        Log.i("R.menu.filter",String.valueOf(R.id.filter));
        Log.i("R.menu.toolbar",String.valueOf(R.id.toolbar));
        Log.i("R.id.app_bar",String.valueOf(R.id.app_bar));

        Log.i("YouSelect", String.valueOf(item));
        //Log.i("GetSupportActionBar",String.valueOf(getSupportActionBar()));
        Log.i("Earth", String.valueOf(R.drawable.earth));




//        Log.i("R.menu.filter",String.valueOf(R.menu.filter));
//        Log.i("R.menu.appBar",String.valueOf(R.layout.app_bar));
//        Log.i("Home", String.valueOf(R.id.home));


        if (id == R.id.account){
            Toast.makeText(this, "Hey Did you Click Account??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
            startActivity(intent);
            finish();
            ///////////////////////// 1pm 25th Feb
            return  true;

        } else



        if (id == R.id.filter){
            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
            startActivity(intent);
            finish();

            ///////////////////////// 1pm 25th Feb
            return  true;

        } else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

//        if id == R.id.
            return super.onOptionsItemSelected(item);
        }
    }

    public void mapUserCenterZoon(Location location){

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//
//
////                JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + newToilet + "&key=AIzaSyCntF-t69vrrNHHD_AMsZszhCPbWOlKSRE",
////                        new Response.Listener<JSONObject>() {
////                            @Override
////                            public void onResponse(JSONObject response) {
////
////
////                            }
////                        }, new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////
////                    }
////
////                });
////                requestQueue.add(request);


            ///////////////////////copied from maps activity
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                mMap.clear();
                Double l1=latLng.latitude;
                Double l2=latLng.longitude;
                String coordl1 = l1.toString();
                String coordl2 = l2.toString();
                l1 = Double.parseDouble(coordl1);
                l2 = Double.parseDouble(coordl2);

                LatLng newToilet = new LatLng(l1,l2);
                mMap.addMarker(new MarkerOptions().position(newToilet)
                        .title("追加するトイレ"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newToilet));

                    AddLocations.latitude = l1;
                    Log.i("AddLocations.latitude",String.valueOf(l1));

                    AddLocations.longitude = l2;
                    Log.i("AddLocations.longitude",String.valueOf(l2));
                ////////


                JSONObject ret = getLocationInfo(l1, l2);
                JSONObject location;
                String location_string;
                try {
                    //Get JSON Array called "results" and then get the 0th complete object as JSON
                    location = ret.getJSONArray("results").getJSONObject(0);
                    // Get the value of the attribute whose name is "formatted_string"
                    location_string = location.getString("formatted_address");
                    Log.d("test", "formattted address:" + location_string);
                } catch (JSONException e1) {
                    e1.printStackTrace();

                }


                //Commented for finding an error




                //////
            }
        });

        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("onLocationChanged", "Called");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        if (Build.VERSION.SDK_INT < 23) {

            Log.i("Build.VERSION.SDK_INT ", "Build.VERSION.SDK_INT ");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {
                //When the permission is granted....
                Log.i("HeyHey333", "locationManager.requestLocationUpdates");


//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);
                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");


                if (lastKnownLocation != null) {
                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");


                    mMap.clear();

                    LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());


                    mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
//                                                       toiletSearch(lastKnownLocation);


                } else {
                    //When you could not get the last known location...

                }
            }
        }
    }

    /////Copied from the internet for reverse geocoding


    ///AsycTask



    /////////AsycTask





    public JSONObject getLocationInfo( double lat, double lng) {


        Log.i("12345","getLocationInfo Called");

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
        Log.i("12345 htttp",String.valueOf(httpGet));
        HttpClient client = new DefaultHttpClient();
        Log.i("12345 client",String.valueOf(client));
        HttpResponse response;




        Log.i("12345 response", "CHeck");
        StringBuilder stringBuilder = new StringBuilder();
        Log.i("12345 stringBuilder",String.valueOf(stringBuilder));



        try {
            response = client.execute(httpGet);
            Log.i("12345 response",String.valueOf(response));
            HttpEntity entity = response.getEntity();
            Log.i("12345 entity",String.valueOf(entity));
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddToilet Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }





    
}