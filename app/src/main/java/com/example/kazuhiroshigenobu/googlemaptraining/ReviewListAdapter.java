package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KazuhiroShigenobu on 8/3/17.
 */
//
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder> {

    private LayoutInflater inflator;
    //List<Toilet> toiletData;

    List<Review> reviewList;

    private DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference thumbsUpRef = firebaseRef.child("ThumbsUpList");
    private DatabaseReference reviewInfoRef = firebaseRef.child("ReviewInfo");
    private DatabaseReference userRef = firebaseRef.child("Users");
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    //String [] toiletNames;


    public ReviewListAdapter(List<Review> reviewList){
        this.reviewList = reviewList;
        // this.toiletData = toiletData;

    }



    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflator.inflate(R.layout.custom_row,parent,false);
//        MyViewHolder holder = new MyViewHolder(view);

        Log.i("Reviewholder12345", "12345");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_custom_row,parent,false);

        ReviewViewHolder holder = new ReviewViewHolder(view);

        Log.i("Reviewholder12345", String.valueOf(holder));

        return holder;
    }



    @Override
    public void onBindViewHolder(final com.example.kazuhiroshigenobu.googlemaptraining.ReviewListAdapter.ReviewViewHolder holder, int position) {


        if (reviewList.isEmpty()) {
            Log.i("Reviewcurrent.key", "Its empty");

        } else {

            final Review current = reviewList.get(position);


            holder.reviewUserName.setText(current.userName);
            holder.reviewLikeCount.setText(String.valueOf(current.totalLikedCount));
            holder.reviewFavoriteCount.setText(String.valueOf(current.totalFavoriteCount));
            holder.reviewManCount.setText(String.valueOf(current.totalHelpedCount));
            holder.reviewStarBar.setRating(Float.parseFloat(current.star));
            holder.reviewWaitingTime.setText(current.waitingtime + "分待ちました");
            holder.reviewFeedback.setText(current.feedback);
            holder.reviewDate.setText(current.time);
            holder.reviewLikeCountNextToButton.setText("いいね" + String.valueOf(current.likedCount)+ "件");


            if (current.userPhoto.equals("")) {
                holder.reviewUserImage.setImageResource(R.drawable.app_default_user_icon);
            } else {
                Uri uri = Uri.parse(current.userPhoto);
                final Context context = holder.reviewUserImage.getContext();
                Picasso.with(context).load(uri).into(holder.reviewUserImage);

            }

            if (current.userLiked){
                holder.reviewLikeButton.setBackgroundResource(R.drawable.app_thumb_icon_colored_24);

            } else {
                holder.reviewLikeButton.setBackgroundResource(R.drawable.app_thumb_icon_white_drawable);

            }



            //final Boolean buttonTapped = false;

            holder.reviewLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (current.userLiked){

                        UserInfo.viewloaded = true;
                        current.totalLikedCount = current.totalLikedCount - 1;
                        current.likedCount = current.likedCount - 1;

                        holder.reviewLikeCount.setText(String.valueOf(current.totalLikedCount));
                        holder.reviewLikeCountNextToButton.setText("いいね" + String.valueOf(current.likedCount)+ "件");
                        holder.reviewLikeButton.setBackgroundResource(R.drawable.app_thumb_icon_white_drawable);

                        thumbsUpRef.child(uid).child(current.rid).removeValue();

                        Log.i("ThumbsUp","Added");



                        //setBackground(R.drawable.app_thumb_icon_white_drawable);
                        current.userLiked = false;

                    } else {
                        UserInfo.viewloaded = true;
                        current.totalLikedCount = current.totalLikedCount + 1;
                        current.likedCount = current.likedCount + 1;
                        holder.reviewLikeCount.setText(String.valueOf(current.totalLikedCount));
                        holder.reviewLikeCountNextToButton.setText("いいね" + String.valueOf(current.likedCount)+ "件");
                        holder.reviewLikeButton.setBackgroundResource(R.drawable.app_thumb_icon_colored_24);

                        thumbsUpRef.child(uid).child(current.rid).setValue(true);
                        Log.i("ThumbsUp","Deleted");

                        current.userLiked = true;

                    }


                    Map<String, Object> usersChildUpdates = new HashMap<>();
                    usersChildUpdates.put("totalLikedCount",current.totalLikedCount);
                    userRef.child(current.uid).updateChildren(usersChildUpdates);

                    Map<String, Object> reviewInfoChildUpdates = new HashMap<>();
                    reviewInfoChildUpdates.put("likedCount",current.likedCount);
                    reviewInfoRef.child(current.rid).updateChildren(reviewInfoChildUpdates);




                }
            });


//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast.makeText(context, String.valueOf(current.key), Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(context, DetailViewActivity.class);
//                        intent.putExtra("EXTRA_SESSION_ID", current.key);
//                        Log.i("Current.key",current.key );
////                    intent.putExtra("EXTRA_SESSION_ID", current.key);
//                        context.startActivity(intent);
//                    }
//                });

//            final String element = toiletData[position];

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    onClickSubject.onNext(element);
//                }
//            });



        }
    }


    @Override
    public int getItemCount() {

        Log.i("ReviewtoiletData.size()",String.valueOf(reviewList.size()));
        return reviewList.size();


    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView reviewUserImage;
        TextView reviewUserName;
        TextView reviewLikeCount;
        TextView reviewFavoriteCount;
        TextView reviewManCount;
        RatingBar reviewStarBar;
        TextView reviewWaitingTime;
        TextView reviewFeedback;
        TextView reviewDate;
        TextView reviewLikeCountNextToButton;
        Button reviewLikeButton;
        //Boolean userLiked = false;

//            TextView name;
//            //        TextView type;
//            TextView waitingTime;
//            TextView starRate;
//            RatingBar ratingBar;
//            TextView distance;
//            ImageView image;


        public ReviewViewHolder(View itemView) {
            super(itemView);



            reviewUserImage = (ImageView) itemView.findViewById(R.id.reviewUserImage);
            reviewUserName = (TextView) itemView.findViewById(R.id.reviewUserName);
            reviewLikeCount = (TextView) itemView.findViewById(R.id.reviewLikeCount);
            reviewFavoriteCount = (TextView) itemView.findViewById(R.id.reviewFavoriteCount);
            reviewManCount = (TextView) itemView.findViewById(R.id.reviewManCount);
            reviewStarBar = (RatingBar) itemView.findViewById(R.id.reviewRatingBar);
            reviewWaitingTime = (TextView) itemView.findViewById(R.id.reviewWaitingTime);
            reviewFeedback = (TextView) itemView.findViewById(R.id.reviewFeedbackTextView);
            reviewDate = (TextView) itemView.findViewById(R.id.reviewDate);
            reviewLikeCountNextToButton = (TextView) itemView.findViewById(R.id.reviewLikeCountNextToButton);
            reviewLikeButton = (Button) itemView.findViewById(R.id.reviewLikeButton);


//                name = (TextView) itemView.findViewById(R.id.tv_name);
////            type = (TextView) itemView.findViewById(R.id.);
//                waitingTime = (TextView) itemView.findViewById(R.id.tv_waitingTime);
//                starRate = (TextView) itemView.findViewById(R.id.tv_starRate);
//                ratingBar = (RatingBar) itemView.findViewById(R.id.tv_starRateBar);
//                distance = (TextView) itemView.findViewById(R.id.distance);
//                image = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }

}


