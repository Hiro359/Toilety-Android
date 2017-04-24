package com.example.kazuhiroshigenobu.googlemaptraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.CalculationByDistance;
import static com.example.kazuhiroshigenobu.googlemaptraining.MapsActivity.round;

public class CommentListView extends AppCompatActivity {

    private DatabaseReference reviewListRef;
    private DatabaseReference reviewRef;
    private DatabaseReference toiletRef;
    UserReviewComment userReviewComment = new UserReviewComment();
    final List<UserReviewComment> commentData = new ArrayList<>();


    private UserCommentListAdapter adapter;
    private RecyclerView recyclertView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list_view);

        toiletRef = FirebaseDatabase.getInstance().getReference().child("Toilets");
        reviewRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reviewListRef = FirebaseDatabase.getInstance().getReference().child("ReviewList").child(uid);

        reviewRidQuery();
    }

    private void reviewRidQuery(){


        reviewListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    final String ridKey = child.getKey();
                    commnetsReviewInfoQuery(ridKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
        //get rid



    private void  commnetsReviewInfoQuery(String ridKey){
        //get review Info

        reviewRef.child(ridKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                userReviewComment.uid = (String) dataSnapshot.child("uid").getValue();
                userReviewComment.feedback = (String) dataSnapshot.child("feedback").getValue();
                userReviewComment.time = (String) dataSnapshot.child("time").getValue();
                userReviewComment.userWaitingtime = (String) dataSnapshot.child("waitingtime").getValue() + "分待ちました";

                userReviewComment.userRatedStar = (String) dataSnapshot.child("star").getValue();


                String tidKey = (String) dataSnapshot.child("tid").getValue();
                commentsToiletInfoQuery(tidKey);


//                 userReviewComment.name = (String) dataSnapshot.child("name").getValue();
//                 userReviewComment.urlOne = (String) dataSnapshot.child("urlOne").getValue();
//                toilet.urlOne = (String) dataSnapshot.child("urlOne").getValue();
//                toilet.averageStar = (String) dataSnapshot.child("averageStar").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void commentsToiletInfoQuery(String tidKey){
        //get toilet Info

        toiletRef.child(tidKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                userReviewComment.name = (String) dataSnapshot.child("name").getValue();
                userReviewComment.urlOne = (String) dataSnapshot.child("urlOne").getValue();
                userReviewComment.averageStar = (String) dataSnapshot.child("averageStar").getValue();
                Long reviewCount = (Long) dataSnapshot.child("reviewCount").getValue();
                userReviewComment.reviewCount = reviewCount.intValue();
                Long averageWait = (Long) dataSnapshot.child("averageWait").getValue();
                userReviewComment.avWaitingtime = String.valueOf(averageWait);
                userReviewComment.urlOne = (String) dataSnapshot.child("urlOne").getValue();


                userReviewComment.latitude = (Double) dataSnapshot.child("latitude").getValue();
                userReviewComment.longitude = (Double) dataSnapshot.child("longitude").getValue();

                //Distance Caluculation....

                LatLng centerLocation = new LatLng(UserInfo.latitude, UserInfo.longitude);
                LatLng toiletLocation = new LatLng(userReviewComment.latitude,userReviewComment.longitude);

                double distance = CalculationByDistance(centerLocation,toiletLocation);


                if (distance > 1){
                    userReviewComment.distance = String.valueOf(round(distance, 1)) + "km";
                    Log.i("toilet.distance", String.valueOf(userReviewComment.distance));
                    //Km

                }else{
                    Double meterDistance = distance * 100;
                    Integer meterA = meterDistance.intValue();
                    Integer meterB = meterA * 10;


                    userReviewComment.distance = String.valueOf(meterB) + "m";

                    Log.i("toilet.distance", String.valueOf(userReviewComment.distance));


                    commentData.add(userReviewComment);
                    createRecyclerView(commentData);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void createRecyclerView(List commentData) {
        Log.i("createReclerView()Caled", "");
        recyclertView = (RecyclerView) findViewById(R.id.commentListRecyclerView);
        adapter = new UserCommentListAdapter(commentData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("createReclerView()Ended", "");

    }

}
