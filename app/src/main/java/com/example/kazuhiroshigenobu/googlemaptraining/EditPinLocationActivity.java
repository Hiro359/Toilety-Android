package com.example.kazuhiroshigenobu.googlemaptraining;


import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
        import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
        import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
        import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.appindexing.Action;
        import com.google.android.gms.appindexing.AppIndex;
        import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
        import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
//import android.os.StrictMode;


//Trying to remove strict network but other pople say its not the right way to do that 4th March

public class EditPinLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
        //implements GooglePlayServicesClient.ConnectionCallbacks {

    //Change Fragment Activity to AppCompatActivity 29th March


    private GoogleMap mMap;
    LocationManager locationManager;

    LocationListener locationListener;
    private Boolean locationFirstLoad = false;


//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

//    private Button buttonLocationSend;

//    private RequestQueue requestQueue;

    private Boolean markerSetted = false;
    //GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
//    private Toolbar toolbar;
//    private TextView toolbarTitle;

//    private DatabaseReference toiletRef;
    private DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");
    GeoFire geoFire = new GeoFire(locationRef);



    Toilet toilet =  new Toilet();

    //private GoogleApiClient client;


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Log.i("Permission","Permission111");
//                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    Log.i("Permission","Permission222");
//                    mMap.setMyLocationEnabled(true);
//
//                    //mapUserCenterZoon();
//
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                    Log.i("Permission","Permission333");
//                }
//
//            }
//
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //final Context context;
        setContentView(R.layout.activity_edit_pin_location);
        toilet.key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.latitude = getIntent().getDoubleExtra("toiletLatitude",0);
        toilet.longitude = getIntent().getDoubleExtra("toiletLongitude",0);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.edit_pin_location_bar_layout);
        //toolbarTitle = (TextView) toolbar.findViewById(R.id.editPinLocationLayoutAppBarTitle);


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




        Button buttonLocationSend;
        buttonLocationSend = (Button) findViewById(R.id.buttonLocationSend);
        buttonLocationSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!markerSetted){
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
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


//        mAuth = FirebaseAuth.getInstance();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.i("FireAuth","onAuthStateChanged:signed_in:" + user.getUid());
//                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.i("FireAuth","onAuthStateChanged:signed_out");
//                    //Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap =googleMap;
        //mMap.setMapType(GoogleMap.);


        ///
        LatLng oldLocation = new LatLng(toilet.latitude,toilet.longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(oldLocation);
        markerOptions.title("Toilet");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);

        ///

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

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
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (!locationFirstLoad) {
            //This will be called once

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            UserInfo.latitude = location.getLatitude();
            UserInfo.longitude = location.getLongitude();
            Log.i("THis is latlng 0000", String.valueOf(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            locationFirstLoad = true;

            //new LatLng(UserInfo.latitude, UserInfo.longitude);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(EditPinLocationActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                //return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





    @Override
    public void onPause(){
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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


        //Atomic datebase update...


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();



        GeoHash geoHash = new GeoHash(new GeoLocation(AddLocations.latitude, AddLocations.longitude));



        Map<String, Object> updates = new HashMap<>();
        updates.put("NoFilter/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("NoFilter/" + toilet.key + "/longitude",AddLocations.longitude);


        updates.put("UnitOne/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitOne/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitTwo/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitTwo/" + toilet.key + "/longitude",AddLocations.longitude);


        updates.put("UnitThree/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitThree/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitFour/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitFour/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitFive/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitFive/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitSix/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitSix/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitSeven/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitSeven/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitEight/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitEight/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitNine/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitNine/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitTen/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitTen/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitEleven/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitEleven/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("UnitTwelve/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("UnitTwelve/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("GroupOne/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("GroupOne/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("GroupTwo/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("GroupTwo/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("GroupThree/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("GroupThree/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("HalfOne/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("HalfOne/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("HalfTwo/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("HalfTwo/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("AllFilter/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("AllFilter/" + toilet.key + "/longitude",AddLocations.longitude);

        updates.put("ToiletView/" + toilet.key + "/latitude",AddLocations.latitude);
        updates.put("ToiletView/" + toilet.key + "/longitude",AddLocations.longitude);
        updates.put("ToiletView/" + toilet.key + "/address",AddLocations.address);

        updates.put("ToiletLocations/" + toilet.key + "/g", geoHash.getGeoHashString());
        updates.put("ToiletLocations/" + toilet.key + "/l", Arrays.asList(AddLocations.latitude, AddLocations.longitude));


        //ref.updateChildren();

        ref.updateChildren(updates,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null){
                    Toast.makeText(EditPinLocationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.i("Error 121", String.valueOf(databaseError));

                } else {
                    Log.i("Edit Pin Update", "Success");
                    goBackToDetailView();
                }
            }
        });



//            geoFire.setLocation(toilet.key, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener(){
//                @Override
//                public void onComplete(String key, DatabaseError error) {
//                    if (error != null) {
//                        System.err.println("There was an error saving the location to GeoFire: " + error);
//
//                    } else {
//                        System.out.println("Location saved on server successfully!");
//                        firebaseToiletsChildLatLonAddressUpdate();
//
////                    firebaseUpdate();
//                    }
//
//                }
//            });
        }

    private void goBackToDetailView(){

//        DatabaseReference toiletRef;
//        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
//        DatabaseReference updateToiletRef = toiletRef.child(toilet.key);
//
//        //Hash map ..
//
////        Post post = new Post(AddLocations.address, AddLocations.latitude, AddLocations.longitude);
////        Map<String, Object> postValues = post.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("address",AddLocations.address);
//        childUpdates.put("latitude",AddLocations.latitude);
//        childUpdates.put("longitude",AddLocations.longitude);
//
//        updateToiletRef.updateChildren(childUpdates);
//


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












//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//
//                mMap.clear();
//                Double l1=latLng.latitude;
//                Double l2=latLng.longitude;
//                String coordl1 = l1.toString();
//                String coordl2 = l2.toString();
//                l1 = Double.parseDouble(coordl1);
//                l2 = Double.parseDouble(coordl2);
//
//                LatLng newToilet = new LatLng(l1,l2);
//                mMap.addMarker(new MarkerOptions().position(newToilet)
//                        .title("追加するトイレ"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(newToilet));
//
//                AddLocations.latitude = l1;
//                Log.i("AddLocations.latitude",String.valueOf(l1));
//
//                AddLocations.longitude = l2;
//                Log.i("AddLocations.longitude",String.valueOf(l2));
//
//              //  new AddToiletActivity.JSONParse().execute();
//                new JSONParse().execute();
//
//
//                markerSetted = true;
//
//
//            }
//        });
//
//        locationListener = new android.location.LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.i("onLocationChanged", "Called");
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//
//        if (Build.VERSION.SDK_INT < 23) {
//
//            Log.i("Build.VERSION.SDK_INT ", "Build.VERSION.SDK_INT ");
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//        } else {
//
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//
//            } else {
//                //When the permission is granted....
//                Log.i("HeyHey333", "locationManager.requestLocationUpdates");
//
//
////
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                mMap.setMyLocationEnabled(true);
//                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");
//
//
//                if (lastKnownLocation != null) {
//                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");
//
//
//                    mMap.clear();
//
//                    //LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//
//
//                    if (toilet.latitude != null && toilet.longitude != null){
//                        LatLng toiletsLatLng = new LatLng(toilet.latitude, toilet.longitude);
//                        mMap.addMarker(new MarkerOptions().position(toiletsLatLng).title("施設の位置"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletsLatLng));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletsLatLng, 14.0f));
//                    }
//
//
////                    mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));
////                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
////                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
//////                                                       toiletSearch(lastKnownLocation);
//
//
//                } else {
//                    Log.i("Last Known","Cannot FInd");
//                    //When you could not get the last known location...
//
//                }
//            }
//        }
//    }

    //Commented for implementing google map location listener May 31

//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("AddToilet Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }





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

            return jParser.getLocationInfo(AddLocations.latitude,AddLocations.longitude);
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