package com.example.kazuhiroshigenobu.googlemaptraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.VERTICAL;

public class FilterListSearchActivity extends AppCompatActivity {


    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;
    private FilterListAdapter adapter;

    SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();


//    Map<Integer, List<FilterBooleans>> filterMap = new HashMap<Integer, List<FilterBooleans>>();
//
//    final List<Map<Integer, Boolean>> filterList = new ArrayList<>();

    //Map<Integer, Boolean> booleanValue = new HashMap<>();

//    Map<Object,ArrayList<Object>> multiMap = new HashMap<Object,ArrayList<Object>>();
//
//    FilterBooleansData filterBooleansData = new FilterBooleansData();

    //Map<String, String> dictionary = new HashMap<String, String>();

    //FilterBooleans filterBooleans = new FilterBooleans();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_search);

        filterSparseArray.append(0, new FilterBooleans("設備",false));
        filterSparseArray.append(1, new FilterBooleans("現在利用可能",true));
        filterSparseArray.append(2, new FilterBooleans("和式トイレ",false));
        filterSparseArray.append(3, new FilterBooleans("洋式トイレ",true));
        filterSparseArray.append(4, new FilterBooleans("女性専用トイレ",false));
        filterSparseArray.append(5, new FilterBooleans("男女兼用トイレ",false));
        filterSparseArray.append(6, new FilterBooleans("機能",false));
        filterSparseArray.append(7, new FilterBooleans("ウォシュレット",true));
        filterSparseArray.append(8, new FilterBooleans("暖房便座",false));
        filterSparseArray.append(9, new FilterBooleans("自動開閉便座",false));
        filterSparseArray.append(10, new FilterBooleans("抗菌便座",false));
        filterSparseArray.append(11, new FilterBooleans("便座用シート",false));
        filterSparseArray.append(12, new FilterBooleans("",false));
        filterSparseArray.append(13, new FilterBooleans("",false));
        filterSparseArray.append(14, new FilterBooleans("",false));
        filterSparseArray.append(15, new FilterBooleans("",false));
        filterSparseArray.append(16, new FilterBooleans("",false));
        filterSparseArray.append(17, new FilterBooleans("",false));
        filterSparseArray.append(18, new FilterBooleans("",false));




       // FilterBooleans filterBooleans = new FilterBooleans(0,"",false);

        String boolString = filterSparseArray.get(0).booleanName;
        Boolean boolBoolean = filterSparseArray.get(0).booleanValue;

        Log.i("boolStringOne",boolString);
        Log.i("boolBooleanOne", String.valueOf(boolBoolean));

        String boolStringTwo = filterSparseArray.get(1).booleanName;
        Boolean boolBooleanTwo = filterSparseArray.get(1).booleanValue;

        Log.i("boolStringTwo",boolStringTwo);
        Log.i("boolBooleanTwo", String.valueOf(boolBooleanTwo));








//        Map<Integer, FilterBooleans> studenMap = new HashMap<Integer, FilterBooleans>();
//        String[] studentInformationArray = new String[]{"name", "address", "email"};
//        int studenId = 1;
//        studenMap.put(studenId, studentInformationArray);
//
//        String name = studenMap.get(studenId)[1];
//        String address = studenMap.get(studenId)[2];
//        String email = studenMap.get(studenId)[3];



//        List<FilterBooleans> filterBooleans = new ArrayList<FilterBooleans>();
//
//        filterBooleans.add(new FilterBooleans(0,"Japanese",false));
//
//        filterMap.put(0,filterBooleans);
//
//
//
//
//        FilterBooleans values = filterMap[0];




       // List<FilterBooleans> values = filterMap[0, fi];




        //FilterBooleansData
        //booleanValue.put(0,Filter.airConditionFilter);



        //filterList.add(booleanValue<2,false>);



        //filterList.add(FilterBooleans(3,"",false));



//        filterList.add("和式トイレ");
//        filterList.add("和式トイレ");
//        filterList.add("和式トイレ");
//        filterList.add("和式トイレ");
//        filterList.add("和式トイレ");

        createRecyclerView(filterSparseArray);

    }


    private void createRecyclerView(SparseArray array) {
        Log.i("reviewRecycle", "Called");
        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new FilterListAdapter(array);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
        recyclertView.addItemDecoration(dividerItemDecoration);


    }


//
//    private void createRecyclerView(List filterList) {
//        Log.i("reviewRecycle", "Called");
//        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
//        adapter = new FilterListAdapter(filterList);
//        layoutManager = new LinearLayoutManager(this);
//        recyclertView.setLayoutManager(layoutManager);
//        recyclertView.setHasFixedSize(true);
//        recyclertView.setAdapter(adapter);
//        Log.i("reviewRecycle", "Ended");
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
//        recyclertView.addItemDecoration(dividerItemDecoration);
//
//
//    }
}
