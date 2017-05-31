package com.example.kazuhiroshigenobu.googlemaptraining;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

//import android.location.Location;
//import android.location.LocationManager;
//Commnet for change it to google location manager


import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.widget.LinearLayout.VERTICAL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        //implements GooglePlayServicesClient.ConnectionCallbacks
        //GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
       // implements OnMapReadyCallback
{
    //extends FramgementActivity to AppCompatActivity
    //AppCompatActivity to Activity April 1






    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    //private SupportMapFragment mapFrag;



    //LocationManager locationManager;

    //android.location.LocationListener locationListener;
    //Commented for google map listener May 31

    private DatabaseReference toiletRef;
//    private GeoFire geoFire;
//    private Filter filter = new Filter();
//    private ToiletMarker tMarker = new ToiletMarker();

//    private ToiletListAdapter adapter;
    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View mProgressView;
    private Button buttonShowListview;
    private Button buttonMapCenter;
    //private Button buttonSearch;
    private Button buttonForOriginalLocation;

    private Integer recycleViewHeight = 900;

    final List<Toilet> toiletData = new ArrayList<>();

    private Boolean locationLoadedOnce = false;



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
                            Manifest.permission.ACCESS_FINE_LOCATION)
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
               // return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //final Context context;

        setContentView(R.layout.activity_maps);


        Log.i("ScreenLoading", "Start");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Get location here..



        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.app_filter_icon_drawable);

        //toolbar.setNavigationIcon(R.drawable.app_help_count_icon_drawable);






        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);

        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {

                                                    // Intent intent = new Intent(v.getContext(), FilterSearchActivity.class);

                                                     Intent intent = new Intent(v.getContext(), FilterListSearchActivity.class);
                                                     startActivity(intent);
                                                     finish();
                                                 }
                                             }
        );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        //mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //Commented for this tring to remove error May 31







        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("FireAuth", "onAuthStateChanged:signed_in:" + user.getUid());
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("FireAuth", "onAuthStateChanged:signed_out");
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        mProgressView = findViewById(R.id.map_search_progress);
        buttonMapCenter = (Button)findViewById(R.id.buttonMapCenter);
        buttonShowListview = (Button)findViewById(R.id.buttonShowListView);
        buttonForOriginalLocation = (Button)findViewById(R.id.buttonForOriginalLocation);
        mProgressView.setVisibility(View.VISIBLE);
        buttonSetClick();

        Log.i("QueryPath 88888", Filter.queryPath);





    }

//    private void loadingScreenDisplay(){
//        ProgressDialog dialog=new ProgressDialog(this);
//        dialog.setMessage("message");
//        dialog.setCancelable(false);
//        dialog.setInverseBackgroundForced(false);
//        dialog.show();
//    }

    private void buttonSetClick(){
        buttonMapCenter.setVisibility(View.INVISIBLE);
        buttonShowListview.setVisibility(View.INVISIBLE);

        if (!UserInfo.userSelectedLocation){
            buttonForOriginalLocation.setVisibility(View.GONE);
        }

        buttonForOriginalLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.userSelectedLocation = false;

                Intent intent = getIntent();
                finish();
                startActivity(intent);


            }
        });




        buttonMapCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapUserCenterZoon();

            }
        });

        buttonShowListview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMapCenter.setVisibility(GONE);
                buttonShowListview.setVisibility(GONE);

                if (recyclertView != null)
                {

                    Log.i("recgetHeight()", String.valueOf(recyclertView.getHeight()));

                    listViewAdjustAnimation();

                } else{
                    Toast.makeText(MapsActivity.this, "We DiD not find toilets sorry man!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            Log.i("Removew Google api", "3333");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.i("RemoGgle Maybe Removed", "3333");

        }
    }
    //For Google Location Listener May 31



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.filter, menu);
        // getMenuInflater().inflate(R.menu.places_search, menu);
        //Google Places API Copied April1



        return super.onCreateOptionsMenu(menu);
    }


    //Commetnted April 1 google places api


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchStartButton) {
            Toast.makeText(this, "Hey Did you Click Account??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SearchLocationActivity.class);
            startActivity(intent);
            finish();
            ///////////////////////// 1pm 25th Feb
            return true;

        } else if (id == R.id.userMyPageButton) {
            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            startActivity(intent);
            finish();

            ///////////////////////// 1pm 25th Feb
            return true;

        } else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

            return super.onOptionsItemSelected(item);
        }
    }

    public void mapUserCenterZoon() {

        LatLng userLatLng = new LatLng(UserInfo.latitude, UserInfo.longitude);
        // mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16.0f));


    }

    @SuppressWarnings("unchecked")
    public void createRecyclerView(List toiletData) {
        Log.i("createReclerView()Caled", "");
        ToiletListAdapter adapter;
        RecyclerView.LayoutManager layoutManager;
        recyclertView = (RecyclerView) findViewById(R.id.toiletRecycleList);
        adapter = new ToiletListAdapter(toiletData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
//        recyclertView.addItemDecoration(dividerItemDecoration);

        //Commented May 12 for removing the divider because i felt its too big



        Log.i("createReclerView()Ended", "");

        if (recyclertView != null) {
            if (toiletData.size() < 2) {
                recycleViewHeight = 300;
                listViewAdjustAnimation();
            } else if (toiletData.size() < 3) {

                recycleViewHeight = 600;
                listViewAdjustAnimation();
            } else {
                recycleViewHeight = 900;
                listViewAdjustAnimation();

                //Commented May 12

            }

        }

    }

    private void listViewAdjustAnimation(){

//        ResizeAnimation resizeAnimation = new ResizeAnimation(
//                recyclertView,
//                recycleViewHeight,
//                recyclertView.getHeight()
//        );

        ResizeAnimation resizeAnimation = new ResizeAnimation(
                recyclertView,
                recycleViewHeight,
                0
        );

        resizeAnimation.setDuration(400);
        recyclertView.startAnimation(resizeAnimation);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
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

                        Toast.makeText(MapsActivity.this, "Map was touched", Toast.LENGTH_SHORT).show();
                        if (recyclertView != null){

                            Log.i("recgetHeight()", String.valueOf(recyclertView.getHeight()));


                            ResizeAnimation resizeAnimation = new ResizeAnimation(
                                    recyclertView,
                                    -recyclertView.getHeight(),
                                    recyclertView.getHeight()
                            );

                            resizeAnimation.setDuration(400);
                            recyclertView.startAnimation(resizeAnimation);

                            buttonMapCenter.setVisibility(View.VISIBLE);
                            buttonShowListview.setVisibility(View.VISIBLE);

                        }
                    }
                });


                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {

                        //View v = getLayoutInflater().inflate(R.layout.marker_window, null);
                        View v = getLayoutInflater().inflate(R.layout.marker_window, null);

                        TextView marketName = (TextView) v.findViewById(R.id.markerName);
                        TextView markerDetail = (TextView) v.findViewById(R.id.markerDetailText);
                        RatingBar markerRatingBar = (RatingBar) v.findViewById(R.id.markerRatingBar);
                        TextView marketRatingString = (TextView) v.findViewById(R.id.markerRatingString);
                        //Button markerButtonDetail = (Button) v.findViewById(R.id.markerDetailButton);

                        Float distanceA = marker.getZIndex();
                        String distanceSting;

                        if (distanceA > 1){
                            distanceSting = String.valueOf(round(distanceA, 1)) + "km";

                        }else{
                            Float meterDistance = distanceA * 100;
                            Integer meterA = meterDistance.intValue();
                            Integer meterB = meterA * 10;

                            distanceSting = String.valueOf(meterB) + "m";
                        }


                        String avStar = marker.getTag().toString();
                        Float ratingValue = Float.parseFloat(avStar);

//                        toilet.averageWait/10000;

                        Log.i("miniAvWait Get 99999", String.valueOf(marker.getRotation()));

                        Float avWaitFloat = marker.getRotation() * 100;
                        Integer avWait = Math.round(avWaitFloat);

                        Log.i("Final avWait Get 99999", String.valueOf(avWait));





                        marketName.setText(marker.getTitle());
                        markerDetail.setText("平均"+ String.valueOf(avWait) +"分待ち/" + distanceSting);
                        markerRatingBar.setRating(ratingValue);
                        marketRatingString.setText(avStar);


                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                Toast.makeText(MapsActivity.this, "DID YOU FUCKING CLICKING STUPID AAA", Toast.LENGTH_SHORT).show();

                                //Convert LatLng to lat log
                                Intent intent = new Intent(getApplicationContext(), DetailViewActivity.class);
                                double lat = marker.getPosition().latitude;
                                double lng = marker.getPosition().longitude;
                                intent.putExtra("EXTRA_SESSION_ID", marker.getSnippet());

                                intent.putExtra("toiletLatitude",lat);
                                intent.putExtra("toiletLongitude",lng);
                                startActivity(intent);
                                finish();
                            }
                        });

                        return v;
                    }
                });

//                if (lastKnownLocation != null) {
//                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");
//
//                    //LatLng userLatLng = new LatLng(LocationManager.GPS_PROVIDER., lastKnownLocation.getLongitude());
//
//
//                    // mMap.clear();
//                    LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
//                    // mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
//
//                    UserInfo.latitude = lastKnownLocation.getLatitude();
//                    UserInfo.longitude = lastKnownLocation.getLongitude();
//                    toiletSearch(lastKnownLocation);
//
//                } else {
//                    Log.i("LastLocation","NOT Found");
//
//                    //When you could not get the last known location...
//
//                //}
//            }
        //}


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //Those method were added becuase of google location listener May 31
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
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
        mLastLocation = location;
        if (!locationLoadedOnce) {
            //This will be called once

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            UserInfo.latitude = location.getLatitude();
            UserInfo.longitude = location.getLongitude();
            Log.i("THis is latlng 0000", String.valueOf(latLng));
            toiletSearch(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            locationLoadedOnce = true;

            //new LatLng(UserInfo.latitude, UserInfo.longitude);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

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
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    //Those method were added becuase of google location listener May 31




    //get last location funtions

    //original toiletSearch ...  ...  ....

    public void toiletSearch(Location location){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");

        GeoFire geoFire;
        geoFire = new GeoFire(ref);

        final Double centerLatitude = location.getLatitude();
        final Double centerLongitude = location.getLongitude();




        Double centerRadius = 5.0;


        String queryPath;

        queryPath = Filter.queryPath;

        if (queryPath.equals("")){
            queryPath = "NoFilter";
        }


        toiletRef = FirebaseDatabase.getInstance().getReference().child(queryPath);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(centerLatitude,centerLongitude), centerRadius);
        //final LatLng centerLocation = new LatLng(centerLatitude,centerLongitude);
//        UserInfo.latitude = centerLatitude;
//        UserInfo.longitude = centerLongitude;

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {


                toiletRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        {

                            getToiletData(dataSnapshot,key,location);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String TAG = "Error";
                        Log.w(TAG, "DatabaseError", databaseError.toException());
                    }
                }
                );


            }


                                              @Override
                                              public void onKeyExited(String key) {

                                              }

                                              @Override
                                              public void onKeyMoved(String key, GeoLocation location) {

                                              }

                                              @Override
                                              public void onGeoQueryReady() {
                                                  mProgressView.setVisibility(GONE);
                                              }

                                              @Override
                                              public void onGeoQueryError(DatabaseError error) {
                                                  Log.i("Firebase111Error", String.valueOf(error));

                                              }
                                          }
        );
    }

    private void getToiletData(DataSnapshot dataSnapshot, String key, GeoLocation location){

        Log.i("getInfoData Called", "333333");





        Toilet toilet = new Toilet();
//        final List<Toilet> toiletData = new ArrayList<>();

        //Commented May 12


        //Filter filter = new Filter();
//
//        final Double centerLatitude = location.getLatitude();
//        final Double centerLongitude = location.getLongitude();
//        UserInfo.latitude = lastKnownLocation.getLatitude();
//        UserInfo.longitude = lastKnownLocation.getLongitude();


        toilet.latitude = location.latitude;
        toilet.longitude = location.longitude;


//        toilet.latitude = (Double)dataSnapshot.child("latitude").getValue();
//        toilet.longitude = (Double)dataSnapshot.child("longitude").getValue();

        Log.i("getData 1", "0000");


        LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
        //LatLng centerLocation = new LatLng(centerLatitude, centerLongitude);

        LatLng toiletLocation = new LatLng(toilet.latitude, toilet.longitude);

        double distance = CalculationByDistance(centerLocation, toiletLocation);
        toilet.distanceNumberString = String.valueOf(distance);


        if (distance > 1) {
            toilet.distance = String.valueOf(round(distance, 1)) + "km";
            Log.i("toilet.distance", String.valueOf(toilet.distance));
            //Km

        } else {
            Double meterDistance = distance * 100;
            Integer meterA = meterDistance.intValue();
            Integer meterB = meterA * 10;


            toilet.distance = String.valueOf(meterB) + "m";

            Log.i("toilet.distance", String.valueOf(toilet.distance));

        }

        Log.i("getData 2", "0000");

        toilet.key = key;
        //Not sure about how to call key....



        //toilet.longitude = location.longitude;

        String name = (String) dataSnapshot.child("name").getValue();

        Long typeLong = (Long) dataSnapshot.child("type").getValue();
        toilet.type = typeLong.intValue();
        toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
        toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();

        Log.i("getData 3", "0000");



//        Long floorLong = (Long) dataSnapshot.child("toiletFloor").getValue();
//        toilet.floor = floorLong.intValue();
//
//
//        toilet.name = name + stringToiletFloor(toilet.floor);





        Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
        toilet.reviewCount = reviewCount.intValue();
        Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
        toilet.averageWait = averageWait.intValue();
//
//        Long openh = (Long) dataSnapshot.child("openHours").getValue();
//        toilet.openHours = openh.intValue();
//        Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
//        toilet.closeHours = closeh.intValue();

        Long floorLong = (Long) dataSnapshot.child("toiletFloor").getValue();
        toilet.floor = floorLong.intValue();


        toilet.name = name + stringToiletFloor(toilet.floor);


        Log.i("getData 4", "0000");

        //basic info

        if (Filter.availableFilter){

            Long openh = (Long) dataSnapshot.child("openHours").getValue();
            toilet.openHours = openh.intValue();
            Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
            toilet.closeHours = closeh.intValue();


        }

        toilet.available = (Boolean) dataSnapshot.child("available").getValue();


        //Basic
        if (Filter.japaneseFilter) {
            toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
            if (!toilet.japanesetoilet) {
                return;
            }
        }

        if (Filter.westernFilter) {
            toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
            if (!toilet.westerntoilet) {
                return;
            }
        }

        if (Filter.onlyFemaleFilter) {
            toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
            if (!toilet.onlyFemale) {
                return;
            }
        }



        if (Filter.unisexFilter) { //Example
            toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();
            if (!toilet.unisex) {
                return;
            }
        }



        //Benki

        if (Filter.washletFilter) {
            toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();
            if (!toilet.washlet) {
                return;
            }
        }

        if (Filter.warmSearFilter) {
            toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
            if (!toilet.warmSeat) {
                return;
            }
        }

        if (Filter.autoOpen) {
            toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
            if (!toilet.autoOpen) {
                return;
            }
        }

        if (Filter.noVirusFilter) {
            toilet.noVirus = (Boolean) dataSnapshot.child("noVirus").getValue();
            if (!toilet.noVirus) {
                return;
            }
        }



        if (Filter.paperForBenkiFilter) {
            toilet.paperForBenki = (Boolean) dataSnapshot.child("paperForBenki").getValue();
            if (!toilet.paperForBenki) {
                return;
            }
        }


        if (Filter.cleanerForBenkiFilter) {
            toilet.cleanerForBenki = (Boolean) dataSnapshot.child("cleanerForBenki").getValue();
            if (!toilet.cleanerForBenki) {
                return;
            }
        }

        if (Filter.autoToiletWashFilter) {
            toilet.autoToiletWash = (Boolean) dataSnapshot.child("nonTouchWash").getValue();
            if (!toilet.autoToiletWash) {
                return;
            }
        }

        //Washstand function

        if (Filter.sensorHandWashFilter) {
            toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
            if (!toilet.sensorHandWash) {
                return;
            }
        }


        if (Filter.handSoapFilter) {
            toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
            if (!toilet.handSoap) {
                return;
            }
        }


        if (Filter.autoHandSoapFilter) {
            toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
            if (!toilet.autoHandSoap) {
                return;
            }
        }


        if (Filter.paperTowelFilter) {
            toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
            if (!toilet.paperTowel) {
                return;
            }
        }

        if (Filter.handDrierFilter) {
            toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();
            if (!toilet.handDrier) {
                return;
            }
        }

        //Other things one

       // toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
//        toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
//        toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
//        toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
       // toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();


        if (Filter.fancy) { //Example
            toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
            if (!toilet.fancy) {
                return;
            }
        }

        // For ladys...

//        if (Filter.otohime && !toilet.otohime) {
//            removedToilet = true;
//        }

        if (Filter.smell) { //Example
            toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
            if (!toilet.smell) {
                return;
            }
        }

//
//        if (Filter.napkinSelling && !toilet.napkinSelling) {
//            removedToilet = true;
//        }

        if (Filter.confortableWise) {
            toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
            if (!toilet.conforatableWide) {
                return;
            }
        }

        if (Filter.clothes) {
            toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
            if (!toilet.clothes) {
                return;
            }
        }

        if (Filter.baggageSpaceFilter) { //Example
            toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
            if (!toilet.baggageSpace) {
                return;
            }
        }


//        toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
//        toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
//        toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
//        toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
//        toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();





        //others two
//        toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
        if (Filter.noNeedAsk) { //Example
            toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
            if (!toilet.noNeedAsk) {
                return;
            }
        }
//        toilet.english = (Boolean) dataSnapshot.child("english").getValue();
        if (Filter.writtenEnglish) { //Example
            toilet.english = (Boolean) dataSnapshot.child("english").getValue();
            if (!toilet.english) {
                return;
            }
        }
//        toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
        if (Filter.parking) { //Example
            toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
            //toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
            if (!toilet.parking) {
                return;
            }
        }


//        toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
        if (Filter.airConditionFilter) { //Example
            toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
            if (!toilet.airCondition) {
                return;
            }
        }


//        toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();
        if (Filter.wifiFilter) { //Example
            toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();
            if (!toilet.wifi) {
                return;
            }
        }


        //for ladys

        if (Filter.otohime) { //Example
            toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
            if (!toilet.otohime) {
                return;
            }
        }


        if (Filter.napkinSelling) { //Example
            toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
            if (!toilet.napkinSelling) {
                return;
            }
        }


        if (Filter.makeroomFilter) { //Example
            toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
            if (!toilet.makeuproom) {
                return;
            }
        }


        if (Filter.ladyOmutuFilter) { //Example
            toilet.ladyOmutu = (Boolean) dataSnapshot.child("ladyOmutu").getValue();
            if (!toilet.ladyOmutu) {
                return;
            }
        }


        if (Filter.ladyBabyChair) { //Example
            toilet.ladyBabyChair = (Boolean) dataSnapshot.child("ladyBabyChair").getValue();
            if (!toilet.ladyBabyChair) {
                return;
            }
        }
//
        if (Filter.ladyBabyChairGood) { //Example
            toilet.ladyBabyChairGood = (Boolean) dataSnapshot.child("ladyBabyChairGood").getValue();

            if (!toilet.ladyBabyChairGood) {
                return;
            }
        }
//
        if (Filter.ladyBabyCarAccess) { //Example

            toilet.ladyBabyCarAccess = (Boolean) dataSnapshot.child("ladyBabyCarAccess").getValue();
            if (!toilet.ladyBabyCarAccess) {
                return;
            }
        }

        //for Mans

            if (Filter.maleOmutuFilter) { //Example
                toilet.maleOmutu = (Boolean) dataSnapshot.child("maleOmutu").getValue();

            if (!toilet.maleOmutu) {
                return;
            }
        }

        if (Filter.maleBabyChair) { //Example
            toilet.maleBabyChair = (Boolean) dataSnapshot.child("maleBabyChair").getValue();
            if (!toilet.maleBabyChair) {
                return;
            }
        }


        if (Filter.maleBabyChairGood) { //Example
            toilet.maleBabyChairGood = (Boolean) dataSnapshot.child("maleBabyChairGood").getValue();
            if (!toilet.maleBabyChairGood) {
                return;
            }
        }

        if (Filter.maleBabyCarAccess) { //Example
            toilet.maleBabyCarAccess = (Boolean) dataSnapshot.child("maleBabyCarAccess").getValue();
            if (!toilet.maleBabyCarAccess) {
                return;
            }
        }



        //for Family Restroom

//        toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
        if (Filter.wheelchairFilter) { //Example
            toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
            if (!toilet.wheelchair) {
                return;
            }
        }
       // toilet.wheelchairAccess = (Boolean) dataSnapshot.child("wheelchairAccess").getValue();
        if (Filter.wheelchairAccessFilter) { //Example
            toilet.wheelchairAccess = (Boolean) dataSnapshot.child("wheelchairAccess").getValue();
            if (!toilet.wheelchairAccess) {
                return;
            }
        }
       // toilet.autoDoor = (Boolean) dataSnapshot.child("handrail").getValue();
        if (Filter.autoDoorFilter) { //Example
            toilet.autoDoor = (Boolean) dataSnapshot.child("handrail").getValue();
            if (!toilet.autoDoor) {
                return;
            }
        }
        //toilet.callHelp = (Boolean) dataSnapshot.child("callHelp").getValue();
        if (Filter.callHelpFilter) { //Example
            toilet.callHelp = (Boolean) dataSnapshot.child("callHelp").getValue();
            if (!toilet.callHelp) {
                return;
            }
        }
        //toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
        if (Filter.ostomateFilter) { //Example
            toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
            if (!toilet.ostomate) {
                return;
            }
        }
        //toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
        if (Filter.braille) { //Example
            toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
            if (!toilet.braille) {
                return;
            }
        }
        //toilet.voiceGuide = (Boolean) dataSnapshot.child("voiceGuide").getValue();
        if (Filter.voiceGuideFilter) { //Example
            toilet.voiceGuide = (Boolean) dataSnapshot.child("voiceGuide").getValue();

            if (!toilet.voiceGuide) {
                return;
            }
        }
        //toilet.familyOmutu = (Boolean) dataSnapshot.child("familyOmutu").getValue();
        if (Filter.familyOmutuFilter) { //Example
            toilet.familyOmutu = (Boolean) dataSnapshot.child("familyOmutu").getValue();
            if (!toilet.familyOmutu) {
                return;
            }
        }


        //toilet.familyBabyChair = (Boolean) dataSnapshot.child("familyBabyChair").getValue();
        if (Filter.familyBabyChair) { //Example
            toilet.familyBabyChair = (Boolean) dataSnapshot.child("familyBabyChair").getValue();

            if (!toilet.familyBabyChair) {
                return;
            }
        }


        //From Maps Activity





        //toilet.milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
        if (Filter.milkspaceFilter) { //Example
            toilet.milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
            //toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
            if (!toilet.milkspace) {
                return;
            }
        }


        //toilet.babyroomOnlyFemale = (Boolean) dataSnapshot.child("babyRoomOnlyFemale").getValue();
        if (Filter.babyRoomOnlyFemaleFilter) { //Example
            toilet.babyroomOnlyFemale = (Boolean) dataSnapshot.child("babyRoomOnlyFemale").getValue();
            if (!toilet.babyroomOnlyFemale) {
                return;
            }
        }


        //toilet.babyroomManCanEnter = (Boolean) dataSnapshot.child("babyRoomMaleEnter").getValue();
        if (Filter.babyRoomMaleCanEnterFilter) { //Example
            toilet.babyroomManCanEnter = (Boolean) dataSnapshot.child("babyRoomMaleEnter").getValue();
            if (!toilet.babyroomManCanEnter) {
                return;
            }
        }


       // toilet.babyPersonalSpace = (Boolean) dataSnapshot.child("babyRoomPersonalSpace").getValue();
        if (Filter.babyRoomPersonalSpaceFilter) { //Example
            toilet.babyPersonalSpace = (Boolean) dataSnapshot.child("babyRoomPersonalSpace").getValue();
            if (!toilet.babyPersonalSpace) {
                return;
            }
        }
        //toilet.babyPersonalSpaceWithLock = (Boolean) dataSnapshot.child("babyRoomPersonalSpaceWithLock").getValue();
        if (Filter.babyRoomPersonalWithLockFilter) { //Example
            toilet.babyPersonalSpaceWithLock = (Boolean) dataSnapshot.child("babyRoomPersonalSpaceWithLock").getValue();
            //toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
            if (!toilet.babyPersonalSpaceWithLock) {
                return;
            }
        }


        //toilet.babyRoomWideSpace = (Boolean) dataSnapshot.child("babyRoomWideSpace").getValue();
        if (Filter.babyRoomWideSpaceFilter) { //Example
            toilet.babyRoomWideSpace = (Boolean) dataSnapshot.child("babyRoomWideSpace").getValue();

            if (toilet.babyRoomWideSpace) {
                return;
            }
        }

        //toilet.babyCarRental = (Boolean) dataSnapshot.child("babyCarRental").getValue();
        if (Filter.babyCarRentalFilter) { //Example
            toilet.babyCarRental = (Boolean) dataSnapshot.child("babyCarRental").getValue();

            if (!toilet.babyCarRental) {
                return;
            }
        }


        //toilet.babyCarAccess = (Boolean) dataSnapshot.child("babyCarAccess").getValue();
        if (Filter.babyCarAccessFilter) { //Example
            toilet.babyCarAccess = (Boolean) dataSnapshot.child("babyCarAccess").getValue();

            if (!toilet.babyCarAccess) {
                return;
            }
        }

        //toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();
        if (Filter.omutuFilter) { //Example
            toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();

            if (!toilet.omutu) {
                return;
            }
        }


        //toilet.hipWashingStuff = (Boolean) dataSnapshot.child("hipCleaningStuff").getValue();
        if (Filter.babyHipWashingStuffFilter) { //Example
            toilet.hipWashingStuff = (Boolean) dataSnapshot.child("hipCleaningStuff").getValue();

            //toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
            if (!toilet.hipWashingStuff) {
                return;
            }
        }


        //toilet.babyTrashCan = (Boolean) dataSnapshot.child("omutuTrashCan").getValue();
        if (Filter.omutuTrashCanFilter) { //Example
            toilet.babyTrashCan = (Boolean) dataSnapshot.child("omutuTrashCan").getValue();

            if (!toilet.babyTrashCan) {
                return;
            }
        }


        if (Filter.omutuSelling) { //Example
            toilet.omutuSelling = (Boolean) dataSnapshot.child("omutuSelling").getValue();
            if (!toilet.omutuSelling) {
                return;
            }
        }


        //toilet.babyRoomSink = (Boolean) dataSnapshot.child("babySink").getValue();
        if (Filter.babySinkFilter) { //Example
            toilet.babyRoomSink = (Boolean) dataSnapshot.child("babySink").getValue();

            if (!toilet.babyRoomSink) {
                return;
            }
        }


        //toilet.babyWashStand = (Boolean) dataSnapshot.child("babyWashstand").getValue();
        if (Filter.babyWashstandFilter) { //Example
            toilet.babyWashStand = (Boolean) dataSnapshot.child("babyWashstand").getValue();

            if (!toilet.babyWashStand) {
                return;
            }
        }


        //toilet.babyHotWater = (Boolean) dataSnapshot.child("babyHotwater").getValue();
        if (Filter.babyHotWaterFilter) { //Example
            toilet.babyHotWater = (Boolean) dataSnapshot.child("babyHotwater").getValue();

            if (!toilet.babyHotWater) {
                return;
            }
        }


        //toilet.babyMicroWave = (Boolean) dataSnapshot.child("babyMicrowave").getValue();
        if (Filter.babyMicrowaveFilter) { //Example
            toilet.babyMicroWave = (Boolean) dataSnapshot.child("babyMicrowave").getValue();

            if (!toilet.babyMicroWave) {
                return;
            }
        }


        //toilet.babyWaterSelling = (Boolean) dataSnapshot.child("babyWaterSelling").getValue();
        if (Filter.babySellingWaterFilter) { //Example
            toilet.babyWaterSelling = (Boolean) dataSnapshot.child("babyWaterSelling").getValue();

            if (!toilet.babyWaterSelling) {
                return;
            }
        }


        //toilet.babyFoddSelling = (Boolean) dataSnapshot.child("babyFoodSelling").getValue();
        if (Filter.babyFoodSellingFilter) { //Example
            toilet.babyFoddSelling = (Boolean) dataSnapshot.child("babyFoodSelling").getValue();

            if (!toilet.babyFoddSelling) {
                return;
            }
        }


        //toilet.babyEatingSpace = (Boolean) dataSnapshot.child("babyEatingSpace").getValue();
        if (Filter.babyEatingSpaceFilter) { //Example
            toilet.babyEatingSpace = (Boolean) dataSnapshot.child("babyEatingSpace").getValue();

            if (!toilet.babyEatingSpace) {
                return;
            }
        }


       // toilet.babyChair = (Boolean) dataSnapshot.child("babyChair").getValue();
        if (Filter.babyChairFilter) { //Example
            toilet.babyChair = (Boolean) dataSnapshot.child("babyChair").getValue();

            if (!toilet.babyChair) {
                return;
            }
        }
        //toilet.babySoffa = (Boolean) dataSnapshot.child("babySoffa").getValue();
        if (Filter.babySoffaFilter) { //Example
            toilet.babySoffa = (Boolean) dataSnapshot.child("babySoffa").getValue();

            if (!toilet.babySoffa) {
                return;
            }
        }
        //toilet.babyKidsToilet = (Boolean) dataSnapshot.child("kidsToilet").getValue();
        if (Filter.babyKidsSpaceFilter) { //Example
            toilet.babyKidsToilet = (Boolean) dataSnapshot.child("kidsToilet").getValue();

            if (!toilet.babyKidsToilet) {
                return;
            }
        }


        //toilet.babyKidsSpace = (Boolean) dataSnapshot.child("kidsSpace").getValue();
        if (Filter.babyKidsSpaceFilter) { //Example
            toilet.babyKidsSpace = (Boolean) dataSnapshot.child("kidsSpace").getValue();

            if (!toilet.babyKidsToilet) {
                return;
            }
        }


        //toilet.babyHeightMeasure = (Boolean) dataSnapshot.child("babyHeight").getValue();
        if (Filter.babyHeightMeasureFilter) { //Example
            toilet.babyHeightMeasure = (Boolean) dataSnapshot.child("babyHeight").getValue();

            if (!toilet.babyHeightMeasure) {
                return;
            }
        }


        //toilet.babyWeightMeasure = (Boolean) dataSnapshot.child("babyWeight").getValue();
        if (Filter.babyWeightMeasureFilter) { //Example
            toilet.babyWeightMeasure = (Boolean) dataSnapshot.child("babyWeight").getValue();

            if (!toilet.babyWeightMeasure) {
                return;
            }
        }


        //toilet.babyToy = (Boolean) dataSnapshot.child("babyToy").getValue();
        if (Filter.babyToyFilter) { //Example
            toilet.babyToy = (Boolean) dataSnapshot.child("babyToy").getValue();

            if (!toilet.babyToy) {
                return;
            }
        }
        //toilet.babyFancy = (Boolean) dataSnapshot.child("babyFancy").getValue();
        if (Filter.babyRoomFancyFilter) { //Example
            toilet.babyFancy = (Boolean) dataSnapshot.child("babyFancy").getValue();

            if (!toilet.babyFancy) {
                return;
            }
        }

        //toilet.babySmellGood = (Boolean) dataSnapshot.child("babySmellGood").getValue();
        if (Filter.babyRoomSmellGoodFilter) { //Example
            toilet.babySmellGood = (Boolean) dataSnapshot.child("babySmellGood").getValue();

            if (!toilet.babySmellGood) {
                return;
            }
        }



        Double averaegeStarDouble = Double.parseDouble(toilet.averageStar);

        if (averaegeStarDouble < Filter.starFilter) {

            return;
        }


        if (Filter.availableFilter && !toilet.available) {
            return;
        }

            toiletData.add(toilet);


            float distanceFloat = Float.parseFloat(toilet.distanceNumberString);
            //float averageWaitFloat = toilet.averageWait;

            float miniAvWaitFloat = toilet.averageWait/100f;





            Double avStar = Double.parseDouble(toilet.averageStar);
            Drawable dImage;

            if (avStar < 2) {
                dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_one_pin_drawable);


            } else if (avStar < 3) {
                dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_two_pin_drawable);


            } else if (avStar < 4) {
                dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_three_pin_drawable);


            } else if (avStar < 5) {
                dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_four_pin_drawable);


            } else {
                dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_five_pin_drawable);


            }

//             markerA = new google.maps.Marker({
//                    map: map,
//                    position: new google.maps.LatLng(0, 0),
//                    customInfo: "Marker A"
//            });



            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(dImage);


            Marker marker = mMap.addMarker(new MarkerOptions().position(toiletLocation)
                    .title(toilet.name)
                    .snippet(toilet.key)
                    .rotation(miniAvWaitFloat)
                    .zIndex(distanceFloat)
                    .flat(toilet.available)
                    .icon(markerIcon)

            );

            marker.setTag(toilet.averageStar);

            createRecyclerView(toiletData);
            Log.i("ToiletSearch1212", "Ended");

        }





    private String stringToiletFloor(Integer tFloor){
        String floorString = "2F";
        if (tFloor == 0){
            floorString = "(B3)";
        } else if (tFloor == 1){
            floorString = "(B2)";
        } else if (tFloor == 2){
            floorString = "(B1)";
        }else if (tFloor == 3){
            floorString = "";
        }else if (tFloor == 4){
            floorString = "(2F)";
        }else if (tFloor == 5){
            floorString = "(3F)";
        }else if (tFloor == 6){
            floorString = "(4F)";
        }else if (tFloor == 7){
            floorString = "(5F)";
        }else if (tFloor == 8){
            floorString = "(6F)";
        }else if (tFloor == 9){
            floorString = "(7F)";
        }else if (tFloor == 10){
            floorString = "(8F)";
        }else if (tFloor == 11){
            floorString = "(9F)";
        }else if (tFloor == 12){
            floorString = "(10F)";
        }else if (tFloor == 13){
            floorString = "(11F)";
        }else if (tFloor == 14){
            floorString = "(12F)";
        }else if (tFloor == 15){
            floorString = "(13F)";
        }else if (tFloor == 16){
            floorString = "(14F)";
        }else if (tFloor == 17){
            floorString = "(15F)";
        }else if (tFloor == 18){
            floorString = "(16F)";
        }else if (tFloor == 19){
            floorString = "(17F)";
        }else if (tFloor == 20){
            floorString = "(18F)";
        }else if (tFloor == 21){
            floorString = "(19F)";
        }else if (tFloor == 22){
            floorString = "(20F)";
        }else if (tFloor == 23){
            floorString = "(21F)";
        }else if (tFloor == 24){
            floorString = "(22F)";
        }else if (tFloor == 25) {
            floorString = "(23F)";
        }

        return floorString;

    }


    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        // drawable.setBounds(0, 0, 20, 20);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }




//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }

//    private boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null;
//    }


//    public boolean hasInternetAccess(Context context) {
//        if (isNetworkAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection)
//                        (new URL("http://clients3.google.com/generate_204")
//                                .openConnection());
//                urlc.setRequestProperty("User-Agent", "Android");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 204 &&
//                        urlc.getContentLength() == 0);
//            } catch (IOException e) {
//                Log.i("","Error checking internet connection");
//                //Log.e(TAG, "Error checking internet connection", e);
//            }
//        } else {
//            Log.i("No network available!","");
//            //Log.d(TAG, "No network available!");
//        }
//        return false;
//    }

}
