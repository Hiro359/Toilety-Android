package com.example.kazuhiroshigenobu.googlemaptraining;

//import android.*;
import android.*;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class DetailViewActivity extends AppCompatActivity implements ReviewListAdapter.ReviewAdapterCallback, ReviewListAdapter.ReviewAdapterExpandImageCallBack {



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

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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
    private ImageView mainImage;
    private ImageView pictureOne;
    private ImageView pictureTwo;
    private ImageView pictureThree;





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



    Long firstPosterFavoriteNumber;
    Long lastEditerFavoriteNumber;

    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    //Copied from android document June 11



    //Experiment April 3 1pm...
    //List<Toilet> universityList = new ArrayList<>();
    //Experiment April 3 1pm...

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

        //Changed to single June 1
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
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



    @Override
    protected void onDestroy() {
        UserInfo.viewloaded = false;
        super.onDestroy();
    }

    private void addDrawerItems() {

        Log.i("addDrawerCalledBoolean",String.valueOf(booleanArray));
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

        mainImage = (ImageView) findViewById(R.id.mainImage);
        pictureOne = (ImageView) findViewById(R.id.picture1);
        pictureTwo = (ImageView) findViewById(R.id.picture2);
        pictureThree = (ImageView) findViewById(R.id.picture3);

        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(mainImage,mainImage);
                //zoomImageFromThumb(mainImage,mainImage,toilet.urlOne);
            }
        });

        pictureOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(pictureOne,pictureOne);
                //zoomImageFromThumb(pictureOne,pictureOne,toilet.urlOne);
            }
        });


        pictureTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(pictureTwo,pictureTwo);
                //zoomImageFromThumb(pictureTwo,pictureTwo,toilet.urlOne);
            }
        });

        pictureThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(pictureTwo,pictureTwo);
                //zoomImageFromThumb(pictureThree,pictureThree,toilet.urlOne);
            }
        });



        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


        buttonGoToReviewList = (Button) findViewById(R.id.buttonGoToReviewList);

        buttonGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userGoToThePlaceAction();

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

                    Log.i("FavoriteButton", "Tapped 22222");
                    favotireButtonTappedAction();

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

    private void zoomImageFromThumb(final View thumbView, ImageView image)
            //, int imageResId)
    {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        //Use Picaso June 11

            expandedImageView.setImageDrawable(image.getDrawable());
            expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));



        //expandedImageView.setImageResource(imageResId);




        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.mainImage)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //Not Sure June 11 changed to activit_detail_view..
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }



    private void userGoToThePlaceAction(){
        DatabaseReference userWentRef = FirebaseDatabase.getInstance().getReference().child("UserWentList");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String uid = user.getUid();

            userWentRef.child(uid).child(toilet.key).setValue(true);

            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + UserInfo.latitude + "," + UserInfo.longitude + "&daddr=" + toilet.latitude + "," + toilet.longitude));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);

        }


    }


    private void favotireButtonTappedAction(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i("FavoriteAction", "Tapped 22222");
        if (user != null) {
            String uid = user.getUid();

            //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (!userLikePushed) {
                //buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);
                //buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_non_colored_drawable);
                userLikePushed = true;
                buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);


                //Change icon May 16
                favoriteButtonCount();
                favoriteRef.child(uid).child(toilet.key).setValue(true);


            } else {
                buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_non_colored_drawable);

//                buttonFavorite.setBackgroundResource(R.drawable.app_love_icon_24_drawable);

                userLikePushed = false;
                favoriteRef.child(uid).child(toilet.key).removeValue();
            }
        }

    }

    private void favoriteButtonCount(){

        Log.i("favoriteButtonCount", "called 22222");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        if (!toilet.addedBy.equals("")) {
            Map<String, Object> firstPosterChildUpdate = new HashMap<>();

            firstPosterChildUpdate.put("totalFavoriteCount", firstPosterFavoriteNumber + 1);

            userRef.child(toilet.addedBy).updateChildren(firstPosterChildUpdate);

            Log.i("favorite Added By", "called 22222");



        }

        if (!toilet.editedBy.equals("")) {
            Map<String, Object> lastEditerChildUpdate = new HashMap<>();

            lastEditerChildUpdate.put("totalFavoriteCount", lastEditerFavoriteNumber + 1);

            userRef.child(toilet.editedBy).updateChildren(lastEditerChildUpdate);

            Log.i("favorite Edited By", "called 22222");
        }



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

            favoriteRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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

        Log.i("toiletGetInfo Called", "333333");

        toiletRef = FirebaseDatabase.getInstance().getReference().child("ToiletView");

        //Changed to single June 1
        toiletRef.child(queryKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("OnDataChangeCalled","333333");
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


                    toilet.key = queryKey;
                    //Not sure about how to call key....


                    toilet.openAndCloseHours = (String) dataSnapshot.child("openAndCloseHours").getValue();
                    Long typeLong = (Long) dataSnapshot.child("type").getValue();
                    toilet.type = typeLong.intValue();




                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();

                    if (!toilet.urlOne.equals("")){
                        //set picaso
                        Uri uri = Uri.parse(toilet.urlOne);
                        Picasso.with(getApplicationContext()).load(uri).into(mainImage);
                        Picasso.with(getApplicationContext()).load(uri).into(pictureOne);

                    } else {
                        mainImage.setImageResource(R.drawable.default_photo_white_drawable);
                        pictureOne.setImageResource(R.drawable.default_photo_white_drawable);

                    }

                    if (!toilet.urlTwo.equals("")){
                        //set picaso
                        Uri uri = Uri.parse(toilet.urlTwo);
                        Picasso.with(getApplicationContext()).load(uri).into(pictureTwo);
                    } else {
                        pictureTwo.setImageResource(R.drawable.default_photo_white_drawable);

                    }

                    if (!toilet.urlThree.equals("")){
                        //set picaso
                        Uri uri = Uri.parse(toilet.urlThree);
                        Picasso.with(getApplicationContext()).load(uri).into(pictureThree);
                    } else {
                        pictureThree.setImageResource(R.drawable.default_photo_white_drawable);

                    }





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

                    toilet.name = dataSnapshot.child("name").getValue() + stringToiletFloor(toilet.floor);

                    Log.i("BOOOL???","4");

                    Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);

                    toiletNameLabel.setText(toilet.name);
                    typeAndDistance.setText(stringType(toilet.type) + "/" + toilet.distance);
                    availableAndWaiting.setText("ご利用時間" + toilet.openAndCloseHours+ "/平均待ち" + String.valueOf(toilet.averageWait) + "分");
                    ratingDisplay.setRating(averaegeStarFloat);
                    ratingNumber.setText(toilet.averageStar);
                    ratingCount.setText("(" + toilet.reviewCount + ")");
                    mapAddress.setText(toilet.address);
                    mapHowToAccess.setText(toilet.howtoaccess);

                    //Moved to here June 2

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


//                    Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);


                    booleanArray.add("トイレの設備");

                    if (toilet.japanesetoilet){  booleanArray.add("和式トイレ");  }

                    if (toilet.westerntoilet){
                        booleanArray.add("洋式トイレ");
                    }

                    if (toilet.onlyFemale){
                        booleanArray.add("女性専用トイレ");
                    }

                    Log.i("Passed Boolean","8.1");
                    if (toilet.unisex){
                        booleanArray.add("男女兼用トイレ");
                    }


                    if (toilet.washlet){
                        booleanArray.add("ウォシュレット");
                    }

                    if (toilet.warmSeat){
                        booleanArray.add("暖房便座");
                    }

                    Log.i("Passed Boolean","8.3");

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

                    Log.i("Passed Boolean","8.6");

                    if (toilet.sensorHandWash){
                        booleanArray.add("センサー式お手洗い");
                    }

                    if (toilet.handSoap){
                        booleanArray.add("ハンドソープ");
                    }

                    Log.i("Passed Boolean","8.7");

                    if (toilet.autoHandSoap){
                        booleanArray.add("センサー式ハンドソープ");
                    }

                    Log.i("Passed Boolean","8.8");
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





//                    toiletNameLabel.setText(toilet.name);
//                    typeAndDistance.setText(stringType(toilet.type) + "/" + toilet.distance);
//                    availableAndWaiting.setText("ご利用時間" + toilet.openAndCloseHours+ "/平均待ち" + String.valueOf(toilet.averageWait) + "分");
//                    ratingDisplay.setRating(averaegeStarFloat);
//                    ratingNumber.setText(toilet.averageStar);
//                    ratingCount.setText("(" + toilet.reviewCount + ")");
//                    mapAddress.setText(toilet.address);
//                    mapHowToAccess.setText(toilet.howtoaccess);

                    if(!toilet.addedBy.equals("")){
                        firstPosterGetInfo(toilet.addedBy);
                         }

                    if(!toilet.addedBy.equals("")){
                        lastEditerGetInfo(toilet.editedBy);
                    }




                    addDrawerItems();
                    //
//                    setupDrawer();

                    //Moved here because I could not see the content of boolean array sometimes

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


    private String stringType(Integer typeInt){
        String typeString = "All Category";

        if (typeInt == 1){
            typeString = "Public Restroom";

        } else if (typeInt == 2){
            typeString = "Convenience Store";

        }else if (typeInt == 3){
            typeString = "Caffe";

        }else if (typeInt == 4){
            typeString = "Restaurant";

        }else if (typeInt == 5){
            typeString = "Shopping Store";

        }else if (typeInt == 6){
            typeString = "Tourist Places";

        }else if (typeInt == 7){
            typeString = "Stadium";

        }else if (typeInt == 8){
            typeString = "Kasetu Toilet";

        } else if (typeInt == 9){
            typeString = "House Toilet";

        }

        return  typeString;

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

        Log.i("ThumbsUPCalled", "1234");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();



            DatabaseReference thumbsUpRef;

            thumbsUpRef = FirebaseDatabase.getInstance().getReference().child("ThumbsUpList");


            Log.i("ThumbsUP Uid 1234", uid);

            //Changed to single June 1
            thumbsUpRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    Log.i("Datasnapshot", String.valueOf(dataSnapshot));

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        Log.i("ThumbsUP Data", "1234");

                        final String ridKey = child.getKey();
                        thumbsUpSet.add(ridKey);
                        Log.i("ThumbsUP Add", "1234");


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.i(" toiletreviewItself", "908");
            /////////////////////////////////qqqqqqqqqqqqqqqqqqqqqqqqq


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


        //Changed to single June 1
        reviewsRef.child(ridKey).addListenerForSingleValueEvent(new ValueEventListener() {
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



    private void firstPosterGetInfo(final String firstPosterID){

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(firstPosterID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName = (String) dataSnapshot.child("userName").getValue();
                user.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();

                Long totalLikedCountLong = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long totalFavoriteCountLong = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long totalHelpedCountLong = (Long) dataSnapshot.child("totalHelpedCount").getValue() + 1;


                firstPosterFavoriteNumber = totalFavoriteCountLong;
                firstPosterName.setText(String.valueOf(user.userName));
                firstPosterLikeCount.setText(String.valueOf(totalLikedCountLong));
                firstPosterFavoriteCount.setText(String.valueOf(totalFavoriteCountLong));
                firstPosterHelpCount.setText(String.valueOf(totalHelpedCountLong));
                //if user == "" ....


                if (!user.userPhoto.equals("")){
                    //set picaso
                    Uri uri = Uri.parse(user.userPhoto);
                    Picasso.with(getApplicationContext()).load(uri).into(firstPosterImage);
                } else {
                    firstPosterImage.setImageResource(R.drawable.app_default_user_icon);

                }

                firstPosterImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImageFromThumb(firstPosterImage,firstPosterImage);
                    }
                });
//
//                Uri uri = Uri.parse(user.userPhoto);
//                Picasso.with(getApplicationContext()).load(uri).into(firstPosterImage);


                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("totalHelpedCount",totalHelpedCountLong);

                userRef.child(firstPosterID).updateChildren(childUpdates);

                //Add help Count

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void lastEditerGetInfo(final String lastEditerID){

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(lastEditerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.userName = (String) dataSnapshot.child("userName").getValue();
                user.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();

                Long totalLikedCountLong = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long totalFavoriteCountLong = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long totalHelpedCountLong = (Long) dataSnapshot.child("totalHelpedCount").getValue() + 1;


                lastEditerFavoriteNumber = totalFavoriteCountLong;
                lastPosterName.setText(user.userName);
                lastEditorLikeCount.setText(String.valueOf(totalLikedCountLong));
                lastEditorFavoriteCount.setText(String.valueOf(totalFavoriteCountLong));
                lastEditorHelpCount.setText(String.valueOf(totalHelpedCountLong));
                //if user == "" ....

                if (!user.userPhoto.equals("")){
                    //set picaso
                    Uri uri = Uri.parse(user.userPhoto);
                    Picasso.with(getApplicationContext()).load(uri).into(lastEditorImage);
                } else {
                    lastEditorImage.setImageResource(R.drawable.app_default_user_icon);

                }

                lastEditorImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImageFromThumb(lastEditorImage,lastEditorImage);
                    }
                });
//                Uri uri = Uri.parse(user.userPhoto);
//                Picasso.with(getApplicationContext()).load(uri).into(lastEditorImage);

                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("totalHelpedCount",totalHelpedCountLong);

                userRef.child(lastEditerID).updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//    private void helpCountActionForPosterAndEdited(String userId){
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
//        userRef.child("Users").child(userId).
//
//    }



    public void onMapReadyCalled(GoogleMap googleMap,Double toiletLat, Double toiletLon) {
         GoogleMap mMap;
         mMap = googleMap;


        //Check it is not null
         LatLng toiletLocation = new LatLng(toiletLat,toiletLon);
         MarkerOptions markerOptions = new MarkerOptions();
         markerOptions.position(toiletLocation);
         markerOptions.title("Toilet");
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
         mMap.addMarker(markerOptions);


        //move map camera
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toiletLocation,16));
        //Check UserInfo is not null
         LatLng userLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
         Drawable userImage = ContextCompat.getDrawable(getApplication(), R.drawable.user_pin_drawable);


         MarkerOptions userMarker = new MarkerOptions();
         userMarker.position(userLocation);
         userMarker.title("User Location");
         BitmapDescriptor markerIcon = getMarkerIconFromDrawable(userImage);
         userMarker.icon(markerIcon);
         mMap.addMarker(userMarker);

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
                                toiletInfoProblemUpload(0);
                                //userWarningsListUpload();
                                break;
                            case 1:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload(1);
                                //userWarningsListUpload();
                                break;
                            case 2:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload(2);
                                //userWarningsListUpload();
                                break;
                            case 3:
                                suspiciosUserId = toilet.editedBy;
                                toiletInfoProblemUpload(3);
                                //userWarningsListUpload();
                                break;
                            case 4:
                                break;


                        }
                    }
                });
        builder.create().show();

    }

    private void toiletInfoProblemUpload(Integer problemInt){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();



            Map<String, Object> updateInfo = new HashMap();

            Map<String, Object> problemData = new HashMap();



            problemData.put("tid", toilet.key);
            problemData.put("uid", uid);
            problemData.put("time", timeString);
            problemData.put("timeNumbers", timeStampDouble);
            problemData.put("problem", problemInt);

            updateInfo.put("ToiletInfoProblems/" + postId, problemData);
            updateInfo.put("UserWarningList/" + suspiciosUserId + "/"+ uid , true);



            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();



            firebaseRef.updateChildren(updateInfo,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });


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
                                reviewReportUploadToDatabase(0,rid);
                                //userWarningsListUpload();
                                break;
                            case 1:
                                reviewReportUploadToDatabase(1,rid);
                                //userWarningsListUpload();
                                break;
                            case 2:
                                reviewReportUploadToDatabase(2,rid);
                                //userWarningsListUpload();
                                break;
                            case 3:
                                reviewReportUploadToDatabase(3,rid);
                                //userWarningsListUpload();
                                break;
                            case 4:
                                break;

                        }
                    }
                });
        builder.create().show();

    }

    private void reviewReportUploadToDatabase(Integer problemInt, String rid){
        //reviewWarningsListUpload();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();


            Map<String, Object> updateInfo = new HashMap();


            Map<String, Object> problemData = new HashMap();



            problemData.put("rid", rid);
            problemData.put("uid", uid);
            problemData.put("time", timeString);
            problemData.put("timeNumbers", timeStampDouble);
            problemData.put("problem", problemInt);



            updateInfo.put("ReviewProblems/" + postId, problemData);
            updateInfo.put("UserWarningList" + suspiciosUserId + "/" + uid, true);


            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

            firebaseRef.updateChildren(updateInfo,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });

            Toast.makeText(this, "Report Is Done", Toast.LENGTH_SHORT).show();
        }
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



    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
                Canvas canvas = new Canvas();
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap);
                // drawable.setBounds(0, 0, 20, 20);

                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                drawable.draw(canvas);

                return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onReviewImageExpandCallback(String imageUrl) {
        zoomImageFromUrl(imageUrl);

    }


    private void zoomImageFromUrl(String imageUrl)
    //, int imageResId)
    {

        final View viewBefore = findViewById(R.id.expanded_image_before);
        Log.i("zoom Called","666");
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        //Use Picaso June 11


        if (imageUrl.equals("")) {
            expandedImageView.setImageResource(R.drawable.app_default_user_icon);
        } else {
            Uri uri = Uri.parse(imageUrl);
            final Context context = expandedImageView.getContext();
            Picasso.with(context).load(uri).into(expandedImageView);

        }

        expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));


//        expandedImageView.setImageDrawable(image.getDrawable());
//        expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));



        //expandedImageView.setImageResource(imageResId);




        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).

        viewBefore.getGlobalVisibleRect(startBounds);
        findViewById(R.id.activity_detail_view)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //Not Sure June 11 changed to activit_detail_view..
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        viewBefore.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewBefore.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        viewBefore.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
