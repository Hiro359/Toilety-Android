package com.example.kazuhiroshigenobu.googlemaptraining;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //extends FramgementActivity to AppCompatActivity
    //AppCompatActivity to Activity April 1




    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

    private DatabaseReference toiletRef;
    private GeoFire geoFire;
    private Filter filter = new Filter();
    private ToiletMarker tMarker = new ToiletMarker();
    private ToiletListAdapter adapter;

    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private View mProgressView;
    private Button buttonShowListview;
    private Button buttonMapCenter;
    private Button buttonSearch;
    private Button buttonForOriginalLocation;

    private Integer recycleViewHeight = 900;





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission111");
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Permission222");
                    mMap.setMyLocationEnabled(true);


                    //mapUserCenterZoon();

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("Permission", "Permission333");
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context;
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.app_filter_icon_drawable);


        if (!isNetworkAvailable()){
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();
            Log.i("NO INTERNET","NO INTERNET");
        } else {
            Toast.makeText(this, "INTERNET OKAY", Toast.LENGTH_SHORT).show();

        }

        Log.i("JAP98789000", String.valueOf(Filter.japaneseFilter));

//        getActionBar().setIcon(R.drawable.earth);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);

        }



        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Commented April 2


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FilterSearchActivity.class);
                startActivity(intent);
                finish();
             }
        }
        );
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



    }

    private void buttonSetClick(){
        buttonMapCenter.setVisibility(View.VISIBLE);
        buttonShowListview.setVisibility(View.VISIBLE);

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


                    //recyclertView.setVisibility(View.VISIBLE);
                    ResizeAnimation resizeAnimation = new ResizeAnimation(
                            recyclertView,
                            recycleViewHeight,
                            recyclertView.getHeight()
                    );

                    resizeAnimation.setDuration(400);
                    recyclertView.startAnimation(resizeAnimation);


                    //recyclertView.setVisibility(View.VISIBLE);
                    //Write something in order to scorll down the view...

                    //April 25

//                    recyclertView.scrollTo(5,10);
                } else{
                    Toast.makeText(MapsActivity.this, "We DiD not find toilets sorry man!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



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

//        if id == R.id.
            return super.onOptionsItemSelected(item);
        }
    }

    public void mapUserCenterZoon(Location location) {

    }

    public void createRecyclerView(List toiletData) {
        Log.i("createReclerView()Caled", "");
        recyclertView = (RecyclerView) findViewById(R.id.toiletRecycleList);
        adapter = new ToiletListAdapter(toiletData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("createReclerView()Ended", "");

        if (recyclertView != null) {
            if (toiletData.size() < 2) {
                recycleViewHeight = 300;
            } else if (toiletData.size() < 3) {

                recycleViewHeight = 600;
            } else {

                recycleViewHeight = 900;
            }

        }

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
//            Log.i("Build.VERSION.SDK_INT>23 ","Build.VERSION.SDK_INT ");

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {
                //When the permission is granted....
                Log.i("HeyHey333", "locationManager.requestLocationUpdates");


//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                //Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                Location lastKnownLocation;
                if (UserInfo.userSelectedLocation){
                    Location userDestination = new Location(LocationManager.GPS_PROVIDER);
                    userDestination.setLatitude(UserInfo.userSelectedLatLng.latitude);
                    userDestination.setLongitude(UserInfo.userSelectedLatLng.longitude);
                    lastKnownLocation = userDestination;

                } else {
                    lastKnownLocation = getLastKnownLocation();
                }


                mMap.setMyLocationEnabled(true);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        Toast.makeText(MapsActivity.this, "Map was touched", Toast.LENGTH_SHORT).show();
                        if (recyclertView != null){
                            //recyclertView.scrollTo(0,recyclertView.getBottom());

                            //recyclertView.layout(0,0,0,0);

                            //recyclertView.layout
                            //April 25 for scrolling down

                            Log.i("recgetHeight()", String.valueOf(recyclertView.getHeight()));


                            ResizeAnimation resizeAnimation = new ResizeAnimation(
                                    recyclertView,
                                    -recyclertView.getHeight(),
                                    recyclertView.getHeight()
                            );

                            //resizeAnimation.setDuration(10000000);
                            //resizeAnimation.scaleCurrentDuration(100);
                            //resizeAnimation.setDuration();

                            resizeAnimation.setDuration(400);
                            recyclertView.startAnimation(resizeAnimation);



                            //recyclertView.setVisibility(View.GONE);
                            buttonMapCenter.setVisibility(View.VISIBLE);
                            buttonShowListview.setVisibility(View.VISIBLE);

                        }
                        //Do what you want on obtained latLng
                    }
                });


                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(final Marker marker) {

                        Log.i("GotMarker","YEAH");

                        View v = getLayoutInflater().inflate(R.layout.marker_window, null);

                        TextView marketName = (TextView) v.findViewById(R.id.markerName);
                        TextView markerDetail = (TextView) v.findViewById(R.id.markerDetailText);
                        RatingBar markerRatingBar = (RatingBar) v.findViewById(R.id.markerRatingBar);
                        TextView marketRatingString = (TextView) v.findViewById(R.id.markerRatingString);
                        Button markerButtonDetail = (Button) v.findViewById(R.id.markerDetailButton);

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


//                        Float fixedAlpha =  marker.getTag().;
////                        Float fixedAlpha =  marker.getAlpha();
//                        Double doubleAlpha = fixedAlpha.doubleValue() - 0.5;
//                        Double alpha = doubleAlpha * 10;
//                        Log.i("alpha000",String.valueOf(alpha));

//                        Float fixedAlpha =  marker.getAlpha();
//                        Double doubleAlpha = fixedAlpha.doubleValue() - 0.5;
//                        Double alpha = doubleAlpha * 10;
//                        Log.i("alpha000",String.valueOf(alpha));

                       // double roundedAlpha = round(alpha, 1);

                        String avStar = marker.getTag().toString();



                        marketName.setText(marker.getTitle());
                        markerDetail.setText("平均"+ String.valueOf(marker.getRotation()) +"分待ち/" + distanceSting);
                        markerRatingBar.setRating(marker.getRotation());
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




                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");


                if (lastKnownLocation != null) {
                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");


                   // mMap.clear();
                    LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                   // mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));
                    toiletSearch(lastKnownLocation);

                } else {

                    //When you could not get the last known location...

                }
            }
        }
    }

    //get last location funtions
    private Location getLastKnownLocation() {


        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                //permission granted .....

                Location l = locationManager.getLastKnownLocation(provider);
                //Location l = locationListener.getLastKnownLocation(provider);
//            Log.d("last known location, provider: %s, location: %s", provider,
//                    l);


                if (l == null) {
                    continue;
                }
                if (bestLocation == null
                        || l.getAccuracy() < bestLocation.getAccuracy()) {
//                ALog.d("found best last known location: %s", l);
                    bestLocation = l;
                }
                //////
            }
            }
            if (bestLocation == null) {
                return null;
            }
            return bestLocation;

    }


    //get last location funtions

    //original toiletSearch ...  ...  ....

    public void toiletSearch(Location location){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");

        Log.i("toiletSearch","Called");
        geoFire = new GeoFire(ref);

        final Double centerLatitude = location.getLatitude();
        final Double centerLongitude = location.getLongitude();


        Double centerRadius = 5.0;

        final List<Toilet> toiletData = new ArrayList<>();
        //This value should be changed depending on the filter...
//
        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");


        Log.i("toiletSearch","BeforeGeoQueryCalled");
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(centerLatitude,centerLongitude), centerRadius);
        final LatLng centerLocation = new LatLng(centerLatitude,centerLongitude);
        UserInfo.latitude = centerLatitude;
        UserInfo.longitude = centerLongitude;

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {


                toiletRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        {

                            Log.i("getKey222",String.valueOf(dataSnapshot.getKey()));

                            Log.i("DataSnapshot",String.valueOf(dataSnapshot));
                            Log.i("toiletSearch1212","Called");


                            Boolean removedToilet = false;

                            Toilet toilet =  new Toilet();
                            Filter filter =  new Filter();


                            LatLng centerLocation = new LatLng(centerLatitude, centerLongitude);
                            Log.i("centerLocationMAP", String.valueOf(centerLocation));
                            LatLng toiletLocation = new LatLng(location.latitude,location.longitude);

                            double distance = CalculationByDistance(centerLocation,toiletLocation);
                            toilet.distanceNumberString = String.valueOf(distance);


                            if (distance > 1){
                                toilet.distance = String.valueOf(round(distance, 1)) + "km";
                                Log.i("toilet.distance", String.valueOf(toilet.distance));
                                //Km

                            }else{
                                Double meterDistance = distance * 100;
                                Integer meterA = meterDistance.intValue();
                                Integer meterB = meterA * 10;


                                toilet.distance = String.valueOf(meterB) + "m";

                                Log.i("toilet.distance", String.valueOf(toilet.distance));

                            }

                            toilet.key = key;
                            //Not sure about how to call key....

                            toilet.latitude = location.latitude;
                            toilet.longitude = location.longitude;

                            toilet.name = (String) dataSnapshot.child("name").getValue();
                            toilet.type = (String) dataSnapshot.child("type").getValue();
                            toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                            toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();

                            Long openh = (Long) dataSnapshot.child("openHours").getValue();
                            toilet.openHours = openh.intValue();
                            Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
                            toilet.closeHours = closeh.intValue();
                            Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                            toilet.reviewCount = reviewCount.intValue();
                            Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                            toilet.averageWait = averageWait.intValue();

                            toilet.available = (Boolean) dataSnapshot.child("available").getValue();
                            toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                            toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                            toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                            toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();

                            toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();
                            toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                            toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
                            toilet.noVirus = (Boolean) dataSnapshot.child("noVirus").getValue();
                            toilet.paperForBenki = (Boolean) dataSnapshot.child("paperForBenki").getValue();
                            toilet.cleanerForBenki = (Boolean) dataSnapshot.child("cleanerForBenki").getValue();
                            toilet.autoToiletWash = (Boolean) dataSnapshot.child("nonTouchWash").getValue();


                            toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
                            toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
                            toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
                            toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
                            toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();

                            toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                            toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
                            toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                            toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                            toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();

                            toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                            toilet.wheelchairAccess = (Boolean) dataSnapshot.child("wheelchairAccess").getValue();
                            toilet.handrail = (Boolean) dataSnapshot.child("handrail").getValue();
                            toilet.callHelp = (Boolean) dataSnapshot.child("callHelp").getValue();
                            toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                            toilet.english = (Boolean) dataSnapshot.child("english").getValue();
                            toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
                            toilet.voiceGuide = (Boolean) dataSnapshot.child("voiceGuide").getValue();


                            toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
                            toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
                            toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
                            toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
                            toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
                            toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
                            toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();

                            toilet.milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
                            toilet.babyroomOnlyFemale = (Boolean) dataSnapshot.child("babyRoomOnlyFemale").getValue();
                            toilet.babyroomManCanEnter = (Boolean) dataSnapshot.child("babyRoomMaleEnter").getValue();
                            toilet.babyPersonalSpace = (Boolean) dataSnapshot.child("babyRoomPersonalSpace").getValue();
                            toilet.babyPersonalSpaceWithLock = (Boolean) dataSnapshot.child("babyRoomPersonalSpaceWithLock").getValue();
                            toilet.babyRoomWideSpace = (Boolean) dataSnapshot.child("babyRoomWideSpace").getValue();

                            toilet.babyCarRental = (Boolean) dataSnapshot.child("babyCarRental").getValue();
                            toilet.babyCarAccess = (Boolean) dataSnapshot.child("babyCarAccess").getValue();
                            toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                            toilet.hipWashingStuff = (Boolean) dataSnapshot.child("hipCleaningStuff").getValue();
                            toilet.babyTrashCan = (Boolean) dataSnapshot.child("omutuTrashCan").getValue();
                            toilet.omutuSelling = (Boolean) dataSnapshot.child("omutuSelling").getValue();


                            toilet.babyRoomSink = (Boolean) dataSnapshot.child("babySink").getValue();
                            toilet.babyWashStand = (Boolean) dataSnapshot.child("babyWashstand").getValue();
                            toilet.babyHotWater = (Boolean) dataSnapshot.child("babyHotwater").getValue();
                            toilet.babyMicroWave = (Boolean) dataSnapshot.child("babyMicrowave").getValue();
                            toilet.babyWaterSelling = (Boolean) dataSnapshot.child("babyWaterSelling").getValue();
                            toilet.babyFoddSelling = (Boolean) dataSnapshot.child("babyFoodSelling").getValue();
                            toilet.babyEatingSpace = (Boolean) dataSnapshot.child("babyEatingSpace").getValue();


                            toilet.babyChair = (Boolean) dataSnapshot.child("babyChair").getValue();
                            toilet.babySoffa = (Boolean) dataSnapshot.child("babySoffa").getValue();
                            toilet.babyKidsToilet = (Boolean) dataSnapshot.child("kidsToilet").getValue();
                            toilet.babyKidsSpace = (Boolean) dataSnapshot.child("kidsSpace").getValue();
                            toilet.babyHeightMeasure = (Boolean) dataSnapshot.child("babyHeight").getValue();
                            toilet.babyWeightMeasure = (Boolean) dataSnapshot.child("babyWeight").getValue();
                            toilet.babyToy = (Boolean) dataSnapshot.child("babyToy").getValue();
                            toilet.babyFancy = (Boolean) dataSnapshot.child("babyFancy").getValue();
                            toilet.babySmellGood = (Boolean) dataSnapshot.child("babySmellGood").getValue();



                            Double averaegeStarDouble = Double.parseDouble(toilet.averageStar);

                            if (averaegeStarDouble < Filter.starFilter) {

                                //Not sure averaegeStarDouble works......

                                removedToilet = true;
                               // continue;
                            }


                            if (Filter.availableFilter  && !toilet.available) {
                                removedToilet = true;
                            }

                            if (Filter.japaneseFilter  && !toilet.japanesetoilet) {
                                removedToilet = true;
                            }

                            if (Filter.westernFilter && !toilet.westerntoilet ) {
                                removedToilet = true;
                            }
                            if (Filter.onlyFemaleFilter && !toilet.onlyFemale) {
                                removedToilet = true;
                            }

                            if (Filter.unisexFilter  && !toilet.unisex) {
                                removedToilet = true;
                            }

                            //Benki function

                            if (Filter.washletFilter && !toilet.washlet) {
                                removedToilet = true;
                            }

                            if (Filter.warmSearFilter && !toilet.warmSeat) {
                                removedToilet = true;
                            }

                            if (Filter.autoOpen  && !toilet.autoOpen) {
                                removedToilet = true;
                            }

                            if (Filter.noVirusFilter  && !toilet.noVirus) {
                                removedToilet = true;
                            }

                            if (Filter.paperForBenkiFilter  && !toilet.paperForBenki) {
                                removedToilet = true;
                            }

                            if (Filter.cleanerForBenkiFilter  && !toilet.cleanerForBenki) {
                                removedToilet = true;
                            }

                            //washStand..

                            if (Filter.sensorHandWashFilter  && !toilet.sensorHandWash) {
                                removedToilet = true;
                            }
                            if (Filter.handSoapFilter  && !toilet.handSoap) {
                                removedToilet = true;
                            }
                            if (Filter.autoHandSoapFilter  && !toilet.autoHandSoap) {
                                removedToilet = true;
                            }
                            if (Filter.paperTowelFilter  && !toilet.paperTowel) {
                                removedToilet = true;
                            }
                            if (Filter.handDrierFilter  && !toilet.handDrier) {
                                removedToilet = true;
                            }

                            // For ladys...

                            if (Filter.otohime  && !toilet.otohime) {
                                removedToilet = true;
                            }


                            if (Filter.napkinSelling  && !toilet.napkinSelling) {
                                removedToilet = true;
                            }



                            if (Filter.makeroomFilter  && !toilet.makeuproom ) {
                                removedToilet = true;
                            }

                            if (Filter.clothes  && !toilet.clothes) {
                                removedToilet = true;
                            }


                            if (Filter.baggageSpaceFilter  && !toilet.baggageSpace) {
                                removedToilet = true;
                            }

                            //Barrier free...

                            if (Filter.wheelchairFilter  && !toilet.wheelchair) {
                            removedToilet = true;
                            }

                            if (Filter.wheelchairAccessFilter  && !toilet.wheelchairAccess) {
                                removedToilet = true;
                            }

                            if (Filter.handrailFilter  && !toilet.handrail) {
                                removedToilet = true;
                            }

                            if (Filter.callHelpFilter && !toilet.callHelp) {
                                removedToilet = true;
                            }

                            if (Filter.ostomateFilter  && !toilet.ostomate) {
                                removedToilet = true;
                            }

                            if (Filter.writtenEnglish  && !toilet.english) {
                                removedToilet = true;
                            }

                            if (Filter.braille  && !toilet.braille) {
                                removedToilet = true;
                            }

                            if (Filter.voiceGuideFilter  && !toilet.voiceGuide) {
                                removedToilet = true;
                            }

                            //Other stuffs...


                            if (Filter.fancy  && !toilet.fancy) {
                                removedToilet = true;
                            }

                            if (Filter.smell  && !toilet.smell) {
                                removedToilet = true;
                            }

                            if (Filter.confortableWise  && !toilet.conforatableWide) {
                                removedToilet = true;
                            }

                            if (Filter.noNeedAsk && !toilet.noNeedAsk) {
                                removedToilet = true;
                            }


                            if (Filter.parking  && !toilet.parking) {
                                removedToilet = true;
                            }
                            if (Filter.airConditionFilter  && !toilet.airCondition) {
                                removedToilet = true;
                            }
                            if (Filter.wifiFilter  && !toilet.wifi) {
                                removedToilet = true;
                            }



                            if (Filter.milkspaceFilter  && !toilet.milkspace) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomOnlyFemaleFilter  && !toilet.babyroomOnlyFemale) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomMaleCanEnterFilter  && !toilet.babyroomManCanEnter) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomPersonalSpaceFilter  && !toilet.babyPersonalSpace) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomPersonalWithLockFilter  && !toilet.babyPersonalSpaceWithLock) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomWideSpaceFilter  && !toilet.babyRoomWideSpace) {
                                removedToilet = true;
                            }



                            if (Filter.babyCarRentalFilter  && !toilet.babyCarRental) {
                                removedToilet = true;
                            }

                            if (Filter.babyCarAccessFilter  && !toilet.babyCarAccess) {
                                removedToilet = true;
                            }



                            if (Filter.omutuFilter  && !toilet.omutu) {
                                removedToilet = true;
                            }

                            if (Filter.babyHipWashingStuffFilter  && !toilet.hipWashingStuff) {
                                removedToilet = true;
                            }

                            if (Filter.omutuTrashCanFilter  && !toilet.babyTrashCan) {
                                removedToilet = true;
                            }

                            if (Filter.omutuSelling  && !toilet.omutuSelling) {
                                removedToilet = true;
                            }




                            if (Filter.babySinkFilter  && !toilet.babyRoomSink) {
                                removedToilet = true;
                            }

                            if (Filter.babyWashstandFilter  && !toilet.babyWashStand) {
                                removedToilet = true;
                            }

                            if (Filter.babyHotWaterFilter  && !toilet.babyHotWater) {
                                removedToilet = true;
                            }

                            if (Filter.babyMicrowaveFilter  && !toilet.babyMicroWave) {
                                removedToilet = true;
                            }


                            if (Filter.babySellingWaterFilter  && !toilet.babyWaterSelling) {
                                removedToilet = true;
                            }

                            if (Filter.babyFoodSellingFilter  && !toilet.babyFoddSelling) {
                                removedToilet = true;
                            }

                            if (Filter.babyEatingSpaceFilter  && !toilet.babyEatingSpace) {
                                removedToilet = true;
                            }


                            if (Filter.babyChairFilter  && !toilet.babyChair) {
                                removedToilet = true;
                            }

                            if (Filter.babySoffaFilter  && !toilet.babySoffa) {
                                removedToilet = true;
                            }


                            if (Filter.babyToiletFilter  && !toilet.babyKidsToilet) {
                                removedToilet = true;
                            }

                            if (Filter.babyKidsSpaceFilter  && !toilet.babyKidsSpace) {
                                removedToilet = true;
                            }


                            if (Filter.babyHeightMeasureFilter  && !toilet.babyHeightMeasure) {
                                removedToilet = true;
                            }

                            if (Filter.babyWeightMeasureFilter  && !toilet.babyWeightMeasure) {
                                removedToilet = true;
                            }

                            if (Filter.babyToyFilter  && !toilet.babyToy) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomFancyFilter  && !toilet.babyFancy) {
                                removedToilet = true;
                            }

                            if (Filter.babyRoomSmellGoodFilter  && !toilet.babySmellGood) {
                                removedToilet = true;
                            }


                            if (Filter.typeFilterOn  && toilet.type != Filter.typeFilter) {
                                removedToilet = true;
                            }

                            if (!removedToilet){

                               // toiletData.add(String.valueOf(toilet.key));
                                toiletData.add(toilet);


                                float distanceFloat = Float.parseFloat(toilet.distanceNumberString);
                                float averageWaitFloat = toilet.averageWait;



                                Double avStar = Double.parseDouble(toilet.averageStar);
                                Drawable dImage;

                                if (avStar < 2){
                                    dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_one_pin_drawable);

                                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.pin_one_icon_drawable);

                                } else if (avStar < 3){
                                    dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_two_pin_drawable);


                                } else if (avStar < 4){
                                    dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_three_pin_drawable);


                                }else if (avStar < 5){
                                    dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_four_pin_drawable);



                                } else {
                                    dImage = ContextCompat.getDrawable(getApplication(), R.drawable.number_five_pin_drawable);



                                }


                                BitmapDescriptor markerIcon = getMarkerIconFromDrawable(dImage);


                                Marker marker= mMap.addMarker(new MarkerOptions().position(toiletLocation)
                                        .title(toilet.name)
                                        .snippet(toilet.key)
                                        .rotation(averageWaitFloat)
                                        .zIndex(distanceFloat)
                                        .flat(toilet.available)
                                        .icon(markerIcon)

                                );

                                marker.setTag(toilet.averageStar);

                                createRecyclerView(toiletData);
                                Log.i("ToiletSearch1212", "Ended");

                        }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String TAG = "Error";
                        Log.w(TAG, "DatabaseError",databaseError.toException());

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
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
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
