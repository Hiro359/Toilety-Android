package com.example.kazuhiroshigenobu.googlemaptraining;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class DetailViewActivity extends AppCompatActivity {



    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

    private DatabaseReference toiletRef;
    private GoogleApiClient client;

    TextView toiletNameLabel;
    TextView typeAndDistance;
    TextView availableAndWaiting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailViewMap);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        settingReady();
        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    // your additional codes goes here
                    onMapReadyCalled(googleMap);

                    String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
                    Log.i("Current.key",key );
                    //get name info
                    Log.i("THis it it", key);
                    toileGetInfo(key);


                }
            }}
        );
    }


    private void settingReady(){
        toiletNameLabel = (TextView) findViewById(R.id.toiletName);
        typeAndDistance = (TextView) findViewById(R.id.typeAndDistance);
        availableAndWaiting = (TextView) findViewById(R.id.toiletName);


    }


    private void toileGetInfo(final String queryKey){

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        toiletRef.child(queryKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("OnDataChangeCalled","777");
                // for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    Log.i("OnDataChangeCalled","777888");
                    Boolean removedToilet = false;

                    Log.i("OnDataChangeCalled","777888999");
                    Toilet toilet =  new Toilet();
                    // List<String> toiletData = new ArrayList<>();


                    LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                    //get from the location Manager


                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();

                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();

                    LatLng toiletLocation = new LatLng(toilet.latitude,toilet.longitude);
                    //get from the database

                    double distance = CalculationByDistance(centerLocation,toiletLocation);

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


                    //Log.i("toilet.distance", String.valueOf(toilet.distance));





                    toilet.key = queryKey;
                    //Not sure about how to call key....

                    Log.i("toilet777888.key",toilet.key);
//                            String urlOne = (String) dataSnapshot.child("urlOne").getValue();
//                            toilet.urlOne = urlOne;

                    toilet.name = (String) dataSnapshot.child("name").getValue();


                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();

                    //String urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();

                    //String urlThree= (String) dataSnapshot.child("urlThree").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();;

                    // String type = (String) dataSnapshot.child("type").getValue();
                    toilet.type = (String) dataSnapshot.child("type").getValue();;

                    Log.i("toilet777.type",toilet.type);
//                            Double star  = (Double) dataSnapshot.child("star").getValue();
//                            toilet.star = star;
                    //commented

                    Log.i("toilet777.star",toilet.type);


                    // Boolean washlet= (Boolean) dataSnapshot.child("washlet").getValue();
                    toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();;


                    //Boolean wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                    toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();


                    //Boolean onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                    toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();


                    //Boolean unisex = (Boolean) dataSnapshot.child("unisex").getValue();
                    toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();
                    Log.i("toilet777.unisex",String.valueOf(toilet.unisex));


                    //Boolean makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                    toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();


                    // Boolean milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
                    toilet.milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();


                    // Boolean omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                    toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();


                    //Boolean ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                    toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();


                    Log.i("OnDataChangeCalled","777888999");
                    //Boolean japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                    toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();

                    // Boolean westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                    toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();


                    //  Boolean warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                    toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                    Log.i("toilet777.warmSeat",String.valueOf(toilet.warmSeat));



                    //Boolean baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
                    toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();


                    // Boolean available = (Boolean) dataSnapshot.child("available").getValue();
                    toilet.available = (Boolean) dataSnapshot.child("available").getValue();
                    Log.i("toilet777.ave",String.valueOf(toilet.available));

                    //add boolean
                    toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
                    Log.i("toilet777.ave",String.valueOf(toilet.autoOpen));

                    toilet.sensor = (Boolean) dataSnapshot.child("sensor").getValue();
                    Log.i("toilet777.sensor",String.valueOf(toilet.sensor));

                    toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                    Log.i("toilet777.ave",String.valueOf(toilet.otohime));

                    toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
                    Log.i("toilet777.ave",String.valueOf(toilet.fancy));

                    toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
                    // Log.i("toilet777.ave",String.valueOf(toilet.available));

                    toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
                    // Log.i("toilet777.ave",String.valueOf(toilet.available));

                    toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                    //Log.i("toilet777.ave",String.valueOf(toilet.available));

                    toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
                    Log.i("toilet777.parking",String.valueOf(toilet.parking));

                    toilet.english = (Boolean) dataSnapshot.child("english").getValue();
                    //Log.i("toilet777.ave",String.valueOf(toilet.available));

                    toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
                    Log.i("toilet777.ave",String.valueOf(toilet.available));





                    // String howtoaceess = (String) dataSnapshot.child("howtoaccess").getValue();
                    toilet.howtoaccess = (String) dataSnapshot.child("howtoaccess").getValue();
                    Log.i("toilet777.waitingtime",toilet.howtoaccess);


//                            Integer waitingtime = (Integer) dataSnapshot.child("waitingtime").getValue();
//                            toilet.waitingtime = waitingtime;
//                            Log.i("toilet777.waitingtime",String.valueOf(toilet.waitingtime));
//                            //I dont think this will be needed anymore......

                    Log.i("toilet777.heyheyyyy",toilet.howtoaccess);
                    //String openinghours = (String) dataSnapshot.child("openinghours").getValue();
                    // Long toilet.openHours= (Long) dataSnapshot.child("star1").getValue();
                    //toilet.openHours = (Integer) dataSnapshot.child("openHours").getValue();

                    Long openh = (Long) dataSnapshot.child("openHours").getValue();
                    toilet.openHours = openh.intValue();

                    Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
                    toilet.openHours = closeh.intValue();

                    Log.i("toilet777.openingHours",String.valueOf(toilet.openHours));

//                            toilet.closeHours = (Integer) dataSnapshot.child("closeHours").getValue();
                    Log.i("toilet777.closeHours",String.valueOf(toilet.closeHours));



                    // String addedBy  = (String) dataSnapshot.child("addedBy").getValue();
                    toilet.addedBy = (String) dataSnapshot.child("addedBy").getValue();

                    //String editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    toilet.editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    Log.i("toilet777.editBt",String.valueOf(toilet.editedBy));




                    // String averageStar = (String) dataSnapshot.child("averageStar").getValue();
                    toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();;

                    Log.i("toilet777.aveStar",String.valueOf(toilet.averageStar));

                    //Its asking for Double, but somtimes it got Integer, which makes an error....



                    Log.i("toilet777.averageStar",String.valueOf(toilet.averageStar));


                    Log.i("I dont getiiiit",String.valueOf(toilet.averageStar));




                    //Integer star1 = (Integer) dataSnapshot.child("star1").getValue();
                    Log.i("What;'s wrog this","");

                    // toilet.star1 = star1;
                    //Log.i("toilet777.star1",String.valueOf(toilet.star1));
                    Log.i("What;'s wrog this","22");
                    Double averaegeStarDouble = Double.parseDouble(toilet.averageStar);


                    Long star1 = (Long) dataSnapshot.child("star1").getValue();
                    toilet.star1 = star1.intValue();

                    Long star2 = (Long) dataSnapshot.child("star2").getValue();
                    toilet.star2 = star2.intValue();
                    Log.i("toilet.star2",String.valueOf(toilet.star2));

                    Long star3 = (Long) dataSnapshot.child("star3").getValue();
                    toilet.star3 = star3.intValue();

                    Long star4 = (Long) dataSnapshot.child("star4").getValue();
                    toilet.star4 = star4.intValue();

                    Long star5 = (Long) dataSnapshot.child("star5").getValue();
                    toilet.star5 = star5.intValue();

                    Long star6 = (Long) dataSnapshot.child("star6").getValue();
                    toilet.star6 = star6.intValue();

                    Long star7 = (Long) dataSnapshot.child("star7").getValue();
                    toilet.star7 = star7.intValue();

                    Long star8 = (Long) dataSnapshot.child("star8").getValue();
                    toilet.star8 = star8.intValue();

                    Long star9 = (Long) dataSnapshot.child("star9").getValue();
                    toilet.star9 = star9.intValue();


                    Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                    toilet.reviewCount = reviewCount.intValue();

                    Log.i("toilet777.reviewCount",String.valueOf(toilet.reviewCount));

                    Long wait1 = (Long) dataSnapshot.child("wait1").getValue();
                    toilet.wait1 = wait1.intValue();

                    Long wait2 = (Long) dataSnapshot.child("wait2").getValue();
                    toilet.wait2 = wait2.intValue();

                    Long wait3 = (Long) dataSnapshot.child("wait3").getValue();
                    toilet.wait3 = wait3.intValue();

                    Long wait4 = (Long) dataSnapshot.child("wait4").getValue();
                    toilet.wait4 = wait4.intValue();

                    Long wait5 = (Long) dataSnapshot.child("wait5").getValue();
                    toilet.wait5 = wait5.intValue();

                    Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                    toilet.averageWait = averageWait.intValue();

                    ////Added feature elements March 3






                    Log.i("toilet777.aveWait",String.valueOf(toilet.averageWait));


                    toiletNameLabel.setText(toilet.name);
                    typeAndDistance.setText(toilet.type + "/" + toilet.distance);





                }}
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String TAG = "Error";
                    Log.w(TAG, "DatabaseError",databaseError.toException());

                }
            });

    }

    public void onMapReadyCalled(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("onLocationChanged","Called");
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

            Log.i("Build.VERSION.SDK_INT ","Build.VERSION.SDK_INT ");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);


        }
        else{
//            Log.i("Build.VERSION.SDK_INT>23 ","Build.VERSION.SDK_INT ");

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){


                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);



            }else {
                //When the permission is granted....
                Log.i("HeyHey333", "locationManager.requestLocationUpdates");


//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);
                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");




                if (lastKnownLocation != null){
                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");


                    mMap.clear();

                    LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());


                    mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));




                } else {
                    //When you could not get the last known location...

                }
            }
        }
    }
}
