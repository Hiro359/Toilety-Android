package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;

public class ReviewToiletViewActivity extends AppCompatActivity implements ReviewListAdapter.ReviewAdapterCallback {


//    private RecyclerView recyclertView;
//    private RecyclerView.LayoutManager layoutManager;
//    private ReviewListAdapter adapter;

//    private DatabaseReference toiletReviewsRef;
//    private DatabaseReference reviewsRef;
//    private DatabaseReference thumbsUpRef;

    private DatabaseReference userRef;


    final List<Review> reviewList = new ArrayList<>();

    Set<String> thumbsUpSet = new HashSet();
    // private Boolean viewOnceLoaded = false;



    Toilet toilet =  new Toilet();
    Toolbar toolbar;
    TextView toolbarTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_toilet_view);

        UserInfo.viewloaded = false;

        toolbar = (Toolbar) findViewById(R.id.app_bar_toilet_review_list_view);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.reviewListViewTitleText);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toilet.key = getIntent().getStringExtra("EXTRA_SESSION_ID");
        final Double toiletLat = getIntent().getDoubleExtra("toiletLatitude",0);
        final Double toiletLon = getIntent().getDoubleExtra("toiletLongitude",0);


        toolbar.setNavigationOnClickListener(
                new View.OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        Log.i("Current.key","is This working???12321");
                        Intent intent = new Intent(getApplicationContext(),DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", toilet.key);
                        intent.putExtra("toiletLatitude",toiletLat);
                        intent.putExtra("toiletLongitude",toiletLon);

                        UserInfo.viewloaded = false;
                        startActivity(intent);
                        finish();
                        //onBackPressed();
                        //finish();
                    }
                }
        );

        Log.i("ReivewToiletList Loaded","Yeah");


        thumbsUpQuery();
        //reviewQuery(key);
    }

    @Override
    protected void onDestroy() {
        Log.i("viewDestory","True");
        UserInfo.viewloaded = false;
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void createRecyclerView(List reviewList) {
        RecyclerView recyclertView;
        RecyclerView.LayoutManager layoutManager;
        ReviewListAdapter adapter;
        Log.i("reviewRecycle", "Called");
        recyclertView = (RecyclerView) findViewById(R.id.toiletReviewList);
        adapter = new ReviewListAdapter(reviewList,this);
        //adapter = new ToiletListAdapter(reviewData);
        layoutManager = new LinearLayoutManager(this);
        recyclertView.setLayoutManager(layoutManager);
        recyclertView.setHasFixedSize(true);
        recyclertView.setAdapter(adapter);
        Log.i("reviewRecycle", "Ended");

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclertView.getContext(),VERTICAL);
        recyclertView.addItemDecoration(dividerItemDecoration);


    }

    private void thumbsUpQuery(){


        DatabaseReference thumbsUpRef;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            thumbsUpRef = FirebaseDatabase.getInstance().getReference().child("ThumbsUpList");


            thumbsUpRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        final String ridkey = child.getKey();


                        thumbsUpSet.add(ridkey);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            toiletReviewQuery(toilet.key);
        }
    }

    private void toiletReviewQuery(String queryKey){
        // final List<Review> reviewList = new ArrayList<>();
        DatabaseReference toiletReviewsRef;


        toiletReviewsRef = FirebaseDatabase.getInstance().getReference().child("ToiletReviews");

        toiletReviewsRef.child(queryKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!UserInfo.viewloaded){

                    Log.i("DataSnap112233", String.valueOf(dataSnapshot));

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {

                        Log.i("DataChild112233", String.valueOf(child.getKey()));


                        //final String ridKey = child.getValue().toString();

                        final String ridKey = child.getKey();

                        getReviewInfoAndUserInfo(ridKey);


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void getReviewInfoAndUserInfo(final String ridKey){


        DatabaseReference reviewsRef;
//        final List<Review> reviewList = new ArrayList<>();

        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");


        reviewsRef.child(ridKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!UserInfo.viewloaded){
                    final Review review = new Review();


                    review.rid = ridKey;

                    if (thumbsUpSet.contains(review.rid)){
                        review.userLiked = true;
                    }



                    review.uid = (String) dataSnapshot.child("uid").getValue();
                    review.feedback = (String) dataSnapshot.child("feedback").getValue();
                    review.time = (String) dataSnapshot.child("time").getValue();
                    review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();


                    Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
                    review.likedCount = likedCount.intValue();
//                Long star = (Long) dataSnapshot.child("star").getValue();
//                review.star = star.doubleValue();


                    review.star = (String) dataSnapshot.child("star").getValue();




                    userRef = FirebaseDatabase.getInstance().getReference().child("Users");

                    userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!UserInfo.viewloaded) {
                                Log.i("userInfoQuery", "Called");



                                review.userName = (String) dataSnapshot.child("userName").getValue();
                                Log.i("userInfoQuery1", "Called");
                                review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
                                Log.i("userInfoQuery2", "Called");
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

                                Log.i("reviewQueryCalled", "End");

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onReviewMethodCallback(final String rid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle(rid);
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"報告する", "報告しない"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                whatIsTheProblem(rid);
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void whatIsTheProblem(final String rid){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle("問題だと思う点を教えてください");
        //Set title localization
        builder.setItems(new CharSequence[]
                        {"感想の内容に誤りがある", "感想の内容が不適切である","ユーザーの写真が不適切である"
                                ,"ユーザーの名前が不適切である","いいえ、問題はありません"},

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                reviewReportUploadToDatabase("The content of the review is not correct",rid);
                                break;
                            case 1:
                                reviewReportUploadToDatabase("The content of the review is not relevent",rid);
                                break;
                            case 2:
                                reviewReportUploadToDatabase("The picture of the user is not appropriate",rid);
                                break;
                            case 3:
                                reviewReportUploadToDatabase("The name of the user is not appropriate",rid);
                                break;
                            case 4:
                                break;


                        }
                    }
                });
        builder.create().show();

    }

    private void reviewReportUploadToDatabase(String problemString, String rid){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();



            DatabaseReference reviewProblemRef = FirebaseDatabase.getInstance().getReference().child("ReviewProblems");

            reviewProblemRef.child(postId).setValue(new ReviewReport(
                  rid,uid, timeString, timeStampDouble, problemString)

            );

            Log.i("Post Done", "222222");




        }


    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        cal.setTimeInMillis(time);

        // String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        Log.i("TIME121", DateFormat.format("yyyy-MM-dd", cal).toString());
        return DateFormat.format("yyyy-MM-dd", cal).toString();
    }


    private String getHour() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = c.get(java.util.Calendar.MINUTE);

        return "-" + String.valueOf(hour) + ":" + String.valueOf(minute);
//        SimpleDateFormat simpleDateFormatArrivals = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        return  simpleDateFormatArrivals;
    }




    //
//
//
//
//
//    private void reviewQuery(String queryKey) {
//
//
//        final List<Review> reviewList = new ArrayList<>();
//
//        toiletReviewsRef = FirebaseDatabase.getInstance().getReference().child("ToiletReviews");
//        toiletReviewsRef.child(queryKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for (final DataSnapshot child : dataSnapshot.getChildren()) {
//
//                    final String ridKey = child.getKey();
//
//
//
//                    reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
//
//
//                    reviewsRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//
//
//
//
//                Log.i("reviewQueryCalled","Start");
//
//                review.uid = (String) dataSnapshot.child("uid").getValue();
//                review.feedback = (String) dataSnapshot.child("feedback").getValue();
//                review.time = (String) dataSnapshot.child("time").getValue();
//                review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//
//
//                Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
//                review.likedCount = likedCount.intValue();
////                Long star = (Long) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//                review.star = (String) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//
//                Log.i("reviewQueryCalled","Middle");
//
//                //this may cause an error, it might be needed to be long
//
//                userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i("userInfoQuery","Called");
//
//
//                        review.userName = (String) dataSnapshot.child("userName").getValue();
//                        Log.i("userInfoQuery1","Called");
//                        review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
//                        Log.i("userInfoQuery2","Called");
////                        review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
////                        Log.i("review.waitingTime",review.waitingtime);
//
//
//
//
//
//
//                        Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
//                        Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
//                        Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
//                        review.totalLikedCount = totalLikedCount.intValue();
//                        review.totalFavoriteCount = totalFavoriteCount.intValue();
//                        review.totalHelpedCount = totalHelpedCount.intValue();
//
//
//                        reviewList.add(review);
//                        createRecyclerView(reviewList);
//
//                        Log.i("reviewQueryCalled","End");
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        reviewsRef = FirebaseDatabase.getInstance().getReference().child("ReviewInfo");
//        //Not sure i get the right toilet.key
//        reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//                final Review review = new Review();
//
//
//
//                Log.i("reviewQueryCalled","Start");
//
//                review.uid = (String) dataSnapshot.child("uid").getValue();
//                review.feedback = (String) dataSnapshot.child("feedback").getValue();
//                review.time = (String) dataSnapshot.child("time").getValue();
//                review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
//
//
//                Long likedCount = (Long) dataSnapshot.child("likedCount").getValue();
//                review.likedCount = likedCount.intValue();
////                Long star = (Long) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//                review.star = (String) dataSnapshot.child("star").getValue();
////                review.star = star.doubleValue();
//
//
//
//                Log.i("reviewQueryCalled","Middle");
//
//                //this may cause an error, it might be needed to be long
//
//                userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                userRef.child(review.uid).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i("userInfoQuery","Called");
//
//
//                        review.userName = (String) dataSnapshot.child("userName").getValue();
//                        Log.i("userInfoQuery1","Called");
//                        review.userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
//                        Log.i("userInfoQuery2","Called");
////                        review.waitingtime = (String) dataSnapshot.child("waitingtime").getValue();
////                        Log.i("review.waitingTime",review.waitingtime);
//
//
//
//
//
//
//                        Long totalLikedCount = (Long) dataSnapshot.child("totalLikedCount").getValue();
//                        Long totalFavoriteCount = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
//                        Long totalHelpedCount = (Long) dataSnapshot.child("totalHelpedCount").getValue();
//                        review.totalLikedCount = totalLikedCount.intValue();
//                        review.totalFavoriteCount = totalFavoriteCount.intValue();
//                        review.totalHelpedCount = totalHelpedCount.intValue();
//
//
//                        reviewList.add(review);
//                        createRecyclerView(reviewList);
//
//                        Log.i("reviewQueryCalled","End");
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    //reviewsRef.orderByChild("tid").equalTo(queryKey).addChildEventListener(new ChildEventListener)

}
