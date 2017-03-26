package com.example.kazuhiroshigenobu.googlemaptraining;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class DetailViewActivity extends AppCompatActivity {



    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

    private DatabaseReference toiletRef;
    private DatabaseReference reviewsRef;
    private DatabaseReference userRef;
    private GoogleApiClient client;

    TextView toiletNameLabel;
    TextView typeAndDistance;
    TextView availableAndWaiting;
    RatingBar ratingDisplay;
    TextView ratingNumber;
    TextView ratingCount;
    TextView mapAddress;
    TextView mapHowToAccess;
    Button buttonMoreDetail;
    Button buttonKansou;
    Button buttonEdit;
    Button buttonFavorite;
    //DrawerLayout drawer;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Boolean userLikePushed = false;
    private FirebaseAuth firebaseAuth;


    Toilet toilet =  new Toilet();

    public ArrayList<String> booleanArray = new ArrayList<String>();
    //public String[] booleanArray = {"設備"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
       // mActivityTitle = getTitle().toString();
        mDrawerList = (ListView)findViewById(R.id.navList);

        toolbar = (Toolbar) findViewById(R.id.app_bar3);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle3);



//        setSupportActionBar(toolbar);


        //Add in xml


        addDrawerItems();
        setupDrawer();


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
                        Log.i("Current.key","is This working???12321");
                        Intent intent = new Intent(v.getContext(),MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        //toolbar.setNavigationIcon(R.drawable.earth);
        //toolbar.setNavigationContentDescription("戻る");

        //Trial....



       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //It may cause a crash....


        //getSupportActionBar().setHomeButtonEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailViewMap);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        Log.i("Current.key",key );
        //get name info
        Log.i("THis it it", key);
        toileGetInfo(key);

        settingReady();
        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    // your additional codes goes here
                    onMapReadyCalled(googleMap);


                }
            }}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.filter2, menu);

        return super.onCreateOptionsMenu(menu);
    }


    private void addDrawerItems() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, booleanArray);
        mDrawerList.setAdapter(mAdapter);

    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        //Commented for adding a back button March 19th 18:30


        mDrawerToggle.setDrawerIndicatorEnabled(false);
       // mDrawerLayout.addDrawerListener(mDrawerToggle);

        //Commented for adding an back button 6:30pm 19th March
    }


    private void settingReady(){
        toiletNameLabel = (TextView) findViewById(R.id.toiletName);
        typeAndDistance = (TextView) findViewById(R.id.typeAndDistance);
        availableAndWaiting = (TextView) findViewById(R.id.avaulableAndWaiting);
        ratingDisplay = (RatingBar) findViewById(R.id.ratingDisplay);
        ratingNumber = (TextView) findViewById(R.id.ratingNumber);
        ratingCount = (TextView) findViewById(R.id.ratingCount);
        mapAddress = (TextView) findViewById(R.id.mapAddress);
        mapHowToAccess = (TextView) findViewById(R.id.mapHowtoaccess);
        buttonMoreDetail = (Button) findViewById(R.id.buttonMoreDetail);
        buttonKansou = (Button) findViewById(R.id.buttonKansou);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonFavorite = (Button) findViewById(R.id.detailFavoriteButton);

        buttonMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);
              


            }
        });

        buttonKansou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), KansouActivity.class);
                intent.putExtra("reviewCount",toilet.reviewCount);
                intent.putExtra("averageWait", toilet.averageWait);
                intent.putExtra("averageStar", toilet.averageStar);
                startActivity(intent);
                finish();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), EditViewActivity.class);
                Toast.makeText(DetailViewActivity.this, String.valueOf(toilet.key), Toast.LENGTH_SHORT).show();
                intent.putExtra("EXTRA_SESSION_ID", toilet.key);
//                intent.putExtra("reviewCount",toilet.reviewCount);
//                intent.putExtra("averageWait", toilet.averageWait);
//                intent.putExtra("averageStar", toilet.averageStar);
                startActivity(intent);
                finish();
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!userLikePushed) {
                    Toast.makeText(DetailViewActivity.this, "Favorite", Toast.LENGTH_SHORT).show();
                    buttonFavorite.setBackgroundResource(R.drawable.like);
                    userLikePushed = true;
                } else {
                    buttonFavorite.setBackgroundResource(R.drawable.likebefore);
                    userLikePushed = false;
                }



            }
        });




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }



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
//                    Toilet toilet =  new Toilet();
                    // List<String> toiletData = new ArrayList<>();



                    LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                    //get from the location Manager
                    Log.i("IS THIS THE ERROR???","4");

                    Log.i("IS THIS THE ERROR???","1");
                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();
                    Log.i("IS THIS THE ERROR???","2");


                    LatLng toiletLocation = new LatLng(toilet.latitude,toilet.longitude);
                    //get from the database
                    Log.i("IS THIS THE ERROR???","5");

                    double distance = CalculationByDistance(centerLocation,toiletLocation);
                    Log.i("IS THIS THE ERROR???","6");

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


                    Log.i("IS THIS THE ERROR???","3");
                    Log.i("BOOOL???","1");

//


                    toilet.key = queryKey;
                    //Not sure about how to call key....

                    toilet.name = (String) dataSnapshot.child("name").getValue();
                    toilet.openAndCloseHours = (String) dataSnapshot.child("openAndCloseHours").getValue();
                    toilet.type = (String) dataSnapshot.child("type").getValue();
                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();

                    Log.i("BOOOL???","2");

                    toilet.addedBy = (String) dataSnapshot.child("addedBy").getValue();
                    toilet.editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();
                    toilet.address = (String) dataSnapshot.child("address").getValue();
                    toilet.howtoaccess = (String) dataSnapshot.child("howtoaccess").getValue();

                    Log.i("BOOOL???","3");

                    Long openh = (Long) dataSnapshot.child("openHours").getValue();
                    toilet.openHours = openh.intValue();
                    Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
                    toilet.closeHours = closeh.intValue();
                    Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                    toilet.reviewCount = reviewCount.intValue();
                    Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                    toilet.averageWait = averageWait.intValue();
                    Long toiletFloor = (Long) dataSnapshot.child("toiletFloor").getValue();
                    toilet.floor = toiletFloor.intValue();

                    Log.i("BOOOL???","4");

//                    Log.i("IS THIS THE ERROR???","1");
//                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
//                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();
//                    Log.i("IS THIS THE ERROR???","2");

                    toilet.available = (Boolean) dataSnapshot.child("available").getValue();
                    toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                    toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                    toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                    toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();

                    Log.i("BOOOL???","5");

                    toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();
                    toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                    toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
                    toilet.noVirus = (Boolean) dataSnapshot.child("noVirus").getValue();
                    toilet.paperForBenki = (Boolean) dataSnapshot.child("paperForBenki").getValue();
                    toilet.cleanerForBenki = (Boolean) dataSnapshot.child("cleanerForBenki").getValue();
                    toilet.autoToiletWash = (Boolean) dataSnapshot.child("nonTouchWash").getValue();

                    Log.i("BOOOL???","6");

                    toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
                    toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
                    toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
                    toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
                    toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();


                    toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                    toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                    toilet.omutuSelling = (Boolean) dataSnapshot.child("omutuSelling").getValue();
                    toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
                    toilet.milkspace = (Boolean) dataSnapshot.child("milkspace").getValue();
                    toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                    toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                    toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();


                    toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
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



                    Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);


                    booleanArray.add("トイレの設備");

                    if (toilet.japanesetoilet){
                        booleanArray.add("和式トイレ");
                    }

                    if (toilet.westerntoilet){
                        booleanArray.add("洋式トイレ");
                    }

                    if (toilet.onlyFemale){
                        booleanArray.add("女性専用トイレ");
                    }

                    if (toilet.unisex){
                        booleanArray.add("男女兼用トイレ");
                    }


                    if (toilet.washlet){
                        booleanArray.add("ウォシュレット");
                    }

                    if (toilet.warmSeat){
                        booleanArray.add("暖房便座");
                    }

                    if (toilet.autoOpen){
                        booleanArray.add("自動開閉便座");
                    }

                    if (toilet.noVirus){
                        booleanArray.add("抗菌便座");
                    }

                    if (toilet.paperForBenki){
                        booleanArray.add("便座用シート");
                    }

                    if (toilet.cleanerForBenki){
                        booleanArray.add("便座クリーナー");
                    }

                    if (toilet.autoToiletWash){
                        booleanArray.add("自動洗浄");
                    }

                    if (toilet.sensorHandWash){
                        booleanArray.add("センサー式お手洗い");
                    }

                    if (toilet.handSoap){
                        booleanArray.add("ハンドソープ");
                    }

                    if (toilet.autoHandSoap){
                        booleanArray.add("センサー式ハンドソープ");
                    }
                    if (toilet.paperTowel){
                        booleanArray.add("ペーパータオル");
                    }

                    if (toilet.handDrier){
                        booleanArray.add("ハンドドライヤー");
                    }



                    if (toilet.otohime){
                        booleanArray.add("音姫");
                    }

                    if (toilet.omutu){
                        booleanArray.add("おむつ交換台");
                    }

                    if (toilet.omutuSelling){
                        booleanArray.add("おむつ販売機");
                    }

                    if (toilet.napkinSelling){
                        booleanArray.add("ナプキン販売機");
                    }

                    if (toilet.milkspace){
                        booleanArray.add("授乳スペース");
                    }


                    if (toilet.makeuproom){
                        booleanArray.add("メイクルーム");
                    }


                    if (toilet.clothes){
                        booleanArray.add("洋服かけ");
                    }

                    if (toilet.baggageSpace){
                        booleanArray.add("荷物置き");
                    }


                    if (toilet.wheelchair){
                        booleanArray.add("車イス対応");
                    }

                    if (toilet.handrail){
                        booleanArray.add("手すり");
                    }

                    if (toilet.callHelp){
                        booleanArray.add("呼び出しボタン");
                    }

                    if (toilet.ostomate){
                        booleanArray.add("オストメイト対応");
                    }

                    if (toilet.english){
                        booleanArray.add("英語表記");
                    }

                    if (toilet.braille){
                        booleanArray.add("点字案内");
                    }
                    if (toilet.voiceGuide){
                        booleanArray.add("音声案内");
                    }

                    if (toilet.fancy){
                        booleanArray.add("おしゃれ");
                    }


                    if (toilet.smell){
                        booleanArray.add("良い香り");
                    }


                    if (toilet.conforatableWide){
                        booleanArray.add("快適な広さ");
                    }
                    if (toilet.noNeedAsk){
                        booleanArray.add("声かけ不要");
                    }


                    if (toilet.parking){
                        booleanArray.add("駐車場");
                    }

                    if (toilet.airCondition){
                        booleanArray.add("冷暖房");
                    }

                    if (toilet.wifi){
                        booleanArray.add("Wi-Fi");
                    }



                    toiletNameLabel.setText(toilet.name);
                    typeAndDistance.setText(toilet.type + "/" + toilet.distance);
                    availableAndWaiting.setText("ご利用時間" + toilet.openAndCloseHours+ "/平均待ち" + String.valueOf(toilet.averageWait) + "分");
                    ratingDisplay.setRating(averaegeStarFloat);
                    ratingNumber.setText(toilet.averageStar);
                    ratingCount.setText("(" + toilet.reviewCount + ")");
                    mapAddress.setText(toilet.address);
                    mapHowToAccess.setText(toilet.howtoaccess);



                }}
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String TAG = "Error";
                    Log.w(TAG, "DatabaseError",databaseError.toException());

                }
            });

    }

    private void reviewQuery(String queryKey) {

        reviewsRef = FirebaseDatabase.getInstance().getReference().child("reviews");

        //reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener)
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


    private void userLeave(){


        String userID = firebaseAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        userRef.child("favourite").setValue(toilet.tid);

        



        //Like list







    }
}
