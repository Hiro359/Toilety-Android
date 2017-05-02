package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class EditViewActivity extends AppCompatActivity {


    Switch toiletJapanese;
    Switch toiletWestern;
    Switch toiletOnlyFemale;
    Switch toiletUnisex;

    Switch toiletWashlet;
    Switch toiletWarmseat;
    Switch toiletAutoopen;
    Switch toiletNoVirusBenki;
    Switch toiletPaperForBenki;
    Switch toiletCleanerForBenki;
    Switch toiletNonTouchWash;

    Switch toiletSensor;
    Switch toiletHandSoap;
    Switch toiletNonHandSoap;
    Switch toiletPaperTowel;
    Switch toiletHandDrier;


    //From Add Toilet Detail Acitiviy
    //others one
    Switch toiletClothes;
    Switch toiletBaggage;
    Switch toiletFancy;
    Switch toiletSmell;
    Switch toiletConfortable;
    //others two
    Switch toiletNoNeedAsk;
    Switch toiletParking;
    Switch toiletAirCondition;
    Switch toiletWifi;
    Switch toiletWrittenEnglish;

    //ladys
    Switch toiletOtohime;
    Switch toiletNapkinSelling;
    Switch toiletMakeroom;
    Switch ladysOmutuSwitch;
    Switch ladysBabyChairSwitch;
    Switch ladysBabyChairGoodSwitch;
    Switch ladysBabyCarAccessSwitch;

    //mens
    Switch maleOmutuSwitch;
    Switch maleBabyChairSwitch;
    Switch maleBabyChairGoodSwitch;
    Switch maleBabyCarAccessSwitch;

    //family room
    Switch toiletWheelchair;
    Switch toiletWheelchairAccess;
    Switch toiletHandrail;
    Switch toiletCallHelp;
    Switch toiletOstomate;
    Switch toiletBraille;
    Switch toiletVoiceGuide;
    Switch familyOmutu;
    Switch familyBabyChair;
    //From Add Toilet Detail Acitiviy



    Switch toiletMilk;
    Switch toiletBabyRoomOnlyFemale;
    Switch toiletBabyRoomMaleEnter;
    Switch toiletBabyPersonalRoom;
    Switch toiletBabyPersonalRoomWithLock;
    Switch toiletBabyRoomWide;

    Switch toiletBabyCarRental;
    Switch toiletBabyCarAccess;
    Switch toiletOmutu;
    Switch toiletHipCleaningStuff;
    Switch toiletOmutuTrashCan;
    Switch toiletOmutuSelling;

    Switch toiletBabySink;
    Switch toiletBabyWashstand;
    Switch toiletBabyHotWater;
    Switch toiletBabyMicrowave;
    Switch toiletBabyWaterSelling;
    Switch toiletBabyFoodSelling;
    Switch toiletBabyEatingSpace;

    Switch toiletBabyChair;
    Switch toiletBabySoffa;
    Switch toiletKidsToilet;
    Switch toiletKidsSpace;
    Switch toiletHeight;
    Switch toiletWeight;
    Switch toiletToy;
    Switch toiletBabyFancy;
    Switch toiletBabySmellGood;



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

    // EditText added is not a TextInputEditText. Please switch to using that class instead.

    //
    RatingBar ratingBar;
    ImageView mainImage;
    ImageView subImage1;
    ImageView subImage2;

    //LocationManager locationManager;

    //android.location.LocationListener locationListener;

    Button addPhoto;
    Button buttonRenewInfo;
    //Button buttonchangePinLocation;

   // Boolean onCreatedSpinner = false;


    private GoogleApiClient client;
    //private GoogleMap mMap;


   // Boolean spinnerLoaded = false;
    Boolean typeSpinnerLoaded = false;
    Boolean openHourSpinnerLoaded = false;
    Boolean openMinutesSpinnerLoaded = false;
    Boolean closeHourSpinnerLoaded = false;
    Boolean closeMinutesSpinnerLoaded = false;
    Boolean floorSpinnerLoaded = false;


    Integer photoSelected = 0;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private DatabaseReference toiletRef;
    private DatabaseReference toiletLocationRef;
//    DatabaseReference deleteLocationsRef
    Toilet toilet =  new Toilet();
    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");
    GeoFire geoFire = new GeoFire(locationRef);

    ArrayAdapter<CharSequence> adapterType;
    ArrayAdapter<CharSequence> adapterWaitingtime;
    ArrayAdapter<CharSequence> adapterFloor;
    ArrayAdapter<CharSequence> adapterStartHours;
    ArrayAdapter<CharSequence> adapterStartMinutes;
    ArrayAdapter<CharSequence> adapterEndHours;
    ArrayAdapter<CharSequence> adapterEndMinutes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        toolbar = (Toolbar) findViewById(R.id.edit_app_bar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.editAppBarTitle);

        switchesReady();
        othersReady();


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        final String originalkey = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.latitude = getIntent().getDoubleExtra("toiletLatitude",0);
        toilet.longitude = getIntent().getDoubleExtra("toiletLongitude",0);

//        final Double originalLon = getIntent().getStringExtra("toiletLongitude");
//        final Double originalLat = getIntent().getStringExtra("toiletLatitude");
//        final Double originalLon = getIntent().getStringExtra("toiletLongitude");




        toileGetData(originalkey);



        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", originalkey);
                        intent.putExtra("toiletLatitude",toilet.latitude);
                        intent.putExtra("toiletLongitude",toilet.longitude);


                        startActivity(intent);
                        finish();
                    }
                }
        );

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.editCheckMap);
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        mapFragment.getMapAsync(new OnMapReadyCallback(){
//            @Override public void onMapReady(GoogleMap googleMap) {
//                if (googleMap != null) {
//                    // your additional codes goes here
//
//                    onMapReadyCalled(googleMap, originalLat, originalLon);
//
//
//                }
//            }}
//        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.editviewbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.postEdit) {
            Toast.makeText(this, "Hey Post Exection!!", Toast.LENGTH_SHORT).show();
            Log.i("toilet.keyBeforePtEdit",toilet.key);

            firebaseRenewdata();
            //firebaseDeleteData();

            //firebaseEditAction();
            ///////////////////////// 1pm 25th Feb
            return true;

        }


        //edit exection.....
        //firebaseUpdate()
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

            return super.onOptionsItemSelected(item);
        }



    private void switchesReady(){

        toiletJapanese = (Switch) findViewById(R.id.editJapaneseSwitch);
        toiletWestern = (Switch) findViewById(R.id.editWesternSwitch);
        toiletOnlyFemale = (Switch) findViewById(R.id.editOnlyFemaleSwitch);
        toiletUnisex = (Switch) findViewById(R.id.editUnisexSwitch);

        toiletWashlet = (Switch) findViewById(R.id.editWashletSwitch);
        toiletWarmseat = (Switch) findViewById(R.id.editWarmSeatSwitch);
        toiletAutoopen = (Switch) findViewById(R.id.editAutoOpenSwitch);
        toiletNoVirusBenki = (Switch) findViewById(R.id.editNoVirusBenkiSwitch);
        toiletPaperForBenki = (Switch) findViewById(R.id.editPaperForBenkiSwitch);
        toiletCleanerForBenki = (Switch) findViewById(R.id.editCleanerForBenkiSwitch);
        toiletNonTouchWash = (Switch) findViewById(R.id.editNonTouchWashSwitch);

        toiletSensor = (Switch) findViewById(R.id.editSenserWashSwitch);
        toiletHandSoap = (Switch) findViewById(R.id.editSoapSwitch);
        toiletNonHandSoap = (Switch) findViewById(R.id.editNonTouchSoapSwitch);
        toiletPaperTowel = (Switch) findViewById(R.id.editPaperTowelSwitch);
        toiletHandDrier = (Switch) findViewById(R.id.editHandDrierSwitch);

        toiletOtohime = (Switch) findViewById(R.id.editOtohimeSwitch);
        toiletNapkinSelling = (Switch) findViewById(R.id.editNapkinSellingSwitch);
        toiletMakeroom = (Switch) findViewById(R.id.editMakeSwitch);
        toiletClothes = (Switch) findViewById(R.id.editClothesSwitch);
        toiletBaggage = (Switch) findViewById(R.id.editBaggageSwitch);

        toiletWheelchair = (Switch) findViewById(R.id.editWheelchairSwitch);
        toiletWheelchairAccess = (Switch) findViewById(R.id.editWheelchairAccessSwitch);
        toiletHandrail = (Switch) findViewById(R.id.editHandrailSwitch);
        toiletCallHelp = (Switch) findViewById(R.id.editCallHelpSwitch);
        toiletOstomate = (Switch) findViewById(R.id.editOstomateSwitch);
        toiletWrittenEnglish = (Switch) findViewById(R.id.editWrittenEnglishSwitch);
        toiletBraille = (Switch) findViewById(R.id.editBraille);
        toiletVoiceGuide = (Switch) findViewById(R.id.editVoiceGuideSwitch);

        toiletFancy = (Switch) findViewById(R.id.editFancySwitch);
        toiletSmell = (Switch) findViewById(R.id.editSmellSwitch);
        toiletConfortable = (Switch) findViewById(R.id.editConforableSwitch);
        toiletNoNeedAsk = (Switch) findViewById(R.id.editNoNeedAskSwitch);
        toiletParking = (Switch) findViewById(R.id.editParkingSwitch);
        toiletAirCondition =  (Switch) findViewById(R.id.editAirConditionSwitch);
        toiletWifi = (Switch) findViewById(R.id.editWifiSwitch);


        toiletMilk = (Switch) findViewById(R.id.editMilkSwitch);
        toiletBabyRoomOnlyFemale = (Switch)findViewById(R.id.editMilkOnlyFemaleSwitch);
        toiletBabyRoomMaleEnter = (Switch)findViewById(R.id.editMilkMaleOkaySwitch);
        toiletBabyPersonalRoom = (Switch)findViewById(R.id.editBabyPersonalSpaceSwitch);
        toiletBabyPersonalRoomWithLock = (Switch)findViewById(R.id.editBabyPersonalSpaceWithLockSwitch);
        toiletBabyRoomWide = (Switch)findViewById(R.id.editWideBabySpaceSwitch);


        toiletBabyCarRental = (Switch)findViewById(R.id.editRentalBabyCarSwitch);
        toiletBabyCarAccess = (Switch)findViewById(R.id.editBabyCarAccessSwitch);
        toiletOmutu = (Switch) findViewById(R.id.editOmutuSwitch);
        toiletHipCleaningStuff = (Switch)findViewById(R.id.editHipCleanStuffSwitch);
        toiletOmutuTrashCan = (Switch)findViewById(R.id.editOmutuTrashCanSwitch);
        toiletOmutuSelling = (Switch) findViewById(R.id.editOmutuSellingSwitch);


        toiletBabySink = (Switch)findViewById(R.id.editBabySinkSwitch);
        toiletBabyWashstand = (Switch)findViewById(R.id.editBabyWashstandSwitch);
        toiletBabyHotWater = (Switch)findViewById(R.id.editBabyHotWaterSwitch);
        toiletBabyMicrowave = (Switch)findViewById(R.id.editBabyMicrowaveSwitch);
        toiletBabyWaterSelling = (Switch)findViewById(R.id.editBabySellingWaterSwitch);
        toiletBabyFoodSelling = (Switch)findViewById(R.id.editfoodForBabySellingSwitch);
        toiletBabyEatingSpace = (Switch)findViewById(R.id.editBabyEatingSpaceSwitch);


        toiletBabyChair = (Switch)findViewById(R.id.editBabyChairSwitch);
        toiletBabySoffa = (Switch)findViewById(R.id.editBabySoffaSwitch);
        toiletKidsToilet = (Switch)findViewById(R.id.editkidsToiletSwitch);
        toiletKidsSpace = (Switch)findViewById(R.id.editkidsSpaceSwitch);
        toiletHeight = (Switch)findViewById(R.id.editheightMeasureSwitch);
        toiletWeight = (Switch)findViewById(R.id.editweightMeasureSwitch);
        toiletToy = (Switch)findViewById(R.id.editBabyToySwitch);
        toiletBabyFancy = (Switch)findViewById(R.id.editBabyFancySwitch);
        toiletBabySmellGood = (Switch)findViewById(R.id.editBabySmellGoodSwitch);

    }

    private void othersReady(){


        textToiletName = (EditText) findViewById(R.id.writeToiletName);
        textHowToAccess = (EditText) findViewById(R.id.inputHowToAccess);
        textFeedback = (EditText) findViewById(R.id.kansou);

        textFeedback.setHint("トイレがとても綺麗でした。ありがとうございます。");
        textFeedback.setMaxLines(Integer.MAX_VALUE);
        textFeedback.setHorizontallyScrolling(false);

        ratingBar = (RatingBar) findViewById(R.id.editRating);
        ratingBar.setRating(3);
        addPhoto = (Button) findViewById(R.id.buttonEditPicture);
        buttonRenewInfo = (Button) findViewById(R.id.buttonEditInfo);
        //buttonchangePinLocation = (Button) findViewById(R.id.buttonEditPinMap);

        mainImage = (ImageView) findViewById(R.id.picture1);
//         ImageView subImage1 = (ImageView) findViewById(R.id.picture2);
//         ImageView subImage2 = (ImageView) findViewById(R.id.picture3);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},2);
                } else {


                   imageSetPlaceChoose();

                }
            }
        });


        buttonRenewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                 pictureUpload(); March 3 18pm

                toiletNameCheck();
//                 firebaseUpdate();

            }
        });

//        buttonchangePinLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Moved to change pin
//
//
//
//
//            }
//        });

    }

    private void toiletNameCheck(){
        String tName = textToiletName.getText().toString();

        if(TextUtils.isEmpty(tName)) {

            textToiletName.setError("Your message");
            Log.i("HEy", "00");
        }
        else {
            //there is a valid name

            //firebaseUpdate();
        }
    }
    private void imageSetPlaceChoose(){
        final Integer imageNum = 0;
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



    private void showPhoto(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);


    }


    private void sppinnerReady(){

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        waitingTimeSpinner = (Spinner) findViewById(R.id.spinnerWaitingTime);
        floorSpinner = (Spinner) findViewById(R.id.locationFloorSpinner);
        startHoursSpinner = (Spinner) findViewById(R.id.startHoursSpinner);
        startMinutesSpinner = (Spinner) findViewById(R.id.startMinutesSpinner);
        endHoursSpinner = (Spinner) findViewById(R.id.endHoursSpinner);
        endMinutesSpinner = (Spinner) findViewById(R.id.endMinutesSpinner);

        adapterType = ArrayAdapter.createFromResource(this,R.array.places_names,android.R.layout.simple_spinner_item);
        adapterWaitingtime = ArrayAdapter.createFromResource(this,R.array.waitingTimeArray,android.R.layout.simple_spinner_item);
        adapterFloor = ArrayAdapter.createFromResource(this,R.array.floorCount,android.R.layout.simple_spinner_item);
        adapterStartHours = ArrayAdapter.createFromResource(this,R.array.hoursOption,android.R.layout.simple_spinner_item);
        adapterStartMinutes = ArrayAdapter.createFromResource(this,R.array.minutesOption,android.R.layout.simple_spinner_item);
        adapterEndHours = ArrayAdapter.createFromResource(this,R.array.hoursOption,android.R.layout.simple_spinner_item);
        adapterEndMinutes = ArrayAdapter.createFromResource(this,R.array.minutesOption,android.R.layout.simple_spinner_item);

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

                                                      if (!typeSpinnerLoaded){
                                                          ((TextView) parent.getChildAt(0)).setText(toilet.type);
                                                          Log.i("TypeCalled","111");
                                                          typeSpinnerLoaded = true;
                                                      }




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
                                                       if (!floorSpinnerLoaded){

                                                           // ((TextView) parent.getChildAt(0)).setText(parent.getSelectedItem()));

                                                               ((TextView) parent.getChildAt(0)).setText(String.valueOf(toilet.floor));
                                                           floorSpinnerLoaded = true;
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
                                                            Integer startHours = toilet.openHours/100;
                                                            if (!openHourSpinnerLoaded) {
                                                                ((TextView) parent.getChildAt(0)).setText(String.valueOf(startHours));
                                                               openHourSpinnerLoaded = true;
                                                            }
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
                                                              Integer startMinutes = toilet.openHours % 100;

                                                              if (!openMinutesSpinnerLoaded) {

                                                                  if (startMinutes != 0) {
                                                                      ((TextView) parent.getChildAt(0)).setText(String.valueOf(startMinutes));
                                                                    openMinutesSpinnerLoaded = true;
                                                                  }
                                                              }

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
                                                          Integer endHours = toilet.closeHours/100;
                                                          if (!closeHourSpinnerLoaded) {

                                                              ((TextView) parent.getChildAt(0)).setText(String.valueOf(endHours));
                                                            closeHourSpinnerLoaded = true;
                                                          }

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
                                                            Integer endMinutes = toilet.closeHours % 100;
                                                            if (!closeMinutesSpinnerLoaded) {

                                                                if (endMinutes != 0) {
                                                                    ((TextView) parent.getChildAt(0)).setText(String.valueOf(endMinutes));
                                                                    closeMinutesSpinnerLoaded= true;

                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {
                                                        }
                                                    }
        );


        //onCreatedSpinner = true;

    }

    private void toileGetData(final String originalKey) {

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        toiletRef.child(originalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("OnDataChangeCalled", "777");
                // for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    Log.i("OnDataChangeCalled", "777888");
                    Boolean removedToilet = false;

                    Log.i("OnDataChangeCalled", "777888999");
//                    Toilet toilet =  new Toilet();
                    // List<String> toiletData = new ArrayList<>();


                    Log.i("UserInfo.latitude", String.valueOf(UserInfo.latitude));
                    Log.i("UserInfo.longitude", String.valueOf(UserInfo.longitude));

                    Log.i("OnDataChangeCalled", "777888999");
                    LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                    //get from the location Manager
                    Log.i("IS THIS THE ERROR???", "4");

                    Log.i("IS THIS THE ERROR???", "1");
                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();
                    Log.i("IS THIS THE ERROR???", "2");


                    LatLng toiletLocation = new LatLng(toilet.latitude, toilet.longitude);
                    //get from the database
                    Log.i("IS THIS THE ERROR???", "5");

                    double distance = CalculationByDistance(centerLocation, toiletLocation);
                    Log.i("IS THIS THE ERROR???", "6");

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


                    Log.i("IS THIS THE ERROR???", "3");
                    Log.i("BOOOL???", "1");

//


                    toilet.key = originalKey;
                    toilet.address = (String) dataSnapshot.child("address").getValue();
                    //Not sure about how to call key....

                    toilet.name = (String) dataSnapshot.child("name").getValue();
                    toilet.openAndCloseHours = (String) dataSnapshot.child("openAndCloseHours").getValue();
                    toilet.type = (String) dataSnapshot.child("type").getValue();
                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();

                    Log.i("BOOOL???", "2");

                    toilet.addedBy = (String) dataSnapshot.child("addedBy").getValue();
                    toilet.editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();
                    toilet.address = (String) dataSnapshot.child("address").getValue();
                    toilet.howtoaccess = (String) dataSnapshot.child("howtoaccess").getValue();

                    Log.i("BOOOL???", "3");

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

                    Log.i("BOOOL???", "4");

//                    Log.i("IS THIS THE ERROR???","1");
//                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
//                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();
//                    Log.i("IS THIS THE ERROR???","2");

                    toilet.available = (Boolean) dataSnapshot.child("available").getValue();
                    toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                    toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                    toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                    toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();

                    Log.i("BOOOL???", "5");

                    toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();
                    toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                    toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
                    toilet.noVirus = (Boolean) dataSnapshot.child("noVirus").getValue();
                    toilet.paperForBenki = (Boolean) dataSnapshot.child("paperForBenki").getValue();
                    toilet.cleanerForBenki = (Boolean) dataSnapshot.child("cleanerForBenki").getValue();
                    toilet.autoToiletWash = (Boolean) dataSnapshot.child("nonTouchWash").getValue();

                    Log.i("BOOOL???", "6");

                    toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
                    toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
                    toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
                    toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
                    toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();


                    //From Maps Activity
                    //others one

                    toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
                    toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
                    toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
                    toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                    toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();



                    //others two
                    toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
                    toilet.english = (Boolean) dataSnapshot.child("english").getValue();
                    toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
                    toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
                    toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();


                    //for ladys

                    toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                    toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
                    toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                    toilet.ladyOmutu = (Boolean) dataSnapshot.child("ladyOmutu").getValue();
                    toilet.ladyBabyChair = (Boolean) dataSnapshot.child("ladyBabyChair").getValue();
                    toilet.ladyBabyChairGood = (Boolean) dataSnapshot.child("ladyBabyChairGood").getValue();
                    toilet.ladyBabyChairAccess = (Boolean) dataSnapshot.child("ladyBabyChairAccess").getValue();

                    //for Mans
                    toilet.maleOmutu = (Boolean) dataSnapshot.child("maleOmutu").getValue();
                    toilet.maleBabyChair = (Boolean) dataSnapshot.child("maleBabyChair").getValue();
                    toilet.maleBabyChairGood = (Boolean) dataSnapshot.child("maleBabyChairGood").getValue();
                    toilet.maleBabyChairAccess = (Boolean) dataSnapshot.child("maleBabyChairAccess").getValue();

                    //for Family Restroom

                    toilet.wheelchair = (Boolean) dataSnapshot.child("wheelchair").getValue();
                    toilet.wheelchairAccess = (Boolean) dataSnapshot.child("wheelchairAccess").getValue();
                    toilet.autoDoor = (Boolean) dataSnapshot.child("handrail").getValue();
                    toilet.callHelp = (Boolean) dataSnapshot.child("callHelp").getValue();
                    toilet.ostomate = (Boolean) dataSnapshot.child("ostomate").getValue();
                    toilet.braille = (Boolean) dataSnapshot.child("braille").getValue();
                    toilet.voiceGuide = (Boolean) dataSnapshot.child("voiceGuide").getValue();
                    toilet.familyOmutu = (Boolean) dataSnapshot.child("familyOmutu").getValue();
                    toilet.familyBabyChair = (Boolean) dataSnapshot.child("familyBabyChair").getValue();
                    //From Maps Activity
                    ///

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




                    Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);


                    toiletJapanese.setChecked(toilet.japanesetoilet);
                    toiletWestern.setChecked(toilet.westerntoilet);
                    toiletOnlyFemale.setChecked(toilet.onlyFemale);
                    toiletUnisex.setChecked(toilet.unisex);


                    toiletWashlet.setChecked(toilet.washlet);
                    toiletWarmseat.setChecked(toilet.warmSeat);
                    toiletAutoopen.setChecked(toilet.autoOpen);
                    toiletNoVirusBenki.setChecked(toilet.noVirus);
                    toiletPaperForBenki.setChecked(toilet.paperForBenki);
                    toiletCleanerForBenki.setChecked(toilet.cleanerForBenki);
                    toiletNonTouchWash.setChecked(toilet.autoToiletWash);


                    toiletSensor.setChecked(toilet.sensorHandWash);
                    toiletHandSoap.setChecked(toilet.handSoap);
                    toiletNonHandSoap.setChecked(toilet.autoHandSoap);
                    toiletPaperTowel.setChecked(toilet.paperTowel);
                    toiletHandDrier.setChecked(toilet.handDrier);



                    toiletOtohime.setChecked(toilet.otohime);
                    toiletNapkinSelling.setChecked(toilet.napkinSelling);
                    toiletMakeroom.setChecked(toilet.makeuproom);
                    toiletClothes.setChecked(toilet.clothes);
                    toiletBaggage.setChecked(toilet.baggageSpace);


                    toiletWheelchair.setChecked(toilet.wheelchair);
                    toiletWheelchairAccess.setChecked(toilet.wheelchairAccess);
                    toiletHandrail.setChecked(toilet.autoDoor);
                    toiletCallHelp.setChecked(toilet.callHelp);
                    toiletOstomate.setChecked(toilet.ostomate);
                    toiletWrittenEnglish.setChecked(toilet.english);
                    toiletBraille.setChecked(toilet.braille);
                    toiletVoiceGuide.setChecked(toilet.voiceGuide);
                    toiletFancy.setChecked(toilet.fancy);
                    toiletSmell.setChecked(toilet.smell);
                    toiletConfortable.setChecked(toilet.conforatableWide);
                    toiletNoNeedAsk.setChecked(toilet.noNeedAsk);
                    toiletParking.setChecked(toilet.parking);
                    toiletAirCondition.setChecked(toilet.airCondition);
                    toiletWifi.setChecked(toilet.wifi);

                    textToiletName.setText(toilet.name);
                    textHowToAccess.setText(toilet.howtoaccess);


                    toiletMilk.setChecked(toilet.milkspace);
                    toiletBabyRoomOnlyFemale.setChecked(toilet.babyroomOnlyFemale);
                    toiletBabyRoomMaleEnter.setChecked(toilet.babyroomManCanEnter);
                    toiletBabyPersonalRoom.setChecked(toilet.babyPersonalSpace);
                    toiletBabyPersonalRoomWithLock.setChecked(toilet.babyPersonalSpaceWithLock);
                    toiletBabyRoomWide.setChecked(toilet.babyRoomWideSpace);

                    toiletBabyCarRental.setChecked(toilet.babyCarRental);
                    toiletBabyCarAccess.setChecked(toilet.babyCarAccess);
                    toiletOmutu.setChecked(toilet.omutu);
                    toiletHipCleaningStuff.setChecked(toilet.hipWashingStuff);
                    toiletOmutuTrashCan.setChecked(toilet.babyTrashCan);
                    toiletOmutuSelling.setChecked(toilet.omutuSelling);

                    toiletBabySink.setChecked(toilet.babyRoomSink);
                    toiletBabyWashstand.setChecked(toilet.babyWashStand);
                    toiletBabyHotWater.setChecked(toilet.babyHotWater);
                    toiletBabyMicrowave.setChecked(toilet.babyMicroWave);
                    toiletBabyWaterSelling.setChecked(toilet.babyWaterSelling);
                    toiletBabyFoodSelling.setChecked(toilet.babyFoddSelling);
                    toiletBabyEatingSpace.setChecked(toilet.babyEatingSpace);

                    toiletBabyChair.setChecked(toilet.babyChair);
                    toiletBabySoffa.setChecked(toilet.babySoffa);
                    toiletKidsToilet.setChecked(toilet.babyKidsToilet);
                    toiletKidsSpace.setChecked(toilet.babyKidsSpace);

                    toiletHeight.setChecked(toilet.babyHeightMeasure);
                    toiletWeight.setChecked(toilet.babyWeightMeasure);
                    toiletToy.setChecked(toilet.babyToy);
                    toiletBabyFancy.setChecked(toilet.babyFancy);
                    toiletBabySmellGood.setChecked(toilet.babySmellGood);


                    Log.i("Type@@@",toilet.type);
                    sppinnerReady();











//
//                    toiletNameLabel.setText(toilet.name);
//                    typeAndDistance.setText(toilet.type + "/" + toilet.distance);
//                    availableAndWaiting.setText("ご利用時間" + toilet.openAndCloseHours + "/平均待ち" + String.valueOf(toilet.averageWait) + "分");
//                    ratingDisplay.setRating(averaegeStarFloat);
//                    ratingNumber.setText(toilet.averageStar);
//                    ratingCount.setText("(" + toilet.reviewCount + ")");
//                    mapAddress.setText(toilet.address);
//                    mapHowToAccess.setText(toilet.howtoaccess);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String TAG = "Error";
                Log.w(TAG, "DatabaseError", databaseError.toException());

            }
        });

    }

//    private void firebaseDeleteData(){
//
//        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
//        toiletLocationRef = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");
//
//        DatabaseReference deleteLocationsRef = toiletLocationRef.child(toilet.key);
//        deleteLocationsRef.removeValue();
//        DatabaseReference deleteToiletRef = toiletRef.child(toilet.key);
//        deleteToiletRef.removeValue();
//
//
//        firebaseRenewdata();
//
//
//
//
//
//
//        //call firebaseRenewData....
//
//
//
//    }
    private void firebaseRenewdata(){


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Integer stHour = Integer.parseInt(String.valueOf(startHoursSpinner.getSelectedItem()));
        Integer stMinu = Integer.parseInt(String.valueOf(startMinutesSpinner.getSelectedItem()));
        Integer enHour = Integer.parseInt(String.valueOf(endHoursSpinner.getSelectedItem()));
        Integer enMinu = Integer.parseInt(String.valueOf(endMinutesSpinner.getSelectedItem()));
        Integer openTime = stHour * 100 + stMinu;
        Integer endTime = enHour * 100 + enMinu;

        Integer openData = 5000;
        Integer endData = 5000;
        String tName = textToiletName.getText().toString();

        if (openTime < endTime){
            openData = openTime;
            endData = endTime;
            Log.i(String.valueOf(openData),String.valueOf(endData));

        }else if (openTime == endTime){
            openData = 5000;
            endData = 5000;
            Log.i(String.valueOf(openData),String.valueOf(endData));
        } else if (openTime > endTime){
            openData = openTime;
            endData = endTime + 2400;
            Log.i(String.valueOf(openData),String.valueOf(endData));

        }

//       Log.i(String.valueOf(openData),String.valueOf(endData));


        double ratingValue = ratingBar.getRating();
        //float to double
        Integer star1Value = ratingBar.getNumStars();

        String waitingV = waitingTimeSpinner.getSelectedItem().toString();
        //float to Int
        Integer waitingValue = Integer.parseInt(waitingV);

        String openingString = startHoursSpinner.getSelectedItem().toString() + ":" + startMinutesSpinner.getSelectedItem().toString() + "〜" + endHoursSpinner.getSelectedItem().toString() + ":" + endMinutesSpinner.getSelectedItem().toString();

        String avStar = String.valueOf(ratingValue);
        Log.i("datbase", "called121");


        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
        toiletLocationRef = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");


       // geolocationUpdate(toilet.key);


        DatabaseReference updateToiletRef = toiletRef.child(toilet.key);




        //String firekey = updateRef.getKey();

        //delete original data in toilets brunch
        //delete original data in toiletLocations brunch

        Log.i("datbaseUpdateLat", String.valueOf(toilet));
        Log.i("datbaseUpdateLon", String.valueOf(toilet.longitude));
        Log.i("datbaseUpdateAVSTAR", String.valueOf(avStar));













//        updateToiletRef.setValue(new Post(
//                tName,
//                openingString,
//                typeSpinner.getSelectedItem().toString(),
//                "",//String urlOne,
//                "",//String urlTwo,
//                "",//String urlThree,
//                uid,//String addedBy,
//                uid,//String editedBy,
//                "",//reviewOne
//                "",//reviewTwo
//                avStar,
//                toilet.address,
//                "",//String howtoaccess,
//                openData,
//                endData,
//                1,//Integer reviewCount,
//                waitingValue,//Integer averageWait,
//                3,//Integer toiletFloor,
//                toilet.latitude,
//                toilet.longitude,
//                true,
//                toiletJapanese.isChecked(),
//                toiletWestern.isChecked(),
//                toiletOnlyFemale.isChecked(),
//                toiletUnisex.isChecked(),
//                toiletWashlet.isChecked(),
//                toiletWashlet.isChecked(),
//                toiletAutoopen.isChecked(),
//                toiletNoVirusBenki.isChecked(),
//                toiletPaperForBenki.isChecked(),
//                toiletCleanerForBenki.isChecked(),
//                toiletNonTouchWash.isChecked(),
//                toiletSensor.isChecked(),
//                toiletHandSoap.isChecked(),
//                toiletNonHandSoap.isChecked(),
//                toiletPaperTowel.isChecked(),
//                toiletHandDrier.isChecked(),
//                toiletOtohime.isChecked(),
//                toiletNapkinSelling.isChecked(),
//                toiletMakeroom.isChecked(),
//                toiletClothes.isChecked(),
//                toiletBaggage.isChecked(),
//                toiletWheelchair.isChecked(),
//                toiletWheelchairAccess.isChecked(),
//                toiletHandrail.isChecked(),
//                toiletCallHelp.isChecked(),
//                toiletOstomate.isChecked(),
//                toiletWrittenEnglish.isChecked(),
//                toiletBraille.isChecked(),
//                toiletVoiceGuide.isChecked(),
//                toiletFancy.isChecked(),
//                toiletSmell.isChecked(),
//                toiletConfortable.isChecked(),
//                toiletNoNeedAsk.isChecked(),
//                toiletParking.isChecked(),
//                toiletAirCondition.isChecked(),
//                toiletWifi.isChecked(),
//                toiletMilk.isChecked(),
//                toiletBabyRoomOnlyFemale.isChecked(),
//                toiletBabyRoomMaleEnter.isChecked(),
//                toiletBabyPersonalRoom.isChecked(),
//                toiletBabyPersonalRoomWithLock.isChecked(),
//                toiletBabyPersonalRoomWithLock.isChecked(),
//                toiletBabyCarRental.isChecked(),
//                toiletBabyCarAccess.isChecked(),
//                toiletOmutu.isChecked(),
//                toiletHipCleaningStuff.isChecked(),
//                toiletOmutuTrashCan.isChecked(),
//                toiletOmutuSelling.isChecked(),
//                toiletBabySink.isChecked(),
//                toiletBabyWashstand.isChecked(),
//                toiletBabyHotWater.isChecked(),
//                toiletBabyMicrowave.isChecked(),
//                toiletBabyWaterSelling.isChecked(),
//                toiletBabyFoodSelling.isChecked(),
//                toiletBabyEatingSpace.isChecked(),
//                toiletBabyChair.isChecked(),
//                toiletBabySoffa.isChecked(),
//                toiletKidsToilet.isChecked(),
//                toiletKidsSpace.isChecked(),
//                toiletHeight.isChecked(),
//                toiletWeight.isChecked(),
//                toiletToy.isChecked(),
//                toiletBabyFancy.isChecked(),
//                toiletBabySmellGood.isChecked()
//
//        ));


        Log.i("please", "...");
       // geolocationUpdate(firekey);

        Intent intent = new Intent(getApplicationContext(),DetailViewActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", toilet.key);
        intent.putExtra("toiletLatitude",toilet.latitude);
        intent.putExtra("toiletLongitude",toilet.longitude);

        startActivity(intent);
        finish();

    }


//    private void geolocationUpdate(String firekey){
//
////        String newRef = ref.child("Toilets");
////
////        String newID = newRef
//        Log.i("datbaseUpdateLat", String.valueOf(AddLocations.latitude));
//        Log.i("datbaseUpdateLon", String.valueOf(AddLocations.longitude));
//
//        geoFire.setLocation(firekey, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener(){
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



//    public void onMapReadyCalled(GoogleMap googleMap, double toiletLat, double toiletLon) {
//        mMap = googleMap;
//
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//        locationListener = new android.location.LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                Log.i("onLocationChanged","Called");
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//
//        if (Build.VERSION.SDK_INT < 23) {
//
//            Log.i("Build.VERSION.SDK_INT ","Build.VERSION.SDK_INT ");
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
//
//
//        }
//        else{
////            Log.i("Build.VERSION.SDK_INT>23 ","Build.VERSION.SDK_INT ");
//
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//
//
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
//
//
//
//            }else {
//                //When the permission is granted....
//                Log.i("HeyHey333", "locationManager.requestLocationUpdates");
//
//
////
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//                mMap.setMyLocationEnabled(true);
//                Log.i("HeyHey333444555", "locationManager.requestLocationUpdates");
//
//
//
//
//                if (lastKnownLocation != null){
//                    Log.i("HeyHey3334445556666", "locationManager.requestLocationUpdates");
//
//
//                    mMap.clear();
//
//
//                   // Log.i("toiletLatLng0909", String.valueOf(toiletLat) + String.valueOf(toiletLon));
//                    LatLng toiletLatLng = new LatLng(toiletLat, toiletLon);
//
//
//
//                    mMap.addMarker(new MarkerOptions().position(toiletLatLng).title("施設の場所"));
//
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletLatLng));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletLatLng, 14.0f));
//
//
//
//
//                } else {
//                    //When you could not get the last known location...
//
//                }
//            }
//        }
//    }


}
