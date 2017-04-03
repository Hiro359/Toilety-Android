package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KazuhiroShigenobu on 3/4/17.
 */

public class UserCommentListAdapter extends RecyclerView.Adapter<UserCommentListAdapter.UserCommentViewHolder> {

        private LayoutInflater inflator;
        List<Toilet> toiletData;
        //String [] toiletNames;


        public UserCommentListAdapter(List<Toilet> toiletData) {

            this.toiletData = toiletData;


        }


        @Override
        public UserCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflator.inflate(R.layout.custom_row,parent,false);
//        MyViewHolder holder = new MyViewHolder(view);

            Log.i("MyMyholder12345", "12345");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);

            UserCommentViewHolder holder = new UserCommentViewHolder(view);

            Log.i("holder12345", String.valueOf(holder));

            return holder;
        }


        @Override
        public void onBindViewHolder(UserCommentViewHolder holder, int position) {


            if (toiletData.isEmpty()) {
                Log.i("current.key", "Its empty");

            } else {
                Log.i("toiletDataContent.get0", String.valueOf(toiletData.get(0)));
                Log.i("current.key", "Its Not empty");
                Log.i("current.position", String.valueOf(position));
                Log.i("current.holder", String.valueOf(holder));


                final Toilet current = toiletData.get(position);

                Log.i("current.key", String.valueOf(position));


                //Uri uri = Uri.parse(current.urlOne);


                final Context context = holder.image.getContext();

                float starFloatValue = Float.parseFloat(current.averageStar);

                holder.toiletName.setText(current.name);
                holder.starRate.setText(current.averageStar);
                holder.waitingTime.setText("待ち時間" + String.valueOf(current.averageWait) + "分");
                holder.ratingBar.setRating(starFloatValue);
                holder.distance.setText(current.distance);

                if (current.urlOne.equals("")) {
                    holder.image.setImageResource(R.drawable.app_logo);

                } else {
                    Uri uri = Uri.parse(current.urlOne);
                    Picasso.with(context).load(uri).into(holder.image);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(context, String.valueOf(current.key), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, DetailViewActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", current.key);
                        intent.putExtra("toiletLatitude", current.latitude);
                        intent.putExtra("toiletLongitude", current.longitude);
                        Log.i("Current.key", current.key);
//                    intent.putExtra("EXTRA_SESSION_ID", current.key);
                        context.startActivity(intent);
                    }
                });

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

            Log.i("toiletData.size()", String.valueOf(toiletData.size()));
            return toiletData.size();


        }


        public class UserCommentViewHolder extends RecyclerView.ViewHolder {

            TextView toiletName;
            TextView waitingTime;
            TextView starRate;
            RatingBar ratingBar;
            TextView distance;
            ImageView image;
            TextView reviewTimeText;
            TextView userReviewText;
            RatingBar userRatedRatingBar;
            TextView userWaitedTime;


            public UserCommentViewHolder(View itemView) {
                super(itemView);
                toiletName = (TextView) itemView.findViewById(R.id.uc_name);
                waitingTime = (TextView) itemView.findViewById(R.id.uc_waitingTime);
                starRate = (TextView) itemView.findViewById(R.id.uc_starRate);
                ratingBar = (RatingBar) itemView.findViewById(R.id.uc_starRateBar);
                distance = (TextView) itemView.findViewById(R.id.userCommentDistance);
                image = (ImageView) itemView.findViewById(R.id.ucImageView);

                reviewTimeText = (TextView) itemView.findViewById(R.id.ucReviewTime);
                userReviewText = (TextView) itemView.findViewById(R.id.ucCommentText);
                userWaitedTime = (TextView) itemView.findViewById(R.id.ucUserWaitedTime);
                userRatedRatingBar = (RatingBar) itemView.findViewById(R.id.uc_starRateBarBefore);





            }
        }

    }

