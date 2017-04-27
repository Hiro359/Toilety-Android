package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewToiletViewActivity extends AppCompatActivity {


    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewListAdapter adapter;

    private DatabaseReference toiletRef;
    private DatabaseReference reviewsRef;
    private DatabaseReference userRef;



    Toilet toilet =  new Toilet();
    Toolbar toolbar;
    TextView toolbarTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_toilet_view);

        toolbar = (Toolbar) findViewById(R.id.app_bar_toilet_review_list_view);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.reviewListViewTitleText);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        final Double toiletLat = getIntent().getDoubleExtra("toiletLatitude",0);
        final Double toiletLon = getIntent().getDoubleExtra("toiletLongitude",0);


        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Log.i("Current.key","is This working???12321");
                        Intent intent = new Intent(getApplicationContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", key);
                        intent.putExtra("toiletLatitude",toiletLat);
                        intent.putExtra("toiletLongitude",toiletLon);

                        startActivity(intent);
                        finish();
                        //onBackPressed();
                        //finish();
                    }
                }
        );

        Log.i("ReivewToiletList Loaded","Yeah");

        reviewQuery(key);
    }

    public void createRecyclerView(List reviewList) {
        Log.i("reviewRecycle", "Called");
        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new ReviewListAdapter(reviewList);
        //adapter = new ToiletListAdapter(reviewData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");


    }

    private void reviewQuery(String queryKey) {


        final List<Review> reviewList = new ArrayList<>();

        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
        //Not sure i get the right toilet.key
        reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                final Review review = new Review();



                Log.i("reviewQueryCalled","Start");

                review.uid = (String) dataSnapshot.child("uid").getValue();
                review.feedback = (String) dataSnapshot.child("feedback").getValue();
                review.time = (String) dataSnapshot.child("time").getValue();
                review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();


                Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
                review.likedCount = likedCount.intValue();
//                Long star = (Long) dataSnapshot.child("star").getValue();
//                review.star = star.doubleValue();


                review.star = (String) dataSnapshot.child("star").getValue();
//                review.star = star.doubleValue();



                Log.i("reviewQueryCalled","Middle");

                //this may cause an error, it might be needed to be long

                userRef = FirebaseDatabase.getInstance().getReference().child("Users");

                userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("userInfoQuery","Called");


                        review.userName = (String) dataSnapshot.child("userName").getValue();
                        Log.i("userInfoQuery1","Called");
                        review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
                        Log.i("userInfoQuery2","Called");
//                        review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//                        Log.i("review.waitingTime",review.waitingtime);






                        Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
                        Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                        Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
                        review.totalLikedCount = totalLikedCount.intValue();
                        review.totalFavoriteCount = totalFavoriteCount.intValue();
                        review.totalHelpedCount = totalHelpedCount.intValue();


                        reviewList.add(review);
                        createRecyclerView(reviewList);

                        Log.i("reviewQueryCalled","End");


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });








            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener)
    }
}
