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
 * Created by KazuhiroShigenobu on 8/3/17.
 */
//
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

        private LayoutInflater inflator;
        //List<Toilet> toiletData;

        List<Review> reviewList;
        //String [] toiletNames;


        public ReviewListAdapter(List<Review> reviewList){
            this.reviewList = reviewList;
           // this.toiletData = toiletData;

        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflator.inflate(R.layout.custom_row,parent,false);
//        MyViewHolder holder = new MyViewHolder(view);

            Log.i("MyMyholder12345", "12345");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_custom_row,parent,false);

            MyViewHolder holder = new MyViewHolder(view);

            Log.i("holder12345", String.valueOf(holder));

            return holder;
        }



        @Override
        public void onBindViewHolder(com.example.kazuhiroshigenobu.googlemaptraining.ReviewListAdapter.MyViewHolder holder, int position) {


            if (reviewList.isEmpty()) {
                Log.i("current.key", "Its empty");

            } else {
                Log.i("toiletDataContent.get0", String.valueOf(reviewList.get(0)));
                Log.i("current.key", "Its Not empty");
                Log.i("current.position", String.valueOf(position));
                Log.i("current.holder", String.valueOf(holder));


                final Review current = reviewList.get(position);

                Log.i("current.key", String.valueOf(position));


//                Uri uri = Uri.parse(current.urlOne);
//
//
//
//                final Context context = holder.image.getContext();
//
////                float starFloatValue = Float.parseFloat(current.averageStar);
//
//
//                holder.name.setText(current.key);
//                holder.starRate.setText(current.averageStar);
//                holder.waitingTime.setText("待ち時間"+ String.valueOf(current.averageWait) +"分");
//                holder.ratingBar.setRating(starFloatValue);
//                holder.distance.setText(current.distance);
//
//                Picasso.with(context).load(uri).into(holder.image);
//
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

            Log.i("toiletData.size()",String.valueOf(reviewList.size()));
            return reviewList.size();


        }


        public class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView reviewUserImage;
            TextView reviewUserName;
            TextView reviewLikeCount;
            TextView reviewFavoriteCount;
            TextView reviewManCount;
            RatingBar reviewStarBar;
            TextView reviewWaitingTime;
//            TextView name;
//            //        TextView type;
//            TextView waitingTime;
//            TextView starRate;
//            RatingBar ratingBar;
//            TextView distance;
//            ImageView image;


            public MyViewHolder(View itemView) {
                super(itemView);

                reviewUserImage = (ImageView) itemView.findViewById(R.id.reviewUserImage);
                reviewUserName = (TextView) itemView.findViewById(R.id.reviewUserName);
                reviewLikeCount = (TextView) itemView.findViewById(R.id.reviewLikeCount);
                reviewFavoriteCount = (TextView) itemView.findViewById(R.id.reviewFavoriteCount);
                reviewManCount = (TextView) itemView.findViewById(R.id.reviewManCount);
                reviewStarBar = (RatingBar) itemView.findViewById(R.id.reviewRatingBar);
                reviewWaitingTime = (TextView) itemView.findViewById(R.id.reviewWaitingTime);

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


