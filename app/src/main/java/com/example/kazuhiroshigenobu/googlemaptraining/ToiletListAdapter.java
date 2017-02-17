package com.example.kazuhiroshigenobu.googlemaptraining;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

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




//    public ToiletListAdapter(String[] toiletNames){
//        this.toiletNames = toiletNames;
//
//    }

    public ToiletListAdapter( List<Toilet> toiletData){

        this.toiletData = toiletData;


    }

//    public ToiletListAdapter(Context context, List<Toilet> toiletData){
//
//       // inflator = LayoutInflater.from(context);
//        this.toiletData = toiletData;
//
//
//    }


//    private Context mCountext;
//    private List<Toilet> toiletList;
    //Not sure about this list....


//    public ToiletListAdapter(List<Toilet> toiletList, Context mCountext) {
//        this.toiletList = toiletList;
//        this.mCountext = mCountext;
//    }



//    @Override
//    public int getCount() {
//        return toiletList.size();
//    }
////
//    @Override
//    public Object getItem(int position) {
//        return toiletList.get(position);
//    }


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

//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflator.inflate(R.layout.custom_row,parent,false);
//        MyViewHolder holder = new MyViewHolder(view);
//
//
//        return holder;
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        if (toiletData != null) {
//            Toilet current = toiletData.get(position);
//            holder.name.setText(current.key);
//            //This code is wrong....
//        }

        if (toiletData.isEmpty()) {
            Log.i("current.key", "Its empty");

        } else {
            Log.i("toiletDataContent.get0", String.valueOf(toiletData.get(0)));
            Log.i("current.key", "Its Not empty");
            Log.i("current.position", String.valueOf(position));
            Log.i("current.holder", String.valueOf(holder));


            Toilet current = toiletData.get(position);

            Log.i("current.key", String.valueOf(position));


            holder.name.setText(current.openinghours);
           // holder.name.setText(String.valueOf(toiletData.get(position)));
           // holder.name.setText();

           // Log.i("current.key", current.key);
//        holder.name.setText(toiletNames[position]);

        }
    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public int getItemCount() {

        Log.i("toiletData.size()",String.valueOf(toiletData.size()));
        return toiletData.size();
       // return toiletData.
        //Log.i("toiletData.size()",String.valueOf(toiletData.size()));
        //Log.i("toiletData.size()",String.valueOf(toiletData.size()));

//        Log.i("toiletNames.length",String.valueOf(toiletNames.length));
//        return toiletNames.length;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
       // TextView type;
//        TextView waitingTime;
//        TextView starRate;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.listText);
           // type = (TextView) itemView.findViewById(R.id.listText);

//            waitingTime = (TextView) itemView.findViewById(R.id.tv_waitingTime);
//            starRate = (TextView) itemView.findViewById(R.id.tv_starRate);
            //Not sure R.id.tv_name works well.....
        }


//        TextView name;
//        TextView waitingTime;
//        TextView starRate;
//
//        public MyViewHolder(View v) {
//
//            name = (TextView) v.findViewById(R.id.tv_name);
//            waitingTime = (TextView) v.findViewById(R.id.tv_waitingTime);
//            starRate = (TextView) v.findViewById(R.id.tv_starRate);
//
//        }
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View v = convertView;
//        MyViewHolder holder = null;
//
//
//        if (v == null) {
//            //first time
//            LayoutInflater inflater = (LayoutInflater) mCountext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.toilet_information_list,parent,false);
//            holder = new MyViewHolder(v);
//
//            v.setTag(holder);
//
//        } else {
//            //recycling...
//            holder = (MyViewHolder)v.getTag();
//
//
//        }
//
//        holder.name.setText();
//
//
//        return v;
//    }

//
//
//
//        v = View.inflate(mCountext, R.layout.toilet_information_list, null);
//        TextView tvName = (TextView) v.findViewById(R.id.tv_name);
//        TextView tvWait = (TextView) v.findViewById(R.id.tv_waitingTime);
//        TextView tvStar = (TextView) v.findViewById(R.id.tv_starRate);
//
//        tvName.setText(toiletList.get(position).getKey();
//        tvStar.setText(String.valueOf(toiletList.get(position).getStar()));
//        tvWait.setText(String.valueOf(toiletList.get(position).getWaitingtime()));
//
//        //Save toilet id ??....
//
//        v.setTag(toiletList.get(position).getKey());
//
//
//
//        return v;
//
//    }

//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        ViewHolder mViewHolder = null;
//        //HashMap<String, String> song = null;
//
//        if (convertView == null) {
//
//            //song = new HashMap <String, String>();
//            mViewHolder = new ViewHolder();
//
//            LayoutInflater vi = (LayoutInflater) mCountext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = vi.inflate(R.layout.toilet_information_list, parent, false);
//            mViewHolder.friendsname = (TextView) convertView.findViewById(R.id.friendsName); // title
//            mViewHolder.thumb_image = (ImageView) convertView.findViewById(R.id.list_image); // thumb image
//
//
//            mViewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
//
//            convertView.setTag(mViewHolder);
//            mViewHolder.cb.setTag(data.get(position));
//
//            mViewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {{
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean ischecked) {
//
//                    InviteFriends.isChecked[position] = buttonView.isChecked();
//
//                }
//            });
//
//        } else {
//
//            mViewHolder = (ViewHolder) convertView.getTag();
//
//        }
//
//        song = mViewHolder.cb.getTag();
//
//        mViewHolder.friendsname.setText(song.get(InviteFriends.KEY_DISPLAY_NAME));
//        mViewHolder.imageLoader.DisplayImage(song.get(InviteFriends.KEY_IMAGEPROFILE_URL), thumb_image);
//        mViewHolder.cb.setChecked(InviteFriends.isChecked[position]);
//
//        return convertView;
//    }
//
//

}
