package com.example.kazuhiroshigenobu.googlemaptraining;


import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.drawable.Drawable;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;
        import java.util.Locale;
import java.util.Map;
//import android.os.StrictMode;


//Trying to remove strict network but other pople say its not the right way to do that 4th March

public class EditPinLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Change Fragment Activity to AppCompatActivity 29th March


    private GoogleMap mMap;
    LocationManager locationManager;

    LocationListener locationListener;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button buttonLocationSend;

    private RequestQueue requestQueue;

    private Boolean markerSetted = false;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private DatabaseReference toiletRef;
    private DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");
    GeoFire geoFire = new GeoFire(locationRef);



    Toilet toilet =  new Toilet();

    private GoogleApiClient client;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("Permission","Permission111");
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        setContentView(R.layout.activity_edit_pin_location);
        toilet.key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.latitude = getIntent().getDoubleExtra("toiletLatitude",0);
        toilet.longitude = getIntent().getDoubleExtra("toiletLongitude",0);

        toolbar = (Toolbar) findViewById(R.id.edit_pin_location_bar_layout);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.editPinLocationLayoutAppBarTitle);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {

                        //firebase locaitons update......

                        Log.i("Current.key","is This working???12321");
                        Intent intent = new Intent(v.getContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", toilet.key);
                        intent.putExtra("toiletLatitude",toilet.latitude);
                        intent.putExtra("toiletLongitude",toilet.longitude);
                        startActivity(intent);
                        finish();
                    }
                }
        );


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
                if (markerSetted == false){
                    //Location is null
                    alertCall();

                } else {
                    firebaseToiletLocationChildRenew();
                    //                    Toast.makeText(getApplicationContext(), "Bring It On!!", Toast.LENGTH_SHORT).show();
//
//
//                    Intent intent = new Intent(v.getContext(), EditViewActivity.class);
//
//
//                    startActivity(intent);
//                    finish();
                }
            }
        });



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

    private void alertCall(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("画面上をタップして、トイレの位置情報を加えてください");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "了解",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.edit_pin_location_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonChangePin){
            if (!markerSetted){
                alertCall();
            }else{
                firebaseToiletLocationChildRenew();
            }
        }

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

//        if id == R.id.
            return super.onOptionsItemSelected(item);

    }


    private void firebaseToiletLocationChildRenew(){


            Log.i("datbaseUpdateLat", String.valueOf(AddLocations.latitude));
            Log.i("datbaseUpdateLon", String.valueOf(AddLocations.longitude));

            geoFire.setLocation(toilet.key, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener(){
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);


                    } else {
                        System.out.println("Location saved on server successfully!");
                        firebaseToiletsChildLatLonAddressUpdate();

//                    firebaseUpdate();
                    }

                }
            });
        }

    private void firebaseToiletsChildLatLonAddressUpdate(){

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
        DatabaseReference updateToiletRef = toiletRef.child(toilet.key);

        //Hash map ..

//        Post post = new Post(AddLocations.address, AddLocations.latitude, AddLocations.longitude);
//        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("address",AddLocations.address);
        childUpdates.put("latitude",AddLocations.latitude);
        childUpdates.put("longitude",AddLocations.longitude);

        updateToiletRef.updateChildren(childUpdates);



        //updateToiletRef.updateChildren(new Post(AddLocations.address,AddLocations.latitude,AddLocations.longitude));

        //updateToiletRef.put(new Post(AddLocations.address,AddLocations.latitude,AddLocations.longitude));
        //this will give me an error becuase it doesnot consider the other boolean values, which are going to be destroyed

        //Maybe the right way to do this is a use update or put instead of using the set value....

        Intent intent = new Intent(getApplicationContext(),DetailViewActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", toilet.key);
        intent.putExtra("toiletLatitude",AddLocations.latitude);
        intent.putExtra("toiletLongitude",AddLocations.longitude);
        startActivity(intent);
        finish();






    }












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

              //  new AddToiletActivity.JSONParse().execute();
                new JSONParse().execute();


                markerSetted = true;


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

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


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


                    if (toilet.latitude != null && toilet.longitude != null){
                        LatLng toiletsLatLng = new LatLng(toilet.latitude, toilet.longitude);
                        mMap.addMarker(new MarkerOptions().position(toiletsLatLng).title("施設の位置"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletsLatLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletsLatLng, 14.0f));
                    }


//                    mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
////                                                       toiletSearch(lastKnownLocation);


                } else {
                    //When you could not get the last known location...

                }
            }
        }
    }

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





   //AsycTask

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            uid = (TextView)findViewById(R.id.uid);
//            name1 = (TextView)findViewById(R.id.name);
//            email1 = (TextView)findViewById(R.id.email);
            pDialog = new ProgressDialog(EditPinLocationActivity.this);


            //pDialog = new ProgressDialog(EditPinLocationActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json = jParser.getLocationInfo(AddLocations.latitude,AddLocations.longitude);
            return  json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {

                JSONObject location;
                String location_string;

                location = json.getJSONArray("results").getJSONObject(0);
                // Get the value of the attribute whose name is "formatted_string"
                location_string = location.getString("formatted_address");
                AddLocations.address = location_string;
                Log.d("test", "formattted address:" + location_string);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}