package com.example.kazuhiroshigenobu.googlemaptraining;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class FilterActivity extends ListActivity{
//    //extends AppCompatActivity to ListActivity
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_filter);
////    }
//private CustomAdapter mAdapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAdapter = new CustomAdapter(this);
//
////        for (int i = 1; i < 20; i++) {
////            mAdapter.addItem("Row Item #" + i);
////            if (i % 4 == 0) {
////                mAdapter.addSectionHeaderItem("Section #" + i);
////            }
////        }
//
//        mAdapter.addSectionHeaderItem("距離");
//        mAdapter.addUserInputData("HEYEHEY");
//        mAdapter.addItem("距離");
//        mAdapter.addSectionHeaderItem("並び順");
//        mAdapter.addItem("並び順");
//        mAdapter.addSectionHeaderItem("詳細");
//        mAdapter.addItem("全てのトイレを検索");
//        mAdapter.addItem("星の数を検索");
//        mAdapter.addSectionHeaderItem("設備");
//        mAdapter.addItem("和式トイレ");
//        mAdapter.addItem("洋式トイレ");
//        mAdapter.addItem("女性専用トイレ");
//        mAdapter.addItem("男女兼用トイレ");
//        mAdapter.addItem("ウォシュレット");
//        mAdapter.addItem("暖房便座");
//        mAdapter.addItem("授乳スペース");
//        mAdapter.addItem("メイクルーム");
//        mAdapter.addItem("荷物置き");
//        mAdapter.addItem("車イス対応");
//        mAdapter.addItem("オストメイト");
//        mAdapter.addItem("利用可能");
//
//
//
//        setListAdapter(mAdapter);
//
//        ListView lv = (ListView) findViewById(android.R.id.list);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getBaseContext(), "Id = -1: " + id, Toast.LENGTH_SHORT).show();
//
//                //mAdapter.myCliquedPosition = position;
//               // debug.print("my pos in activity"+mAdapter.myCliquedPosition);
//                mAdapter.notifyDataSetInvalidated();
//            }
//        });
//
//    }
//
//
//

}
