package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import static android.widget.LinearLayout.VERTICAL;

public class FilterListSearchActivity extends AppCompatActivity implements FilterListAdapter.AdapterCallback {


//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;
//    private FilterListAdapter adapter;

    SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();

    Button button;
    Toolbar toolbar;
    TextView toolbarTitle;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;
    ArrayAdapter<CharSequence> adapter4;

    Boolean spinner3LoadFirstTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_search);

        toolbarReady();
        sparseArrayReady();
        spinnersReady();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.filter2, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter){

            Toast.makeText(this, "Hey Did you Click filter??", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
            finish();
            return  true;
        } else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }


    private void toolbarReady(){

        button = (Button) findViewById(R.id.startSearchButton);
        toolbar = (Toolbar) findViewById(R.id.app_bar2);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

//                Filter.availableFilter = false;
//                Filter.japaneseFilter = false;
//                Filter.westernFilter = false;
//                Filter.onlyFemaleFilter = false;
//                Filter.washletFilter = false;
//                Filter.warmSearFilter = false;
//                Filter.omutuFilter = false;
//                Filter.milkspaceFilter = false;
//                Filter.makeroomFilter = false;
//                Filter.baggageSpaceFilter = false;
//                Filter.wheelchairFilter = false;
//                Filter.ostomateFilter = false;


                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
                                             }
        );



    }

    private void spinnersReady(){
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        adapter1 = ArrayAdapter.createFromResource(this,R.array.distance_names,android.R.layout.simple_spinner_item);
        adapter2 = ArrayAdapter.createFromResource(this,R.array.order_names,android.R.layout.simple_spinner_item);
        adapter3 = ArrayAdapter.createFromResource(this,R.array.places_filters,android.R.layout.simple_spinner_item);
        adapter4 = ArrayAdapter.createFromResource(this,R.array.star_numbers,android.R.layout.simple_spinner_item);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);



        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                ((TextView) parent.getChildAt(0)).setTextSize(20);


                if (position == 0){
                    Log.i("DIDIDID", "9090");

                }

                if (position == 1){
                    Toast.makeText(getBaseContext(),"1",Toast.LENGTH_SHORT).show();
                    Filter.distanceSetted = true;
                    Filter.distanceFilter = 1.0;
                }

                if (position == 2){
                    Toast.makeText(getBaseContext(),"2",Toast.LENGTH_SHORT).show();
                    Filter.distanceSetted = true;
                    Filter.distanceFilter = 3.0;
                }

                if (position == 3){
                    Toast.makeText(getBaseContext(),"3",Toast.LENGTH_SHORT).show();
                    Filter.distanceSetted = true;
                    Filter.distanceFilter = 5.0;
                }

                if (position == 4){
                    Toast.makeText(getBaseContext(),"4",Toast.LENGTH_SHORT).show();
                    Filter.distanceSetted = true;
                    Filter.distanceFilter = 10.0;
                }

                Toast.makeText(getBaseContext(),String.valueOf(Filter.distanceFilter) + "After",Toast.LENGTH_SHORT).show();

                ((TextView) parent.getChildAt(0)).setText(Filter.distanceFilter + "kmのトイレを検索" );

                // ((TextView) parent.getChildAt(0)).setText(filter.distanceFilter + "kmのトイレを検索" );

//                Toast.makeText(getBaseContext(),String.valueOf(position) + "Selected",Toast.LENGTH_SHORT).show();
//                //This might cause an error 2pm 24th
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String filterName;
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
//                ((TextView) parent.getChildAt(0)).setText(String.valueOf(parent.getItemAtPosition(position)));


                //I think i need one more filter

                if (position == 1){
                    Toast.makeText(getBaseContext(),"0",Toast.LENGTH_SHORT).show();
                    Filter.orderDistanceFilter = true;
                    Filter.orderStarFilter = false;
                    Filter.orderReviewFilter = false;

                }

                if (position == 2){
                    Toast.makeText(getBaseContext(),"1",Toast.LENGTH_SHORT).show();
                    Filter.orderStarFilter = true;
                    Filter.orderDistanceFilter = false;
                    Filter.orderReviewFilter = false;
                }

                if (position == 3){
                    Toast.makeText(getBaseContext(),"2",Toast.LENGTH_SHORT).show();
                    Filter.orderReviewFilter = true;
                    Filter.orderDistanceFilter = false;
                    Filter.orderStarFilter = false;
                }

                if (Filter.orderDistanceFilter){
                    ((TextView) parent.getChildAt(0)).setText("現在地から近い順");
                } else if (Filter.orderStarFilter){
                    ((TextView) parent.getChildAt(0)).setText("評価が高い順");
                } else if (Filter.orderReviewFilter){
                    ((TextView) parent.getChildAt(0)).setText("感想が多い順");
                } else {

                    ((TextView) parent.getChildAt(0)).setText("現在地から近い順");
                }

                //Commented 8pm 23th

//                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + "Selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

                Log.i("TypeFilterNumber", String.valueOf(Filter.typeFilter));

                if (!spinner3LoadFirstTime){
                    spinner3LoadFirstTime = true;
                    if (Filter.typeFilter ==  0){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutZero);

                    } else if (Filter.typeFilter ==  1){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutOne);

                    } else if (Filter.typeFilter ==  2){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutTwo);

                    } else if (Filter.typeFilter ==  3){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutThree);

                    } else if (Filter.typeFilter ==  4){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutFour);

                    } else if (Filter.typeFilter ==  5){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutFive);

                    } else if (Filter.typeFilter ==  6){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutSix);


                    } else if (Filter.typeFilter ==  7){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutSeven);


                    } else if (Filter.typeFilter ==  8){
                        ((TextView) parent.getChildAt(0)).setText(R.string.typeDefalutEight);

                    }
                }

                if (position == 0){

                    Filter.typeFilter = 0;

                } else if (position == 1){
                    Filter.typeFilter = 1;

                } else if (position == 2){
                    Filter.typeFilter = 2;

                } else if (position == 3){
                    Filter.typeFilter = 3;

                } else if (position == 4){
                    Filter.typeFilter = 4;

                } else if (position == 5){
                    Filter.typeFilter = 5;

                } else if (position == 6){
                    Filter.typeFilter = 6;

                } else if (position == 7){
                    Filter.typeFilter = 7;

                } else if (position == 8){
                    Filter.typeFilter = 8;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                //((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position) + "以上を検索");


                if (position == 0){
                    Log.i("SHoud not be ","star zero");
//                    filter.starFilter = 1.0;
                }

                if (position == 1){
                    Filter.starFilter = 1.0;
                }

                if (position == 2){
                    Filter.starFilter = 2.0;
                }

                if (position == 3){
                    Filter.starFilter = 3.0;
                }
                if (position == 4){
                    Filter.starFilter = 4.0;
                }



                ((TextView) parent.getChildAt(0)).setText(Filter.starFilter + "以上を検索");


                // Toast.makeText(getBaseContext(),String.valueOf(filter.starFilter),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }


    private void sparseArrayReady(){


        filterSparseArray.append(0, new FilterBooleans("設定",false));
        filterSparseArray.append(1, new FilterBooleans("現在利用可能",Filter.availableFilter));
        filterSparseArray.append(2, new FilterBooleans(getResources().getString(R.string.name_japaneseToilet),Filter.japaneseFilter));
        filterSparseArray.append(3, new FilterBooleans("洋式トイレ",Filter.westernFilter));
        filterSparseArray.append(4, new FilterBooleans("女性専用トイレ",Filter.onlyFemaleFilter));
        filterSparseArray.append(5, new FilterBooleans("男女兼用トイレ",Filter.unisexFilter));


        filterSparseArray.append(6, new FilterBooleans("機能",false));
        filterSparseArray.append(7, new FilterBooleans("ウォシュレット",Filter.washletFilter));
        filterSparseArray.append(8, new FilterBooleans("暖房便座",Filter.warmSearFilter));
        filterSparseArray.append(9, new FilterBooleans("自動開閉便座",Filter.autoOpen));
        filterSparseArray.append(10, new FilterBooleans("抗菌便座",Filter.noVirusFilter));
        filterSparseArray.append(11, new FilterBooleans("便座用シート",Filter.paperForBenkiFilter));
        filterSparseArray.append(12, new FilterBooleans("便座クリーナー",Filter.cleanerForBenkiFilter));
        filterSparseArray.append(13, new FilterBooleans("自動洗浄",Filter.autoToiletWashFilter));


        filterSparseArray.append(14, new FilterBooleans("洗面台設備",false));
        filterSparseArray.append(15, new FilterBooleans("センサー式お手洗い",Filter.sensorHandWashFilter));
        filterSparseArray.append(16, new FilterBooleans("ハンドソープ",Filter.handSoapFilter));
        filterSparseArray.append(17, new FilterBooleans("自動ハンドソープ",Filter.autoHandSoapFilter));
        filterSparseArray.append(18, new FilterBooleans("ペーパータオル",Filter.paperTowelFilter));
        filterSparseArray.append(19, new FilterBooleans("ハンドドライヤー",Filter.handDrierFilter));



        filterSparseArray.append(20, new FilterBooleans("1,その他",false));
        filterSparseArray.append(21, new FilterBooleans("おしゃれ",Filter.fancy));
        filterSparseArray.append(22, new FilterBooleans("いい香り",Filter.smell));
        filterSparseArray.append(23, new FilterBooleans("快適な広さ",Filter.confortableWise));
        filterSparseArray.append(24, new FilterBooleans("洋服掛け",Filter.clothes));
        filterSparseArray.append(25, new FilterBooleans("荷物置き",Filter.baggageSpaceFilter));

        filterSparseArray.append(26, new FilterBooleans("2,その他",false));
        filterSparseArray.append(27, new FilterBooleans("利用の際の声かけ不要",Filter.noNeedAsk));
        filterSparseArray.append(28, new FilterBooleans("英語表記",Filter.writtenEnglish));
        filterSparseArray.append(29, new FilterBooleans("駐車場",Filter.parking));
        filterSparseArray.append(30, new FilterBooleans("冷暖房",Filter.airConditionFilter));
        filterSparseArray.append(31, new FilterBooleans("無料Wi-Fi",Filter.wifiFilter));






        filterSparseArray.append(32, new FilterBooleans("女性トイレ",false));
        filterSparseArray.append(33, new FilterBooleans("音姫",Filter.otohime));
        filterSparseArray.append(34, new FilterBooleans("ナプキン販売機",Filter.napkinSelling));
        filterSparseArray.append(35, new FilterBooleans("パウダールーム",Filter.makeroomFilter));
        filterSparseArray.append(36, new FilterBooleans("おむつ交換台",Filter.ladyOmutuFilter));
        filterSparseArray.append(37, new FilterBooleans("ベビーキープ",Filter.ladyBabyChair));
        filterSparseArray.append(38, new FilterBooleans("安全なベビーキープ",Filter.ladyBabyChairGood));
        filterSparseArray.append(39, new FilterBooleans("ベビーカーでのアクセス",Filter.ladyBabyCarAccess));


        filterSparseArray.append(40, new FilterBooleans("男性トイレ",false));
        filterSparseArray.append(41, new FilterBooleans("おむつ交換台",Filter.maleOmutuFilter));
        filterSparseArray.append(42, new FilterBooleans("ベビーキープ",Filter.maleBabyChair));
        filterSparseArray.append(43, new FilterBooleans("安全なベビーキープ",Filter.maleBabyChairGood));
        filterSparseArray.append(44, new FilterBooleans("ベビーカーでのアクセス",Filter.maleBabyCarAccess));


        filterSparseArray.append(45, new FilterBooleans("多目的トイレ",false));
        filterSparseArray.append(46, new FilterBooleans("車イス対応",Filter.wheelchairFilter));
        filterSparseArray.append(47, new FilterBooleans("車イスでアクセス可能",Filter.wheelchairAccessFilter));
        filterSparseArray.append(48, new FilterBooleans("自動ドア",Filter.autoDoorFilter));
        filterSparseArray.append(49, new FilterBooleans("呼び出しボタン",Filter.callHelpFilter));
        filterSparseArray.append(50, new FilterBooleans("オストメイト",Filter.ostomateFilter));
        filterSparseArray.append(51, new FilterBooleans("点字案内",Filter.braille));
        filterSparseArray.append(52, new FilterBooleans("音声案内",Filter.voiceGuideFilter));
        filterSparseArray.append(53, new FilterBooleans("おむつ交換台",Filter.familyOmutuFilter));
        filterSparseArray.append(54, new FilterBooleans("ベビーチェア",Filter.familyBabyChair));



        filterSparseArray.append(55, new FilterBooleans("1,ベビールームについて",false));
        filterSparseArray.append(56, new FilterBooleans("授乳スペース",Filter.milkspaceFilter));
        filterSparseArray.append(57, new FilterBooleans("女性限定",Filter.babyRoomOnlyFemaleFilter));
        filterSparseArray.append(58, new FilterBooleans("男性入室可能",Filter.babyRoomMaleCanEnterFilter));
        filterSparseArray.append(59, new FilterBooleans("個室あり",Filter.babyRoomPersonalSpaceFilter));
        filterSparseArray.append(60, new FilterBooleans("鍵付き個室あり",Filter.babyRoomPersonalWithLockFilter));
        filterSparseArray.append(61, new FilterBooleans("広いスペース",Filter.babyRoomWideSpaceFilter));


        filterSparseArray.append(62, new FilterBooleans("2,ベビールームについて",false));
        filterSparseArray.append(63, new FilterBooleans("ベビーカー貸出し",Filter.babyCarRentalFilter));
        filterSparseArray.append(64, new FilterBooleans("ベビーカーでアクセス可能",Filter.babyCarAccessFilter));
        filterSparseArray.append(65, new FilterBooleans("おむつ交換台",Filter.omutuFilter));
        filterSparseArray.append(66, new FilterBooleans("おしりふき",Filter.babyHipWashingStuffFilter));
        filterSparseArray.append(67, new FilterBooleans("おむつ用ゴミ箱",Filter.omutuTrashCanFilter));
        filterSparseArray.append(68, new FilterBooleans("おむつ販売機",Filter.omutuSelling));


        filterSparseArray.append(69, new FilterBooleans("3,ベビールームについて",false));
        filterSparseArray.append(70, new FilterBooleans("シンク",Filter.babySinkFilter));
        filterSparseArray.append(71, new FilterBooleans("洗面台",Filter.babyWashstandFilter));
        filterSparseArray.append(72, new FilterBooleans("給湯器",Filter.babyHotWaterFilter));
        filterSparseArray.append(73, new FilterBooleans("電子レンジ",Filter.babyMicrowaveFilter));
        filterSparseArray.append(74, new FilterBooleans("飲料自販機",Filter.babySellingWaterFilter));
        filterSparseArray.append(75, new FilterBooleans("離乳食販売機",Filter.babyFoodSellingFilter));
        filterSparseArray.append(76, new FilterBooleans("飲食スペース",Filter.babyEatingSpaceFilter));


        filterSparseArray.append(77, new FilterBooleans("4,ベビールームについて",false));
        filterSparseArray.append(78, new FilterBooleans("ベビーチェア",Filter.babyChairFilter));
        filterSparseArray.append(79, new FilterBooleans("ソファ",Filter.babySoffaFilter));
        filterSparseArray.append(80, new FilterBooleans("キッズトイレ",Filter.babyToiletFilter));
        filterSparseArray.append(81, new FilterBooleans("キッズスペース",Filter.babyKidsSpaceFilter));
        filterSparseArray.append(82, new FilterBooleans("身長計",Filter.babyHeightMeasureFilter));
        filterSparseArray.append(83, new FilterBooleans("体重計",Filter.babyWeightMeasureFilter));
        filterSparseArray.append(84, new FilterBooleans("おもちゃ",Filter.babyToyFilter));
        filterSparseArray.append(85, new FilterBooleans("おしゃれ",Filter.babyRoomFancyFilter));
        filterSparseArray.append(86, new FilterBooleans("いい香り",Filter.babyRoomSmellGoodFilter));



        createRecyclerView(filterSparseArray);


    }


    @SuppressWarnings("unchecked")
    private void createRecyclerView(SparseArray array) {
        Log.i("reviewRecycle", "Called");
        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        FilterListAdapter adapter;
        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new FilterListAdapter(array, this);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
        recyclertView.addItemDecoration(dividerItemDecoration);

        recyclertView.setHasFixedSize(true);
        recyclertView.setNestedScrollingEnabled(false);


    }

    @Override
    public void onMethodCallback() {


        Log.i("CallBack","Called 99999");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("Safe Toilet");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"ログインをする", "ログインをしない"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();



        // do something
    }





    //Trying to access from filter list adapter


}
