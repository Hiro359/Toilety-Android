package com.example.kazuhiroshigenobu.googlemaptraining;

//import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.BooleanResult;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class DetailViewActivity extends AppCompatActivity implements ReviewListAdapter.ReviewAdapterCallback {



//    private GoogleMap mMap;
    LocationManager locationManager;

    android.location.LocationListener locationListener;

//    private DatabaseReference toiletRef;
//    private DatabaseReference reviewsRef;
    private DatabaseReference userRef;


    private DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference favoriteRef = firebaseRef.child("FavoriteList");

    //private DatabaseReference toiletReviewsRef;

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
    Button buttonGetDirection;
    Button buttonGoToReviewList;

    Boolean userAlreadyLogin = false;
    final List<Review> reviewList = new ArrayList<>();

    Set<String> thumbsUpSet = new HashSet();

    private String suspiciosReviewId;
    //Set<String> favoriteSet = new HashSet();
    //DrawerLayout drawer;

    private ListView mDrawerList;
//    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private String suspiciosUserId;

//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;
//    private ReviewListAdapter adapter;


//    private Toolbar toolbar;
//    private TextView toolbarTitle;
    private Boolean userLikePushed = false;
//    private FirebaseAuth firebaseAuth;

    private ImageView firstPosterImage;
    private TextView firstPosterName;
    private TextView firstPosterLikeCount;
    private TextView firstPosterFavoriteCount;
    private TextView lastPosterName;

    private ImageView lastEditorImage;
    private TextView firstPosterHelpCount;
    private TextView lastEditorLikeCount;
    private TextView lastEditorFavoriteCount;
    private TextView lastEditorHelpCount;

    private Boolean reviewWarningLoadedOnce = false;

    //Experiment April 3 1pm...
    //List<Toilet> universityList = new ArrayList<>();
    //Experiment April 3 1pm...

    Boolean userWarningLoadedOnce = false;
    Toilet toilet =  new Toilet();
    User user = new User();

    public ArrayList<String> booleanArray = new ArrayList<>();
    //public String[] booleanArray = {"設備"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        // mActivityTitle = getTitle().toString();
        mDrawerList = (ListView)findViewById(R.id.navList);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar3);
//        TextView toolbarTitle;
//        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle3);





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

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            Log.i("Login 88888", "Start");
            String userID = firebaseAuth.getCurrentUser().getUid();
            userDataCheck(userID);


        } else  {

            Log.i("Login Null 88888", "Start");

        }


        //getSupportActionBar().setHomeButtonEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailViewMap);
//        GoogleApiClient client;
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.key = key;
        final Double toiletLat = getIntent().getDoubleExtra("toiletLatitude",0);
        final Double toiletLon = getIntent().getDoubleExtra("toiletLongitude",0);
        Log.i("OnCreateTOiletLati",String.valueOf(toiletLat));
        Log.i("OnCreateTOiletLong",String.valueOf(toiletLon));



        Log.i("Current.key",key );
        //get name info
        Log.i("THis it it", key);
        toileGetInfo(key);

        settingReady();
        favoriteQuery();
        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    // your additional codes goes here
                    onMapReadyCalled(googleMap, toiletLat, toiletLon);



                }
            }}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.detailviewappbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.detailSettingsButton) {
            isThereInfoProblem();
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void userDataCheck(final String userID){
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Toast.makeText(DetailViewActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("User", "Found");
                    userAlreadyLogin = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loginPlease(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("この機能を利用するにはログインが必要です");
        builder.setItems(new CharSequence[]
                        {"ログインをする", "ログインをしない"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), FirstTimeActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();
    }



//    public void createRecyclerView(List reviewList) {
//        Log.i("reviewRecycle", "Called");
//        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
//        adapter = new ReviewListAdapter(reviewList);
//        //adapter = new ToiletListAdapter(reviewData);
//        layoutManager = new LinearLayoutManager(this);
//        recyclertView.setLayoutManager(layoutManager);
//        recyclertView.setHasFixedSize(true);
//        recyclertView.setAdapter(adapter);
//        Log.i("reviewRecycle", "Ended");
//
//
//    }



    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.i("","Error checking internet connection");
                //Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.i("No network available!","");
            //Log.d(TAG, "No network available!");
        }
        return false;
    }


//    public static boolean hasActiveInternetConnection(Context context) {
//        if (isNetworkAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//                urlc.setRequestProperty("User-Agent", "Test");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 200);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error checking internet connection", e);
//            }
//        } else {
//            Log.d(LOG_TAG, "No network available!");
//        }
//        return false;
//    }

    @Override
    protected void onDestroy() {
        UserInfo.viewloaded = false;
        super.onDestroy();
    }

    private void addDrawerItems() {
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, booleanArray);
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

        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }


    private void settingReady(){
        //First Poster and Last Editer...
        firstPosterImage = (ImageView) findViewById(R.id.firstPosterImage);
        firstPosterName = (TextView) findViewById(R.id.firstPosterUserName);
        firstPosterLikeCount = (TextView) findViewById(R.id.firstPosterLikeCount);
        firstPosterFavoriteCount  = (TextView) findViewById(R.id.firstPosterFavoriteCount);
        firstPosterHelpCount = (TextView) findViewById(R.id.firstPosterHelpCount);


        lastEditorImage = (ImageView) findViewById(R.id.lastEditerImage);
        lastPosterName = (TextView) findViewById(R.id.lastEditerUserName) ;
        lastEditorLikeCount = (TextView) findViewById(R.id.lastEditerLikeCount);
        lastEditorFavoriteCount = (TextView) findViewById(R.id.lastEditerFavoriteCount);
        lastEditorHelpCount = (TextView) findViewById(R.id.lastEditerHelpCount);




        //First Poster and Last Editer
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
        buttonGetDirection = (Button) findViewById(R.id.buttonGoToThisPlace);

        buttonGoToReviewList = (Button) findViewById(R.id.buttonGoToReviewList);

        buttonGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?" + "saddr="+ UserInfo.latitude + "," + UserInfo.longitude + "&daddr=" + toilet.latitude + "," + toilet.longitude));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });



        buttonMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);



            }
        });

        buttonKansou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin) {
                    loginPlease();

                } else {

                    Intent intent = new Intent(v.getContext(), KansouActivity.class);
                    intent.putExtra("EXTRA_SESSION_ID", toilet.key);
                    intent.putExtra("toiletLatitude", toilet.latitude);
                    intent.putExtra("toiletLongitude", toilet.longitude);
                    intent.putExtra("reviewCount", toilet.reviewCount);
                    intent.putExtra("averageWait", toilet.averageWait);
                    intent.putExtra("averageStar", toilet.averageStar);
                    intent.putExtra("reviewOne", toilet.reviewOne);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin) {
                    loginPlease();
                } else {
                    nextAvtivityChoose();
                }
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin) {
                    loginPlease();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();

                        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (!userLikePushed) {
                            //buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);
                            buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_non_colored_drawable);
                            userLikePushed = true;

                            favoriteRef.child(uid).child(toilet.key).setValue(true);
                        } else {
                            buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);

                            userLikePushed = false;
                            favoriteRef.child(uid).child(toilet.key).removeValue();
                        }
                    }
                }
            }
        });

        buttonGoToReviewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin) {
                    loginPlease();

                } else {
                    Intent intent = new Intent(v.getContext(), ReviewToiletViewActivity.class);
                    intent.putExtra("EXTRA_SESSION_ID", toilet.key);
                    intent.putExtra("toiletLatitude", toilet.latitude);
                    intent.putExtra("toiletLongitude", toilet.longitude);
                    startActivity(intent);
                    finish();
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

    private void nextAvtivityChoose(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("どちらの情報を編集しますか");
        builder.setItems(new CharSequence[]
                        {"位置情報を編集する", "設備情報を変更する"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent intentA = new Intent(getApplicationContext(), EditPinLocationActivity.class);
                                Toast.makeText(DetailViewActivity.this, String.valueOf(toilet.key), Toast.LENGTH_SHORT).show();
                                intentA.putExtra("EXTRA_SESSION_ID", toilet.key);
                                intentA.putExtra("toiletLatitude",toilet.latitude);
                                intentA.putExtra("toiletLongitude",toilet.longitude);
                                startActivity(intentA);
                                finish();


                                break;

                            case 1:
                                Intent intentB = new Intent(getApplicationContext(), EditViewListActivity.class);

                                //Intent intentB = new Intent(getApplicationContext(), EditViewActivity.class);
                                Toast.makeText(DetailViewActivity.this, String.valueOf(toilet.key), Toast.LENGTH_SHORT).show();
                                intentB.putExtra("EXTRA_SESSION_ID", toilet.key);
                                intentB.putExtra("toiletLatitude",toilet.latitude);
                                intentB.putExtra("toiletLongitude",toilet.longitude);

                                startActivity(intentB);
                                finish();

                                break;

                        }
                    }
                });
        builder.create().show();


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

    private void favoriteQuery(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            favoriteRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        String favoriteKey = child.getKey();

                        if (favoriteKey.equals(toilet.key)) {
                            buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);
                            userLikePushed = true;
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    private void toileGetInfo(final String queryKey){
        DatabaseReference toiletRef;

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        toiletRef.child(queryKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("OnDataChangeCalled","777");
                // for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    dataSnapshot.getChildren();

                    //Boolean removedToilet = false;

//                    Toilet toilet =  new Toilet();
                    // List<String> toiletData = new ArrayList<>();



                    LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                    //get from the location Manager
                    Log.i("IS THIS THE ERROR???","4");

                    Log.i("IS THIS THE ERROR???","1");
                    toilet.name = (String) dataSnapshot.child("name").getValue();
                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();
                    Log.i("IS THIS THE ERROR???","2");


                    LatLng toiletLocation = new LatLng(toilet.latitude,toilet.longitude);



//                    mMap.addMarker(new MarkerOptions().position(toiletLocation).title(toilet.name));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletLocation));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletLocation, 20.0f));
                    //14.0f to 20.0f
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




                    toilet.openAndCloseHours = (String) dataSnapshot.child("openAndCloseHours").getValue();
                    Long typeLong = (Long) dataSnapshot.child("type").getValue();
                    toilet.type = typeLong.intValue();


                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();

                    Log.i("BOOOL???","2");

                    toilet.addedBy = (String) dataSnapshot.child("addedBy").getValue();
                    toilet.editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    toilet.reviewOne = (String) dataSnapshot.child("reviewOne").getValue();
                    toilet.reviewTwo = (String) dataSnapshot.child("reviewTwo").getValue();
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



                    Log.i("toilet.ReviewCountII",String.valueOf(toilet.reviewCount));


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

                    Log.i("Passed Boolean","1");

                    toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
                    toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
                    toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
                    toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
                    toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();


                    Log.i("Passed Boolean","2");
                    //From Maps Activity
                    //others one

                    toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
                    toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
                    toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
                    toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                    toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();


                    Log.i("Passed Boolean","3");

                    //others two
                    toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
                    toilet.english = (Boolean) dataSnapshot.child("english").getValue();
                    toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
                    toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
                    toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();


                    Log.i("Passed Boolean","4");
                    //for ladys

                    toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                    toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
                    toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                    toilet.ladyOmutu = (Boolean) dataSnapshot.child("ladyOmutu").getValue();
                    toilet.ladyBabyChair = (Boolean) dataSnapshot.child("ladyBabyChair").getValue();
                    toilet.ladyBabyChairGood = (Boolean) dataSnapshot.child("ladyBabyChairGood").getValue();
                    toilet.ladyBabyCarAccess = (Boolean) dataSnapshot.child("ladyBabyCarAccess").getValue();

                    Log.i("Passed Boolean","5");
                    //for Mans
                    toilet.maleOmutu = (Boolean) dataSnapshot.child("maleOmutu").getValue();
                    toilet.maleBabyChair = (Boolean) dataSnapshot.child("maleBabyChair").getValue();
                    toilet.maleBabyChairGood = (Boolean) dataSnapshot.child("maleBabyChairGood").getValue();
                    toilet.maleBabyCarAccess = (Boolean) dataSnapshot.child("maleBabyCarAccess").getValue();

                    //for Family Restroom
                    Log.i("Passed Boolean","6");

                    toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                    toilet.wheelchairAccess = (Boolean) dataSnapshot.child("wheelchairAccess").getValue();
                    toilet.autoDoor = (Boolean) dataSnapshot.child("autoDoor").getValue();
                    toilet.callHelp = (Boolean) dataSnapshot.child("callHelp").getValue();
                    toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                    toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
                    toilet.voiceGuide = (Boolean) dataSnapshot.child("voiceGuide").getValue();
                    toilet.familyOmutu = (Boolean) dataSnapshot.child("familyOmutu").getValue();
                    toilet.familyBabyChair = (Boolean) dataSnapshot.child("familyBabyChair").getValue();
                    //From Maps Activity
                    ///
                    Log.i("Passed Boolean","7");




                    ////
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



                    Log.i("Passed Boolean","8");


                    Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);


                    booleanArray.add("トイレの設備");

                    if (toilet.japanesetoilet){  booleanArray.add("和式トイレ");  }

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



                    //others One
                    Log.i("Passed Boolean","9");



                    if (toilet.clothes){
                        booleanArray.add("洋服かけ");
                    }

                    if (toilet.baggageSpace){
                        booleanArray.add("荷物置き");
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



                    //others Two


                    if (toilet.noNeedAsk){
                        booleanArray.add("声かけ不要");
                    }

                    if (toilet.english){
                        booleanArray.add("英語表記");
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

                    //Ladys

                    Log.i("Passed Boolean","10");

                    if (toilet.otohime){
                        booleanArray.add("音姫");
                    }


                    if (toilet.napkinSelling){
                        booleanArray.add("ナプキン販売機");
                    }


                    if (toilet.makeuproom){
                        booleanArray.add("メイクルーム");

                    }
                    if (toilet.ladyOmutu){
                        booleanArray.add("おむつ交換台(女性)");

                    }
                    if (toilet.ladyBabyChair){
                        booleanArray.add("ベビーチェア(女性)");

                    }
                    if (toilet.ladyBabyChairGood){
                        booleanArray.add("安全なベビーチェア(女性)");

                    }
                    if (toilet.ladyBabyCarAccess){
                        booleanArray.add("ベビーカーでのアクセス(女性)");

                    }




                    Log.i("Passed Boolean","11");







                    //Males

                    if (toilet.maleOmutu){
                        booleanArray.add("おむつ交換台(男性)");

                    }
                    if (toilet.maleBabyChair){
                        booleanArray.add("ベビーチェア(男性)");

                    }
                    if (toilet.maleBabyChairGood){
                        booleanArray.add("安全なベビーチェア(男性)");

                    }
                    if (toilet.maleBabyCarAccess){
                        booleanArray.add("ベビーカーでのアクセス(男性)");

                    }


                    Log.i("Passed Boolean","12");


                    //Family











                    if (toilet.wheelchair){
                        booleanArray.add("車イス対応");
                    }

                    if (toilet.wheelchairAccess){
                        booleanArray.add("車イスでアクセス可能");
                    }

                    if (toilet.autoDoor){
                        booleanArray.add("自動ドア");
                    }

                    if (toilet.callHelp){
                        booleanArray.add("呼び出しボタン");
                    }

                    if (toilet.ostomate){
                        booleanArray.add("オストメイト対応");
                    }

                    if (toilet.braille){
                        booleanArray.add("点字案内");
                    }
                    if (toilet.voiceGuide){
                        booleanArray.add("音声案内");
                    }






                    Log.i("Passed Boolean","13");

                    if (toilet.milkspace){
                        booleanArray.add("授乳スペース");
                    }

                    if (toilet.babyroomOnlyFemale){
                        booleanArray.add("女性限定");
                    }

                    if (toilet.babyroomManCanEnter){
                        booleanArray.add("男性入室可能");
                    }


                    if (toilet.babyPersonalSpace){
                        booleanArray.add("個室あり");
                    }
                    if (toilet.babyPersonalSpaceWithLock){
                        booleanArray.add("鍵付き個室あり");
                    }

                    if (toilet.babyRoomWideSpace){
                        booleanArray.add("広いスペース");
                    }




                    if (toilet.babyCarRental){
                        booleanArray.add("ベビーカーレンタル");
                    }


                    if (toilet.babyCarAccess){
                        booleanArray.add("ベビーカーでアクセス可能");
                    }



                    if (toilet.omutu){
                        booleanArray.add("おむつ交換台");


                    }

                    if (toilet.hipWashingStuff){
                        booleanArray.add("おしりふき");
                    }

                    if (toilet.babyTrashCan){
                        booleanArray.add("おむつ用ゴミ箱");
                    }


                    if (toilet.omutuSelling){
                        booleanArray.add("おむつ販売機");
                    }

                    if (toilet.babyRoomSink){
                        booleanArray.add("シンク");
                    }

                    if (toilet.babyWashStand){
                        booleanArray.add("洗面台");
                    }


                    if (toilet.babyHotWater){
                        booleanArray.add("給湯器");
                    }
                    if (toilet.babyMicroWave){
                        booleanArray.add("電子レンジ");
                    }

                    if (toilet.babyWaterSelling){
                        booleanArray.add("飲料自販機");
                    }

                    if (toilet.babyFoddSelling){
                        booleanArray.add("離乳食販売");
                    }
                    if (toilet.babyEatingSpace){
                        booleanArray.add("飲食スペース");
                    }



                    if (toilet.babyChair){
                        booleanArray.add("ベビーチェア");
                    }

                    if (toilet.babySoffa){
                        booleanArray.add("ソファ");
                    }
                    if (toilet.babyKidsToilet){
                        booleanArray.add("キッズトイレ");
                    }

                    if (toilet.babyKidsSpace){
                        booleanArray.add("キッズスペース");
                    }

                    if (toilet.babyHeightMeasure){
                        booleanArray.add("身長計");
                    }
                    if (toilet.babyWeightMeasure){
                        booleanArray.add("体重計");
                    }

                    if (toilet.babyToy){
                        booleanArray.add("おもちゃ");
                    }


                    if (toilet.babyFancy){
                        booleanArray.add("おしゃれ");
                    }
                    if (toilet.babySmellGood){
                        booleanArray.add("良い香り");
                    }





                    toiletNameLabel.setText(toilet.name);
                    typeAndDistance.setText(toilet.type + "/" + toilet.distance);
                    availableAndWaiting.setText("ご利用時間" + toilet.openAndCloseHours+ "/平均待ち" + String.valueOf(toilet.averageWait) + "分");
                    ratingDisplay.setRating(averaegeStarFloat);
                    ratingNumber.setText(toilet.averageStar);
                    ratingCount.setText("(" + toilet.reviewCount + ")");
                    mapAddress.setText(toilet.address);
                    mapHowToAccess.setText(toilet.howtoaccess);

                    firstPosterGetInfo(toilet.addedBy);
                    lastEditerGetInfo(toilet.editedBy);

                    Log.i("thumbsUp","908");
                    thumbsUpQuery(toilet.reviewOne, toilet.reviewTwo);
                    //reviewQuery(toilet.key);



                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                String TAG = "Error";
                Log.w(TAG, "DatabaseError",databaseError.toException());

            }
        });


    }


    @SuppressWarnings("unchecked")
    private void createRecyclerView(List reviewList) {
        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        ReviewListAdapter adapter;
        Log.i("reviewRecycle", "Called");
        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new ReviewListAdapter(reviewList, this);
        //adapter = new ToiletListAdapter(reviewData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");



        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
        recyclertView.addItemDecoration(dividerItemDecoration);

        //Added for devider April 30

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),
//                layoutManager.getOrientation());
//        recyclertView.addItemDecoration(dividerItemDecoration);


    }

    private void thumbsUpQuery(String ridOne, String ridTwo){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();


            DatabaseReference thumbsUpRef;

            thumbsUpRef = FirebaseDatabase.getInstance().getReference().child("ThumbsUpList");


            thumbsUpRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        final String ridKey = child.getKey();
                        thumbsUpSet.add(ridKey);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.i(" toiletreviewItself", "908");

            if (!ridOne.equals("")) {
                getReviewInfoAndUserInfo(ridOne);
                //toiletReviewQuery(ridOne);
                Log.i(" toiletreviewOne);", "908");
            }

            if (!ridTwo.equals("")) {
                getReviewInfoAndUserInfo(ridTwo);
                //toiletReviewQuery(ridTwo);
                Log.i(" toiletReviereviewTwo);", "908");
            }
//        if (!toilet.reviewOne.equals("")) {
//            toiletReviewQuery(toilet.reviewOne);
//            Log.i(" toiletreviewOne);","");
//        }
//
//        if (!toilet.reviewTwo.equals("")) {
//            toiletReviewQuery(toilet.reviewTwo);
//            Log.i(" toiletReviereviewTwo);","");
//        }

        }

    }


//    private void toiletReviewQuery(String queryKey){
//        // final List<Review> reviewList = new ArrayList<>();
//
//        toiletReviewsRef = FirebaseDatabase.getInstance().getReference().child("ToiletReviews");
//
//        toiletReviewsRef.child(queryKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (!UserInfo.viewloaded){
//
//                    Log.i("DataSnap112233", String.valueOf(dataSnapshot));
//
//                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
//
//                        Log.i("DataChild112233", String.valueOf(child.getKey()));
//
//
//                        //final String ridKey = child.getValue().toString();
//
//                        final String ridKey = child.getKey();
//
//                        getReviewInfoAndUserInfo(ridKey);
//
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void getReviewInfoAndUserInfo(final String ridKey) {


//        final List<Review> reviewList = new ArrayList<>();
        DatabaseReference reviewsRef;

        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");


        reviewsRef.child(ridKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!UserInfo.viewloaded) {
                    final Review review = new Review();


                    review.rid = ridKey;

                    if (thumbsUpSet.contains(review.rid)) {
                        review.userLiked = true;
                    }


                    review.uid = (String) dataSnapshot.child("uid").getValue();
                    review.feedback = (String) dataSnapshot.child("feedback").getValue();
                    review.time = (String) dataSnapshot.child("time").getValue();
                    review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();


                    Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
                    review.likedCount = likedCount.intValue();
//                Long star = (Long) dataSnapshot.child("star").getValue();
//                review.star = star.doubleValue();


                    review.star = (String) dataSnapshot.child("star").getValue();


                    userRef = FirebaseDatabase.getInstance().getReference().child("Users");

                    userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!UserInfo.viewloaded) {
                                Log.i("userInfoQuery", "Called");


                                review.userName = (String) dataSnapshot.child("userName").getValue();
                                Log.i("userInfoQuery1", "Called");
                                review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
                                Log.i("userInfoQuery2", "Called");
//                        review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//                        Log.i("review.waitingTime",review.waitingtime);


                                Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
                                Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                                Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
                                review.totalLikedCount = totalLikedCount.intValue();
                                review.totalFavoriteCount = totalFavoriteCount.intValue();
                                review.totalHelpedCount = totalHelpedCount.intValue();


                                reviewList.add(review);
                                createRecyclerView(reviewList);

                                Log.i("reviewQueryCalled", "End");

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




//    private void reviewQuery(String queryKey) {
//
//
//        final List<Review> reviewList = new ArrayList<>();
//
//        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
//        //Not sure i get the right toilet.key
//        reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//                final Review review = new Review();
//
//
//
//                Log.i("reviewQueryCalled","Start");
//
//                review.uid = (String) dataSnapshot.child("uid").getValue();
//                review.feedback = (String) dataSnapshot.child("feedback").getValue();
//                review.time = (String) dataSnapshot.child("time").getValue();
//                review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//
//
//                Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
//                review.likedCount = likedCount.intValue();
////                Long star = (Long) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//                review.star = (String) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//
//                Log.i("reviewQueryCalled","Middle");
//
//                //this may cause an error, it might be needed to be long
//
//                userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i("userInfoQuery","Called");
//
//
//                        review.userName = (String) dataSnapshot.child("userName").getValue();
//                        Log.i("userInfoQuery1","Called");
//                        review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
//                        Log.i("userInfoQuery2","Called");
////                        review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
////                        Log.i("review.waitingTime",review.waitingtime);
//
//
//
//
//
//
//                        Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
//                        Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
//                        Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
//                        review.totalLikedCount = totalLikedCount.intValue();
//                        review.totalFavoriteCount = totalFavoriteCount.intValue();
//                        review.totalHelpedCount = totalHelpedCount.intValue();
//
//
//                        reviewList.add(review);
//                        createRecyclerView(reviewList);
//
//                        Log.i("reviewQueryCalled","End");
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    //reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener)
    //}

    private void firstPosterGetInfo(String firstPosterID){

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(firstPosterID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName = (String) dataSnapshot.child("userName").getValue();
                user.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();

                Long totalLikedCountLong = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long totalFavoriteCountLong = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long totalHelpedCountLong = (Long) dataSnapshot.child("totalHelpedCount").getValue();

                firstPosterName.setText(String.valueOf(user.userName));
                firstPosterLikeCount.setText(String.valueOf(totalLikedCountLong));
                firstPosterFavoriteCount.setText(String.valueOf(totalFavoriteCountLong));
                firstPosterHelpCount.setText(String.valueOf(totalHelpedCountLong));
                //if user == "" ....

                Uri uri = Uri.parse(user.userPhoto);
                Picasso.with(getApplicationContext()).load(uri).into(firstPosterImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void lastEditerGetInfo(String lastEditerID){

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(lastEditerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName = (String) dataSnapshot.child("userName").getValue();
                user.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();

                Long totalLikedCountLong = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long totalFavoriteCountLong = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long totalHelpedCountLong = (Long) dataSnapshot.child("totalHelpedCount").getValue();

                lastPosterName.setText(user.userName);
                lastEditorLikeCount.setText(String.valueOf(totalLikedCountLong));
                lastEditorFavoriteCount.setText(String.valueOf(totalFavoriteCountLong));
                lastEditorHelpCount.setText(String.valueOf(totalHelpedCountLong));
                //if user == "" ....
                Uri uri = Uri.parse(user.userPhoto);
                Picasso.with(getApplicationContext()).load(uri).into(lastEditorImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void reviewOneGetInfo(String ridOne){
//
//        final Review reviewOne = new Review();
//
//        reviewsRef.child(ridOne).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                reviewOne.uid = (String) dataSnapshot.child("uid").getValue();
//                reviewOne.feedback = (String) dataSnapshot.child("feedback").getValue();
//                reviewOne.time = (String) dataSnapshot.child("time").getValue();
//                reviewOne.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//
//                Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
//                reviewOne.likedCount = likedCount.intValue();
////                Long star = (Long) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//                reviewOne.star = (String) dataSnapshot.child("star").getValue();
//
//
//                userRef.child(reviewOne.uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//
//                        reviewOne.userName = (String) dataSnapshot.child("userName").getValue();
//                        reviewOne.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
//
//
//                        Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
//                        Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
//                        Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
//                        reviewOne.totalLikedCount = totalLikedCount.intValue();
//                        reviewOne.totalFavoriteCount = totalFavoriteCount.intValue();
//                        reviewOne.totalHelpedCount = totalHelpedCount.intValue();
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    public void onMapReadyCalled(GoogleMap googleMap,Double toiletLat, Double toiletLon) {
         GoogleMap mMap;
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        } else{

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);

            }else {
                //When the permission is granted....
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);

                if (lastKnownLocation != null){
                    LatLng toiletLocation = new LatLng(toiletLat,toiletLon);
                    mMap.addMarker(new MarkerOptions().position(toiletLocation).title("施設の位置"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletLocation, 14.0f));
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    //When you could not get the last known location...
                    //I am not sure how to handle this

                }
            }
        }
    }

    private void isThereInfoProblem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("情報の誤りを報告しますか");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"報告する", "報告しない"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                whatIsInfoProblem();
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();

    }

    private void whatIsInfoProblem(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("問題だと思う点を教えてください");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"施設の写真が不適切である", "施設の情報が正確でない","投稿者の名前または写真が適切でない"
                                ,"編集者の名前または写真が適切でない","いいえ、問題はありません"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload("the picture of this place is not appropriate");
                                userWarningsListUpload();
                                break;
                            case 1:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload("the infomation of this place is not correct");
                                userWarningsListUpload();
                                break;
                            case 2:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload("Fisrt Poster Not Appropriate");
                                userWarningsListUpload();
                                break;
                            case 3:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload("Last Editer Not Appropriate");
                                userWarningsListUpload();
                                break;
                            case 4:
                                break;


                        }
                    }
                });
        builder.create().show();

    }

    private void toiletInfoProblemUpload(String problemString){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();



            DatabaseReference reviewProblemRef = FirebaseDatabase.getInstance().getReference().child("ToiletInfoProblems");

            reviewProblemRef.child(postId).setValue(new ToiletProblem(
                    toilet.key ,uid, timeString, timeStampDouble, problemString)

            );

            Toast.makeText(this, "Report Is Done", Toast.LENGTH_SHORT).show();
            //Should i make a dialog for this?? May 14


            Log.i("Post Done", "222222");


        }


    }

    @Override
    public void onReviewMethodCallback(final String rid, final String suspiciosUser) {
        //Show Dialog

        suspiciosReviewId = rid;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("Report Review");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"ログインをする", "ログインをしない"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                whatIsTheProblem(rid, suspiciosUser);
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();


        //Post Report




    }


    private void whatIsTheProblem(final String rid, final String suspiciosUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        suspiciosUserId = suspiciosUser;
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("問題だと思う点を教えてください");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"感想の内容に誤りがある", "感想の内容が不適切である","ユーザーの写真が不適切である"
                                ,"ユーザーの名前が不適切である","いいえ、問題はありません"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                reviewReportUploadToDatabase("The content of the review is not correct",rid);
                                userWarningsListUpload();
                                break;
                            case 1:
                                reviewReportUploadToDatabase("The content of the review is not relevent",rid);
                                userWarningsListUpload();
                                break;
                            case 2:
                                reviewReportUploadToDatabase("The picture of the user is not appropriate",rid);
                                userWarningsListUpload();
                                break;
                            case 3:
                                reviewReportUploadToDatabase("The name of the user is not appropriate",rid);
                                userWarningsListUpload();
                                break;
                            case 4:
                                break;


                        }
                    }
                });
        builder.create().show();

    }

    private void reviewReportUploadToDatabase(String problemString, String rid){
        reviewWarningsListUpload();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();

            DatabaseReference reviewProblemRef = FirebaseDatabase.getInstance().getReference().child("ReviewProblems");

            reviewProblemRef.child(postId).setValue(new ReviewReport(
                    rid,uid, timeString, timeStampDouble, problemString)

            );

            Toast.makeText(this, "Report Is Done", Toast.LENGTH_SHORT).show();
            //Should i make a dialog for this?? May 14

            Log.i("Post Done", "222222");


        }
    }

    private void userWarningsListUpload(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("UserWarningList");
        if (user != null){
            String uid = user.getUid();
            userWarningsListRef.child(suspiciosUserId).child(uid).setValue(true);
            userWarningCount();
        }

    }

    private void userWarningCount(){
        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("UserWarningList");


        userWarningsListRef.child(suspiciosUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!userWarningLoadedOnce) {
                    //Call Once //Maybe I need boolean filter
                    Long warningCount = dataSnapshot.getChildrenCount();
                    userWarningCountUpload(warningCount);

                    userWarningLoadedOnce = true;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void userWarningCountUpload(Long warningCount){
        DatabaseReference userWarningsCountRef = FirebaseDatabase.getInstance().getReference().child("UserWarningCount");

        userWarningsCountRef.child(suspiciosUserId).setValue(warningCount);

    }

    private void reviewWarningsListUpload(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningList");
        if (user != null){
            String uid = user.getUid();
            userWarningsListRef.child(suspiciosReviewId).child(uid).setValue(true);
            reviewWarningCount();
        }

    }

    private void reviewWarningCount(){
        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningList");


        userWarningsListRef.child(suspiciosReviewId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!reviewWarningLoadedOnce) {
                    //Call Once //Maybe I need boolean filter
                    Long warningCount = dataSnapshot.getChildrenCount();
                    reviewWarningCountUpload(warningCount);
                    reviewWarningLoadedOnce = true;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reviewWarningCountUpload(Long warningCount){
        DatabaseReference userWarningsCountRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningCount");

        userWarningsCountRef.child(suspiciosReviewId).setValue(warningCount);

    }


    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(time);

        // String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        Log.i("TIME121", DateFormat.format("yyyy-MM-dd", cal).toString());
        return DateFormat.format("yyyy-MM-dd", cal).toString();
    }


    private String getHour() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);

        return "-" + String.valueOf(hour) + ":" + String.valueOf(minute);
//        SimpleDateFormat simpleDateFormatArrivals = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        return  simpleDateFormatArrivals;
    }





}
