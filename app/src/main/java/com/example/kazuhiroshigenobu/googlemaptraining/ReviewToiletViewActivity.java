package com.example.kazuhiroshigenobu.googlemaptraining;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;

public class ReviewToiletViewActivity extends AppCompatActivity implements ReviewListAdapter.ReviewAdapterCallback, ReviewListAdapter.ReviewAdapterExpandImageCallBack {


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
    //Boolean userWarningLoadedOnce = false;
    //Boolean reviewWarningLoadedOnce = false;
    private String suspiciosUserId;
    private String suspiciosReviewId;



    Toilet toilet =  new Toilet();
    Toolbar toolbar;
    TextView toolbarTitle;

    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;




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


            //Changed to single June 1
            thumbsUpRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference toiletReviewRef;


        toiletReviewRef = FirebaseDatabase.getInstance().getReference().child("ToiletReview");

        //Changed to single June 1
        toiletReviewRef.child(queryKey).addListenerForSingleValueEvent(new ValueEventListener() {
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


        //Changed to single June 1
        reviewsRef.child(ridKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    //Changed to single June 1
                    userRef.child(review.uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void onReviewImageExpandCallback(String imageUrl) {

        Log.i("onReviewImage ccc", "666");

        zoomImageFromUrl(imageUrl);



    }

    @Override
    public void onReviewMethodCallback(final String rid, final String suspiciouUser) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        suspiciosReviewId = rid;
        //AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setTitle(rid);
        suspiciosUserId = suspiciouUser;
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
                                reviewReportUploadToDatabase(0,rid);
                                //userWarningsListUpload();
                                break;
                            case 1:
                                reviewReportUploadToDatabase(1,rid);
                                //userWarningsListUpload();
                                break;
                            case 2:
                                reviewReportUploadToDatabase(2,rid);
                                //userWarningsListUpload();
                                break;
                            case 3:
                                reviewReportUploadToDatabase(3,rid);
                                //userWarningsListUpload();
                                break;
                            case 4:
                                break;


                        }
                    }
                });
        builder.create().show();

    }

    private void reviewReportUploadToDatabase(Integer problemInt, String rid){

//        reviewWarningsListUpload();
//        userWarningsListUpload();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            long timeStamp = System.currentTimeMillis();
            Double timeStampDouble = Double.parseDouble(String.valueOf(timeStamp));
            String timeString = getDate(timeStamp) + getHour();

            String postId = UUID.randomUUID().toString();


            Log.i("Post Done", "222222");

            Map<String, Object> updateInfo = new HashMap();


            Map<String, Object> problemData = new HashMap();



            problemData.put("rid", rid);
            problemData.put("uid", uid);
            problemData.put("time", timeString);
            problemData.put("timeNumbers", timeStampDouble);
            problemData.put("problem", problemInt);



            updateInfo.put("ReviewProblems/" + postId, problemData);
            updateInfo.put("UserWarningList" + suspiciosUserId + "/" + uid, true);


            DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

            firebaseRef.updateChildren(updateInfo,new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                }
            });

        }


    }

//    private void userWarningsListUpload(){
//
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("UserWarningList");
////        if (user != null){
////            String uid = user.getUid();
////            userWarningsListRef.child(suspiciosUserId).child(uid).setValue(true);
////        }
//
//    }

//    private void userWarningCount(){
//        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("UserWarningList");
//
//
//        userWarningsListRef.child(suspiciosUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                    //Call Once //Maybe I need boolean filter
//                    Long warningCount = dataSnapshot.getChildrenCount();
//                    userWarningCountUpload(warningCount);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void userWarningCountUpload(Long warningCount){
//        DatabaseReference userWarningsCountRef = FirebaseDatabase.getInstance().getReference().child("UserWarningCount");
//
//        userWarningsCountRef.child(suspiciosUserId).setValue(warningCount);
//
//    }

    ///
//    private void reviewWarningsListUpload(){
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningList");
//        if (user != null){
//            String uid = user.getUid();
//            userWarningsListRef.child(suspiciosReviewId).child(uid).setValue(true);
//            //reviewWarningCount();
//        }
//
//    }

//    private void reviewWarningCount(){
//        DatabaseReference userWarningsListRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningList");
//
//
//        userWarningsListRef.child(suspiciosReviewId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                    //Call Once //Maybe I need boolean filter
//                    Long warningCount = dataSnapshot.getChildrenCount();
//                    reviewWarningCountUpload(warningCount);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void reviewWarningCountUpload(Long warningCount){
//        DatabaseReference userWarningsCountRef = FirebaseDatabase.getInstance().getReference().child("ReviewWarningCount");
//
//        userWarningsCountRef.child(suspiciosReviewId).setValue(warningCount);
//
//    }



    ///


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

    //private void zoomImageFromThumb(final View thumbView, ImageView image)

    private void zoomImageFromUrl(String imageUrl)
    //, int imageResId)
    {

        final View viewBefore = findViewById(R.id.expanded_image_before);
        Log.i("zoom Called","666");
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);

        //Use Picaso June 11


        if (imageUrl.equals("")) {
            expandedImageView.setImageResource(R.drawable.app_default_user_icon);
        } else {
            Uri uri = Uri.parse(imageUrl);
            final Context context = expandedImageView.getContext();
            Picasso.with(context).load(uri).into(expandedImageView);

        }

        expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));


//        expandedImageView.setImageDrawable(image.getDrawable());
//        expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));



        //expandedImageView.setImageResource(imageResId);




        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).

        viewBefore.getGlobalVisibleRect(startBounds);
        findViewById(R.id.activity_review_toilet_view)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        //Not Sure June 11 changed to activit_detail_view..
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        viewBefore.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewBefore.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        viewBefore.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
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
