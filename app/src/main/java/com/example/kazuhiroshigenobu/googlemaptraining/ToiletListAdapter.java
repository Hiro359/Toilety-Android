package com.example.kazuhiroshigenobu.googlemaptraining;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;




/**
 * Created by KazuhiroShigenobu on 15/2/17.
 */

public class ToiletListAdapter extends RecyclerView.Adapter<ToiletListAdapter.MyViewHolder> {

    private LayoutInflater inflator;
    List<Toilet> toiletData;
    //String [] toiletNames;


    public ToiletListAdapter( List<Toilet> toiletData){

        this.toiletData = toiletData;


    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflator.inflate(R.layout.custom_row,parent,false);
//        MyViewHolder holder = new MyViewHolder(view);

        Log.i("MyMyholder12345", "12345");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row,parent,false);

        MyViewHolder holder = new MyViewHolder(view);

        Log.i("holder12345", String.valueOf(holder));

        return holder;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if (toiletData.isEmpty()) {
            Log.i("current.key", "Its empty");

        } else {
            Log.i("toiletDataContent.get0", String.valueOf(toiletData.get(0)));
            Log.i("current.key", "Its Not empty");
            Log.i("current.position", String.valueOf(position));
            Log.i("current.holder", String.valueOf(holder));


            Toilet current = toiletData.get(position);

            Log.i("current.key", String.valueOf(position));


            Uri uri = Uri.parse(current.urlOne);


            Context context = holder.image.getContext();

            float starFloatValue = Float.parseFloat(current.averageStar);


            holder.name.setText(current.key);
            holder.starRate.setText(current.averageStar);
            holder.waitingTime.setText("待ち時間"+ String.valueOf(current.averageWait) +"分");
            holder.ratingBar.setRating(starFloatValue);
            holder.distance.setText(current.distance);

            Picasso.with(context).load(uri).into(holder.image);



        }
    }


    @Override
    public int getItemCount() {

        Log.i("toiletData.size()",String.valueOf(toiletData.size()));
        return toiletData.size();


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
//        TextView type;
        TextView waitingTime;
        TextView starRate;
        RatingBar ratingBar;
        TextView distance;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
//            type = (TextView) itemView.findViewById(R.id.);
            waitingTime = (TextView) itemView.findViewById(R.id.tv_waitingTime);
            starRate = (TextView) itemView.findViewById(R.id.tv_starRate);
            ratingBar = (RatingBar) itemView.findViewById(R.id.tv_starRateBar);
            distance = (TextView) itemView.findViewById(R.id.distance);
            image = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }

}
