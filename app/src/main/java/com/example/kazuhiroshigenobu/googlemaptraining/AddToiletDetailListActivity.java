package com.example.kazuhiroshigenobu.googlemaptraining;

//import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;

public class AddToiletDetailListActivity extends AppCompatActivity {


    Spinner typeSpinner;
    Spinner waitingTimeSpinner;
    Spinner floorSpinner;
    Spinner startHoursSpinner;
    Spinner startMinutesSpinner;
    Spinner endHoursSpinner;
    Spinner endMinutesSpinner;

    EditText textToiletName;
    EditText textHowToAccess;
    EditText textFeedback;

    RatingBar ratingBar;

    LocationManager locationManager;

    android.location.LocationListener locationListener;

    Button addPhoto;
    Button buttonAddInfo;

//    Boolean mainImageAdded;
//    Boolean subImageOneAdded;
//    Boolean subImageTwoAdded;

    //private GoogleApiClient client;
    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;

    Boolean spinnerLoaded = false;
    Integer photoSelected = 0;
    Integer toiletFloor = 3;
    private String urlOne = "";
    private String urlTwo = "";
    private String urlThree = "";

    ImageView mainImage;
    ImageView subImage1;
    ImageView subImage2;

    //private Uri filePath;

//    Uri mainImageUri;
//    Uri subImageOneUri;
//    Uri subImageTwoUri;


    ArrayAdapter<CharSequence> adapterType;
    ArrayAdapter<CharSequence> adapterWaitingtime;
    ArrayAdapter<CharSequence> adapterFloor;
    ArrayAdapter<CharSequence> adapterStartHours;
    ArrayAdapter<CharSequence> adapterStartMinutes;
    ArrayAdapter<CharSequence> adapterEndHours;
    ArrayAdapter<CharSequence> adapterEndMinutes;


    FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("images");


    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");

    GeoFire geoFire = new GeoFire(locationRef);

    DatabaseReference toiletRef = FirebaseDatabase.getInstance().getReference("Toilets");

    String newRid = UUID.randomUUID().toString();
    String newTid = UUID.randomUUID().toString();


    Toolbar toolbar;
    TextView toolbarTitle;

    java.util.Calendar c = java.util.Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH) + 1;
    int day = c.get(Calendar.DATE);
    int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
    int minute = c.get(java.util.Calendar.MINUTE);

    SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();
    //private AddBooleansListAdapter adapter;
    //I dont know how to deal with this warning May 7


//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;
//    private AddBooleansListAdapter adapter;
    //private FilterListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet_detail_list);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addCheckMap);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        if (googleMap != null) {
                                            // your additional codes goes here
                                            onMapReadyCalled(googleMap);


                                        }
                                    }
                                }
        );

//        GoogleApiClient client;
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.getCurrentUser().getUid();


        //firebaseAuth.getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.app_bar_add_toilet);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.addToiletTitleText);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        backToAccountActivity();
                    }
                }
        );


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        sparseArrayReady();
        sppinnerReady();
        textReady();
        othersReady();

        //final java.util.Calendar c = java.util.Calendar.getInstance();


        if (hour == 0) {
            Log.i("Time12321", String.valueOf(minute));

        } else {
            int hourMul = hour * 100;
            Log.i("Time12321", String.valueOf(hourMul + minute));
        }


        setupUI(findViewById(R.id.activity_add_toilet_detail));


        //THis is Wrong!!!!!!!
    }

    private void backToAccountActivity() {

        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_pin_location_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.buttonChangePin) {
            valueCheck();
        }
        return super.onOptionsItemSelected(item);
    }


    public static void hideSoftKeyboard(Activity activity) {


        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(AddToiletDetailListActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void sparseArrayReady() {


        filterSparseArray.append(0, new FilterBooleans("設備", false));
        filterSparseArray.append(1, new FilterBooleans("和式トイレ", false));
        filterSparseArray.append(2, new FilterBooleans("洋式トイレ", false));
        filterSparseArray.append(3, new FilterBooleans("女性専用トイレ", false));
        filterSparseArray.append(4, new FilterBooleans("男女兼用トイレ", false));


        filterSparseArray.append(5, new FilterBooleans("機能", false));
        filterSparseArray.append(6, new FilterBooleans("ウォシュレット", false));
        filterSparseArray.append(7, new FilterBooleans("暖房便座", false));
        filterSparseArray.append(8, new FilterBooleans("自動開閉便座", false));
        filterSparseArray.append(9, new FilterBooleans("抗菌便座", false));
        filterSparseArray.append(10, new FilterBooleans("便座用シート", false));
        filterSparseArray.append(11, new FilterBooleans("便座クリーナー", false));
        filterSparseArray.append(12, new FilterBooleans("自動洗浄", false));


        filterSparseArray.append(13, new FilterBooleans("洗面台設備", false));
        filterSparseArray.append(14, new FilterBooleans("センサー式お手洗い", false));
        filterSparseArray.append(15, new FilterBooleans("ハンドソープ", false));
        filterSparseArray.append(16, new FilterBooleans("自動ハンドソープ", false));
        filterSparseArray.append(17, new FilterBooleans("ペーパータオル", false));
        filterSparseArray.append(18, new FilterBooleans("ハンドドライヤー", false));


        filterSparseArray.append(19, new FilterBooleans("1,その他", false));
        filterSparseArray.append(20, new FilterBooleans("おしゃれ", false));
        filterSparseArray.append(21, new FilterBooleans("いい香り", false));
        filterSparseArray.append(22, new FilterBooleans("快適な広さ", false));
        filterSparseArray.append(23, new FilterBooleans("洋服掛け", false));
        filterSparseArray.append(24, new FilterBooleans("荷物置き", false));

        filterSparseArray.append(25, new FilterBooleans("2,その他", false));
        filterSparseArray.append(26, new FilterBooleans("利用の際の声かけ不要", false));
        filterSparseArray.append(27, new FilterBooleans("英語表記", false));
        filterSparseArray.append(28, new FilterBooleans("駐車場", false));
        filterSparseArray.append(29, new FilterBooleans("冷暖房", false));
        filterSparseArray.append(30, new FilterBooleans("無料Wi-Fi", false));


        filterSparseArray.append(31, new FilterBooleans("女性トイレ", false));
        filterSparseArray.append(32, new FilterBooleans("音姫", false));
        filterSparseArray.append(33, new FilterBooleans("ナプキン販売機", false));
        filterSparseArray.append(34, new FilterBooleans("パウダールーム", false));
        filterSparseArray.append(35, new FilterBooleans("おむつ交換台", false));
        filterSparseArray.append(36, new FilterBooleans("ベビーキープ", false));
        filterSparseArray.append(37, new FilterBooleans("安全なベビーキープ", false));
        filterSparseArray.append(38, new FilterBooleans("ベビーカーでのアクセス", false));


        filterSparseArray.append(39, new FilterBooleans("男性トイレ", false));
        filterSparseArray.append(40, new FilterBooleans("おむつ交換台", false));
        filterSparseArray.append(41, new FilterBooleans("ベビーキープ", false));
        filterSparseArray.append(42, new FilterBooleans("安全なベビーキープ", false));
        filterSparseArray.append(43, new FilterBooleans("ベビーカーでのアクセス", false));


        filterSparseArray.append(44, new FilterBooleans("多目的トイレ", false));

        filterSparseArray.append(45, new FilterBooleans("車イス対応", false));
        filterSparseArray.append(46, new FilterBooleans("車イスでアクセス可能", false));
        filterSparseArray.append(47, new FilterBooleans("自動ドア", false));
        filterSparseArray.append(48, new FilterBooleans("呼び出しボタン", false));
        filterSparseArray.append(49, new FilterBooleans("オストメイト",false));
        filterSparseArray.append(50, new FilterBooleans("点字案内", false));
        filterSparseArray.append(51, new FilterBooleans("音声案内", false));
        filterSparseArray.append(52, new FilterBooleans("おむつ交換台", false));
        filterSparseArray.append(53, new FilterBooleans("ベビーチェア", false));


        filterSparseArray.append(54, new FilterBooleans("1,ベビールームについて", false));
        filterSparseArray.append(55, new FilterBooleans("授乳スペース", false));
        filterSparseArray.append(56, new FilterBooleans("女性限定", false));
        filterSparseArray.append(57, new FilterBooleans("男性入室可能", false));
        filterSparseArray.append(58, new FilterBooleans("個室あり", false));
        filterSparseArray.append(59, new FilterBooleans("鍵付き個室あり", false));
        filterSparseArray.append(60, new FilterBooleans("広いスペース", false));


        filterSparseArray.append(61, new FilterBooleans("2,ベビールームについて", false));
        filterSparseArray.append(62, new FilterBooleans("ベビーカー貸出し", false));
        filterSparseArray.append(63, new FilterBooleans("ベビーカーでアクセス可能", false));
        filterSparseArray.append(64, new FilterBooleans("おむつ交換台", false));
        filterSparseArray.append(65, new FilterBooleans("おしりふき", false));
        filterSparseArray.append(66, new FilterBooleans("おむつ用ゴミ箱", false));
        filterSparseArray.append(67, new FilterBooleans("おむつ販売機", false));


        filterSparseArray.append(68, new FilterBooleans("3,ベビールームについて", false));
        filterSparseArray.append(69, new FilterBooleans("シンク", false));
        filterSparseArray.append(70, new FilterBooleans("洗面台", false));
        filterSparseArray.append(71, new FilterBooleans("給湯器", false));
        filterSparseArray.append(72, new FilterBooleans("電子レンジ", false));
        filterSparseArray.append(73, new FilterBooleans("飲料自販機", false));
        filterSparseArray.append(74, new FilterBooleans("離乳食販売機", false));
        filterSparseArray.append(75, new FilterBooleans("飲食スペース", false));


        filterSparseArray.append(76, new FilterBooleans("4,ベビールームについて", false));
        filterSparseArray.append(77, new FilterBooleans("ベビーチェア", false));
        filterSparseArray.append(78, new FilterBooleans("ソファ", false));
        filterSparseArray.append(79, new FilterBooleans("キッズトイレ", false));
        filterSparseArray.append(80, new FilterBooleans("キッズスペース", false));
        filterSparseArray.append(81, new FilterBooleans("身長計", false));
        filterSparseArray.append(82, new FilterBooleans("体重計", false));
        filterSparseArray.append(83, new FilterBooleans("おもちゃ", false));
        filterSparseArray.append(84, new FilterBooleans("おしゃれ", false));
        filterSparseArray.append(85, new FilterBooleans("いい香り", false));


        createRecyclerView(filterSparseArray);


    }


    @SuppressWarnings("unchecked")
    private void createRecyclerView(SparseArray array) {
        Log.i("reviewRecycle", "Called");

        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        AddBooleansListAdapter adapter;


        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new AddBooleansListAdapter(array);
        //adapter = new FilterListAdapter(array);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(), VERTICAL);
        recyclertView.addItemDecoration(dividerItemDecoration);

        recyclertView.setHasFixedSize(true);
        recyclertView.setNestedScrollingEnabled(false);


    }

    private void sppinnerReady() {

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        waitingTimeSpinner = (Spinner) findViewById(R.id.spinnerWaitingTime);
        floorSpinner = (Spinner) findViewById(R.id.locationFloorSpinner);
        startHoursSpinner = (Spinner) findViewById(R.id.startHoursSpinner);
        startMinutesSpinner = (Spinner) findViewById(R.id.startMinutesSpinner);
        endHoursSpinner = (Spinner) findViewById(R.id.endHoursSpinner);
        endMinutesSpinner = (Spinner) findViewById(R.id.endMinutesSpinner);

        adapterType = ArrayAdapter.createFromResource(this, R.array.places_names, android.R.layout.simple_spinner_item);
        adapterWaitingtime = ArrayAdapter.createFromResource(this, R.array.waitingTimeArray, android.R.layout.simple_spinner_item);
        adapterFloor = ArrayAdapter.createFromResource(this, R.array.floorCount, android.R.layout.simple_spinner_item);
        adapterStartHours = ArrayAdapter.createFromResource(this, R.array.hoursOption, android.R.layout.simple_spinner_item);
        adapterStartMinutes = ArrayAdapter.createFromResource(this, R.array.minutesOption, android.R.layout.simple_spinner_item);
        adapterEndHours = ArrayAdapter.createFromResource(this, R.array.hoursOption, android.R.layout.simple_spinner_item);
        adapterEndMinutes = ArrayAdapter.createFromResource(this, R.array.minutesOption, android.R.layout.simple_spinner_item);


        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWaitingtime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFloor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterStartHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterStartMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEndHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEndMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        typeSpinner.setAdapter(adapterType);
        waitingTimeSpinner.setAdapter(adapterWaitingtime);
        floorSpinner.setAdapter(adapterFloor);

        startHoursSpinner.setAdapter(adapterStartHours);
        startMinutesSpinner.setAdapter(adapterStartMinutes);
        endHoursSpinner.setAdapter(adapterEndHours);
        endMinutesSpinner.setAdapter(adapterEndMinutes);


        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                  @Override
                                                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                      ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                      ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {
                                                  }
                                              }
        );

        waitingTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                         @Override
                                                         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                             ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                             ((TextView) parent.getChildAt(0)).setTextSize(16);

                                                             ((TextView) parent.getChildAt(0)).setText("待ち時間  " + parent.getItemAtPosition(position) + "分");


                                                         }

                                                         @Override
                                                         public void onNothingSelected(AdapterView<?> parent) {
                                                         }
                                                     }

        );

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                   @Override
                                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                       ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                       ((TextView) parent.getChildAt(0)).setTextSize(16);
//                                                            ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "以上を検索");
                                                       if (!spinnerLoaded) {



                                                           // ((TextView) parent.getChildAt(0)).setText(parent.getSelectedItem()));
                                                           ((TextView) parent.getChildAt(0)).setText(String.valueOf(parent.getItemAtPosition(3)));

                                                           toiletFloor = floorSpinner.getSelectedItemPosition();
                                                           //Set localize string...

                                                           spinnerLoaded = true;
                                                       }


                                                   }

                                                   @Override
                                                   public void onNothingSelected(AdapterView<?> parent) {
                                                   }
                                               }

        );
        startHoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                            ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {
                                                        }
                                                    }
        );

        startMinutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                          @Override
                                                          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                              ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                              ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                          }

                                                          @Override
                                                          public void onNothingSelected(AdapterView<?> parent) {
                                                          }
                                                      }

        );

        endHoursSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                          ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                      }
                                                  }

        );

        endMinutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                            ((TextView) parent.getChildAt(0)).setTextSize(16);
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {
                                                        }
                                                    }
        );
    }


    private void textReady() {
        textToiletName = (EditText) findViewById(R.id.writeToiletName);
        textHowToAccess = (EditText) findViewById(R.id.inputHowToAccess);
        textFeedback = (EditText) findViewById(R.id.kansou);
//        textToiletName.setInputType(0);
        textToiletName.setTextColor(Color.BLACK);
        textHowToAccess.setTextColor(Color.BLACK);
        textFeedback.setTextColor(Color.BLACK);

        textHowToAccess.setHorizontallyScrolling(false);
        textFeedback.setHorizontallyScrolling(false);

        textHowToAccess.setMaxLines(Integer.MAX_VALUE);
        textFeedback.setMaxLines(Integer.MAX_VALUE);


    }

    private void othersReady() {

        ratingBar = (RatingBar) findViewById(R.id.addRating);
        ratingBar.setRating(3);
        addPhoto = (Button) findViewById(R.id.buttonAddPicture);
        buttonAddInfo = (Button) findViewById(R.id.buttonAddInfo);

        mainImage = (ImageView) findViewById(R.id.picture1);
//         ImageView subImage1 = (ImageView) findViewById(R.id.picture2);
//         ImageView subImage2 = (ImageView) findViewById(R.id.picture3);


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermissionAndAddPhoto();

            }
        });


        buttonAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                 pictureUpload(); March 3 18pm

                valueCheck();
//                 firebaseUpdate();

            }
        });

    }


    public void checkPermissionAndAddPhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //request permission...
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            } else {
                //Have a permission
                imageSetPlaceChoose();
            }
        } else {
            //Build.VERSION.SDK_INT < Build.VERSION_CODES.M(23)

            imageSetPlaceChoose();

        }

    }


    private void imageSetPlaceChoose() {
       // final Integer imageNum = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("どこに写真を追加しますか");
        builder.setItems(new CharSequence[]
                        {"メインイメージ", "サブイメージ1", "サブイメージ2"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                photoSelected = 0;
                                showPhoto();

//                                Toast.makeText(this, "clicked 1", 0).show();
                                break;

                            case 1:
                                photoSelected = 1;
                                showPhoto();
//                                Toast.makeText(context, "clicked 2", 0).show();
                                break;
                            case 2:
                                photoSelected = 2;
                                showPhoto();
                                //Toast.makeText(context, "clicked 3", 0).show();
                                break;

                        }
                    }
                });
        builder.create().show();


    }

    private void showPhoto() {

//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
//        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                mainImage = (ImageView) findViewById(R.id.picture1);
                subImage1 = (ImageView) findViewById(R.id.picture2);
                subImage2 = (ImageView) findViewById(R.id.picture3);

                //I wrote this one twice
                ImageView targetView = mainImage;


                if (photoSelected == 0) {
                    targetView = mainImage;
                    uploadImageToDatabase(0, selectedImage);

                } else if (photoSelected == 1) {
                    targetView = subImage1;
                    uploadImageToDatabase(1, selectedImage);
                    //subOnefilePath = selectedImage;

                } else if (photoSelected == 2) {
                    targetView = subImage2;
                    uploadImageToDatabase(3, selectedImage);
                    //subTwofilePath = selectedImage;

                }

                targetView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void valueCheck() {
        String tName = textToiletName.getText().toString();

        if (TextUtils.isEmpty(tName)) {

            textToiletName.setError("Your message");
            Log.i("HEy", "00");
        } else {
            //there is a valid name
             firebaseUpdate();
        }
    }


//    private void pictureUpload() {
//
//
//        Log.i("pictureUpload()", "called");
//        // Get the data from an ImageView as bytes
//        mainImage.setDrawingCacheEnabled(true);
//        mainImage.buildDrawingCache();
//        Bitmap bitmap = mainImage.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = storageRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });
//    }


    @SuppressWarnings("unchecked")
    private void firebaseUpdate(){

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            String uid = user.getUid();

            Integer stHour = Integer.parseInt(String.valueOf(startHoursSpinner.getSelectedItem()));
            Integer stMinu = Integer.parseInt(String.valueOf(startMinutesSpinner.getSelectedItem()));
            Integer enHour = Integer.parseInt(String.valueOf(endHoursSpinner.getSelectedItem()));
            Integer enMinu = Integer.parseInt(String.valueOf(endMinutesSpinner.getSelectedItem()));
            Integer openTime = stHour * 100 + stMinu;
            Integer endTime = enHour * 100 + enMinu;

            Integer openData = 5000;
            Integer endData = 5000;
            String tName = textToiletName.getText().toString();

            if (openTime < endTime) {

                openData = openTime;
                endData = endTime;
                Log.i(String.valueOf(openData), String.valueOf(endData));

            } else if (openTime.equals(endTime)) {
                openData = 5000;
                endData = 5000;
                Log.i(String.valueOf(openData), String.valueOf(endData));
            }

            //This needs to be changed !!!!!!!!!!!!!!!!!!!!!!
//            else if (openTime > endTime) {
//                openData = openTime;
//                endData = endTime + 2400;
//                Log.i(String.valueOf(openData), String.valueOf(endData));
//
//            }

//       Log.i(String.valueOf(openData),String.valueOf(endData));


            double ratingValue = ratingBar.getRating();
            //float to double
           // Integer star1Value = ratingBar.getNumStars();

            String waitingV = waitingTimeSpinner.getSelectedItem().toString();
            //float to Int
            Integer waitingValue = Integer.parseInt(waitingV);

            String openingString = startHoursSpinner.getSelectedItem().toString() + ":" + startMinutesSpinner.getSelectedItem().toString() + "〜" + endHoursSpinner.getSelectedItem().toString() + ":" + endMinutesSpinner.getSelectedItem().toString();

            String avStar = String.valueOf(ratingValue);
            Log.i("datbase", "called121");


            //double ratingValue = ratingBar.getRating();
            //String avStar = String.valueOf(ratingValue);
            String dateString = year + "-" + month + "-" + day;

            String waitingTime = waitingTimeSpinner.getSelectedItem().toString();
            //float to Int
            //Integer waitingValue = Integer.parseInt(waitingV);


            Long timeLong = System.currentTimeMillis() / 1000;
            //Long timeLong = System.currentTimeMillis() / 1000l;
            double timeNumbers = timeLong.doubleValue();


//            DatabaseReference reviewInfoRef = fireDatabase.getReference("ReviewInfo");
//            DatabaseReference toiletReviewsRef = fireDatabase.getReference("ToiletReviews");
//            DatabaseReference reviewListRef = fireDatabase.getReference("ReviewList");

            ReviewPost newPost = new ReviewPost(
                    true,
                    textFeedback.getText().toString(),//String feedback,
                    0,//Integer likedCount,
                    avStar,//String star,
                    newTid,
                    dateString,//String time,
                    timeNumbers,
                    uid,
                    waitingTime);


//            reviewInfoRef.child(newRid).setValue(newPost);
//
//            reviewListRef.child(uid).child(newRid).setValue(true);
//
//            toiletReviewsRef.child(newTid).child(newRid).setValue(true);
//.


            Map<String, Object> noFilterData = new HashMap();

            noFilterData.put("name",tName);
            noFilterData.put("type",typeSpinner.getSelectedItemPosition());
            noFilterData.put("urlOne",urlOne);
            noFilterData.put("averageStar",avStar);
            noFilterData.put("reviewCount",1);
            noFilterData.put("available",true);
            noFilterData.put("averageWait",waitingValue);
            noFilterData.put("toiletFloor",toiletFloor);


            Map<String, Object> toiletUserList = new HashMap();
            toiletUserList.put("name",tName);
            toiletUserList.put("type",typeSpinner.getSelectedItemPosition());
            toiletUserList.put("urlOne",urlOne);
            toiletUserList.put("averageStar",avStar);
            toiletUserList.put("reviewCount",1);
            toiletUserList.put("available",true);
            toiletUserList.put("averageWait",waitingValue);
            toiletUserList.put("toiletFloor",toiletFloor);
            toiletUserList.put("latitude",AddLocations.latitude);
            toiletUserList.put("longitude",AddLocations.longitude);


            Map<String, Object> unitOneData = new HashMap();
            unitOneData.put("name",tName);
            unitOneData.put("type",typeSpinner.getSelectedItemPosition());
            unitOneData.put("urlOne",urlOne);
            unitOneData.put("averageStar",avStar);
            unitOneData.put("reviewCount",1);
            unitOneData.put("available",true);
            unitOneData.put("averageWait",waitingValue);
            unitOneData.put("toiletFloor",toiletFloor);
            unitOneData.put("openHours",openData);
            unitOneData.put("closeHours",endData);
            unitOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            unitOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            unitOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            unitOneData.put("unisex",AddDetailBooleans.unisex);



            Map<String, Object> unitTwoData = new HashMap();

            unitTwoData.put("name",tName);
            unitTwoData.put("type",typeSpinner.getSelectedItemPosition());
            unitTwoData.put("urlOne",urlOne);
            unitTwoData.put("averageStar",avStar);
            unitTwoData.put("reviewCount",1);
            unitTwoData.put("available",true);
            unitTwoData.put("averageWait",waitingValue);
            unitTwoData.put("toiletFloor",toiletFloor);
            unitTwoData.put("washlet",AddDetailBooleans.washlet);
            unitTwoData.put("warmSeat",AddDetailBooleans.warmSeat);
            unitTwoData.put("autoOpen",AddDetailBooleans.autoOpen);
            unitTwoData.put("noVirus",AddDetailBooleans.noVirus);
            unitTwoData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            unitTwoData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            unitTwoData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);




            Map<String, Object> unitThreeData = new HashMap();
            unitThreeData.put("name",tName);
            unitThreeData.put("type",typeSpinner.getSelectedItemPosition());
            unitThreeData.put("urlOne",urlOne);
            unitThreeData.put("averageStar",avStar);
            unitThreeData.put("reviewCount",1);
            unitThreeData.put("available",true);
            unitThreeData.put("averageWait",waitingValue);
            unitThreeData.put("toiletFloor",toiletFloor);
            unitThreeData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            unitThreeData.put("handSoap", AddDetailBooleans.handSoap);
            unitThreeData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            unitThreeData.put("paperTowel",AddDetailBooleans.paperTowel);
            unitThreeData.put("handDrier",AddDetailBooleans.handDrier);


            Map<String, Object> unitFourData = new HashMap();
            unitFourData.put("name",tName);
            unitFourData.put("type",typeSpinner.getSelectedItemPosition());
            unitFourData.put("urlOne",urlOne);
            unitFourData.put("averageStar",avStar);
            unitFourData.put("reviewCount",1);
            unitFourData.put("available",true);
            unitFourData.put("averageWait",waitingValue);
            unitFourData.put("toiletFloor",toiletFloor);
            unitFourData.put("fancy", AddDetailBooleans.fancy);
            unitFourData.put("smell",AddDetailBooleans.smell);
            unitFourData.put("confortable",AddDetailBooleans.conforatableWide);
            unitFourData.put("clothes",AddDetailBooleans.clothes);
            unitFourData.put("baggageSpace",AddDetailBooleans.baggageSpace);



            Map<String, Object> unitFiveData = new HashMap();
            unitFiveData.put("name",tName);
            unitFiveData.put("type",typeSpinner.getSelectedItemPosition());
            unitFiveData.put("urlOne",urlOne);
            unitFiveData.put("averageStar",avStar);
            unitFiveData.put("reviewCount",1);
            unitFiveData.put("available",true);
            unitFiveData.put("averageWait",waitingValue);
            unitFiveData.put("toiletFloor",toiletFloor);
            unitFiveData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            unitFiveData.put("english",AddDetailBooleans.english);
            unitFiveData.put("parking",AddDetailBooleans.parking);
            unitFiveData.put("airCondition",AddDetailBooleans.airCondition);
            unitFiveData.put("wifi",AddDetailBooleans.wifi);



            Map<String, Object> unitSixData = new HashMap();
            unitSixData.put("name",tName);
            unitSixData.put("type",typeSpinner.getSelectedItemPosition());
            unitSixData.put("urlOne",urlOne);
            unitSixData.put("averageStar",avStar);
            unitSixData.put("reviewCount",1);
            unitSixData.put("available",true);
            unitSixData.put("averageWait",waitingValue);
            unitSixData.put("toiletFloor",toiletFloor);
            unitSixData.put("otohime",AddDetailBooleans.otohime);
            unitSixData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            unitSixData.put("makeuproom",AddDetailBooleans.makeuproom);
            unitSixData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            unitSixData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            unitSixData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            unitSixData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            Map<String, Object> unitSevenData = new HashMap();
            unitSevenData.put("name",tName);
            unitSevenData.put("type",typeSpinner.getSelectedItemPosition());
            unitSevenData.put("urlOne",urlOne);
            unitSevenData.put("averageStar",avStar);
            unitSevenData.put("reviewCount",1);
            unitSevenData.put("available",true);
            unitSevenData.put("averageWait",waitingValue);
            unitSevenData.put("toiletFloor",toiletFloor);
            unitSevenData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            unitSevenData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            unitSevenData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            unitSevenData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);



            Map<String, Object> unitEightData = new HashMap();
            unitEightData.put("name",tName);
            unitEightData.put("type",typeSpinner.getSelectedItemPosition());
            unitEightData.put("urlOne",urlOne);
            unitEightData.put("averageStar",avStar);
            unitEightData.put("reviewCount",1);
            unitEightData.put("available",true);
            unitEightData.put("averageWait",waitingValue);
            unitEightData.put("toiletFloor",toiletFloor);
            unitEightData.put("wheelchair",AddDetailBooleans.wheelchair);
            unitEightData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            unitEightData.put("autoDoor",AddDetailBooleans.autoDoor);
            unitEightData.put("callHelp",AddDetailBooleans.callHelp);
            unitEightData.put("ostomate",AddDetailBooleans.ostomate);
            unitEightData.put("braille",AddDetailBooleans.braille);
            unitEightData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            unitEightData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            unitEightData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);



            Map<String, Object> unitNineData = new HashMap();
            unitNineData.put("name",tName);
            unitNineData.put("type",typeSpinner.getSelectedItemPosition());
            unitNineData.put("urlOne",urlOne);
            unitNineData.put("averageStar",avStar);
            unitNineData.put("reviewCount",1);
            unitNineData.put("available",true);
            unitNineData.put("averageWait",waitingValue);
            unitNineData.put("toiletFloor",toiletFloor);
            unitNineData.put("milkspace",AddDetailBooleans.milkspace);
            unitNineData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            unitNineData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            unitNineData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            unitNineData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            unitNineData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);



            Map<String, Object> unitTenData = new HashMap();
            unitTenData.put("name",tName);
            unitTenData.put("type",typeSpinner.getSelectedItemPosition());
            unitTenData.put("urlOne",urlOne);
            unitTenData.put("averageStar",avStar);
            unitTenData.put("reviewCount",1);
            unitTenData.put("available",true);
            unitTenData.put("averageWait",waitingValue);
            unitTenData.put("toiletFloor",toiletFloor);
            unitTenData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            unitTenData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            unitTenData.put("omutu",AddDetailBooleans.omutu);
            unitTenData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            unitTenData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            unitTenData.put("omutuSelling",AddDetailBooleans.omutuSelling);

            Map<String, Object> unitElevenData = new HashMap();
            unitElevenData.put("name",tName);
            unitElevenData.put("type",typeSpinner.getSelectedItemPosition());
            unitElevenData.put("urlOne",urlOne);
            unitElevenData.put("averageStar",avStar);
            unitElevenData.put("reviewCount",1);
            unitElevenData.put("available",true);
            unitElevenData.put("averageWait",waitingValue);
            unitElevenData.put("toiletFloor",toiletFloor);
            unitElevenData.put("babySink",AddDetailBooleans.babyRoomSink);
            unitElevenData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            unitElevenData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            unitElevenData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            unitElevenData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            unitElevenData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            unitElevenData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);



            Map<String, Object> unitTwelveData = new HashMap();
            unitTwelveData.put("name",tName);
            unitTwelveData.put("type",typeSpinner.getSelectedItemPosition());
            unitTwelveData.put("urlOne",urlOne);
            unitTwelveData.put("averageStar",avStar);
            unitTwelveData.put("reviewCount",1);
            unitTwelveData.put("available",true);
            unitTwelveData.put("averageWait",waitingValue);
            unitTwelveData.put("toiletFloor",toiletFloor);
            unitTwelveData.put("babyChair",AddDetailBooleans.babyChair);
            unitTwelveData.put("babySoffa",AddDetailBooleans.babySoffa);
            unitTwelveData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            unitTwelveData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            unitTwelveData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            unitTwelveData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            unitTwelveData.put("babyToy",AddDetailBooleans.babyToy);
            unitTwelveData.put("babyFancy",AddDetailBooleans.babyFancy);
            unitTwelveData.put("babySmellGood",AddDetailBooleans.babySmellGood);


            Map<String, Object> groupOneData = new HashMap();
            groupOneData.put("name",tName);
            groupOneData.put("type",typeSpinner.getSelectedItemPosition());
            groupOneData.put("urlOne",urlOne);
            groupOneData.put("averageStar",avStar);
            groupOneData.put("reviewCount",1);
            groupOneData.put("available",true);
            groupOneData.put("averageWait",waitingValue);
            groupOneData.put("toiletFloor",toiletFloor);
            groupOneData.put("openHours",openData);
            groupOneData.put("closeHours",endData);


            groupOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            groupOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            groupOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            groupOneData.put("unisex",AddDetailBooleans.unisex);

            groupOneData.put("washlet",AddDetailBooleans.washlet);
            groupOneData.put("warmSeat",AddDetailBooleans.warmSeat);
            groupOneData.put("autoOpen",AddDetailBooleans.autoOpen);
            groupOneData.put("noVirus",AddDetailBooleans.noVirus);
            groupOneData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            groupOneData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            groupOneData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);



            groupOneData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            groupOneData.put("handSoap", AddDetailBooleans.handSoap);
            groupOneData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            groupOneData.put("paperTowel",AddDetailBooleans.paperTowel);
            groupOneData.put("handDrier",AddDetailBooleans.handDrier);



            groupOneData.put("fancy", AddDetailBooleans.fancy);
            groupOneData.put("smell",AddDetailBooleans.smell);
            groupOneData.put("confortable",AddDetailBooleans.conforatableWide);
            groupOneData.put("clothes",AddDetailBooleans.clothes);
            groupOneData.put("baggageSpace",AddDetailBooleans.baggageSpace);


            groupOneData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            groupOneData.put("english",AddDetailBooleans.english);
            groupOneData.put("parking",AddDetailBooleans.parking);
            groupOneData.put("airCondition",AddDetailBooleans.airCondition);
            groupOneData.put("wifi",AddDetailBooleans.wifi);



            Map<String, Object> groupTwoData = new HashMap();
            groupTwoData.put("name",tName);
            groupTwoData.put("type",typeSpinner.getSelectedItemPosition());
            groupTwoData.put("urlOne",urlOne);
            groupTwoData.put("averageStar",avStar);
            groupTwoData.put("reviewCount",1);
            groupTwoData.put("available",true);
            groupTwoData.put("averageWait",waitingValue);
            groupTwoData.put("toiletFloor",toiletFloor);

            groupTwoData.put("otohime",AddDetailBooleans.otohime);
            groupTwoData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            groupTwoData.put("makeuproom",AddDetailBooleans.makeuproom);
            groupTwoData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            groupTwoData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            groupTwoData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            groupTwoData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            groupTwoData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            groupTwoData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            groupTwoData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            groupTwoData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);


            groupTwoData.put("wheelchair",AddDetailBooleans.wheelchair);
            groupTwoData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            groupTwoData.put("autoDoor",AddDetailBooleans.autoDoor);
            groupTwoData.put("callHelp",AddDetailBooleans.callHelp);
            groupTwoData.put("ostomate",AddDetailBooleans.ostomate);
            groupTwoData.put("braille",AddDetailBooleans.braille);
            groupTwoData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            groupTwoData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            groupTwoData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);


            Map<String, Object> groupThreeData = new HashMap();
            groupThreeData.put("name",tName);
            groupThreeData.put("type",typeSpinner.getSelectedItemPosition());
            groupThreeData.put("urlOne",urlOne);
            groupThreeData.put("averageStar",avStar);
            groupThreeData.put("reviewCount",1);
            groupThreeData.put("available",true);
            groupThreeData.put("averageWait",waitingValue);
            groupThreeData.put("toiletFloor",toiletFloor);
            groupThreeData.put("milkspace",AddDetailBooleans.milkspace);
            groupThreeData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            groupThreeData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            groupThreeData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            groupThreeData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            groupThreeData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);


            groupThreeData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            groupThreeData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            groupThreeData.put("omutu",AddDetailBooleans.omutu);
            groupThreeData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            groupThreeData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            groupThreeData.put("omutuSelling",AddDetailBooleans.omutuSelling);


            groupThreeData.put("babySink",AddDetailBooleans.babyRoomSink);
            groupThreeData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            groupThreeData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            groupThreeData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            groupThreeData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            groupThreeData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            groupThreeData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);



            groupThreeData.put("babyChair",AddDetailBooleans.babyChair);
            groupThreeData.put("babySoffa",AddDetailBooleans.babySoffa);
            groupThreeData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            groupThreeData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            groupThreeData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            groupThreeData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            groupThreeData.put("babyToy",AddDetailBooleans.babyToy);
            groupThreeData.put("babyFancy",AddDetailBooleans.babyFancy);
            groupThreeData.put("babySmellGood",AddDetailBooleans.babySmellGood);


            Map<String, Object> halfOneData = new HashMap();
            halfOneData.put("name",tName);
            halfOneData.put("type",typeSpinner.getSelectedItemPosition());
            halfOneData.put("urlOne",urlOne);
            halfOneData.put("averageStar",avStar);
            halfOneData.put("reviewCount",1);
            halfOneData.put("available",true);
            halfOneData.put("averageWait",waitingValue);
            halfOneData.put("toiletFloor",toiletFloor);
            halfOneData.put("openHours",openData);
            halfOneData.put("closeHours",endData);
            halfOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            halfOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            halfOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            halfOneData.put("unisex",AddDetailBooleans.unisex);

            halfOneData.put("washlet",AddDetailBooleans.washlet);
            halfOneData.put("warmSeat",AddDetailBooleans.warmSeat);
            halfOneData.put("autoOpen",AddDetailBooleans.autoOpen);
            halfOneData.put("noVirus",AddDetailBooleans.noVirus);
            halfOneData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            halfOneData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            halfOneData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);



            halfOneData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            halfOneData.put("handSoap", AddDetailBooleans.handSoap);
            halfOneData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            halfOneData.put("paperTowel",AddDetailBooleans.paperTowel);
            halfOneData.put("handDrier",AddDetailBooleans.handDrier);



            halfOneData.put("fancy", AddDetailBooleans.fancy);
            halfOneData.put("smell",AddDetailBooleans.smell);
            halfOneData.put("confortable",AddDetailBooleans.conforatableWide);
            halfOneData.put("clothes",AddDetailBooleans.clothes);
            halfOneData.put("baggageSpace",AddDetailBooleans.baggageSpace);


            halfOneData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            halfOneData.put("english",AddDetailBooleans.english);
            halfOneData.put("parking",AddDetailBooleans.parking);
            halfOneData.put("airCondition",AddDetailBooleans.airCondition);
            halfOneData.put("wifi",AddDetailBooleans.wifi);


            //for males


            halfOneData.put("otohime",AddDetailBooleans.otohime);
            halfOneData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            halfOneData.put("makeuproom",AddDetailBooleans.makeuproom);
            halfOneData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            halfOneData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            halfOneData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            halfOneData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            halfOneData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            halfOneData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            halfOneData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            halfOneData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);


            halfOneData.put("wheelchair",AddDetailBooleans.wheelchair);
            halfOneData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            halfOneData.put("autoDoor",AddDetailBooleans.autoDoor);
            halfOneData.put("callHelp",AddDetailBooleans.callHelp);
            halfOneData.put("ostomate",AddDetailBooleans.ostomate);
            halfOneData.put("braille",AddDetailBooleans.braille);
            halfOneData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            halfOneData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            halfOneData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);



            Map<String, Object> halfTwoData = new HashMap();
            halfTwoData.put("name",tName);
            halfTwoData.put("type",typeSpinner.getSelectedItemPosition());
            halfTwoData.put("urlOne",urlOne);
            halfTwoData.put("averageStar",avStar);
            halfTwoData.put("reviewCount",1);
            halfTwoData.put("available",true);
            halfTwoData.put("averageWait",waitingValue);
            halfTwoData.put("toiletFloor",toiletFloor);

            halfTwoData.put("otohime",AddDetailBooleans.otohime);
            halfTwoData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            halfTwoData.put("makeuproom",AddDetailBooleans.makeuproom);
            halfTwoData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            halfTwoData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            halfTwoData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            halfTwoData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            halfTwoData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            halfTwoData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            halfTwoData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            halfTwoData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);


            halfTwoData.put("wheelchair",AddDetailBooleans.wheelchair);
            halfTwoData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            halfTwoData.put("autoDoor",AddDetailBooleans.autoDoor);
            halfTwoData.put("callHelp",AddDetailBooleans.callHelp);
            halfTwoData.put("ostomate",AddDetailBooleans.ostomate);
            halfTwoData.put("braille",AddDetailBooleans.braille);
            halfTwoData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            halfTwoData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            halfTwoData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);

            halfTwoData.put("milkspace",AddDetailBooleans.milkspace);
            halfTwoData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            halfTwoData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            halfTwoData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            halfTwoData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            halfTwoData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);


            halfTwoData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            halfTwoData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            halfTwoData.put("omutu",AddDetailBooleans.omutu);
            halfTwoData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            halfTwoData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            halfTwoData.put("omutuSelling",AddDetailBooleans.omutuSelling);


            halfTwoData.put("babySink",AddDetailBooleans.babyRoomSink);
            halfTwoData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            halfTwoData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            halfTwoData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            halfTwoData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            halfTwoData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            halfTwoData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);



            halfTwoData.put("babyChair",AddDetailBooleans.babyChair);
            halfTwoData.put("babySoffa",AddDetailBooleans.babySoffa);
            halfTwoData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            halfTwoData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            halfTwoData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            halfTwoData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            halfTwoData.put("babyToy",AddDetailBooleans.babyToy);
            halfTwoData.put("babyFancy",AddDetailBooleans.babyFancy);
            halfTwoData.put("babySmellGood",AddDetailBooleans.babySmellGood);

            Map<String, Object> allFilterData = new HashMap();
            allFilterData.put("name",tName);
            allFilterData.put("type",typeSpinner.getSelectedItemPosition());
            allFilterData.put("urlOne",urlOne);
            allFilterData.put("averageStar",avStar);
            allFilterData.put("reviewCount",1);
            allFilterData.put("available",true);
            allFilterData.put("averageWait",waitingValue);
            allFilterData.put("toiletFloor",toiletFloor);
            allFilterData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            allFilterData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            allFilterData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            allFilterData.put("unisex",AddDetailBooleans.unisex);

            allFilterData.put("washlet",AddDetailBooleans.washlet);
            allFilterData.put("warmSeat",AddDetailBooleans.warmSeat);
            allFilterData.put("autoOpen",AddDetailBooleans.autoOpen);
            allFilterData.put("noVirus",AddDetailBooleans.noVirus);
            allFilterData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            allFilterData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            allFilterData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);



            allFilterData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            allFilterData.put("handSoap", AddDetailBooleans.handSoap);
            allFilterData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            allFilterData.put("paperTowel",AddDetailBooleans.paperTowel);
            allFilterData.put("handDrier",AddDetailBooleans.handDrier);



            allFilterData.put("fancy", AddDetailBooleans.fancy);
            allFilterData.put("smell",AddDetailBooleans.smell);
            allFilterData.put("confortable",AddDetailBooleans.conforatableWide);
            allFilterData.put("clothes",AddDetailBooleans.clothes);
            allFilterData.put("baggageSpace",AddDetailBooleans.baggageSpace);


            allFilterData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            allFilterData.put("english",AddDetailBooleans.english);
            allFilterData.put("parking",AddDetailBooleans.parking);
            allFilterData.put("airCondition",AddDetailBooleans.airCondition);
            allFilterData.put("wifi",AddDetailBooleans.wifi);


            //for males


            allFilterData.put("otohime",AddDetailBooleans.otohime);
            allFilterData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            allFilterData.put("makeuproom",AddDetailBooleans.makeuproom);
            allFilterData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            allFilterData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            allFilterData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            allFilterData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            allFilterData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            allFilterData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            allFilterData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            allFilterData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);


            allFilterData.put("wheelchair",AddDetailBooleans.wheelchair);
            allFilterData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            allFilterData.put("autoDoor",AddDetailBooleans.autoDoor);
            allFilterData.put("callHelp",AddDetailBooleans.callHelp);
            allFilterData.put("ostomate",AddDetailBooleans.ostomate);
            allFilterData.put("braille",AddDetailBooleans.braille);
            allFilterData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            allFilterData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            allFilterData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);

            allFilterData.put("milkspace",AddDetailBooleans.milkspace);
            allFilterData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            allFilterData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            allFilterData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            allFilterData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            allFilterData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);


            allFilterData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            allFilterData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            allFilterData.put("omutu",AddDetailBooleans.omutu);
            allFilterData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            allFilterData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            allFilterData.put("omutuSelling",AddDetailBooleans.omutuSelling);


            allFilterData.put("babySink",AddDetailBooleans.babyRoomSink);
            allFilterData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            allFilterData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            allFilterData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            allFilterData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            allFilterData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            allFilterData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);


            allFilterData.put("babyChair",AddDetailBooleans.babyChair);
            allFilterData.put("babySoffa",AddDetailBooleans.babySoffa);
            allFilterData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            allFilterData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            allFilterData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            allFilterData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            allFilterData.put("babyToy",AddDetailBooleans.babyToy);
            allFilterData.put("babyFancy",AddDetailBooleans.babyFancy);
            allFilterData.put("babySmellGood",AddDetailBooleans.babySmellGood);


            Map<String, Object> toiletViewData = new HashMap();
            toiletViewData.put("name",tName);
            toiletViewData.put("type",typeSpinner.getSelectedItemPosition());
            toiletViewData.put("urlOne",urlOne);
            toiletViewData.put("urlTwo",urlTwo);
            toiletViewData.put("urlThree",urlThree);
            toiletViewData.put("addedBy",uid);
            toiletViewData.put("editedBy",uid);
            toiletViewData.put("reviewOne",newRid);
            toiletViewData.put("reviewTwo","");
            toiletViewData.put("averageStar",avStar);
            toiletViewData.put("address",AddLocations.address);
            toiletViewData.put("howtoaccess","");
            toiletViewData.put("openAndCloseHours",openingString);
            toiletViewData.put("openHours",openData);
            toiletViewData.put("closeHours",endData);
            toiletViewData.put("reviewCount",1);
            toiletViewData.put("averageWait",waitingValue);
            toiletViewData.put("toiletFloor",toiletFloor);





            toiletViewData.put("latitude",AddLocations.latitude);
            toiletViewData.put("longitude",AddLocations.longitude);


            toiletViewData.put("available",true);
            toiletViewData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            toiletViewData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            toiletViewData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            toiletViewData.put("unisex",AddDetailBooleans.unisex);

            toiletViewData.put("washlet",AddDetailBooleans.washlet);
            toiletViewData.put("warmSeat",AddDetailBooleans.warmSeat);
            toiletViewData.put("autoOpen",AddDetailBooleans.autoOpen);
            toiletViewData.put("noVirus",AddDetailBooleans.noVirus);
            toiletViewData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            toiletViewData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            toiletViewData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);



            toiletViewData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            toiletViewData.put("handSoap", AddDetailBooleans.handSoap);
            toiletViewData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            toiletViewData.put("paperTowel",AddDetailBooleans.paperTowel);
            toiletViewData.put("handDrier",AddDetailBooleans.handDrier);



            toiletViewData.put("fancy", AddDetailBooleans.fancy);
            toiletViewData.put("smell",AddDetailBooleans.smell);
            toiletViewData.put("confortable",AddDetailBooleans.conforatableWide);
            toiletViewData.put("clothes",AddDetailBooleans.clothes);
            toiletViewData.put("baggageSpace",AddDetailBooleans.baggageSpace);


            toiletViewData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            toiletViewData.put("english",AddDetailBooleans.english);
            toiletViewData.put("parking",AddDetailBooleans.parking);
            toiletViewData.put("airCondition",AddDetailBooleans.airCondition);
            toiletViewData.put("wifi",AddDetailBooleans.wifi);


            //for males


            toiletViewData.put("otohime",AddDetailBooleans.otohime);
            toiletViewData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            toiletViewData.put("makeuproom",AddDetailBooleans.makeuproom);
            toiletViewData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            toiletViewData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            toiletViewData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            toiletViewData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);


            toiletViewData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            toiletViewData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            toiletViewData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            toiletViewData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);


            toiletViewData.put("wheelchair",AddDetailBooleans.wheelchair);
            toiletViewData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            toiletViewData.put("autoDoor",AddDetailBooleans.autoDoor);
            toiletViewData.put("callHelp",AddDetailBooleans.callHelp);
            toiletViewData.put("ostomate",AddDetailBooleans.ostomate);
            toiletViewData.put("braille",AddDetailBooleans.braille);
            toiletViewData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            toiletViewData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            toiletViewData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);

            toiletViewData.put("milkspace",AddDetailBooleans.milkspace);
            toiletViewData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            toiletViewData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            toiletViewData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            toiletViewData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            toiletViewData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);


            toiletViewData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            toiletViewData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            toiletViewData.put("omutu",AddDetailBooleans.omutu);
            toiletViewData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            toiletViewData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            toiletViewData.put("omutuSelling",AddDetailBooleans.omutuSelling);


            toiletViewData.put("babySink",AddDetailBooleans.babyRoomSink);
            toiletViewData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            toiletViewData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            toiletViewData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            toiletViewData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            toiletViewData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            toiletViewData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);



            toiletViewData.put("babyChair",AddDetailBooleans.babyChair);
            toiletViewData.put("babySoffa",AddDetailBooleans.babySoffa);
            toiletViewData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            toiletViewData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            toiletViewData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            toiletViewData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            toiletViewData.put("babyToy",AddDetailBooleans.babyToy);
            toiletViewData.put("babyFancy",AddDetailBooleans.babyFancy);
            toiletViewData.put("babySmellGood",AddDetailBooleans.babySmellGood);





            Map<String, Object> updateData = new HashMap();


            updateData.put("ToiletView/" + newTid, toiletViewData);
            updateData.put("NoFilter/" + newTid, noFilterData);
            updateData.put("ToiletUserList/" + newTid, toiletUserList);
            updateData.put("UnitOne/" + newTid, unitOneData);
            updateData.put("UnitTwo/" + newTid, unitTwoData);
            updateData.put("UnitThree/" + newTid, unitThreeData);
            updateData.put("UnitFour/" + newTid, unitFourData);
            updateData.put("UnitFive/" + newTid, unitFiveData);
            updateData.put("UnitSix/" + newTid, unitSixData);
            updateData.put("UnitSeven/" + newTid, unitSevenData);
            updateData.put("UnitEight/" + newTid, unitEightData);
            updateData.put("UnitNine/" + newTid, unitNineData);
            updateData.put("UnitTen/" + newTid, unitTenData);
            updateData.put("UnitEleven/" + newTid, unitElevenData);
            updateData.put("UnitTwelve/" + newTid, unitTwelveData);
            updateData.put("GroupOne/" + newTid, groupOneData);
            updateData.put("GroupTwo/" + newTid, groupTwoData);
            updateData.put("GroupThree/" + newTid, groupThreeData);
            updateData.put("HalfOne/" + newTid, halfOneData);
            updateData.put("HalfTwo/" + newTid, halfTwoData);
            updateData.put("AllFilter/" + newTid, allFilterData);
            updateData.put("ReviewInfo/" + newRid, newPost);
            updateData.put("ToiletReview/" + newTid + "/" + newRid, true);
            updateData.put("ReviewList/" + uid + "/" + newRid, true);





            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();


            firebaseRef.updateChildren(updateData,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null){
                        Toast.makeText(AddToiletDetailListActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    } else {
                        //Success
                        geoFire.setLocation(newTid, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);

                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }

                            }
                        });


                    }
                }
            });



            //Copied from firebase blog

            Log.i("please", "...");
            //geolocationUpload();
            //reviewUpload();

            backToAccountActivity();
        }

    }
//    private void geolocationUpload() {
//
////        String newRef = ref.child("Toilets");
////
////        String newID = newRef
//
//        geoFire.setLocation(newTid, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error != null) {
//                    System.err.println("There was an error saving the location to GeoFire: " + error);
//
//                } else {
//                    System.out.println("Location saved on server successfully!");
////                    firebaseUpdate();
//                }
//
//            }
//        });
//    }

//    private void reviewUpload() {
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user != null) {
//
//            String uid = user.getUid();
//
//
//            //I need to do somthing.. inv
//
//            double ratingValue = ratingBar.getRating();
//            String avStar = String.valueOf(ratingValue);
//            String dateString = year + "-" + month + "-" + day;
//
//            String waitingTime = waitingTimeSpinner.getSelectedItem().toString();
//            //float to Int
//            //Integer waitingValue = Integer.parseInt(waitingV);
//
//
//            Long timeLong = System.currentTimeMillis() / 1000;
//            //Long timeLong = System.currentTimeMillis() / 1000l;
//            double timeNumbers = timeLong.doubleValue();
//
//
//            DatabaseReference reviewInfoRef = fireDatabase.getReference("ReviewInfo");
//            DatabaseReference toiletReviewsRef = fireDatabase.getReference("ToiletReviews");
//            DatabaseReference reviewListRef = fireDatabase.getReference("ReviewList");
//
//            ReviewPost newPost = new ReviewPost(
//                    true,
//                    textFeedback.getText().toString(),//String feedback,
//                    0,//Integer likedCount,
//                    avStar,//String star,
//                    newTid,
//                    dateString,//String time,
//                    timeNumbers,
//                    uid,
//                    waitingTime);
//
//
//            reviewInfoRef.child(newRid).setValue(newPost);
//
//            reviewListRef.child(uid).child(newRid).setValue(true);
//
//            toiletReviewsRef.child(newTid).child(newRid).setValue(true);
//
//        }
//
//    }

    //added "VisibleForTests" May 15


    @SuppressWarnings("VisibleForTests")
    private void uploadImageToDatabase(final int placeNumber, Uri file) {


        String photoId = UUID.randomUUID().toString();


// Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        UploadTask uploadTask = storageRef.child(photoId).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete

                if (taskSnapshot.getMetadata() != null) {

                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                    if (downloadUrl != null) {

                        if (placeNumber == 0) {
                            Log.i("urlOne found", downloadUrl.toString());
                            urlOne = downloadUrl.toString();
                        }
                        if (placeNumber == 1) {
                            Log.i("urlTwo found", downloadUrl.toString());
                            urlTwo = downloadUrl.toString();
                        }
                        if (placeNumber == 2) {
                            Log.i("urlThree found", downloadUrl.toString());
                            urlThree = downloadUrl.toString();
                        }

                        //changed urlOne to this downloadUrl...
                    }

                }

            }
        });
    }


    public void onMapReadyCalled(GoogleMap googleMap) {
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

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {
                //When the permission is granted....
                Log.i("HeyHey333", "locationManager.requestLocationUpdates");


//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);
                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");


                if (lastKnownLocation != null) {
                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");


                    mMap.clear();

                    LatLng userLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());


                    mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location222"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0f));


                } else {
                    Log.i("No lastLocation","");
                    //When you could not get the last known location...

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "Permission111");
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Permission222");
                    mMap.setMyLocationEnabled(true);

                    //mapUserCenterZoon();

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("Permission", "Permission333");
                }

            }
        }
        if (requestCode == 2) {
            //Photo Permission

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageSetPlaceChoose();

            }
        }

    }

}
