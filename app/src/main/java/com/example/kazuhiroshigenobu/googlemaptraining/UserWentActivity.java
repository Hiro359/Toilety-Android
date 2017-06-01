package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class UserWentActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView toolbarTitle;

//    private DatabaseReference favRef;
    private DatabaseReference toiletRef;

//    private ToiletListAdapter adapter;
//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_went);

        toolbar = (Toolbar) findViewById(R.id.user_went_app_bar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.userWentTitleText);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Log.i("Current.key","is This working???12321");
                        Intent intent = new Intent(v.getContext(),AccountActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );


        favoriteListQuery();

    }


    private void favoriteListQuery()
    {

        DatabaseReference favRef;
        final List<Toilet> toiletData = new ArrayList<>();
        toiletRef = FirebaseDatabase.getInstance().getReference().child("NoFilter");
        favRef = FirebaseDatabase.getInstance().getReference().child("UserWentList");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();


            //Changed to single June 1
            favRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Get tid lists
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        final String queryKey = child.getKey();
                        toiletRef.child(queryKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Toilet toilet = new Toilet();


                                toilet.name = (String) dataSnapshot.child("name").getValue();

                                toilet.latitude = (Double) dataSnapshot.child("latitude").getValue();
                                toilet.longitude = (Double) dataSnapshot.child("longitude").getValue();


                                LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                                LatLng toiletLocation = new LatLng(toilet.latitude, toilet.longitude);

                                Log.i("centerLocationFAV", String.valueOf(centerLocation));

                                double distance = CalculationByDistance(centerLocation, toiletLocation);
                                toilet.distanceNumberString = String.valueOf(distance);


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

                                toilet.key = queryKey;
                                toilet.name = (String) dataSnapshot.child("name").getValue();
                                toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                                toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();
                                Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                                toilet.reviewCount = reviewCount.intValue();
                                Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                                toilet.averageWait = averageWait.intValue();


                                toiletData.add(toilet);
                                createRecyclerView(toiletData);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        // child == tid


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //Child uid .. get UID

        }
    }

    @SuppressWarnings("unchecked")
    public void createRecyclerView(List toiletData) {
        Log.i("createReclerView()Caled", "");
        ToiletListAdapter adapter;
        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        recyclertView = (RecyclerView) findViewById(R.id.toiletWentList);
        adapter = new ToiletListAdapter(toiletData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("createReclerView()Ended", "");



    }

}
