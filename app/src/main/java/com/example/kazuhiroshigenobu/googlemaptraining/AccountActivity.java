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
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {


    Button buttonAddToielt;
    Button buttonFavorite;
    Button buttonYouWent;
    Button buttonYouAddd;

    ImageView userAccountImage;

    Toolbar toolbar;
    TextView toolbarTitle;
    TextView accountNameText;
    TextView likeCountText;
    TextView favoriteCountText;
    TextView helpedCountText;
    private DatabaseReference userRef;

    Boolean userAlreadyLogin = false;

    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        FirebaseAuth firebaseAuth;

        Log.i("Account Loaded", "Start");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            Log.i("Login 88888", "Start");
            String userID = firebaseAuth.getCurrentUser().getUid();

            userDataCheck(userID);


        } else  {

            Log.i("Login Null 88888", "Start");

        }

        userAccountImage = (ImageView) findViewById(R.id.imageViewForAccount);

        buttonAddToielt = (Button) findViewById(R.id.buttonAddToilet);
        buttonFavorite = (Button) findViewById(R.id.buttonFavoriteList);
        buttonYouWent = (Button) findViewById(R.id.buttonToiletYouWent);
        buttonYouAddd = (Button) findViewById(R.id.buttonYouAdded);
        accountNameText = (TextView) findViewById(R.id.userAccountName);
        likeCountText = (TextView) findViewById(R.id.likeCount);
        favoriteCountText =  (TextView) findViewById(R.id.favoriteCount);
        helpedCountText = (TextView) findViewById(R.id.helepedCount);


        toolbar = (Toolbar) findViewById(R.id.app_bar4);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle4);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
                finish();
            }
        }
        );


        buttonAddToielt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), AddToiletActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), FavotiteListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonYouWent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), UserWentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        buttonYouAddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userAlreadyLogin){
                    loginPlease();

                }else {
                    Toast.makeText(AccountActivity.this, "FAV", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), CommentListView.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.accountbuttonmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settingButton) {

            if (!userAlreadyLogin){

                loginPlease();
                return true;
            } else {

                Toast.makeText(this, "Hey Did you Click Setting??", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SettingViewActivity.class);
                startActivity(intent);
                finish();
                return true;
            }


        }  else {

            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();

           return super.onOptionsItemSelected(item);
        }
    }

    private void loginPlease(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("この機能を利用するにはログインが必要です");
        builder.setItems(new CharSequence[]
                        {"ログインをする", "ログインをしない"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), FirstTimeActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void userDataCheck(final String userID){
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        //Changed to single June 1
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    Toast.makeText(AccountActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i("User", "Found");
                    userAlreadyLogin = true;
                    getUserInfo(userID);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    private void getUserInfo(final String userID){
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        //addValueEventListener(new ValueEventListener() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userPhoto = (String) dataSnapshot.child("userPhoto").getValue();
                String userName = (String) dataSnapshot.child("userName").getValue();
                Long likeNumber = (Long) dataSnapshot.child("totalLikedCount").getValue();
                Long favoriteNumber = (Long) dataSnapshot.child("totalFavoriteCount").getValue();
                Long helpedNumber = (Long) dataSnapshot.child("totalHelpedCount").getValue();

                String likeString = String.valueOf(likeNumber);
                String favoString = String.valueOf(favoriteNumber);
                String helpString = String.valueOf(helpedNumber);

                UserInfo.userImageURL = userPhoto;
                UserInfo.userName = userName;



                accountNameText.setText(userName);
                likeCountText.setText(likeString);
                favoriteCountText.setText(favoString);
                helpedCountText.setText(helpString);

                if (!userPhoto.equals("")){
                    Uri uri = Uri.parse(userPhoto);
                    Picasso.with(getApplicationContext()).load(uri).into(userAccountImage);

                } else {
                    userAccountImage.setImageResource(R.drawable.app_default_user_icon);
                }

                 userAccountImage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         zoomImageFromThumb(userAccountImage, userAccountImage);
                     }
                 });

                 Toast.makeText(AccountActivity.this, userName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void zoomImageFromThumb(final View thumbView, ImageView image)
    {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.



        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);



        //Use Picaso June 11



        expandedImageView.setImageDrawable(image.getDrawable());
        expandedImageView.setBackgroundColor(Color.parseColor("#F3F3F3"));



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

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.imageViewForAccount)
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
        thumbView.setAlpha(0f);
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
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
