package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
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


    Switch toiletOtohime;
    Switch toiletOmutu;
    Switch toiletOmutuSelling;
    Switch toiletNapkinSelling;
    Switch toiletMilk;
    Switch toiletMakeroom;
    Switch toiletClothes;
    Switch toiletBaggage;

    Switch toiletWheelchair;
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

   // Boolean onCreatedSpinner = false;


    private GoogleApiClient client;
    private GoogleMap mMap;


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
    Toilet toilet =  new Toilet();

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
        final String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toileGetData(key);


        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", key);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.editCheckMap);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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


        getMenuInflater().inflate(R.menu.editviewbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



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
        toiletOmutu = (Switch) findViewById(R.id.editOmutuSwitch);
        toiletOmutuSelling = (Switch) findViewById(R.id.editOmutuSellingSwitch);
        toiletNapkinSelling = (Switch) findViewById(R.id.editNapkinSellingSwitch);
        toiletMilk = (Switch) findViewById(R.id.editMilkSwitch);
        toiletMakeroom = (Switch) findViewById(R.id.editMakeSwitch);
        toiletClothes = (Switch) findViewById(R.id.editClothesSwitch);
        toiletBaggage = (Switch) findViewById(R.id.editBaggageSwitch);

        toiletWheelchair = (Switch) findViewById(R.id.editWheelchairSwitch);
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

    }

    private void othersReady(){
        textToiletName = (EditText) findViewById(R.id.writeToiletName);
        textHowToAccess = (EditText) findViewById(R.id.inputHowToAccess);
        textFeedback = (EditText) findViewById(R.id.kansou);

        textFeedback.setHint("トイレがとても綺麗でした。ありがとうございます。");
        textFeedback.setMaxLines(Integer.MAX_VALUE);
        textFeedback.setHorizontallyScrolling(false);






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

    private void toileGetData(final String queryKey) {

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");

        toiletRef.child(queryKey).addValueEventListener(new ValueEventListener() {
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


                    toilet.key = queryKey;
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
                    toiletOmutu.setChecked(toilet.omutu);
                    toiletOmutuSelling.setChecked(toilet.omutuSelling);
                    toiletNapkinSelling.setChecked(toilet.napkinSelling);
                    toiletMilk.setChecked(toilet.milkspace);
                    toiletMakeroom.setChecked(toilet.makeuproom);
                    toiletClothes.setChecked(toilet.clothes);
                    toiletBaggage.setChecked(toilet.baggageSpace);


                    toiletWheelchair.setChecked(toilet.wheelchair);
                    toiletHandrail.setChecked(toilet.handrail);
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

                    LatLng toiletLatLng = new LatLng(toilet.latitude, toilet.longitude);


                    mMap.addMarker(new MarkerOptions().position(toiletLatLng).title("Toilet Location"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(toiletLatLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toiletLatLng, 14.0f));




                } else {
                    //When you could not get the last known location...

                }
            }
        }
    }


}
