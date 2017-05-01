package com.example.kazuhiroshigenobu.googlemaptraining;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KazuhiroShigenobu on 30/4/17.
 */

public class FilterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflator;
    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;

        //private List<FilterBooleans> filterList;

    SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();



    public FilterListAdapter(SparseArray<FilterBooleans> filterSparseArray){
        this.filterSparseArray = filterSparseArray;
    }


//    public FilterListAdapter(List<FilterBooleans> filterList){
//            this.filterList = filterList;
//        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_custom_row, parent, false);
            RecyclerView.ViewHolder returnViewHolder = new FilterViewHolder(view);



            if (viewType == ITEM_TYPE_HEADER) {

                View headerview = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_header_custom_row, parent, false);

                returnViewHolder = new HeaderFilterViewHolder(headerview);
                //return new HeaderFilterViewHolder(headerview);

            }

            return returnViewHolder;

        }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_NORMAL) {
            final int posA = position;
            ((FilterViewHolder)holder).bindData(filterSparseArray.get(position).booleanName);
            ((FilterViewHolder) holder).booleanSwitch.setChecked(filterSparseArray.get(position).booleanValue);
            ((FilterViewHolder) holder).booleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()){

                        Log.i("ButtonChanged",filterSparseArray.get(posA).booleanName);



                        //Toast.makeText(buttonView, "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //((FilterViewHolder) holder).booleanSwitch.setChecked();
        } else if (itemType == ITEM_TYPE_HEADER) {
            ((HeaderFilterViewHolder)holder).setHeaderText(filterSparseArray.get(position).booleanName);
        }

    }



//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        final int itemType = getItemViewType(position);
//
//        if (itemType == ITEM_TYPE_NORMAL) {
//            ((FilterViewHolder)holder).bindData(filterList.get(position).booleanName);
//            //((FilterViewHolder) holder).booleanSwitch.setChecked();
//        } else if (itemType == ITEM_TYPE_HEADER) {
//            ((HeaderFilterViewHolder)holder).setHeaderText(filterList.get(position).booleanName);
//        }
//
//    }


//    @Override
//        public void onBindViewHolder(final FilterViewHolder holder, int position) {
//
//
//            if (filterList.isEmpty()) {
//                Log.i("Reviewcurrent.key", "Its empty");
//
//            } else {
//
//
//            }
//        }

    @Override
    public int getItemCount() {

        Log.i("ReviewtoiletData.size()",String.valueOf(filterSparseArray.size()));
        return filterSparseArray.size();


    }


//        @Override
//        public int getItemCount() {
//
//            Log.i("ReviewtoiletData.size()",String.valueOf(filterList.size()));
//            return filterList.size();
//
//
//        }

    @Override
    public int getItemViewType(int position) {

       if (position == 0 || position == 6){
           return ITEM_TYPE_HEADER;
       } else {
           return ITEM_TYPE_NORMAL;
       }

    }


    public class FilterViewHolder extends RecyclerView.ViewHolder {

             Switch booleanSwitch;

            public FilterViewHolder(View itemView) {
                super(itemView);

                booleanSwitch = (Switch) itemView.findViewById(R.id.filterBooleanSwitch);

            }

            public void bindData(String text) {

                booleanSwitch.setText(text);
//            titleLabel.setText(model.getTitle());
//            descriptionLabel.setText(model.getDescription());
        }
    }


    public class HeaderFilterViewHolder extends RecyclerView.ViewHolder {

        TextView headerText;

        public HeaderFilterViewHolder(View itemView) {
            super(itemView);

            headerText = (TextView) itemView.findViewById(R.id.headerTextView);
        }

        public void setHeaderText(String text) {
            headerText.setText(text);
        }
    }

    }


