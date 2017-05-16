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


//        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

            DatabaseReference newRef = toiletRef.child(newTid);

            //String firekey = newRef.getKey();


            newRef.setValue(new Post(
                    tName,
                    openingString,
                    typeSpinner.getSelectedItemPosition(),
                    urlOne,//String urlOne,
                    urlTwo,//String urlTwo,
                    urlThree,//String urlThree,
                    uid,//String addedBy,
                    uid,//String editedBy,
                    newRid,
                    "",
                    avStar,
                    AddLocations.address,
                    "",//String howtoaccess,
                    openData,
                    endData,
                    1,//Integer reviewCount,
                    waitingValue,//Integer averageWait,
                    toiletFloor,//Integer toiletFloor,
                    AddLocations.latitude,
                    AddLocations.longitude,
                    true,
                    AddDetailBooleans.japanesetoilet,
                    AddDetailBooleans.westerntoilet,
                    AddDetailBooleans.onlyFemale,
                    AddDetailBooleans.unisex,


                    AddDetailBooleans.washlet,
                    AddDetailBooleans.warmSeat,
                    AddDetailBooleans.autoOpen,
                    AddDetailBooleans.noVirus,
                    AddDetailBooleans.paperForBenki,
                    AddDetailBooleans.cleanerForBenki,
                    AddDetailBooleans.autoToiletWash,


                    AddDetailBooleans.sensorHandWash,
                    AddDetailBooleans.handSoap,
                    AddDetailBooleans.autoHandSoap,
                    AddDetailBooleans.paperTowel,
                    AddDetailBooleans.handDrier,


                    AddDetailBooleans.fancy,
                    AddDetailBooleans.smell,
                    AddDetailBooleans.conforatableWide,
                    AddDetailBooleans.clothes,
                    AddDetailBooleans.baggageSpace,


                    AddDetailBooleans.noNeedAsk,
                    AddDetailBooleans.english,
                    AddDetailBooleans.parking,
                    AddDetailBooleans.airCondition,
                    AddDetailBooleans.wifi,

//

                    AddDetailBooleans.otohime,
                    AddDetailBooleans.napkinSelling,
                    AddDetailBooleans.makeuproom,
                    AddDetailBooleans.ladyOmutu,
                    AddDetailBooleans.ladyBabyChair,
                    AddDetailBooleans.ladyBabyChairGood,
                    AddDetailBooleans.ladyBabyCarAccess,


                    AddDetailBooleans.maleOmutu,
                    AddDetailBooleans.maleBabyChair,
                    AddDetailBooleans.maleBabyChairGood,
                    AddDetailBooleans.babyCarAccess,


                    AddDetailBooleans.wheelchair,
                    AddDetailBooleans.wheelchairAccess,
                    AddDetailBooleans.autoDoor,
                    AddDetailBooleans.callHelp,
                    AddDetailBooleans.ostomate,
                    AddDetailBooleans.braille,
                    AddDetailBooleans.voiceGuide,
                    AddDetailBooleans.familyOmutu,
                    AddDetailBooleans.familyBabyChair,


                    AddDetailBooleans.milkspace,
                    AddDetailBooleans.babyroomOnlyFemale,
                    AddDetailBooleans.babyroomManCanEnter,
                    AddDetailBooleans.babyPersonalSpace,
                    AddDetailBooleans.babyPersonalSpaceWithLock,
                    AddDetailBooleans.babyRoomWideSpace,


                    AddDetailBooleans.babyCarRental,
                    AddDetailBooleans.babyCarAccess,
                    AddDetailBooleans.omutu,
                    AddDetailBooleans.hipWashingStuff,
                    AddDetailBooleans.babyTrashCan,
                    AddDetailBooleans.omutuSelling,


                    AddDetailBooleans.babyRoomSink,
                    AddDetailBooleans.babyWashStand,
                    AddDetailBooleans.babyHotWater,
                    AddDetailBooleans.babyMicroWave,
                    AddDetailBooleans.babyWaterSelling,
                    AddDetailBooleans.babyFoddSelling,
                    AddDetailBooleans.babyEatingSpace,


                    AddDetailBooleans.babyChair,
                    AddDetailBooleans.babySoffa,
                    AddDetailBooleans.babyKidsToilet,
                    AddDetailBooleans.babyKidsSpace,
                    AddDetailBooleans.babyHeightMeasure,
                    AddDetailBooleans.babyWeightMeasure,
                    AddDetailBooleans.babyToy,
                    AddDetailBooleans.babyFancy,
                    AddDetailBooleans.babySmellGood


            ));


            Log.i("please", "...");
            geolocationUpload();
            reviewUpload();

            backToAccountActivity();
        }

    }
    private void geolocationUpload() {

//        String newRef = ref.child("Toilets");
//
//        String newID = newRef

        geoFire.setLocation(newTid, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);

                } else {
                    System.out.println("Location saved on server successfully!");
//                    firebaseUpdate();
                }

            }
        });
    }

    private void reviewUpload() {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            String uid = user.getUid();


            //I need to do somthing.. inv

            double ratingValue = ratingBar.getRating();
            String avStar = String.valueOf(ratingValue);
            String dateString = year + "-" + month + "-" + day;

            String waitingTime = waitingTimeSpinner.getSelectedItem().toString();
            //float to Int
            //Integer waitingValue = Integer.parseInt(waitingV);


            Long timeLong = System.currentTimeMillis() / 1000;
            //Long timeLong = System.currentTimeMillis() / 1000l;
            double timeNumbers = timeLong.doubleValue();


            DatabaseReference reviewInfoRef = fireDatabase.getReference("ReviewInfo");
            DatabaseReference toiletReviewsRef = fireDatabase.getReference("ToiletReviews");
            DatabaseReference reviewListRef = fireDatabase.getReference("ReviewList");

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


            reviewInfoRef.child(newRid).setValue(newPost);

            reviewListRef.child(uid).child(newRid).setValue(true);

            toiletReviewsRef.child(newTid).child(newRid).setValue(true);

        }

    }

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
