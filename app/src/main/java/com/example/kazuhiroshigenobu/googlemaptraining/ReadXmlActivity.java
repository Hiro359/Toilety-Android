package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadXmlActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ToiletLocations");
    GeoFire geoFire = new GeoFire(ref);
    private List<Toilet> geoFireArray = new ArrayList<>();
    private Integer databaseUploadCount = 0;
    private Integer successUpdateCount = 0;

    private FirebaseAuth firebaseAuth;
    //Copy from database creation activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_xml);

        Log.i("readXML loaded", "333");


        //readXmlData();
        siginIn();
        //later it reads xml file
        //August 10

    }

    private void siginIn() {

        String email = "database@gmail.com";
        String password = "Database";
        //String userName = userNameText.getText().toString();

        firebaseAuth = FirebaseAuth.getInstance();

//        firebaseAuth.signInAnonymously();
//        readXmlData();
        //August 10
//
//        pointAllocation(33.590, 130.40, 35.689, 139.69);


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("task called", "3344");
                    readXmlData();

                } else {
                    Log.i("task did not called", "3344");

                    Toast.makeText(ReadXmlActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    System.out.print("Error Found!!!!!!!!");
                    //Toast.makeText(StartLoginActivity.this, "UnSuccess", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void readXmlData(){
        try {
            Log.i("readXML try", "333");

            InputStream is = getAssets().open("new_file");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Placemark");
//            NodeList nList = doc.getElementsByTagName("employee");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;


                    String name = getValue("name",element2);

                    String coordinate = getValue("coordinates", element2);

                    String latitude = coordinate.split(",")[1];
                    String longitude = coordinate.split(",")[0];


                    Double latDouble = Double.parseDouble(latitude);
                    Double lonDouble = Double.parseDouble(longitude);


                    Log.i("name 222",name);
                    Log.i("lat 222",String.valueOf(latDouble));
                    Log.i("lon 222",String.valueOf(lonDouble));






                    if ((latDouble.floatValue() > -90.0) && (latDouble.floatValue() < 90.0)) {
                        //Latitude is a right value
                        if ((lonDouble.floatValue() > -180.0) && (lonDouble.floatValue() < 180.0)) {
                            //Longitude is a right value
                            databaseUpload(name, latDouble, lonDouble);
                        } else {
                            Log.i("long error 6666",name + String.valueOf(lonDouble));
                        }
                    } else {
                        Log.i("lat error 6666",name + String.valueOf(latDouble));
                    }
                      //July 30 7pm




                    List<String> elephantList = Arrays.asList(coordinate.split(","));

                    System.out.print(elephantList + "444");



//                    tv1.setText(tv1.getText()+"\nName : " + getValue("name", element2)+"\n");
//                    tv1.setText(tv1.getText()+"Surname : " + getValue("surname", element2)+"\n");
//                    tv1.setText(tv1.getText()+"-----------------------");
                }
            }

        } catch (Exception e) {e.printStackTrace();
            Log.i("readXML catch", "333");}

    }

    private static String getValue(String tag, Element element) {
        Log.i("readXML get", "333");
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }



    private void databaseUpload(String name, Double lat, Double lng){


        Log.i("First Upload", "23456");
        //String id =

        String id = UUID.randomUUID().toString();
        String address = "";
        databaseUploadCount = databaseUploadCount + 1;
        Log.i("databaseUploadCount 222", String.valueOf(databaseUploadCount));

        String urlOne = "";

        String tName = name;

        String newTid = id;

        AddLocations.latitude = lat;

        AddLocations.longitude = lng;

        Integer waitingValue = 0;

        AddLocations.address = address;

        Integer type = 2;

        String avStar = "0";

        Integer toiletFloor = 3;
        Integer openData = 0;
        Integer endData = 2400;
        Integer reviewCount = 0;

        String openingString = "0:00 〜 24:00";


        final Toilet toilet = new Toilet();

        toilet.key = id;
        toilet.latitude = lat;
        toilet.longitude = lng;

        //Get data from google places api







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




        Map<String, Object> noFilterData = new HashMap();

        noFilterData.put("name",tName);
        noFilterData.put("type",type);
        noFilterData.put("urlOne",urlOne);
        noFilterData.put("averageStar",avStar);
        noFilterData.put("reviewCount",reviewCount);
        noFilterData.put("available",true);
        noFilterData.put("averageWait",waitingValue);
        noFilterData.put("toiletFloor",toiletFloor);
        noFilterData.put("latitude",AddLocations.latitude);
        noFilterData.put("longitude",AddLocations.longitude);


//            Map<String, Object> toiletUserList = new HashMap();
//            toiletUserList.put("name",tName);
//            toiletUserList.put("type",typeSpinner.getSelectedItemPosition());
//            toiletUserList.put("urlOne",urlOne);
//            toiletUserList.put("averageStar",avStar);
//            toiletUserList.put("reviewCount",1);
//            toiletUserList.put("available",true);
//            toiletUserList.put("averageWait",waitingValue);
//            toiletUserList.put("toiletFloor",toiletFloor);
//            toiletUserList.put("latitude",AddLocations.latitude);
//            toiletUserList.put("longitude",AddLocations.longitude);


        Map<String, Object> unitOneData = new HashMap();
        unitOneData.put("name",tName);
        unitOneData.put("type",type);
        unitOneData.put("urlOne",urlOne);
        unitOneData.put("averageStar",avStar);
        unitOneData.put("reviewCount",reviewCount);
        unitOneData.put("available",true);
        unitOneData.put("averageWait",waitingValue);
        unitOneData.put("toiletFloor",toiletFloor);
        unitOneData.put("openHours",openData);
        unitOneData.put("closeHours",endData);
        unitOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
        unitOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
        unitOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
        unitOneData.put("unisex",AddDetailBooleans.unisex);
        unitOneData.put("latitude",AddLocations.latitude);
        unitOneData.put("longitude",AddLocations.longitude);
        unitOneData.put("openHours",3600);
        unitOneData.put("closeHours",3600);



        Map<String, Object> unitTwoData = new HashMap();

        unitTwoData.put("name",tName);
        unitTwoData.put("type",type);
        unitTwoData.put("urlOne",urlOne);
        unitTwoData.put("averageStar",avStar);
        unitTwoData.put("reviewCount",reviewCount);
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
        unitTwoData.put("latitude",AddLocations.latitude);
        unitTwoData.put("longitude",AddLocations.longitude);




        Map<String, Object> unitThreeData = new HashMap();
        unitThreeData.put("name",tName);
        unitThreeData.put("type",type);
        unitThreeData.put("urlOne",urlOne);
        unitThreeData.put("averageStar",avStar);
        unitThreeData.put("reviewCount",reviewCount);
        unitThreeData.put("available",true);
        unitThreeData.put("averageWait",waitingValue);
        unitThreeData.put("toiletFloor",toiletFloor);
        unitThreeData.put("sensorHandWash",AddDetailBooleans.sensorHandWash);
        unitThreeData.put("handSoap", AddDetailBooleans.handSoap);
        unitThreeData.put("nonTouchHandSoap", AddDetailBooleans.autoHandSoap);
        unitThreeData.put("paperTowel",AddDetailBooleans.paperTowel);
        unitThreeData.put("handDrier",AddDetailBooleans.handDrier);
        unitThreeData.put("latitude",AddLocations.latitude);
        unitThreeData.put("longitude",AddLocations.longitude);


        Map<String, Object> unitFourData = new HashMap();
        unitFourData.put("name",tName);
        unitFourData.put("type",type);
        unitFourData.put("urlOne",urlOne);
        unitFourData.put("averageStar",avStar);
        unitFourData.put("reviewCount",reviewCount);
        unitFourData.put("available",true);
        unitFourData.put("averageWait",waitingValue);
        unitFourData.put("toiletFloor",toiletFloor);
        unitFourData.put("fancy", AddDetailBooleans.fancy);
        unitFourData.put("smell",AddDetailBooleans.smell);
        unitFourData.put("confortable",AddDetailBooleans.conforatableWide);
        unitFourData.put("clothes",AddDetailBooleans.clothes);
        unitFourData.put("baggageSpace",AddDetailBooleans.baggageSpace);
        unitFourData.put("latitude",AddLocations.latitude);
        unitFourData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitFiveData = new HashMap();
        unitFiveData.put("name",tName);
        unitFiveData.put("type",type);
        unitFiveData.put("urlOne",urlOne);
        unitFiveData.put("averageStar",avStar);
        unitFiveData.put("reviewCount",reviewCount);
        unitFiveData.put("available",true);
        unitFiveData.put("averageWait",waitingValue);
        unitFiveData.put("toiletFloor",toiletFloor);
        unitFiveData.put("noNeedAsk",AddDetailBooleans.noNeedAsk);
        unitFiveData.put("english",AddDetailBooleans.english);
        unitFiveData.put("parking",AddDetailBooleans.parking);
        unitFiveData.put("airCondition",AddDetailBooleans.airCondition);
        unitFiveData.put("wifi",AddDetailBooleans.wifi);
        unitFiveData.put("latitude",AddLocations.latitude);
        unitFiveData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitSixData = new HashMap();
        unitSixData.put("name",tName);
        unitSixData.put("type",type);
        unitSixData.put("urlOne",urlOne);
        unitSixData.put("averageStar",avStar);
        unitSixData.put("reviewCount",reviewCount);
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
        unitSixData.put("latitude",AddLocations.latitude);
        unitSixData.put("longitude",AddLocations.longitude);


        Map<String, Object> unitSevenData = new HashMap();
        unitSevenData.put("name",tName);
        unitSevenData.put("type",type);
        unitSevenData.put("urlOne",urlOne);
        unitSevenData.put("averageStar",avStar);
        unitSevenData.put("reviewCount",reviewCount);
        unitSevenData.put("available",true);
        unitSevenData.put("averageWait",waitingValue);
        unitSevenData.put("toiletFloor",toiletFloor);
        unitSevenData.put("maleOmutu",AddDetailBooleans.maleOmutu);
        unitSevenData.put("maleBabyChair",AddDetailBooleans.maleBabyChair);
        unitSevenData.put("maleBabyChairGood",AddDetailBooleans.maleBabyChairGood);
        unitSevenData.put("maleBabyCarAccess",AddDetailBooleans.babyCarAccess);
        unitSevenData.put("latitude",AddLocations.latitude);
        unitSevenData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitEightData = new HashMap();
        unitEightData.put("name",tName);
        unitEightData.put("type",type);
        unitEightData.put("urlOne",urlOne);
        unitEightData.put("averageStar",avStar);
        unitEightData.put("reviewCount",reviewCount);
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
        unitEightData.put("latitude",AddLocations.latitude);
        unitEightData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitNineData = new HashMap();
        unitNineData.put("name",tName);
        unitNineData.put("type",type);
        unitNineData.put("urlOne",urlOne);
        unitNineData.put("averageStar",avStar);
        unitNineData.put("reviewCount",reviewCount);
        unitNineData.put("available",true);
        unitNineData.put("averageWait",waitingValue);
        unitNineData.put("toiletFloor",toiletFloor);
        unitNineData.put("milkspace",AddDetailBooleans.milkspace);
        unitNineData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
        unitNineData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
        unitNineData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
        unitNineData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
        unitNineData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);
        unitNineData.put("latitude",AddLocations.latitude);
        unitNineData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitTenData = new HashMap();
        unitTenData.put("name",tName);
        unitTenData.put("type",type);
        unitTenData.put("urlOne",urlOne);
        unitTenData.put("averageStar",avStar);
        unitTenData.put("reviewCount",reviewCount);
        unitTenData.put("available",true);
        unitTenData.put("averageWait",waitingValue);
        unitTenData.put("toiletFloor",toiletFloor);
        unitTenData.put("babyCarRental",AddDetailBooleans.babyCarRental);
        unitTenData.put("babyCarAccess",AddDetailBooleans.babyCarAccess);
        unitTenData.put("omutu",AddDetailBooleans.omutu);
        unitTenData.put("hipCleaningStuff",AddDetailBooleans.hipWashingStuff);
        unitTenData.put("omutuTrashCan",AddDetailBooleans.babyTrashCan);
        unitTenData.put("omutuSelling",AddDetailBooleans.omutuSelling);
        unitTenData.put("latitude",AddLocations.latitude);
        unitTenData.put("longitude",AddLocations.longitude);

        Map<String, Object> unitElevenData = new HashMap();
        unitElevenData.put("name",tName);
        unitElevenData.put("type",type);
        unitElevenData.put("urlOne",urlOne);
        unitElevenData.put("averageStar",avStar);
        unitElevenData.put("reviewCount",reviewCount);
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
        unitElevenData.put("latitude",AddLocations.latitude);
        unitElevenData.put("longitude",AddLocations.longitude);



        Map<String, Object> unitTwelveData = new HashMap();
        unitTwelveData.put("name",tName);
        unitTwelveData.put("type",type);
        unitTwelveData.put("urlOne",urlOne);
        unitTwelveData.put("averageStar",avStar);
        unitTwelveData.put("reviewCount",reviewCount);
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
        unitTwelveData.put("latitude",AddLocations.latitude);
        unitTwelveData.put("longitude",AddLocations.longitude);


        Map<String, Object> groupOneData = new HashMap();
        groupOneData.put("name",tName);
        groupOneData.put("type",type);
        groupOneData.put("urlOne",urlOne);
        groupOneData.put("averageStar",avStar);
        groupOneData.put("reviewCount",reviewCount);
        groupOneData.put("available",true);
        groupOneData.put("averageWait",waitingValue);
        groupOneData.put("toiletFloor",toiletFloor);
        groupOneData.put("openHours",openData);
        groupOneData.put("closeHours",endData);
        groupOneData.put("latitude",AddLocations.latitude);
        groupOneData.put("longitude",AddLocations.longitude);
        groupOneData.put("openHours",3600);
        groupOneData.put("closeHours",3600);


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
        groupTwoData.put("type",type);
        groupTwoData.put("urlOne",urlOne);
        groupTwoData.put("averageStar",avStar);
        groupTwoData.put("reviewCount",reviewCount);
        groupTwoData.put("available",true);
        groupTwoData.put("averageWait",waitingValue);
        groupTwoData.put("toiletFloor",toiletFloor);
        groupTwoData.put("latitude",AddLocations.latitude);
        groupTwoData.put("longitude",AddLocations.longitude);

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
        groupThreeData.put("type",type);
        groupThreeData.put("urlOne",urlOne);
        groupThreeData.put("averageStar",avStar);
        groupThreeData.put("reviewCount",reviewCount);
        groupThreeData.put("available",true);
        groupThreeData.put("averageWait",waitingValue);
        groupThreeData.put("toiletFloor",toiletFloor);
        groupThreeData.put("milkspace",AddDetailBooleans.milkspace);
        groupThreeData.put("babyRoomOnlyFemale",AddDetailBooleans.babyroomOnlyFemale);
        groupThreeData.put("babyRoomMaleEnter",AddDetailBooleans.babyroomManCanEnter);
        groupThreeData.put("babyRoomPersonalSpace",AddDetailBooleans.babyPersonalSpace);
        groupThreeData.put("babyRoomPersonalSpaceWithLock",AddDetailBooleans.babyPersonalSpaceWithLock);
        groupThreeData.put("babyRoomWideSpace",AddDetailBooleans.babyRoomWideSpace);
        groupThreeData.put("latitude",AddLocations.latitude);
        groupThreeData.put("longitude",AddLocations.longitude);


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
        halfOneData.put("type",type);
        halfOneData.put("urlOne",urlOne);
        halfOneData.put("averageStar",avStar);
        halfOneData.put("reviewCount",reviewCount);
        halfOneData.put("available",true);
        halfOneData.put("averageWait",waitingValue);
        halfOneData.put("toiletFloor",toiletFloor);
        halfOneData.put("openHours",openData);
        halfOneData.put("closeHours",endData);
        halfOneData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
        halfOneData.put("westerntoilet",AddDetailBooleans.westerntoilet);
        halfOneData.put("onlyFemale",AddDetailBooleans.onlyFemale);
        halfOneData.put("unisex",AddDetailBooleans.unisex);
        halfOneData.put("latitude",AddLocations.latitude);
        halfOneData.put("longitude",AddLocations.longitude);
        halfOneData.put("openHours",3600);
        halfOneData.put("closeHours",3600);

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
        halfTwoData.put("type",type);
        halfTwoData.put("urlOne",urlOne);
        halfTwoData.put("averageStar",avStar);
        halfTwoData.put("reviewCount",reviewCount);
        halfTwoData.put("available",true);
        halfTwoData.put("averageWait",waitingValue);
        halfTwoData.put("toiletFloor",toiletFloor);
        halfTwoData.put("latitude",AddLocations.latitude);
        halfTwoData.put("longitude",AddLocations.longitude);

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
        allFilterData.put("type",type);
        allFilterData.put("urlOne",urlOne);
        allFilterData.put("averageStar",avStar);
        allFilterData.put("reviewCount",reviewCount);
        allFilterData.put("available",true);
        allFilterData.put("averageWait",waitingValue);
        allFilterData.put("toiletFloor",toiletFloor);
        allFilterData.put("japanesetoilet", AddDetailBooleans.japanesetoilet);
        allFilterData.put("westerntoilet",AddDetailBooleans.westerntoilet);
        allFilterData.put("onlyFemale",AddDetailBooleans.onlyFemale);
        allFilterData.put("unisex",AddDetailBooleans.unisex);
        allFilterData.put("latitude",AddLocations.latitude);
        allFilterData.put("longitude",AddLocations.longitude);
        allFilterData.put("openHours",3600);
        allFilterData.put("closeHours",3600);

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
        toiletViewData.put("type",type);
        toiletViewData.put("urlOne","");
        toiletViewData.put("urlTwo","");
        toiletViewData.put("urlThree","");
        toiletViewData.put("addedBy","");
        toiletViewData.put("editedBy","");
        toiletViewData.put("reviewOne","");
        toiletViewData.put("reviewTwo","");
        toiletViewData.put("averageStar",avStar);
        toiletViewData.put("address",AddLocations.address);
        toiletViewData.put("howtoaccess","");
        toiletViewData.put("openAndCloseHours",openingString);
        toiletViewData.put("openHours",openData);
        toiletViewData.put("closeHours",endData);
        toiletViewData.put("reviewCount",reviewCount);
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



        GeoHash geoHash = new GeoHash(new GeoLocation(AddLocations.latitude, AddLocations.longitude));



        Map<String, Object> updateData = new HashMap();


        updateData.put("ToiletView/" + newTid, toiletViewData);
        updateData.put("NoFilter/" + newTid, noFilterData);
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
        //Permission Error Here??


        updateData.put("AllFilter/" + newTid, allFilterData);

//            updateData.put("ToiletLocations/" + newTid + "/g", geoHash.getGeoHashString());
//            updateData.put("ToiletLocations/" + newTid + "/l", Arrays.asList(AddLocations.latitude, AddLocations.longitude));
//            updateData.put("ToiletLocations/" + newTid + "/.priority", geoHash.getGeoHashString());




//        System.out.print("geoHash.getGeoHashString()" + geoHash.getGeoHashString());
//        System.out.print("Arrays.asList(AddLocations.latitude, AddLocations.longitude)" + Arrays.asList(AddLocations.latitude, AddLocations.longitude));




        JSONObject json = new JSONObject(allFilterData); // Convert text to object
        System.out.println(json);


        geoFireArray.add(toilet);

        Log.i("Before Upload", "23456");


        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

        Log.i("Before 2 Upload", "23456");


        firebaseRef.updateChildren(updateData,new DatabaseReference.CompletionListener() {


            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                Log.i("Try Upload", "23456");

                if (databaseError != null){
                    Toast.makeText(ReadXmlActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.i("Error in Upload", "234567");

                } else {

                    successUpdateCount = successUpdateCount + 1;
                    Log.i("successUpdateCount 222", String.valueOf(successUpdateCount));


                    if (successUpdateCount.equals(databaseUploadCount)) {

                        Log.i("geoFireArrayStart", "23456");

                        for (Toilet toilet : geoFireArray) {
                            // access foo here

                            Log.i("geoFireArrayInside", toilet.key);

                            geoFire.setLocation(toilet.key, new GeoLocation(toilet.latitude, toilet.longitude), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if (error != null) {
                                        System.err.println("222 Error saving the location to GeoFire: " + error + "key" + key);
                                        Log.i("Error Upload", "234567");

                                    } else {
                                        System.out.println("Location saved on server successfully!");
                                        Log.i("Success Upload", "234568");
                                    }

                                }
                            });


                        }
                    }

                }

                Log.i("In end Upload", "23456");
            }




        });



        Log.i("database end", "23456");
//
//
//
//
//            //Copied from firebase blog
//
//            Log.i("please", "...");
//            //geolocationUpload();
//            //reviewUpload();
//
    }

//    private static String getNodeValue(String tag, Element element) {
//        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
//        Node node = nodeList.item(0);
//        return node.getNodeValue();
//    }
//
//    //I have made this June 10.




    }

