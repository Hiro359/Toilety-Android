package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class EditViewListActivity extends AppCompatActivity {



    Spinner typeSpinner;
    //Spinner waitingTimeSpinner;
    Spinner floorSpinner;
    Spinner startHoursSpinner;
    Spinner startMinutesSpinner;
    Spinner endHoursSpinner;
    Spinner endMinutesSpinner;

    Integer openData = 0;
    Integer endData = 2400;



    EditText textToiletName;
    EditText textHowToAccess;
    //EditText textFeedback;

    //RatingBar ratingBar;
    ImageView mainImage;
    ImageView subImage1;
    ImageView subImage2;

    Button addPhoto;
    Button buttonRenewInfo;

    //private GoogleApiClient client;

    Boolean typeSpinnerLoaded = false;
    Boolean startHourSpinnerLoaded = false;
    Boolean startMinutesSpinnerLoaded = false;
    Boolean closeHourSpinnerLoaded = false;
    Boolean closeMinutesSpinnerLoaded = false;
    Boolean floorSpinnerLoaded = false;


    Boolean typeSpinnerSelected = false;
    Boolean startHourSpinnerSelected = false;
    Boolean startMinutesSpinnerSelected = false;
    Boolean closeHourSpinnerSelected = false;
    Boolean closeMinutesSpinnerSelected = false;
    Boolean floorSpinnerSelected = false;



    Integer photoSelected = 0;

//    private Toolbar toolbar;
    //private TextView toolbarTitle;
    private DatabaseReference toiletRef;
//    private DatabaseReference toiletLocationRef;

    Toilet toilet =  new Toilet();
//    DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("ToiletLocations");
    //GeoFire geoFire = new GeoFire(locationRef);

    ArrayAdapter<CharSequence> adapterType;
    ArrayAdapter<CharSequence> adapterWaitingtime;
    ArrayAdapter<CharSequence> adapterFloor;
    ArrayAdapter<CharSequence> adapterStartHours;
    ArrayAdapter<CharSequence> adapterStartMinutes;
    ArrayAdapter<CharSequence> adapterEndHours;
    ArrayAdapter<CharSequence> adapterEndMinutes;

    private String urlOne = "";
    private String urlTwo = "";
    private String urlThree = "";

    SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();
//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;
//    private AddBooleansListAdapter adapter;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("images");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view_list);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.edit_app_bar);
        //toolbarTitle = (TextView) toolbar.findViewById(R.id.editAppBarTitle);

        //switchesReady();
        layoutReady();
        othersReady();


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        final String originalkey = getIntent().getStringExtra("EXTRA_SESSION_ID");
        toilet.latitude = getIntent().getDoubleExtra("toiletLatitude",0);
        toilet.longitude = getIntent().getDoubleExtra("toiletLongitude",0);


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
    }

    private void layoutReady(){


        mainImage = (ImageView) findViewById(R.id.picture1);
        subImage1 = (ImageView) findViewById(R.id.picture2);
        subImage2 = (ImageView) findViewById(R.id.picture3);
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


    private void othersReady(){


        textToiletName = (EditText) findViewById(R.id.writeToiletName);
        textHowToAccess = (EditText) findViewById(R.id.inputHowToAccess);
        //textFeedback = (EditText) findViewById(R.id.kansou);

//        textFeedback.setHint("トイレがとても綺麗でした。ありがとうございます。");
//        textFeedback.setMaxLines(Integer.MAX_VALUE);
//        textFeedback.setHorizontallyScrolling(false);

//        ratingBar = (RatingBar) findViewById(R.id.editRating);
//        ratingBar.setRating(3);
        addPhoto = (Button) findViewById(R.id.buttonEditPicture);
        buttonRenewInfo = (Button) findViewById(R.id.buttonEditInfo);
        //buttonchangePinLocation = (Button) findViewById(R.id.buttonEditPinMap);

        mainImage = (ImageView) findViewById(R.id.picture1);
//         ImageView subImage1 = (ImageView) findViewById(R.id.picture2);
//         ImageView subImage2 = (ImageView) findViewById(R.id.picture3);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndAddPhoto();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

//                mainImage = (ImageView) findViewById(R.id.picture1);
//                subImage1 = (ImageView) findViewById(R.id.picture2);
//                subImage2 = (ImageView) findViewById(R.id.picture3);

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

    private void toiletNameCheck(){
        String tName = textToiletName.getText().toString();

        if(TextUtils.isEmpty(tName)) {

            textToiletName.setError("Your message");
            Log.i("HEy", "00");
        } else {
            Log.i("Valid", "00");
            //there is a valid name

            //firebaseUpdate();
        }
    }


    private void imageSetPlaceChoose(){
        //final Integer imageNum = 0;
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
        //waitingTimeSpinner = (Spinner) findViewById(R.id.spinnerWaitingTime);
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
        //waitingTimeSpinner.setAdapter(adapterWaitingtime);
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
                                                          ((TextView) parent.getChildAt(0)).setText(String.valueOf(toilet.type));
                                                          Log.i("TypeCalled","111");
                                                          typeSpinnerLoaded = true;
                                                      } else {
                                                          typeSpinnerSelected = true;
                                                      }

                                                  }
                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {
                                                  }
                                              }
        );

//       // waitingTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                                                         @Override
//                                                         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                                             ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
//                                                             ((TextView) parent.getChildAt(0)).setTextSize(16);
//
//                                                             ((TextView) parent.getChildAt(0)).setText("待ち時間  " + parent.getItemAtPosition(position) + "分");
//
//
//
//                                                         }
//                                                         @Override
//                                                         public void onNothingSelected(AdapterView<?> parent) {
//                                                         }
//                                                     }
//
//        );

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                   @Override
                                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                       ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                                       ((TextView) parent.getChildAt(0)).setTextSize(16);
//                                                            ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "以上を検索");
                                                       if (!floorSpinnerLoaded){
                                                           ((TextView) parent.getChildAt(0)).setText(String.valueOf(toilet.floor));
                                                           floorSpinnerLoaded = true;
                                                       } else {
                                                           floorSpinnerSelected = true;

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
                                                            if (!startHourSpinnerLoaded) {
                                                                //This part is loaded only for the first time

                                                                String selected = parent.getItemAtPosition(position).toString();

                                                                ((TextView) parent.getChildAt(0)).setText(String.valueOf(startHours));
                                                                startHourSpinnerLoaded = true;
                                                                Log.i("User", "Initial NOT Selected88888");
                                                                Log.i("User", "Initial NOT Selected88888" + String.valueOf(selected));
                                                            } else {
                                                                startHourSpinnerSelected = true;
                                                                String selected = parent.getItemAtPosition(position).toString();
                                                                Log.i("User", "Selected88888");
                                                                Log.i("User", "Selected88888" + String.valueOf(selected));

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

                                                              Log.i("StartMiNutes 88888", "FUCK");

                                                              if (!startMinutesSpinnerLoaded) {
                                                                  startMinutesSpinnerLoaded = true;
                                                                  if (startMinutes != 0) {
                                                                      ((TextView) parent.getChildAt(0)).setText(String.valueOf(startMinutes));
                                                                  } else {
                                                                      String selected = parent.getItemAtPosition(0).toString();
                                                                      ((TextView) parent.getChildAt(0)).setText(selected);
                                                                  }
                                                              } else {
                                                                  startMinutesSpinnerSelected = true;
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
                                                          } else {

                                                              closeHourSpinnerSelected = true;

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
                                                                closeMinutesSpinnerLoaded = true;
                                                                if (endMinutes != 0) {

                                                                    ((TextView) parent.getChildAt(0)).setText(String.valueOf(endMinutes));
                                                                } else {
                                                                    String selected = parent.getItemAtPosition(0).toString();
                                                                    ((TextView) parent.getChildAt(0)).setText(selected);

                                                                }
                                                            } else {
                                                                closeMinutesSpinnerSelected = true;

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

        toiletRef = FirebaseDatabase.getInstance().getReference().child("ToiletView");

        //Changed to single June 1
        toiletRef.child(originalKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("OnDataChangeCalled", "777");
                // for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    Log.i("OnDataChangeCalled", "777888");
                    //Boolean removedToilet = false;

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
                    Long typeLong = (Long) dataSnapshot.child("type").getValue();
                    toilet.type = typeLong.intValue();

                    toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                    toilet.urlTwo = (String) dataSnapshot.child("urlTwo").getValue();
                    toilet.urlThree = (String) dataSnapshot.child("urlThree").getValue();


                    toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
                    toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();



                    urlOne = toilet.urlOne;
                    urlTwo = toilet.urlTwo;
                    urlThree = toilet.urlThree;

                    Log.i("BOOOL???", "2");

                    toilet.addedBy = (String) dataSnapshot.child("addedBy").getValue();
                    toilet.editedBy = (String) dataSnapshot.child("editedBy").getValue();
                    toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();
                    toilet.address = (String) dataSnapshot.child("address").getValue();
                    toilet.howtoaccess = (String) dataSnapshot.child("howtoaccess").getValue();

                    toilet.reviewOne = (String) dataSnapshot.child("reviewOne").getValue();
                    toilet.reviewTwo = (String) dataSnapshot.child("reviewTwo").getValue();

                    Log.i("BOOOL???", "3");

                    Long openh = (Long) dataSnapshot.child("openHours").getValue();
                    toilet.openHours = openh.intValue();
                    openData = toilet.openHours;
                    Long closeh = (Long) dataSnapshot.child("closeHours").getValue();
                    toilet.closeHours = closeh.intValue();
                    endData = toilet.closeHours;


                    Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                    toilet.reviewCount = reviewCount.intValue();
                    Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                    toilet.averageWait = averageWait.intValue();
                    Long toiletFloor = (Long) dataSnapshot.child("toiletFloor").getValue();
                    toilet.floor = toiletFloor.intValue();

                    Log.i("BOOOL???", "4");


                    //Copied from Detail View


                    toilet.available = (Boolean) dataSnapshot.child("available").getValue();
                    toilet.japanesetoilet = (Boolean) dataSnapshot.child("japanesetoilet").getValue();
                    toilet.westerntoilet = (Boolean) dataSnapshot.child("westerntoilet").getValue();
                    toilet.onlyFemale = (Boolean) dataSnapshot.child("onlyFemale").getValue();
                    toilet.unisex = (Boolean) dataSnapshot.child("unisex").getValue();

                    AddDetailBooleans.japanesetoilet = toilet.japanesetoilet;
                    AddDetailBooleans.westerntoilet = toilet.westerntoilet;
                    AddDetailBooleans.onlyFemale = toilet.onlyFemale;
                    AddDetailBooleans.unisex = toilet.unisex;

                    Log.i("BOOOL???","5");

                    toilet.washlet = (Boolean) dataSnapshot.child("washlet").getValue();
                    toilet.warmSeat = (Boolean) dataSnapshot.child("warmSeat").getValue();
                    toilet.autoOpen = (Boolean) dataSnapshot.child("autoOpen").getValue();
                    toilet.noVirus = (Boolean) dataSnapshot.child("noVirus").getValue();
                    toilet.paperForBenki = (Boolean) dataSnapshot.child("paperForBenki").getValue();
                    toilet.cleanerForBenki = (Boolean) dataSnapshot.child("cleanerForBenki").getValue();
                    toilet.autoToiletWash = (Boolean) dataSnapshot.child("nonTouchWash").getValue();

                        AddDetailBooleans.washlet = toilet.washlet;
                        AddDetailBooleans.warmSeat = toilet.warmSeat;
                        AddDetailBooleans.autoOpen = toilet.autoOpen;
                        AddDetailBooleans.noVirus = toilet.noVirus;
                        AddDetailBooleans.paperForBenki = toilet.paperForBenki;
                        AddDetailBooleans.cleanerForBenki = toilet.cleanerForBenki;
                        AddDetailBooleans.autoToiletWash = toilet.autoToiletWash;





                    Log.i("BOOOL???","6");

                    Log.i("Passed Boolean","1");

                    toilet.sensorHandWash = (Boolean) dataSnapshot.child("sensorHandWash").getValue();
                    toilet.handSoap = (Boolean) dataSnapshot.child("handSoap").getValue();
                    toilet.autoHandSoap = (Boolean) dataSnapshot.child("nonTouchHandSoap").getValue();
                    toilet.paperTowel = (Boolean) dataSnapshot.child("paperTowel").getValue();
                    toilet.handDrier = (Boolean) dataSnapshot.child("handDrier").getValue();

                    AddDetailBooleans.sensorHandWash = toilet.sensorHandWash;
                    AddDetailBooleans.handSoap = toilet.handSoap;
                    AddDetailBooleans.autoHandSoap = toilet.autoHandSoap;
                    AddDetailBooleans.paperTowel = toilet.paperTowel;
                    AddDetailBooleans.handDrier = toilet.handDrier;





                    Log.i("Passed Boolean","2");
                    //From Maps Activity
                    //others one

                    toilet.fancy = (Boolean) dataSnapshot.child("fancy").getValue();
                    toilet.smell = (Boolean) dataSnapshot.child("smell").getValue();
                    toilet.conforatableWide = (Boolean) dataSnapshot.child("confortable").getValue();
                    toilet.clothes = (Boolean) dataSnapshot.child("clothes").getValue();
                    toilet.baggageSpace = (Boolean) dataSnapshot.child("baggageSpace").getValue();
                    AddDetailBooleans.fancy = toilet.fancy;
                    AddDetailBooleans.smell = toilet.smell;
                    AddDetailBooleans.conforatableWide = toilet.conforatableWide;
                    AddDetailBooleans.clothes = toilet.clothes;
                    AddDetailBooleans.baggageSpace = toilet.baggageSpace;



                    Log.i("Passed Boolean","3");

                    //others two
                    toilet.noNeedAsk = (Boolean) dataSnapshot.child("noNeedAsk").getValue();
                    toilet.english = (Boolean) dataSnapshot.child("english").getValue();
                    toilet.parking = (Boolean) dataSnapshot.child("parking").getValue();
                    toilet.airCondition = (Boolean) dataSnapshot.child("airCondition").getValue();
                    toilet.wifi = (Boolean) dataSnapshot.child("wifi").getValue();

                    AddDetailBooleans.noNeedAsk = toilet.noNeedAsk;
                    AddDetailBooleans.english = toilet.english;
                    AddDetailBooleans.parking = toilet.parking;
                    AddDetailBooleans.airCondition = toilet.airCondition;
                    AddDetailBooleans.wifi = toilet.wifi;


                    Log.i("Passed Boolean","4");
                    //for ladys

                    toilet.otohime = (Boolean) dataSnapshot.child("otohime").getValue();
                    toilet.napkinSelling = (Boolean) dataSnapshot.child("napkinSelling").getValue();
                    toilet.makeuproom = (Boolean) dataSnapshot.child("makeuproom").getValue();
                    toilet.ladyOmutu = (Boolean) dataSnapshot.child("ladyOmutu").getValue();
                    toilet.ladyBabyChair = (Boolean) dataSnapshot.child("ladyBabyChair").getValue();
                    toilet.ladyBabyChairGood = (Boolean) dataSnapshot.child("ladyBabyChairGood").getValue();
                    toilet.ladyBabyCarAccess = (Boolean) dataSnapshot.child("ladyBabyCarAccess").getValue();


                    AddDetailBooleans.otohime = toilet.otohime;
                    AddDetailBooleans.napkinSelling = toilet.napkinSelling;
                    AddDetailBooleans.makeuproom = toilet.makeuproom;
                    AddDetailBooleans.ladyOmutu = toilet.ladyOmutu;
                    AddDetailBooleans.ladyBabyChair = toilet.ladyBabyChair;
                    AddDetailBooleans.ladyBabyChairGood = toilet.ladyBabyChairGood;
                    AddDetailBooleans.ladyBabyCarAccess = toilet.ladyBabyCarAccess;

                    //for Mans
                    toilet.maleOmutu = (Boolean) dataSnapshot.child("maleOmutu").getValue();
                    toilet.maleBabyChair = (Boolean) dataSnapshot.child("maleBabyChair").getValue();
                    toilet.maleBabyChairGood = (Boolean) dataSnapshot.child("maleBabyChairGood").getValue();
                    toilet.maleBabyCarAccess = (Boolean) dataSnapshot.child("maleBabyCarAccess").getValue();

                    AddDetailBooleans.maleOmutu = toilet.maleOmutu;
                    AddDetailBooleans.maleBabyChair = toilet.maleBabyChair;
                    AddDetailBooleans.maleBabyChairGood = toilet.maleBabyChairGood;
                    AddDetailBooleans.maleBabyCarAccess = toilet.maleBabyCarAccess;
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

                    AddDetailBooleans.wheelchair = toilet.wheelchair;
                    AddDetailBooleans.wheelchairAccess = toilet.wheelchairAccess;
                    AddDetailBooleans.autoDoor= toilet.autoDoor;
                    AddDetailBooleans.callHelp = toilet.callHelp;
                    AddDetailBooleans.ostomate = toilet.ostomate;
                    AddDetailBooleans.braille = toilet.braille;
                    AddDetailBooleans.voiceGuide = toilet.voiceGuide;
                    AddDetailBooleans.familyOmutu = toilet.familyOmutu;
                    AddDetailBooleans.familyBabyChair = toilet.familyBabyChair;
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

                    AddDetailBooleans.milkspace = toilet.milkspace;
                    AddDetailBooleans.babyroomOnlyFemale = toilet.onlyFemale;
                    AddDetailBooleans.babyroomManCanEnter = toilet.babyroomManCanEnter;
                    AddDetailBooleans.babyPersonalSpace = toilet.babyPersonalSpace;
                    AddDetailBooleans.babyPersonalSpaceWithLock = toilet.babyPersonalSpaceWithLock;
                    AddDetailBooleans.babyRoomWideSpace = toilet.babyRoomWideSpace;


                    toilet.babyCarRental = (Boolean) dataSnapshot.child("babyCarRental").getValue();
                    toilet.babyCarAccess = (Boolean) dataSnapshot.child("babyCarAccess").getValue();
                    toilet.omutu = (Boolean) dataSnapshot.child("omutu").getValue();
                    toilet.hipWashingStuff = (Boolean) dataSnapshot.child("hipCleaningStuff").getValue();
                    toilet.babyTrashCan = (Boolean) dataSnapshot.child("omutuTrashCan").getValue();
                    toilet.omutuSelling = (Boolean) dataSnapshot.child("omutuSelling").getValue();

                    AddDetailBooleans.babyCarRental = toilet.babyCarRental;
                    AddDetailBooleans.babyCarAccess = toilet.babyCarAccess;
                    AddDetailBooleans.omutu = toilet.omutu;
                    AddDetailBooleans.hipWashingStuff = toilet.hipWashingStuff;
                    AddDetailBooleans.babyTrashCan = toilet.babyTrashCan;
                    AddDetailBooleans.omutuSelling = toilet.omutuSelling;


                    toilet.babyRoomSink = (Boolean) dataSnapshot.child("babySink").getValue();
                    toilet.babyWashStand = (Boolean) dataSnapshot.child("babyWashstand").getValue();
                    toilet.babyHotWater = (Boolean) dataSnapshot.child("babyHotwater").getValue();
                    toilet.babyMicroWave = (Boolean) dataSnapshot.child("babyMicrowave").getValue();
                    toilet.babyWaterSelling = (Boolean) dataSnapshot.child("babyWaterSelling").getValue();
                    toilet.babyFoddSelling = (Boolean) dataSnapshot.child("babyFoodSelling").getValue();
                    toilet.babyEatingSpace = (Boolean) dataSnapshot.child("babyEatingSpace").getValue();

                    AddDetailBooleans.babyRoomSink = toilet.babyRoomSink;
                    AddDetailBooleans.babyWashStand = toilet.babyWashStand;
                    AddDetailBooleans.babyHotWater = toilet.babyHotWater;
                    AddDetailBooleans.babyMicroWave = toilet.babyMicroWave;
                    AddDetailBooleans.babyWaterSelling = toilet.babyWaterSelling;
                    AddDetailBooleans.babyFoddSelling = toilet.babyFoddSelling;
                    AddDetailBooleans.babyEatingSpace = toilet.babyEatingSpace;


                    toilet.babyChair = (Boolean) dataSnapshot.child("babyChair").getValue();
                    toilet.babySoffa = (Boolean) dataSnapshot.child("babySoffa").getValue();
                    toilet.babyKidsToilet = (Boolean) dataSnapshot.child("kidsToilet").getValue();
                    toilet.babyKidsSpace = (Boolean) dataSnapshot.child("kidsSpace").getValue();
                    toilet.babyHeightMeasure = (Boolean) dataSnapshot.child("babyHeight").getValue();
                    toilet.babyWeightMeasure = (Boolean) dataSnapshot.child("babyWeight").getValue();
                    toilet.babyToy = (Boolean) dataSnapshot.child("babyToy").getValue();
                    toilet.babyFancy = (Boolean) dataSnapshot.child("babyFancy").getValue();
                    toilet.babySmellGood = (Boolean) dataSnapshot.child("babySmellGood").getValue();

                    AddDetailBooleans.babyChair = toilet.babyChair;
                    AddDetailBooleans.babySoffa = toilet.babySoffa;
                    AddDetailBooleans.babyKidsToilet = toilet.babyKidsToilet;
                    AddDetailBooleans.babyKidsSpace = toilet.babyKidsSpace;
                    AddDetailBooleans.babyHeightMeasure = toilet.babyHeightMeasure;
                    AddDetailBooleans.babyWeightMeasure = toilet.babyWeightMeasure;
                    AddDetailBooleans.babyToy = toilet.babyToy;
                    AddDetailBooleans.babyFancy = toilet.babyFancy;
                    AddDetailBooleans.babySmellGood = toilet.babySmellGood;




                    textToiletName.setText(toilet.name);

                    //Float averaegeStarFloat = Float.parseFloat(toilet.averageStar);
                    sppinnerReady();
                    sparseArrayReady();
                    setIntialImage();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String TAG = "Error";
                Log.w(TAG, "DatabaseError", databaseError.toException());

            }
        });

    }


    private void setIntialImage(){


        Log.i("Set!!!","1");

        if (!toilet.urlOne.equals("")){
            Uri uri = Uri.parse(toilet.urlOne);
            Picasso.with(getApplicationContext()).load(uri).into(mainImage);

        } else {

            Log.i("SetMainImage","1");
            mainImage.setImageResource(R.drawable.default_photo_white_drawable);
            Log.i("SetMainImage","2");

        }

        if (!toilet.urlTwo.equals("")){
            Uri uri = Uri.parse(toilet.urlTwo);
            Picasso.with(getApplicationContext()).load(uri).into(subImage1);
        } else {
            Log.i("SetSubOneImage","1");
            subImage1.setImageResource(R.drawable.default_photo_white_drawable);
            Log.i("SetSubOnenImage","2");
        }

        if (!toilet.urlThree.equals("")){
            Uri uri = Uri.parse(toilet.urlThree);
            Picasso.with(getApplicationContext()).load(uri).into(subImage2);

        } else {
            Log.i("SetSubTwoImage","1");
            subImage2.setImageResource(R.drawable.default_photo_white_drawable);
            Log.i("SetSubTwoImage","2");
        }


    }



    private void sparseArrayReady() {


        filterSparseArray.append(0, new FilterBooleans("設備", false));

        filterSparseArray.append(1, new FilterBooleans("和式トイレ", toilet.japanesetoilet));
        filterSparseArray.append(2, new FilterBooleans("洋式トイレ", toilet.westerntoilet));
        filterSparseArray.append(3, new FilterBooleans("女性専用トイレ", toilet.onlyFemale));
        filterSparseArray.append(4, new FilterBooleans("男女兼用トイレ", toilet.unisex));


        filterSparseArray.append(5, new FilterBooleans("機能", false));
        filterSparseArray.append(6, new FilterBooleans("ウォシュレット",  toilet.washlet));
        filterSparseArray.append(7, new FilterBooleans("暖房便座",  toilet.warmSeat));
        filterSparseArray.append(8, new FilterBooleans("自動開閉便座",  toilet.autoOpen));
        filterSparseArray.append(9, new FilterBooleans("抗菌便座",  toilet.noVirus));
        filterSparseArray.append(10, new FilterBooleans("便座用シート",  toilet.paperForBenki));
        filterSparseArray.append(11, new FilterBooleans("便座クリーナー",  toilet.cleanerForBenki));
        filterSparseArray.append(12, new FilterBooleans("自動洗浄",  toilet.autoToiletWash));


        filterSparseArray.append(13, new FilterBooleans("洗面台設備", false));
        filterSparseArray.append(14, new FilterBooleans("センサー式お手洗い",  toilet.sensorHandWash));
        filterSparseArray.append(15, new FilterBooleans("ハンドソープ",  toilet.handSoap));
        filterSparseArray.append(16, new FilterBooleans("自動ハンドソープ",  toilet.autoHandSoap));
        filterSparseArray.append(17, new FilterBooleans("ペーパータオル",  toilet.paperTowel));
        filterSparseArray.append(18, new FilterBooleans("ハンドドライヤー",  toilet.handDrier));


        filterSparseArray.append(19, new FilterBooleans("1,その他", false));
        filterSparseArray.append(20, new FilterBooleans("おしゃれ",  toilet.fancy));
        filterSparseArray.append(21, new FilterBooleans("いい香り",  toilet.smell));
        filterSparseArray.append(22, new FilterBooleans("快適な広さ",  toilet.conforatableWide));
        filterSparseArray.append(23, new FilterBooleans("洋服掛け",  toilet.clothes));
        filterSparseArray.append(24, new FilterBooleans("荷物置き",  toilet.baggageSpace));

        filterSparseArray.append(25, new FilterBooleans("2,その他", false));
        filterSparseArray.append(26, new FilterBooleans("利用の際の声かけ不要", toilet.noNeedAsk));
        filterSparseArray.append(27, new FilterBooleans("英語表記",  toilet.english));
        filterSparseArray.append(28, new FilterBooleans("駐車場",  toilet.parking));
        filterSparseArray.append(29, new FilterBooleans("冷暖房",  toilet.airCondition));
        filterSparseArray.append(30, new FilterBooleans("無料Wi-Fi",  toilet.wifi));


        filterSparseArray.append(31, new FilterBooleans("女性トイレ", false));
        filterSparseArray.append(32, new FilterBooleans("音姫",  toilet.otohime));
        filterSparseArray.append(33, new FilterBooleans("ナプキン販売機",  toilet.napkinSelling));
        filterSparseArray.append(34, new FilterBooleans("パウダールーム",  toilet.makeuproom));
        filterSparseArray.append(35, new FilterBooleans("おむつ交換台",  toilet.ladyOmutu));
        filterSparseArray.append(36, new FilterBooleans("ベビーキープ",  toilet.ladyBabyChair));
        filterSparseArray.append(37, new FilterBooleans("安全なベビーキープ",  toilet.ladyBabyChairGood));
        filterSparseArray.append(38, new FilterBooleans("ベビーカーでのアクセス", toilet.ladyBabyCarAccess));


        filterSparseArray.append(39, new FilterBooleans("男性トイレ", false));
        filterSparseArray.append(40, new FilterBooleans("おむつ交換台",  toilet.maleOmutu));
        filterSparseArray.append(41, new FilterBooleans("ベビーキープ",  toilet.maleBabyChair));
        filterSparseArray.append(42, new FilterBooleans("安全なベビーキープ",  toilet.maleBabyChairGood));
        filterSparseArray.append(43, new FilterBooleans("ベビーカーでのアクセス",  toilet.maleBabyCarAccess));


        filterSparseArray.append(44, new FilterBooleans("多目的トイレ", false));

        filterSparseArray.append(45, new FilterBooleans("車イス対応",  toilet.wheelchair));
        filterSparseArray.append(46, new FilterBooleans("車イスでアクセス可能",  toilet.wheelchairAccess));
        filterSparseArray.append(47, new FilterBooleans("自動ドア",  toilet.autoDoor));
        filterSparseArray.append(48, new FilterBooleans("呼び出しボタン",  toilet.callHelp));
        filterSparseArray.append(49, new FilterBooleans("オストメイト", toilet.ostomate));
        filterSparseArray.append(50, new FilterBooleans("点字案内",  toilet.braille));
        filterSparseArray.append(51, new FilterBooleans("音声案内",  toilet.voiceGuide));
        filterSparseArray.append(52, new FilterBooleans("おむつ交換台",  toilet.familyOmutu));
        filterSparseArray.append(53, new FilterBooleans("ベビーチェア",  toilet.familyBabyChair));


        filterSparseArray.append(54, new FilterBooleans("1,ベビールームについて", false));
        filterSparseArray.append(55, new FilterBooleans("授乳スペース",  toilet.milkspace));
        filterSparseArray.append(56, new FilterBooleans("女性限定",  toilet.babyroomOnlyFemale));
        filterSparseArray.append(57, new FilterBooleans("男性入室可能",  toilet.babyroomManCanEnter));
        filterSparseArray.append(58, new FilterBooleans("個室あり",  toilet.babyPersonalSpace));
        filterSparseArray.append(59, new FilterBooleans("鍵付き個室あり",  toilet.babyPersonalSpaceWithLock));
        filterSparseArray.append(60, new FilterBooleans("広いスペース",  toilet.babyRoomWideSpace));


        filterSparseArray.append(61, new FilterBooleans("2,ベビールームについて", false));
        filterSparseArray.append(62, new FilterBooleans("ベビーカー貸出し",  toilet.babyCarRental));
        filterSparseArray.append(63, new FilterBooleans("ベビーカーでアクセス可能",  toilet.babyCarAccess));
        filterSparseArray.append(64, new FilterBooleans("おむつ交換台", toilet.omutu));
        filterSparseArray.append(65, new FilterBooleans("おしりふき",  toilet.hipWashingStuff));
        filterSparseArray.append(66, new FilterBooleans("おむつ用ゴミ箱",  toilet.babyTrashCan));
        filterSparseArray.append(67, new FilterBooleans("おむつ販売機",  toilet.omutuSelling));


        filterSparseArray.append(68, new FilterBooleans("3,ベビールームについて", false));
        filterSparseArray.append(69, new FilterBooleans("シンク",  toilet.babyRoomSink));
        filterSparseArray.append(70, new FilterBooleans("洗面台",  toilet.babyWashStand));
        filterSparseArray.append(71, new FilterBooleans("給湯器",  toilet.babyHotWater));
        filterSparseArray.append(72, new FilterBooleans("電子レンジ",  toilet.babyMicroWave));
        filterSparseArray.append(73, new FilterBooleans("飲料自販機",  toilet.babyWaterSelling));
        filterSparseArray.append(74, new FilterBooleans("離乳食販売機",  toilet.babyFoddSelling));
        filterSparseArray.append(75, new FilterBooleans("飲食スペース",  toilet.babyEatingSpace));


        filterSparseArray.append(76, new FilterBooleans("4,ベビールームについて", false));
        filterSparseArray.append(77, new FilterBooleans("ベビーチェア",  toilet.babyChair));
        filterSparseArray.append(78, new FilterBooleans("ソファ", toilet.babySoffa));
        filterSparseArray.append(79, new FilterBooleans("キッズトイレ",  toilet.babyKidsToilet));
        filterSparseArray.append(80, new FilterBooleans("キッズスペース",  toilet.babyKidsSpace));
        filterSparseArray.append(81, new FilterBooleans("身長計",  toilet.babyHeightMeasure));
        filterSparseArray.append(82, new FilterBooleans("体重計",  toilet.babyWeightMeasure));
        filterSparseArray.append(83, new FilterBooleans("おもちゃ",  toilet.babyToy));
        filterSparseArray.append(84, new FilterBooleans("おしゃれ",  toilet.babyFancy));
        filterSparseArray.append(85, new FilterBooleans("いい香り",  toilet.babySmellGood));


        createRecyclerView(filterSparseArray);


    }


    @SuppressWarnings("unchecked")
    private void createRecyclerView(SparseArray array) {
        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        AddBooleansListAdapter adapter;
        Log.i("reviewRecycle", "Called");
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

    @SuppressWarnings("unchecked")
    private void firebaseRenewdata(){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            Integer updateStHour;
            Integer updateStMinute;
            Integer updateEndHour;
            Integer updateEndMinute;
            Integer updateFloor;
            Integer updateType;


            if (startHourSpinnerSelected) {
                updateStHour = Integer.parseInt(String.valueOf(startHoursSpinner.getSelectedItem()));
            } else {
                updateStHour = toilet.openHours / 100;
            }

            if (startMinutesSpinnerSelected) {
                updateStMinute = Integer.parseInt(String.valueOf(startMinutesSpinner.getSelectedItem()));
            } else {
                updateStMinute = toilet.openHours % 100;
            }

            if (closeHourSpinnerSelected) {
                updateEndHour = Integer.parseInt(String.valueOf(endHoursSpinner.getSelectedItem()));
            } else {
                updateEndHour = toilet.closeHours / 100;
            }

            if (closeMinutesSpinnerSelected) {
                updateEndMinute = Integer.parseInt(String.valueOf(endMinutesSpinner.getSelectedItem()));
            } else {
                updateEndMinute = toilet.closeHours % 100;
            }

            if (floorSpinnerSelected) {
                Log.i("SelectedPosition88888", String.valueOf(floorSpinner.getSelectedItemPosition()));


                //Log.i("SelectedPosition88888", floorSpinner.getSelectedItem().toString());
                // updateFloor = Integer.parseInt(floorSpinner.getSelectedItem().toString());


                //It cannot be integet because it is like "一階"　


            } else {
                Log.i("SelectedPosition88888", String.valueOf(floorSpinner.getSelectedItemPosition()));

                // Log.i("SelectedPosition88888", floorSpinner.getSelectedItem().toString());
                //updateFloor = toilet.floor;
            }


            if (typeSpinnerSelected) {
                updateType = typeSpinner.getSelectedItemPosition();
            } else {
                updateType = toilet.type;
            }


            Integer openTime = updateStHour * 100 + updateStMinute;
            Integer endTime = updateEndHour * 100 + updateEndMinute;

            String updateStMinuteString;
            String updateEndMinuteString;

            if (updateStMinute == 0) {
                updateStMinuteString = "00";
            } else {
                updateStMinuteString = String.valueOf(updateStMinute);
            }

            if (updateEndMinute == 0) {
                updateEndMinuteString = "00";
            } else {
                updateEndMinuteString = String.valueOf(updateStMinute);
            }


            String openingString = String.valueOf(updateStHour) + ":" + updateStMinuteString + "〜" + String.valueOf(updateEndHour) + ":" + updateEndMinuteString;


            String tName = textToiletName.getText().toString();


            toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
//            DatabaseReference toiletLocationRef;
//            toiletLocationRef = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");


            // geolocationUpdate(toilet.key);


            DatabaseReference updateToiletRef = toiletRef.child(toilet.key);


            //String firekey = updateRef.getKey();

            //delete original data in toilets brunch
            //delete original data in toiletLocations brunch

            Log.i("datbaseUpdateLat", String.valueOf(toilet));
            Log.i("datbaseUpdateLon", String.valueOf(toilet.longitude));
            //Log.i("datbaseUpdateAVSTAR", String.valueOf(avStar));


//            ////From AddDetailView
//
//            Map<String, Object> noFilterData = new HashMap();
//
//            noFilterData.put("name",tName);
//            noFilterData.put("type",typeSpinner.getSelectedItemPosition());
//            noFilterData.put("urlOne",urlOne);
//            noFilterData.put("averageStar",toilet.averageStar);
//            noFilterData.put("reviewCount",1);
//            noFilterData.put("available",true);
//            noFilterData.put("averageWait",toilet.averageWait);
//            noFilterData.put("toiletFloor",toilet.floor);
//
//            //

            ////From AddDetailView
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


//            reviewInfoRef.child(newRid).setValue(newPost);
//
//            reviewListRef.child(uid).child(newRid).setValue(true);
//
//            toiletReviewsRef.child(newTid).child(newRid).setValue(true);
//.



            Map<String, Object> noFilterData = new HashMap();
//
            noFilterData.put("name",tName);
            noFilterData.put("type",typeSpinner.getSelectedItemPosition());
            noFilterData.put("urlOne",toilet.urlOne);
            noFilterData.put("averageStar",toilet.averageStar);
            noFilterData.put("reviewCount",1);
            noFilterData.put("available",true);
            noFilterData.put("averageWait",toilet.averageWait);
            noFilterData.put("toiletFloor",toilet.floor);
            noFilterData.put("latitude",toilet.latitude);
            noFilterData.put("longitude",toilet.longitude);

            Log.i("URL 999", "888");


//            Map<String, Object> toiletUserList = new HashMap();
//            toiletUserList.put("name",tName);
//            toiletUserList.put("type",typeSpinner.getSelectedItemPosition());
//            toiletUserList.put("urlOne",urlOne);
//            toiletUserList.put("averageStar",toilet.averageStar);
//            toiletUserList.put("reviewCount",1);
//            toiletUserList.put("available",true);
//            toiletUserList.put("averageWait",toilet.averageWait);
//            toiletUserList.put("toiletFloor",toilet.floor);
//            toiletUserList.put("latitude",toilet.latitude);
//            toiletUserList.put("longitude",toilet.longitude);
//            toiletViewData.put("latitude",toilet.latitude);
//            toiletViewData.put("longitude",toilet.longitude);

            Log.i("URL 999", "888 777");


            Map<String, Object> unitOneData = new HashMap();
            unitOneData.put("name",tName);
            unitOneData.put("type",typeSpinner.getSelectedItemPosition());
            unitOneData.put("urlOne",urlOne);
            unitOneData.put("averageStar",toilet.averageStar);
            unitOneData.put("reviewCount",1);
            unitOneData.put("available",true);
            unitOneData.put("averageWait",toilet.averageWait);
            unitOneData.put("toiletFloor",toilet.floor);
            unitOneData.put("openHours",openData);
            unitOneData.put("closeHours",endData);
            unitOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            unitOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            unitOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            unitOneData.put("unisex",AddDetailBooleans.unisex);
            unitOneData.put("latitude",toilet.latitude);
            unitOneData.put("longitude",toilet.longitude);



            Map<String, Object> unitTwoData = new HashMap();

            unitTwoData.put("name",tName);
            unitTwoData.put("type",typeSpinner.getSelectedItemPosition());
            unitTwoData.put("urlOne",urlOne);
            unitTwoData.put("averageStar",toilet.averageStar);
            unitTwoData.put("reviewCount",1);
            unitTwoData.put("available",true);
            unitTwoData.put("averageWait",toilet.averageWait);
            unitTwoData.put("toiletFloor",toilet.floor);
            unitTwoData.put("washlet",AddDetailBooleans.washlet);
            unitTwoData.put("warmSeat",AddDetailBooleans.warmSeat);
            unitTwoData.put("autoOpen",AddDetailBooleans.autoOpen);
            unitTwoData.put("noVirus",AddDetailBooleans.noVirus);
            unitTwoData.put("paperForBenki",AddDetailBooleans.paperForBenki);
            unitTwoData.put("cleanerForBenki",AddDetailBooleans.cleanerForBenki);
            unitTwoData.put("nonTouchWash",AddDetailBooleans.autoToiletWash);
            unitTwoData.put("latitude",toilet.latitude);
            unitTwoData.put("longitude",toilet.longitude);




            Map<String, Object> unitThreeData = new HashMap();
            unitThreeData.put("name",tName);
            unitThreeData.put("type",typeSpinner.getSelectedItemPosition());
            unitThreeData.put("urlOne",urlOne);
            unitThreeData.put("averageStar",toilet.averageStar);
            unitThreeData.put("reviewCount",1);
            unitThreeData.put("available",true);
            unitThreeData.put("averageWait",toilet.averageWait);
            unitThreeData.put("toiletFloor",toilet.floor);
            unitThreeData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
            unitThreeData.put("handSoap", AddDetailBooleans.handSoap);
            unitThreeData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
            unitThreeData.put("paperTowel",AddDetailBooleans.paperTowel);
            unitThreeData.put("handDrier",AddDetailBooleans.handDrier);
            unitThreeData.put("latitude",toilet.latitude);
            unitThreeData.put("longitude",toilet.longitude);


            Map<String, Object> unitFourData = new HashMap();
            unitFourData.put("name",tName);
            unitFourData.put("type",typeSpinner.getSelectedItemPosition());
            unitFourData.put("urlOne",urlOne);
            unitFourData.put("averageStar",toilet.averageStar);
            unitFourData.put("reviewCount",1);
            unitFourData.put("available",true);
            unitFourData.put("averageWait",toilet.averageWait);
            unitFourData.put("toiletFloor",toilet.floor);
            unitFourData.put("fancy", AddDetailBooleans.fancy);
            unitFourData.put("smell",AddDetailBooleans.smell);
            unitFourData.put("confortable",AddDetailBooleans.conforatableWide);
            unitFourData.put("clothes",AddDetailBooleans.clothes);
            unitFourData.put("baggageSpace",AddDetailBooleans.baggageSpace);
            unitFourData.put("latitude",toilet.latitude);
            unitFourData.put("longitude",toilet.longitude);



            Map<String, Object> unitFiveData = new HashMap();
            unitFiveData.put("name",tName);
            unitFiveData.put("type",typeSpinner.getSelectedItemPosition());
            unitFiveData.put("urlOne",urlOne);
            unitFiveData.put("averageStar",toilet.averageStar);
            unitFiveData.put("reviewCount",1);
            unitFiveData.put("available",true);
            unitFiveData.put("averageWait",toilet.averageWait);
            unitFiveData.put("toiletFloor",toilet.floor);
            unitFiveData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
            unitFiveData.put("english",AddDetailBooleans.english);
            unitFiveData.put("parking",AddDetailBooleans.parking);
            unitFiveData.put("airCondition",AddDetailBooleans.airCondition);
            unitFiveData.put("wifi",AddDetailBooleans.wifi);
            unitFiveData.put("latitude",toilet.latitude);
            unitFiveData.put("longitude",toilet.longitude);



            Map<String, Object> unitSixData = new HashMap();
            unitSixData.put("name",tName);
            unitSixData.put("type",typeSpinner.getSelectedItemPosition());
            unitSixData.put("urlOne",urlOne);
            unitSixData.put("averageStar",toilet.averageStar);
            unitSixData.put("reviewCount",1);
            unitSixData.put("available",true);
            unitSixData.put("averageWait",toilet.averageWait);
            unitSixData.put("toiletFloor",toilet.floor);
            unitSixData.put("otohime",AddDetailBooleans.otohime);
            unitSixData.put("napkinSelling",AddDetailBooleans.napkinSelling);
            unitSixData.put("makeuproom",AddDetailBooleans.makeuproom);
            unitSixData.put("ladyOmutu",AddDetailBooleans.ladyOmutu);
            unitSixData.put("ladyBabyChair",AddDetailBooleans.ladyBabyChair);
            unitSixData.put("ladyBabyChairGood",AddDetailBooleans.ladyBabyChairGood);
            unitSixData.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);
            unitSixData.put("latitude",toilet.latitude);
            unitSixData.put("longitude",toilet.longitude);


            Map<String, Object> unitSevenData = new HashMap();
            unitSevenData.put("name",tName);
            unitSevenData.put("type",typeSpinner.getSelectedItemPosition());
            unitSevenData.put("urlOne",urlOne);
            unitSevenData.put("averageStar",toilet.averageStar);
            unitSevenData.put("reviewCount",1);
            unitSevenData.put("available",true);
            unitSevenData.put("averageWait",toilet.averageWait);
            unitSevenData.put("toiletFloor",toilet.floor);
            unitSevenData.put("maleOmutu",AddDetailBooleans.maleOmutu);
            unitSevenData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
            unitSevenData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
            unitSevenData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);
            unitSevenData.put("latitude",toilet.latitude);
            unitSevenData.put("longitude",toilet.longitude);



            Map<String, Object> unitEightData = new HashMap();
            unitEightData.put("name",tName);
            unitEightData.put("type",typeSpinner.getSelectedItemPosition());
            unitEightData.put("urlOne",urlOne);
            unitEightData.put("averageStar",toilet.averageStar);
            unitEightData.put("reviewCount",1);
            unitEightData.put("available",true);
            unitEightData.put("averageWait",toilet.averageWait);
            unitEightData.put("toiletFloor",toilet.floor);
            unitEightData.put("wheelchair",AddDetailBooleans.wheelchair);
            unitEightData.put("wheelchairAccess",AddDetailBooleans.wheelchairAccess);
            unitEightData.put("autoDoor",AddDetailBooleans.autoDoor);
            unitEightData.put("callHelp",AddDetailBooleans.callHelp);
            unitEightData.put("ostomate",AddDetailBooleans.ostomate);
            unitEightData.put("braille",AddDetailBooleans.braille);
            unitEightData.put("voiceGuide",AddDetailBooleans.voiceGuide);
            unitEightData.put("familyOmutu",AddDetailBooleans.familyOmutu);
            unitEightData.put("familyBabyChair",AddDetailBooleans.familyBabyChair);
            unitEightData.put("latitude",toilet.latitude);
            unitEightData.put("longitude",toilet.longitude);



            Map<String, Object> unitNineData = new HashMap();
            unitNineData.put("name",tName);
            unitNineData.put("type",typeSpinner.getSelectedItemPosition());
            unitNineData.put("urlOne",urlOne);
            unitNineData.put("averageStar",toilet.averageStar);
            unitNineData.put("reviewCount",1);
            unitNineData.put("available",true);
            unitNineData.put("averageWait",toilet.averageWait);
            unitNineData.put("toiletFloor",toilet.floor);
            unitNineData.put("milkspace",AddDetailBooleans.milkspace);
            unitNineData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            unitNineData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            unitNineData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            unitNineData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            unitNineData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);
            unitNineData.put("latitude",toilet.latitude);
            unitNineData.put("longitude",toilet.longitude);



            Map<String, Object> unitTenData = new HashMap();
            unitTenData.put("name",tName);
            unitTenData.put("type",typeSpinner.getSelectedItemPosition());
            unitTenData.put("urlOne",urlOne);
            unitTenData.put("averageStar",toilet.averageStar);
            unitTenData.put("reviewCount",1);
            unitTenData.put("available",true);
            unitTenData.put("averageWait",toilet.averageWait);
            unitTenData.put("toiletFloor",toilet.floor);
            unitTenData.put("babyCarRental",AddDetailBooleans.babyCarRental);
            unitTenData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
            unitTenData.put("omutu",AddDetailBooleans.omutu);
            unitTenData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
            unitTenData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
            unitTenData.put("omutuSelling",AddDetailBooleans.omutuSelling);
            unitTenData.put("latitude",toilet.latitude);
            unitTenData.put("longitude",toilet.longitude);

            Map<String, Object> unitElevenData = new HashMap();
            unitElevenData.put("name",tName);
            unitElevenData.put("type",typeSpinner.getSelectedItemPosition());
            unitElevenData.put("urlOne",urlOne);
            unitElevenData.put("averageStar",toilet.averageStar);
            unitElevenData.put("reviewCount",1);
            unitElevenData.put("available",true);
            unitElevenData.put("averageWait",toilet.averageWait);
            unitElevenData.put("toiletFloor",toilet.floor);
            unitElevenData.put("babySink",AddDetailBooleans.babyRoomSink);
            unitElevenData.put("babyWashstand",AddDetailBooleans.babyWashStand);
            unitElevenData.put("babyHotwater",AddDetailBooleans.babyHotWater);
            unitElevenData.put("babyMicrowave",AddDetailBooleans.babyMicroWave);
            unitElevenData.put("babyWaterSelling",AddDetailBooleans.babyWaterSelling);
            unitElevenData.put("babyFoodSelling",AddDetailBooleans.babyFoddSelling);
            unitElevenData.put("babyEatingSpace",AddDetailBooleans.babyEatingSpace);
            unitElevenData.put("latitude",toilet.latitude);
            unitElevenData.put("longitude",toilet.longitude);



            Map<String, Object> unitTwelveData = new HashMap();
            unitTwelveData.put("name",tName);
            unitTwelveData.put("type",typeSpinner.getSelectedItemPosition());
            unitTwelveData.put("urlOne",urlOne);
            unitTwelveData.put("averageStar",toilet.averageStar);
            unitTwelveData.put("reviewCount",1);
            unitTwelveData.put("available",true);
            unitTwelveData.put("averageWait",toilet.averageWait);
            unitTwelveData.put("toiletFloor",toilet.floor);
            unitTwelveData.put("babyChair",AddDetailBooleans.babyChair);
            unitTwelveData.put("babySoffa",AddDetailBooleans.babySoffa);
            unitTwelveData.put("kidsToilet",AddDetailBooleans.babyKidsToilet);
            unitTwelveData.put("kidsSpace",AddDetailBooleans.babyKidsSpace);
            unitTwelveData.put("babyHeight",AddDetailBooleans.babyHeightMeasure);
            unitTwelveData.put("babyWeight",AddDetailBooleans.babyWeightMeasure);
            unitTwelveData.put("babyToy",AddDetailBooleans.babyToy);
            unitTwelveData.put("babyFancy",AddDetailBooleans.babyFancy);
            unitTwelveData.put("babySmellGood",AddDetailBooleans.babySmellGood);
            unitTwelveData.put("latitude",toilet.latitude);
            unitTwelveData.put("longitude",toilet.longitude);


            Map<String, Object> groupOneData = new HashMap();
            groupOneData.put("name",tName);
            groupOneData.put("type",typeSpinner.getSelectedItemPosition());
            groupOneData.put("urlOne",urlOne);
            groupOneData.put("averageStar",toilet.averageStar);
            groupOneData.put("reviewCount",1);
            groupOneData.put("available",true);
            groupOneData.put("averageWait",toilet.averageWait);
            groupOneData.put("toiletFloor",toilet.floor);
            groupOneData.put("openHours",openData);
            groupOneData.put("closeHours",endData);
            groupOneData.put("latitude",toilet.latitude);
            groupOneData.put("longitude",toilet.longitude);


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
            groupTwoData.put("averageStar",toilet.averageStar);
            groupTwoData.put("reviewCount",1);
            groupTwoData.put("available",true);
            groupTwoData.put("averageWait",toilet.averageWait);
            groupTwoData.put("toiletFloor",toilet.floor);
            groupTwoData.put("latitude",toilet.latitude);
            groupTwoData.put("longitude",toilet.longitude);

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
            groupThreeData.put("averageStar",toilet.averageStar);
            groupThreeData.put("reviewCount",1);
            groupThreeData.put("available",true);
            groupThreeData.put("averageWait",toilet.averageWait);
            groupThreeData.put("toiletFloor",toilet.floor);
            groupThreeData.put("milkspace",AddDetailBooleans.milkspace);
            groupThreeData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
            groupThreeData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
            groupThreeData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
            groupThreeData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
            groupThreeData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);
            groupThreeData.put("latitude",toilet.latitude);
            groupThreeData.put("longitude",toilet.longitude);


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
            halfOneData.put("averageStar",toilet.averageStar);
            halfOneData.put("reviewCount",1);
            halfOneData.put("available",true);
            halfOneData.put("averageWait",toilet.averageWait);
            halfOneData.put("toiletFloor",toilet.floor);
            halfOneData.put("openHours",openData);
            halfOneData.put("closeHours",endData);
            halfOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            halfOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            halfOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            halfOneData.put("unisex",AddDetailBooleans.unisex);
            halfOneData.put("latitude",toilet.latitude);
            halfOneData.put("longitude",toilet.longitude);

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
            halfTwoData.put("averageStar",toilet.averageStar);
            halfTwoData.put("reviewCount",1);
            halfTwoData.put("available",true);
            halfTwoData.put("averageWait",toilet.averageStar);
            halfTwoData.put("toiletFloor",toilet.floor);
            halfTwoData.put("latitude",toilet.latitude);
            halfTwoData.put("longitude",toilet.longitude);

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
            allFilterData.put("averageStar",toilet.averageStar);
            allFilterData.put("reviewCount",1);
            allFilterData.put("available",true);
            allFilterData.put("averageWait",toilet.averageStar);
            allFilterData.put("toiletFloor",toilet.floor);
            allFilterData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
            allFilterData.put("westerntoilet",AddDetailBooleans.westerntoilet);
            allFilterData.put("onlyFemale",AddDetailBooleans.onlyFemale);
            allFilterData.put("unisex",AddDetailBooleans.unisex);
            allFilterData.put("latitude",toilet.latitude);
            allFilterData.put("longitude",toilet.longitude);

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
            toiletViewData.put("addedBy",toilet.addedBy);
            toiletViewData.put("editedBy",uid);
            toiletViewData.put("reviewOne",toilet.reviewOne);
            toiletViewData.put("reviewTwo",toilet.reviewTwo);
            toiletViewData.put("averageStar",toilet.averageStar);
            toiletViewData.put("address",toilet.address);
            toiletViewData.put("howtoaccess","");
            toiletViewData.put("openAndCloseHours",openingString);
            toiletViewData.put("openHours",openData);
            toiletViewData.put("closeHours",endData);
            toiletViewData.put("reviewCount",1);
            toiletViewData.put("averageWait",toilet.averageWait);
            toiletViewData.put("toiletFloor",toilet.floor);





            toiletViewData.put("latitude",toilet.latitude);
            toiletViewData.put("longitude",toilet.longitude);


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


            updateData.put("ToiletView/" + toilet.key, toiletViewData);
            updateData.put("NoFilter/" + toilet.key, noFilterData);
            //updateData.put("ToiletUserList/" + toilet.key, toiletUserList);
            updateData.put("UnitOne/" + toilet.key, unitOneData);
            updateData.put("UnitTwo/" + toilet.key, unitTwoData);
            updateData.put("UnitThree/" + toilet.key, unitThreeData);
            updateData.put("UnitFour/" + toilet.key, unitFourData);
            updateData.put("UnitFive/" + toilet.key, unitFiveData);
            updateData.put("UnitSix/" + toilet.key, unitSixData);
            updateData.put("UnitSeven/" + toilet.key, unitSevenData);
            updateData.put("UnitEight/" + toilet.key, unitEightData);
            updateData.put("UnitNine/" + toilet.key, unitNineData);
            updateData.put("UnitTen/" + toilet.key, unitTenData);
            updateData.put("UnitEleven/" + toilet.key, unitElevenData);
            updateData.put("UnitTwelve/" + toilet.key, unitTwelveData);
            updateData.put("GroupOne/" + toilet.key, groupOneData);
            updateData.put("GroupTwo/" + toilet.key, groupTwoData);
            updateData.put("GroupThree/" + toilet.key, groupThreeData);
            updateData.put("HalfOne/" + toilet.key, halfOneData);
            updateData.put("HalfTwo/" + toilet.key, halfTwoData);
            updateData.put("AllFilter/" + toilet.key, allFilterData);



            //


            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();


            firebaseRef.updateChildren(updateData,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });

            ///From AddDetailView




//
//            Map<String, Object> childUpdates = new HashMap<>();
//
//
//            //I could not get tName
//            //Maybe I could not get other values either
//
//
//            childUpdates.put("name", tName);
//            childUpdates.put("openAndCloseHours", openingString);
//            childUpdates.put("type", updateType);
//
//            childUpdates.put("urlOne", urlOne);
//            childUpdates.put("urlTwo", urlTwo);
//            childUpdates.put("urlThree", urlThree);
//            childUpdates.put("editedBy", uid);
//            childUpdates.put("howtoaccess", "");
//            childUpdates.put("openHours", openTime);
//            childUpdates.put("closeHours", endTime);
//            childUpdates.put("toiletFloor", 3);
//
//
//            childUpdates.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
//            childUpdates.put("westerntoilet", AddDetailBooleans.westerntoilet);
//            childUpdates.put("onlyFemale", AddDetailBooleans.onlyFemale);
//            childUpdates.put("unisex", AddDetailBooleans.unisex);
//
//            childUpdates.put("washlet", AddDetailBooleans.washlet);
//            childUpdates.put("warmSeat", AddDetailBooleans.warmSeat);
//            childUpdates.put("autoOpen", AddDetailBooleans.autoOpen);
//            childUpdates.put("noVirus", AddDetailBooleans.noVirus);
//            childUpdates.put("paperForBenki", AddDetailBooleans.paperForBenki);
//            childUpdates.put("cleanerForBenki", AddDetailBooleans.cleanerForBenki);
//            childUpdates.put("nonTouchWash", AddDetailBooleans.autoToiletWash);
//
//
//            childUpdates.put("sensorHandWash", AddDetailBooleans.sensorHandWash);
//            childUpdates.put("handSoap", AddDetailBooleans.handSoap);
//            childUpdates.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
//            childUpdates.put("paperTowel", AddDetailBooleans.paperTowel);
//            childUpdates.put("handDrier", AddDetailBooleans.handDrier);
//
//
//            childUpdates.put("fancy", AddDetailBooleans.fancy);
//            childUpdates.put("smell", AddDetailBooleans.smell);
//            childUpdates.put("confortable", AddDetailBooleans.conforatableWide);
//            childUpdates.put("clothes", AddDetailBooleans.clothes);
//            childUpdates.put("baggageSpace", AddDetailBooleans.baggageSpace);
//
//            childUpdates.put("noNeedAsk", AddDetailBooleans.noNeedAsk);
//            childUpdates.put("english", AddDetailBooleans.english);
//            childUpdates.put("parking", AddDetailBooleans.parking);
//            childUpdates.put("airCondition", AddDetailBooleans.airCondition);
//            childUpdates.put("wifi", AddDetailBooleans.wifi);
//
//            childUpdates.put("otohime", AddDetailBooleans.otohime);
//            childUpdates.put("napkinSelling", AddDetailBooleans.napkinSelling);
//            childUpdates.put("makeuproom", AddDetailBooleans.makeuproom);
//            childUpdates.put("ladyOmutu", AddDetailBooleans.ladyOmutu);
//            childUpdates.put("ladyBabyChair", AddDetailBooleans.ladyBabyChair);
//            childUpdates.put("ladyBabyChairGood", AddDetailBooleans.ladyBabyChairGood);
//            childUpdates.put("ladyBabyCarAccess", AddDetailBooleans.ladyBabyCarAccess);
//
//            childUpdates.put("maleOmutu", AddDetailBooleans.maleOmutu);
//            childUpdates.put("maleBabyChair", AddDetailBooleans.maleBabyChair);
//            childUpdates.put("maleBabyChairGood", AddDetailBooleans.maleBabyChairGood);
//            childUpdates.put("maleBabyCarAccess", AddDetailBooleans.maleBabyCarAccess);
//
//            //for Family Restroom
//            Log.i("Passed Boolean", "6");
//
//            childUpdates.put("wheelchair", AddDetailBooleans.wheelchair);
//            childUpdates.put("wheelchairAccess", AddDetailBooleans.wheelchairAccess);
//            childUpdates.put("autoDoor", AddDetailBooleans.autoDoor);
//            childUpdates.put("callHelp", AddDetailBooleans.callHelp);
//            childUpdates.put("ostomate", AddDetailBooleans.ostomate);
//            childUpdates.put("braille", AddDetailBooleans.braille);
//            childUpdates.put("voiceGuide", AddDetailBooleans.voiceGuide);
//            childUpdates.put("familyOmutu", AddDetailBooleans.familyOmutu);
//            childUpdates.put("familyBabyChair", AddDetailBooleans.familyBabyChair);
//
//
//            childUpdates.put("milkspace", AddDetailBooleans.milkspace);
//            childUpdates.put("babyRoomOnlyFemale", AddDetailBooleans.babyroomOnlyFemale);
//            childUpdates.put("babyRoomMaleEnter", AddDetailBooleans.babyroomManCanEnter);
//            childUpdates.put("babyRoomPersonalSpace", AddDetailBooleans.babyPersonalSpace);
//            childUpdates.put("babyRoomPersonalSpaceWithLock", AddDetailBooleans.babyPersonalSpaceWithLock);
//            childUpdates.put("babyRoomWideSpace", AddDetailBooleans.babyRoomWideSpace);
//
//
//            childUpdates.put("babyCarRental", AddDetailBooleans.babyCarRental);
//            childUpdates.put("babyCarAccess", AddDetailBooleans.babyCarAccess);
//            childUpdates.put("omutu", AddDetailBooleans.omutu);
//            childUpdates.put("hipCleaningStuff", AddDetailBooleans.hipWashingStuff);
//            childUpdates.put("omutuTrashCan", AddDetailBooleans.babyTrashCan);
//            childUpdates.put("omutuSelling", AddDetailBooleans.omutuSelling);
//
//
//            childUpdates.put("babySink", AddDetailBooleans.babyRoomSink);
//            childUpdates.put("babyWashstand", AddDetailBooleans.babyWashStand);
//            childUpdates.put("babyHotwater", AddDetailBooleans.babyHotWater);
//            childUpdates.put("babyMicrowave", AddDetailBooleans.babyMicroWave);
//            childUpdates.put("babyWaterSelling", AddDetailBooleans.babyWaterSelling);
//            childUpdates.put("babyFoodSelling", AddDetailBooleans.babyFoddSelling);
//            childUpdates.put("babyEatingSpace", AddDetailBooleans.babyEatingSpace);
//
//
//            childUpdates.put("babyChair", AddDetailBooleans.babyChair);
//            childUpdates.put("babySoffa", AddDetailBooleans.babySoffa);
//            childUpdates.put("kidsToilet", AddDetailBooleans.babyKidsToilet);
//            childUpdates.put("kidsSpace", AddDetailBooleans.babyKidsSpace);
//            childUpdates.put("babyHeight", AddDetailBooleans.babyHeightMeasure);
//            childUpdates.put("babyWeight", AddDetailBooleans.babyWeightMeasure);
//            childUpdates.put("babyToy", AddDetailBooleans.babyToy);
//            childUpdates.put("babyFancy", AddDetailBooleans.babyFancy);
//            childUpdates.put("babySmellGood", AddDetailBooleans.babySmellGood);
//
//
//            //childUpdates.put("editedBy",uid);
//
//            //We dont need to updata editedBy uid....
//
//
//            updateToiletRef.updateChildren(childUpdates);


            Log.i("please", "...");
            // geolocationUpdate(firekey);

            Intent intent = new Intent(getApplicationContext(), DetailViewActivity.class);
            intent.putExtra("EXTRA_SESSION_ID", toilet.key);
            intent.putExtra("toiletLatitude", toilet.latitude);
            intent.putExtra("toiletLongitude", toilet.longitude);

            startActivity(intent);
            finish();
        }

    }

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            //Photo Permission

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageSetPlaceChoose();


            }
        }

    }

}
