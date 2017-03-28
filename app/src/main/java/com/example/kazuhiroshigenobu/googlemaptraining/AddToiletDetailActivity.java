package com.example.kazuhiroshigenobu.googlemaptraining;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
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
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AddToiletDetailActivity extends AppCompatActivity {


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


    Switch toiletOtohime;
    Switch toiletNapkinSelling;
    Switch toiletMakeroom;
    Switch toiletClothes;
    Switch toiletBaggage;

    Switch toiletWheelchair;
    Switch toiletWheelchairAccess;
    Switch toiletHandrail;
    Switch toiletCallHelp;
    Switch toiletOstomate;
    Switch toiletWrittenEnglish;
    Switch toiletBraille;
    Switch toiletVoiceGuide;

    Switch toiletFancy;
    Switch toiletSmell;
    Switch toiletConfortable;
    Switch toiletNoNeedAsk;
    Switch toiletParking;
    Switch toiletAirCondition;
    Switch toiletWifi;



    Spinner typeSpinner;
    Spinner waitingTimeSpinner;
    Spinner floorSpinner;
    Spinner startHoursSpinner;
    Spinner startMinutesSpinner;
    Spinner endHoursSpinner;
    Spinner endMinutesSpinner;

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



    Switch toilet;








    EditText textToiletName;
    EditText textHowToAccess;
    EditText textFeedback;

    // EditText added is not a TextInputEditText. Please switch to using that class instead.

//
    RatingBar ratingBar;

    LocationManager locationManager;

    android.location.LocationListener locationListener;

    Button addPhoto;
    Button buttonAddInfo;

    private GoogleApiClient client;
    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;



    Boolean spinnerLoaded = false;
    Integer photoSelected = 0;
    private Uri mainfilePath;
    private Uri subOnefilePath;
    private Uri subTwofilePath;

    //private GeoFire geoFire;

    File file;
    Metadata metadata;

    ImageView mainImage;
    ImageView subImage1;
    ImageView subImage2;



//    ArrayAdapter<CharSequence> adapterJapaneseCount;
//    ArrayAdapter<CharSequence> adapterWesternCount;
//    ArrayAdapter<CharSequence> adapterPppCount;
//    ArrayAdapter<CharSequence> adapterWomenJapaneseCount;
//    ArrayAdapter<CharSequence> adapterWomenWesternCount;
    ArrayAdapter<CharSequence> adapterType;
    ArrayAdapter<CharSequence> adapterWaitingtime;
    ArrayAdapter<CharSequence> adapterFloor;
    ArrayAdapter<CharSequence> adapterStartHours;
    ArrayAdapter<CharSequence> adapterStartMinutes;
    ArrayAdapter<CharSequence> adapterEndHours;
    ArrayAdapter<CharSequence> adapterEndMinutes;



    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference ref = database.getReference();

//    private DatabaseReference toiletRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("images");

    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");
    GeoFire geoFire = new GeoFire(locationRef);

    DatabaseReference toiletRef = FirebaseDatabase.getInstance().getReference("Toilets");

//    AddLocations addLocations;

//    String tName = textToiletName.getText().toString();
    //the above causes an null warining ....
    //March 4th 9am



//    Integer stHour = Integer.parseInt(String.valueOf(startHoursSpinner.getSelectedItem()));
//    Integer stMinu = Integer.parseInt(String.valueOf(startMinutesSpinner.getSelectedItem()));
//    Integer enHour = Integer.parseInt(String.valueOf(endHoursSpinner.getSelectedItem()));
//    Integer enMinu = Integer.parseInt(String.valueOf(endMinutesSpinner.getSelectedItem()));
//    Integer openTime = stHour * 100 + stMinu;
//    Integer endTime = enHour * 100 + enMinu;




//    StorageReference storageRef = FirebaseStorage.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toilet_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addCheckMap);

        mapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override public void onMapReady(GoogleMap googleMap) {
                if (googleMap != null) {
                    // your additional codes goes here
                    onMapReadyCalled(googleMap);


                }
            }}
            );

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.getCurrentUser().getUid();

        Log.i("getUIDddd",String.valueOf(firebaseAuth.getCurrentUser().getUid()));

        //firebaseAuth.getCurrentUser();

        sppinnerReady();
        textReady();
        switchReady();
        othersReady();
        final java.util.Calendar c = java.util.Calendar.getInstance();
        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);
        if (hour == 0){
            Log.i("Time12321", String.valueOf(minute));

        } else {
            int hourMul = hour * 100;
            Log.i("Time12321", String.valueOf(hourMul + minute));
        }





    }


    private void sppinnerReady(){

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        waitingTimeSpinner = (Spinner) findViewById(R.id.spinnerWaitingTime);
        floorSpinner = (Spinner) findViewById(R.id.locationFloorSpinner);
        startHoursSpinner = (Spinner) findViewById(R.id.startHoursSpinner);
        startMinutesSpinner = (Spinner) findViewById(R.id.startMinutesSpinner);
        endHoursSpinner = (Spinner) findViewById(R.id.endHoursSpinner);
        endMinutesSpinner = (Spinner) findViewById(R.id.endMinutesSpinner);



//        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
//        adapterJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
//        adapterWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
//        adapterPppCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
//        adapterWomenJapaneseCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
//        adapterWomenWesternCount = ArrayAdapter.createFromResource(this,R.array.benkiCountArray,android.R.layout.simple_spinner_item);
        adapterType = ArrayAdapter.createFromResource(this,R.array.places_names,android.R.layout.simple_spinner_item);
        adapterWaitingtime = ArrayAdapter.createFromResource(this,R.array.waitingTimeArray,android.R.layout.simple_spinner_item);
        adapterFloor = ArrayAdapter.createFromResource(this,R.array.floorCount,android.R.layout.simple_spinner_item);
        adapterStartHours = ArrayAdapter.createFromResource(this,R.array.hoursOption,android.R.layout.simple_spinner_item);
        adapterStartMinutes = ArrayAdapter.createFromResource(this,R.array.minutesOption,android.R.layout.simple_spinner_item);
        adapterEndHours = ArrayAdapter.createFromResource(this,R.array.hoursOption,android.R.layout.simple_spinner_item);
        adapterEndMinutes = ArrayAdapter.createFromResource(this,R.array.minutesOption,android.R.layout.simple_spinner_item);


//        adapterJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterPppCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterWomenJapaneseCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterWomenWesternCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        // I have not writtern setOnItemSElected yet...
        //...





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
                 if (!spinnerLoaded){

                     // ((TextView) parent.getChildAt(0)).setText(parent.getSelectedItem()));
                     ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(3) + "");
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


    private void textReady(){
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

    private void switchReady(){


        toiletJapanese = (Switch) findViewById(R.id.addJapaneseSwitch);
        toiletWestern = (Switch) findViewById(R.id.addWesternSwitch);
        toiletOnlyFemale = (Switch) findViewById(R.id.addOnlyFemaleSwitch);
        toiletUnisex  = (Switch) findViewById(R.id.addUnisexSwitch);

        toiletWashlet  = (Switch) findViewById(R.id.addWashletSwitch);
        toiletWarmseat = (Switch) findViewById(R.id.addWarmSeatSwitch);
        toiletAutoopen = (Switch) findViewById(R.id.addAutoOpenSwitch);
        toiletNoVirusBenki = (Switch) findViewById(R.id.addnoVirusBenkiSwitch);
        toiletPaperForBenki = (Switch) findViewById(R.id.addPaperForBenkiSwitch);
        toiletCleanerForBenki = (Switch) findViewById(R.id.addCleanerForBenkiSwitch);
        toiletNonTouchWash = (Switch) findViewById(R.id.addNonTouchWashSwitch);


        toiletSensor = (Switch) findViewById(R.id.addSenserWashSwitch);
        toiletHandSoap = (Switch) findViewById(R.id.addSoapSwitch);
        toiletNonHandSoap = (Switch) findViewById(R.id.addNonTouchSoapSwitch);
        toiletPaperTowel = (Switch) findViewById(R.id.addPaperTowelSwitch);
        toiletHandDrier = (Switch) findViewById(R.id.addHandDrierSwitch);


        toiletOtohime = (Switch) findViewById(R.id.addOtohimeSwitch);
        toiletNapkinSelling = (Switch) findViewById(R.id.addNapkinSellingSwitch);
        toiletMakeroom = (Switch) findViewById(R.id.addMakeSwitch);
        toiletClothes = (Switch) findViewById(R.id.addClothesSwitch);
        toiletBaggage = (Switch) findViewById(R.id.addBaggageSwitch);



        toiletWheelchair = (Switch) findViewById(R.id.addWheelchairSwitch);
        toiletWheelchairAccess = (Switch) findViewById(R.id.addWheelchairAccessSwitch);
        toiletHandrail = (Switch) findViewById(R.id.addHandrailSwitch);
        toiletCallHelp = (Switch)findViewById(R.id.addCallHelpSwitch);
        toiletOstomate = (Switch) findViewById(R.id.addOstomateSwitch);
        toiletWrittenEnglish = (Switch) findViewById(R.id.addWrittenEnglishSwitch);
        toiletBraille = (Switch) findViewById(R.id.addBraille);
        toiletVoiceGuide = (Switch) findViewById(R.id.addVoiceGuideSwitch);


        toiletFancy = (Switch) findViewById(R.id.addFancySwitch);
        toiletSmell = (Switch) findViewById(R.id.addSmellSwitch);
        toiletConfortable = (Switch) findViewById(R.id.addConforableSwitch);
        toiletNoNeedAsk = (Switch) findViewById(R.id.addNoNeedAskSwitch);
        toiletParking = (Switch) findViewById(R.id.addParkingSwitch);
        toiletAirCondition = (Switch) findViewById(R.id.addAirConditionSwitch);
        toiletWifi = (Switch) findViewById(R.id.addWifiSwitch);

        toiletMilk = (Switch) findViewById(R.id.addMilkSwitch);
        toiletBabyRoomOnlyFemale = (Switch)findViewById(R.id.addMilkOnlyFemaleSwitch);
        toiletBabyRoomMaleEnter = (Switch)findViewById(R.id.addMilkMaleOkaySwitch);
        toiletBabyPersonalRoom = (Switch)findViewById(R.id.addBabyPersonalSpaceSwitch);
        toiletBabyPersonalRoomWithLock = (Switch)findViewById(R.id.addBabyPersonalSpaceWithLockSwitch);
        toiletBabyRoomWide = (Switch)findViewById(R.id.addWideBabySpaceSwitch);


        toiletBabyCarRental = (Switch)findViewById(R.id.addRentalBabyCarSwitch);
        toiletBabyCarAccess = (Switch)findViewById(R.id.addBabyCarAccessSwitch);
        toiletOmutu = (Switch) findViewById(R.id.addOmutuSwitch);
        toiletHipCleaningStuff = (Switch)findViewById(R.id.addHipCleanStuffSwitch);
        toiletOmutuTrashCan = (Switch)findViewById(R.id.addOmutuTrashCanSwitch);
        toiletOmutuSelling = (Switch) findViewById(R.id.addOmutuSellingSwitch);


        toiletBabySink = (Switch)findViewById(R.id.addBabySinkSwitch);
        toiletBabyWashstand = (Switch)findViewById(R.id.addBabyWashstandSwitch);
        toiletBabyHotWater = (Switch)findViewById(R.id.addBabyHotWaterSwitch);
        toiletBabyMicrowave = (Switch)findViewById(R.id.addBabyMicrowaveSwitch);
        toiletBabyWaterSelling = (Switch)findViewById(R.id.addBabySellingWaterSwitch);
        toiletBabyFoodSelling = (Switch)findViewById(R.id.addfoodForBabySellingSwitch);
        toiletBabyEatingSpace = (Switch)findViewById(R.id.addBabyEatingSpaceSwitch);


        toiletBabyChair = (Switch)findViewById(R.id.addBabyChairSwitch);
        toiletBabySoffa = (Switch)findViewById(R.id.addBabySoffaSwitch);
        toiletKidsToilet = (Switch)findViewById(R.id.addkidsToiletSwitch);
        toiletKidsSpace = (Switch)findViewById(R.id.addkidsSpaceSwitch);
        toiletHeight = (Switch)findViewById(R.id.addheightMeasureSwitch);
        toiletWeight = (Switch)findViewById(R.id.addweightMeasureSwitch);
        toiletToy = (Switch)findViewById(R.id.addBabyToySwitch);
        toiletBabyFancy = (Switch)findViewById(R.id.addBabyFancySwitch);
        toiletBabySmellGood = (Switch)findViewById(R.id.addBabySmellGoodSwitch);



    }

     private void othersReady(){

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
                 if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){


                     requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                 } else {

                     imageSetPlaceChoose();

                 }
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

//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
//        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                mainImage = (ImageView) findViewById(R.id.picture1);
                subImage1 = (ImageView) findViewById(R.id.picture2);
                subImage2 = (ImageView) findViewById(R.id.picture3);

                //I wrote this one twice
                ImageView targetView = mainImage;


                if (photoSelected == 0){
                    targetView = mainImage;
                    //mainfilePath = selectedImage;


                } else if (photoSelected == 1){
                    targetView = subImage1;
                    //subOnefilePath = selectedImage;

                } else if  (photoSelected == 2){
                    targetView = subImage2;
                    //subTwofilePath = selectedImage;

                }

                targetView.setImageBitmap(bitmap);




            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");



    }

    private void valueCheck(){
        String tName = textToiletName.getText().toString();

        if(TextUtils.isEmpty(tName)) {

            textToiletName.setError("Your message");
            Log.i("HEy", "00");
        }
        else {
            //there is a valid name
            firebaseUpdate();
        }
    }



   private void firebaseUpdate(){


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


//        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        DatabaseReference newRef = toiletRef.push();

        String firekey = newRef.getKey();


       newRef.setValue(new Post(
               tName,
               openingString,
               typeSpinner.getSelectedItem().toString(),
               "",//String urlOne,
               "",//String urlTwo,
               "",//String urlThree,
               "",//String addedBy,
               "",//String editedBy,
               avStar,
               AddLocations.address,
               "",//String howtoaccess,
               openData,
               endData,
               1,//Integer reviewCount,
               waitingValue,//Integer averageWait,
               3,//Integer toiletFloor,
               AddLocations.latitude,
               AddLocations.longitude,
               true,
               toiletJapanese.isChecked(),
               toiletWestern.isChecked(),
               toiletOnlyFemale.isChecked(),
               toiletUnisex.isChecked(),
               toiletWashlet.isChecked(),
               toiletWashlet.isChecked(),
               toiletAutoopen.isChecked(),
               toiletNoVirusBenki.isChecked(),
               toiletPaperForBenki.isChecked(),
               toiletCleanerForBenki.isChecked(),
               toiletNonTouchWash.isChecked(),
               toiletSensor.isChecked(),
               toiletHandSoap.isChecked(),
               toiletNonHandSoap.isChecked(),
               toiletPaperTowel.isChecked(),
               toiletHandDrier.isChecked(),
               toiletOtohime.isChecked(),
               toiletNapkinSelling.isChecked(),
               toiletMakeroom.isChecked(),
               toiletClothes.isChecked(),
               toiletBaggage.isChecked(),
               toiletWheelchair.isChecked(),
               toiletWheelchairAccess.isChecked(),
               toiletHandrail.isChecked(),
               toiletCallHelp.isChecked(),
               toiletOstomate.isChecked(),
               toiletWrittenEnglish.isChecked(),
               toiletBraille.isChecked(),
               toiletVoiceGuide.isChecked(),
               toiletFancy.isChecked(),
               toiletSmell.isChecked(),
               toiletConfortable.isChecked(),
               toiletNoNeedAsk.isChecked(),
               toiletParking.isChecked(),
               toiletAirCondition.isChecked(),
               toiletWifi.isChecked(),
               toiletMilk.isChecked(),
               toiletBabyRoomOnlyFemale.isChecked(),
               toiletBabyRoomMaleEnter.isChecked(),
               toiletBabyPersonalRoom.isChecked(),
               toiletBabyPersonalRoomWithLock.isChecked(),
               toiletBabyPersonalRoomWithLock.isChecked(),
               toiletBabyCarRental.isChecked(),
               toiletBabyCarAccess.isChecked(),
               toiletOmutu.isChecked(),
               toiletHipCleaningStuff.isChecked(),
               toiletOmutuTrashCan.isChecked(),
               toiletOmutuSelling.isChecked(),
               toiletBabySink.isChecked(),
               toiletBabyWashstand.isChecked(),
               toiletBabyHotWater.isChecked(),
               toiletBabyMicrowave.isChecked(),
               toiletBabyWaterSelling.isChecked(),
               toiletBabyFoodSelling.isChecked(),
               toiletBabyEatingSpace.isChecked(),
               toiletBabyChair.isChecked(),
               toiletBabySoffa.isChecked(),
               toiletKidsToilet.isChecked(),
               toiletKidsSpace.isChecked(),
               toiletHeight.isChecked(),
               toiletWeight.isChecked(),
               toiletToy.isChecked(),
               toiletBabyFancy.isChecked(),
               toiletBabySmellGood.isChecked()

       ));


    Log.i("please", "...");
       geolocationUpdate(firekey);

       Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
       startActivity(intent);
       finish();

    }


    private void pictureUpload(){



        Log.i("pictureUpload()", "called");
        // Get the data from an ImageView as bytes
        mainImage.setDrawingCacheEnabled(true);
        mainImage.buildDrawingCache();
        Bitmap bitmap = mainImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(AddToiletDetailActivity.this, String.valueOf(downloadUrl), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void geolocationUpdate(String firekey){

//        String newRef = ref.child("Toilets");
//
//        String newID = newRef

        geoFire.setLocation(firekey, new GeoLocation(AddLocations.latitude, AddLocations.longitude), new GeoFire.CompletionListener(){
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

//
//    private void  photoUpload(){
//        // File or Blob
////        file = Uri.fromFile(new File("path/to/mountains.jpg"));
//        file = mainImage.
//
//// Create the file metadata
//        metadata = new StorageMetadata.Builder()
//                .setContentType("image/jpeg")
//                .build();
//
//// Upload file and metadata to the path 'images/mountains.jpg'
//        uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);
//
//// Listen for state changes, errors, and completion of the upload.
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                System.out.println("Upload is " + progress + "% done");
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("Upload is paused");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // Handle successful uploads on complete
//                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
//            }
//        });
//    }
//

//    private void uploadFile() {
//        //if there is a file to upload
//        if (filePath != null) {
//            //displaying a progress dialog while upload is going on
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();
//
//            //In this example, filePath means the image
//
//            StorageReference riversRef = storageRef.child("images");
//
//
////            riversRef.putFile(filePath)
//            riversRef.putFile()
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //if the upload is successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying a success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            //if the upload is not successfull
//                            //hiding the progress dialog
//                            progressDialog.dismiss();
//
//                            //and displaying error message
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //calculating progress percentage
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                            //displaying percentage in progress dialog
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//        }
//        //if there is not any file
//        else {
//            //you can display an error toast
//        }
//    }


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




                } else {
                    //When you could not get the last known location...

                }
            }
        }
    }


                @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("Permission","Permission111");
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission","Permission222");
                    mMap.setMyLocationEnabled(true);

                    //mapUserCenterZoon();

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Log.i("Permission","Permission333");
                }

            }


        }


                    if (requestCode == 2){
                        //Photo Permission

                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            imageSetPlaceChoose();


                        }
                    }

    }

}

