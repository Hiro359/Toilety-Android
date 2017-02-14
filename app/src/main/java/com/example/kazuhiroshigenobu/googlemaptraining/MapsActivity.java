package com.example.kazuhiroshigenobu.googlemaptraining;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

    private DatabaseReference databaseReference;
    private DatabaseReference toiletRef;
    private GeoFire geoFire;


    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("path/to/geofire");
    //GeoFire geoFire = new GeoFire(ref);

//    LocationManager locationManager;
//    android.location.LocationListener

//    android.location.LocationListener locationListener ;
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

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("Permission","Permission333");
                }

            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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
                // Add a marker in Sydney and move the camera
                mMap.clear();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                Log.i("toiletSearch","willBeCalled");
                toiletSearch(location);
                Log.i("toiletSearch","AlreadyCalled");

                //Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (listAddresses != null && listAddresses.size() > 0){

                        Log.i("Address", listAddresses.get(0).toString());

                        String address = "";

                        if (listAddresses.get(0).getSubThoroughfare() != null){

                            address += listAddresses.get(0).getSubThoroughfare() + ",";
                            Log.i("AddressAdress", address);


                        }
                        if (listAddresses.get(0).getLocality() != null){

                            address += listAddresses.get(0).getLocality() + ",";
                            Log.i("AddressAdress", address);

                        }
                        if (listAddresses.get(0).getPostalCode() != null){

                            address += listAddresses.get(0).getPostalCode() + ",";
                            Log.i("AddressAdress", address);

                        }
                        if (listAddresses.get(0).getCountryName() != null){

                            address += listAddresses.get(0).getCountryName() + "";
                            Log.i("AddressAdress", address);

                        }
                        Log.i("AddressAdress", address);
                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


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


            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }else {
                Log.i("HeyHey2", "locationManager.requestLocationUpdates");

//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);


              if (lastKnownLocation != null){
                  Log.i("lastKnown != null","");

                mMap.clear();
                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                //Commented fot tryout


            }


            }
        }
            // I dont think this is right but i will take a shot




//            // Add a marker in Sydney and move the camera
//            LatLng sydney = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker In L0"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//     //   }
    }

    public void toiletSearch(Location location){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");
        Log.i("toiletSearch","Called");

        geoFire = new GeoFire(ref);
        Log.i("Geo.getLatitude()",String.valueOf(location.getLatitude()));
        Log.i("Geo.getLongitude()",String.valueOf(location.getLongitude()));


        Double centerLatitude = location.getLatitude();
        Double centerLongitude = location.getLongitude();

        Double centerRadius = 3.0;
        //This value should be changed depending on the filter...

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");


        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(centerLatitude, centerLongitude), centerRadius);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(final String key, GeoLocation location) {

                Log.i("Geokey",key);
                Log.i("Geolocation",String.valueOf(location));
                toiletRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                        {
                            Boolean removedToilet = false;
                            Toilet toilet =  new Toilet();
                            Filter filter =  new Filter();
                            //String key = (String) dataSnapshot.child("key").getValue();
                            toilet.key = key;
                            //Not sure about how to call key....

                            String urlOne = (String) dataSnapshot.child("urlOne").getValue();
                            toilet.urlOne = urlOne;

                            String urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                            toilet.urlTwo = urlTwo;

                            String urlThree= (String) dataSnapshot.child("urlThree").getValue();
                            toilet.urlThree = urlThree;

                            String type = (String) dataSnapshot.child("type").getValue();
                            toilet.type = type;

                            Double star  = (Double) dataSnapshot.child("star").getValue();
                            toilet.star = star;


                            Boolean washlet= (Boolean) dataSnapshot.child("washlet").getValue();
                            toilet.washlet = washlet;


                            Boolean wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                            toilet.wheelchair = wheelchair;


                            Boolean onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                            toilet.onlyFemale = onlyFemale;


                            Boolean unisex = (Boolean) dataSnapshot.child("unisex").getValue();
                            toilet.unisex = unisex;


                            Boolean makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                            toilet.makeuproom = makeuproom;


                            Boolean milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
                            toilet.milkspace = milkspace;


                            Boolean omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                            toilet.omutu = omutu;


                            Boolean ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                            toilet.ostomate = ostomate;


                            Boolean japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                            toilet.japanesetoilet = japanesetoilet;

                            Boolean westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                            toilet.westerntoilet = westerntoilet;


                            Boolean warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                            toilet.warmSeat = warmSeat;


                            Boolean baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
                            toilet.baggageSpace = baggageSpace;


                            Boolean available = (Boolean) dataSnapshot.child("available").getValue();
                            toilet.available = available;


                            String howtoaceess = (String) dataSnapshot.child("howtoaceess").getValue();
                            toilet.howtoaccess = howtoaceess;


                            Integer waitingtime = (Integer) dataSnapshot.child("waitingtime").getValue();
                            toilet.waitingtime = waitingtime;
                            //I dont think this will be needed anymore......

                            String openinghours = (String) dataSnapshot.child("openinghours").getValue();
                            toilet.openinghours = openinghours;


                            String addedBy  = (String) dataSnapshot.child("addedBy").getValue();
                            toilet.addedBy = addedBy;

                            String editedBy = (String) dataSnapshot.child("editedBy").getValue();
                            toilet.editedBy = editedBy;

                            Double averageStar = (Double) dataSnapshot.child("averageStar").getValue();
                            toilet.averageStar = averageStar;

                            Integer star1 = (Integer) dataSnapshot.child("star1").getValue();
                            toilet.star1 = star1;

                            Integer star2 = (Integer) dataSnapshot.child("star2").getValue();
                            toilet.star2 = star2;

                            Integer star3 = (Integer) dataSnapshot.child("star3").getValue();
                            toilet.star3 = star3;

                            Integer star4 = (Integer) dataSnapshot.child("star4").getValue();
                            toilet.star4 = star4;

                            Integer star5 = (Integer) dataSnapshot.child("star5").getValue();
                            toilet.star5 = star5;

                            Integer star6 = (Integer) dataSnapshot.child("star6").getValue();
                            toilet.star6 = star6;

                            Integer star7 = (Integer) dataSnapshot.child("star7").getValue();
                            toilet.star7 = star7;

                            Integer star8 = (Integer) dataSnapshot.child("star8").getValue();
                            toilet.star8 = star8;

                            Integer star9 = (Integer) dataSnapshot.child("star9").getValue();
                            toilet.star9 = star9;

                            Integer reviewCount = (Integer) dataSnapshot.child("reviewCount").getValue();
                            toilet.reviewCount = reviewCount;


                            Integer wait1 = (Integer) dataSnapshot.child("wait1").getValue();
                            toilet.wait1 = wait1;

                            Integer wait2 = (Integer) dataSnapshot.child("wait2").getValue();
                            toilet.wait2 = wait2;

                            Integer wait3 = (Integer) dataSnapshot.child("wait3").getValue();
                            toilet.wait3 = wait3;

                            Integer wait4 = (Integer) dataSnapshot.child("wait4").getValue();
                            toilet.wait4 = wait4;

                            Integer wait5 = (Integer) dataSnapshot.child("wait5").getValue();
                            toilet.wait5 = wait5;

                            Integer averageWait = (Integer) dataSnapshot.child("averageWait").getValue();
                            toilet.averageWait = averageWait;

                            if (filter.starFilterSetted == true && toilet.averageStar < filter.starFilter) {
                                removedToilet = true;
                            }

                            if (filter.washletFilter == true && toilet.washlet == false) {
                                removedToilet = true;
                            }

                            if (filter.wheelchairFilter == true && toilet.wheelchair == false) {
                                removedToilet = true;
                            }

                            if (filter.onlyFemaleFilter == true && toilet.onlyFemale == false) {
                                removedToilet = true;
                            }

                            if (filter.unisexFilter == true && toilet.unisex == false) {
                                removedToilet = true;
                            }

                            if (filter.makeroomFilter == true && toilet.makeuproom == false) {
                                removedToilet = true;
                            }

                            if (filter.milkspaceFilter == true && toilet.milkspace == false) {
                                removedToilet = true;
                            }

                            if (filter.omutuFilter == true && toilet.omutu == false) {
                                removedToilet = true;
                            }

                            if (filter.ostomateFilter == true && toilet.ostomate == false) {
                                removedToilet = true;
                            }

                            if (filter.japaneseFilter == true && toilet.japanesetoilet == false) {
                                removedToilet = true;
                            }

                            if (filter.westernFilter == true && toilet.westerntoilet == false) {
                                removedToilet = true;
                            }

                            if (filter.warmSearFilter == true && toilet.warmSeat == false) {
                                removedToilet = true;
                            }

                            if (filter.baggageSpaceFilter == true && toilet.baggageSpace == false) {
                                removedToilet = true;
                            }

                            if (filter.availableFilter == true && toilet.available == false) {
                                removedToilet = true;
                            }

                            if (filter.typeFilterOn == true && toilet.type != filter.typeFilter) {
                                removedToilet = true;
                            }

                            if removedToilet == false{
                            //Added to the array

                            //Make markers


                        }










                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        String TAG = "Error";
                        Log.w(TAG, "DatabaseError",databaseError.toException());

                    }
                });


            }


            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                Log.i("GeoQueryReady","Fuckkkk");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });}
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
