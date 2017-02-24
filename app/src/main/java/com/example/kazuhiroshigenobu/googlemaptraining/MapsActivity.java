package com.example.kazuhiroshigenobu.googlemaptraining;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.ListView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //extends FramgementActivity to AppCompatActivity

    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

    private DatabaseReference databaseReference;
    private DatabaseReference toiletRef;
    private GeoFire geoFire;
   // private Toilet toilet = new Toilet();
    //Maybe delete this and use toiletList.....
    private Filter filter = new Filter();
    private List toilets = new ArrayList();
    private ListView lvtoilet;
    private ToiletListAdapter adapter;
    private List<Toilet> toiletList;
    //<Toilet> to <String>

    private List<Toilet> toiletData;
    //<Toilet> to <String>
    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayoutManager mLinearLayoutManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private ActionMenuView amvMenu;




    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference();

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
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.earth);

        Log.i("JAP98789000",String.valueOf(filter.japaneseFilter));

        //amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
//        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return onOptionsItemSelected(menuItem);
//            }
//        });

//        getActionBar().setIcon(R.drawable.earth);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("YouareThebest", "11");

                Intent intent = new Intent(v.getContext(),FilterSearchActivity.class);
                startActivity(intent);
                finish();


//                v.startActivity(SettingsActivity.class);

//                Intent intent = new Intent(SettingsActivity.class);
              //  startActivity(intent);

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
        Log.i("GetSupportActionBar",String.valueOf(getSupportActionBar()));
        Log.i("Earth", String.valueOf(R.drawable.earth));




//        Log.i("R.menu.filter",String.valueOf(R.menu.filter));
//        Log.i("R.menu.appBar",String.valueOf(R.layout.app_bar));
//        Log.i("Home", String.valueOf(R.id.home));


        if (id == R.id.account){
            Toast.makeText(this, "Hey Did you Click Account??", Toast.LENGTH_SHORT).show();
            return  true;

        } else



        if (id == R.id.filter){
            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();
            return  true;

        } else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

//        if id == R.id.
            return super.onOptionsItemSelected(item);
        }
    }

    public void mapUserCenterZoon(Location location){

    }

    public void createRecyclerView(List toiletData){
        Log.i("createReclerView()Caled","");
        recyclertView = (RecyclerView) findViewById(R.id.toiletRecycleList);
        adapter = new ToiletListAdapter(toiletData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("createReclerView()Ended","");


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

                Log.i("onLocationChanged","Called");


                // Add a marker in Sydney and move the camera
//                mMap.clear();
//                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location111"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14.0f));
//
//
//
//                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
//                Log.i("toiletSearch","willBeCalled");
//                toiletSearch(location);
//                Log.i("toiletSearch","AlreadyCalled");
//
//                //Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                try {
//                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                    if (listAddresses != null && listAddresses.size() > 0){
//
//                        Log.i("Address", listAddresses.get(0).toString());
//
//                        String address = "";
//
//                        if (listAddresses.get(0).getSubThoroughfare() != null){
//
//                            address += listAddresses.get(0).getSubThoroughfare() + ",";
//                            Log.i("AddressAdress", address);
//
//
//                        }
//                        if (listAddresses.get(0).getLocality() != null){
//
//                            address += listAddresses.get(0).getLocality() + ",";
//                            Log.i("AddressAdress", address);
//
//                        }
//                        if (listAddresses.get(0).getPostalCode() != null){
//
//                            address += listAddresses.get(0).getPostalCode() + ",";
//                            Log.i("AddressAdress", address);
//
//                        }
//                        if (listAddresses.get(0).getCountryName() != null){
//
//                            address += listAddresses.get(0).getCountryName() + "";
//                            Log.i("AddressAdress", address);
//
//                        }
//                        Log.i("AddressAdress", address);
//                        Toast.makeText(MapsActivity.this, address, Toast.LENGTH_SHORT).show();
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//

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

            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);



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
                  toiletSearch(lastKnownLocation);



              } else {
                  //When you could not get the last known location...

              }
            }
        }
    }

    public void toiletSearch(Location location){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");


        Log.i("toiletSearch","Called");

        geoFire = new GeoFire(ref);

//        Log.i("LastKnownLocation is here", String.valueOf(location));


        final Double centerLatitude = location.getLatitude();
        final Double centerLongitude = location.getLongitude();

        //LatLng centerLocation = new LatLng(centerLatitude,centerLongitude);

        Log.i("centerLatitude", String.valueOf(centerLatitude));
        Log.i("centerLongitude", String.valueOf(centerLongitude));

//        Double centerLatitude =
//        Double centerLongitude = location.getLongitude();


        Double centerRadius = 5.0;

        final List<Toilet> toiletData = new ArrayList<>();
        //This value should be changed depending on the filter...
//
        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        //toiletList = new ArrayList<>();

        //final List<Toilet>
        // toiletData = new ArrayList<>();

//
  //GeoQuery geoQuery = geoFire.queryAtLocation(centerlocation), centerRadius);
//
        Log.i("toiletSearch","BeforeDeoQueryCalled");
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(centerLatitude,centerLongitude), centerRadius);
        final LatLng centerLocation = new LatLng(centerLatitude,centerLongitude);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {



            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {



                Log.i("Geokey",key);
                Log.i("Geolocation",String.valueOf(location));
                toiletRef.child(key).addValueEventListener(new ValueEventListener() {
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


                            Log.i("OnDataChangeCalled","777888999000");
                            Filter filter =  new Filter();
                            Log.i("OnDataChangeCalled","777888999000111");

                            Log.i("toilet777.key","777888999000111");

                            LatLng centerLocation = new LatLng(centerLatitude, centerLongitude);
                            LatLng toiletLocation = new LatLng(location.latitude,location.longitude);

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





                            toilet.key = key;
                            //Not sure about how to call key....

                            Log.i("toilet777888.key",toilet.key);
                            String urlOne = (String) dataSnapshot.child("urlOne").getValue();
                            toilet.urlOne = urlOne;

                            String urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                            toilet.urlTwo = urlTwo;

                            String urlThree= (String) dataSnapshot.child("urlThree").getValue();
                            toilet.urlThree = urlThree;

                            String type = (String) dataSnapshot.child("type").getValue();
                            toilet.type = type;

                            Log.i("toilet777.type",toilet.type);
//                            Double star  = (Double) dataSnapshot.child("star").getValue();
//                            toilet.star = star;
                            //commented

                            Log.i("toilet777.star",toilet.type);


                            Boolean washlet= (Boolean) dataSnapshot.child("washlet").getValue();
                            toilet.washlet = washlet;


                            Boolean wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                            toilet.wheelchair = wheelchair;


                            Boolean onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                            toilet.onlyFemale = onlyFemale;


                            Boolean unisex = (Boolean) dataSnapshot.child("unisex").getValue();
                            toilet.unisex = unisex;
                            Log.i("toilet777.unisex",String.valueOf(toilet.unisex));


                            Boolean makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                            toilet.makeuproom = makeuproom;


                            Boolean milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
                            toilet.milkspace = milkspace;


                            Boolean omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                            toilet.omutu = omutu;


                            Boolean ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                            toilet.ostomate = ostomate;


                            Log.i("OnDataChangeCalled","777888999");
                            Boolean japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                            toilet.japanesetoilet = japanesetoilet;

                            Boolean westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                            toilet.westerntoilet = westerntoilet;


                            Boolean warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                            toilet.warmSeat = warmSeat;
                            Log.i("toilet777.warmSeat",String.valueOf(toilet.warmSeat));



                            Boolean baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
                            toilet.baggageSpace = baggageSpace;


                            Boolean available = (Boolean) dataSnapshot.child("available").getValue();
                            toilet.available = available;
                            Log.i("toilet777.ave",String.valueOf(toilet.available));


                            String howtoaceess = (String) dataSnapshot.child("howtoaccess").getValue();
                            toilet.howtoaccess = howtoaceess;
                            Log.i("toilet777.waitingtime",toilet.howtoaccess);


//                            Integer waitingtime = (Integer) dataSnapshot.child("waitingtime").getValue();
//                            toilet.waitingtime = waitingtime;
//                            Log.i("toilet777.waitingtime",String.valueOf(toilet.waitingtime));
//                            //I dont think this will be needed anymore......

                            Log.i("toilet777.heyheyyyy",toilet.howtoaccess);
                            String openinghours = (String) dataSnapshot.child("openinghours").getValue();
                            toilet.openinghours = openinghours;
                            Log.i("toilet777.openingHours",toilet.openinghours);


                            String addedBy  = (String) dataSnapshot.child("addedBy").getValue();
                            toilet.addedBy = addedBy;

                            String editedBy = (String) dataSnapshot.child("editedBy").getValue();
                            toilet.editedBy = editedBy;
                            Log.i("toilet777.editBt",String.valueOf(toilet.editedBy));




                            String averageStar = (String) dataSnapshot.child("averageStar").getValue();
                            toilet.averageStar = averageStar;

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


                            Log.i("toilet777.aveWait",String.valueOf(toilet.averageWait));


                            if (averaegeStarDouble < filter.starFilter) {

                                //Not sure averaegeStarDouble works......
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

                            if (removedToilet == false){


                              // toiletData.add(new Toilet(key));
                                Log.i("toiletData0",String.valueOf(toiletData));
                                //Toilet obj = new Toilet(toilet.key);
                               // Toilet objt = new Toilet(key);



                               // toiletData.add(String.valueOf(toilet.key));
                                toiletData.add(toilet);


                                //Data objt = new Data(name, address, contact);
                                Log.i("toiletData1",String.valueOf(toiletData));
//
                                Log.i("Trying to set a pin!!!", "");

//                                LatLng centerLocation = new LatLng(centerLatitude,centerLongitude);
//                                LatLng toiletLocation = new LatLng(location.latitude,location.longitude);

                                //LatLng sydney = new LatLng(-33.852, 151.211);
                                mMap.addMarker(new MarkerOptions().position(toiletLocation)
                                        .title(key));
                                Log.i("set set set a pin!!!", "");

                                //System.out.println(toilets);

                                createRecyclerView(toiletData);
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
               // prepareData();
                Log.i("GeoQueryReady","Fuckkkk");
               // createRecyclerView();
                Log.i("GeoQueryReady","Sorry");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.i("Firebase111Error", String.valueOf(error));

            }
        }
  );
}

//
//    public static Drawable LoadImageFromWebOperations(String url) {
//        try {
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, "src name");
//            return d;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
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
}
