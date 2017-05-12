package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

/**
 * Created by KazuhiroShigenobu on 30/4/17.
 *
 */

class FilterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private LayoutInflater inflator;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_SAFEBABYCHAIR = 2;

    //private Context mContext;

    private AdapterCallback mAdapterCallback;


        //private List<FilterBooleans> filterList;

    private SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();


//    FilterListAdapter(SparseArray<FilterBooleans> filterSparseArray){
//        this.filterSparseArray = filterSparseArray;
//    }
    //Original....May 12



//    FilterListAdapter(SparseArray<FilterBooleans> filterSparseArray, Context context){
//        this.filterSparseArray = filterSparseArray;
//        this.mContext=context;
//    }

    FilterListAdapter(SparseArray<FilterBooleans> filterSparseArray, Context context){
        this.filterSparseArray = filterSparseArray;
        try {

            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_NORMAL) {

            final int pos = position;
            ((FilterViewHolder)holder).bindData(filterSparseArray.get(position).booleanName);
            ((FilterViewHolder) holder).booleanSwitch.setChecked(filterSparseArray.get(position).booleanValue);
            ((FilterViewHolder) holder).booleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    Log.i("Click Called","99999");
                    if (pos == 1) {
                        Log.i("Click Av","99999");
                        Filter.availableFilter = buttonView.isChecked();
                    }
                    if (pos == 2) {
                        Filter.japaneseFilter = buttonView.isChecked();
                    }
                    if (pos == 3) {
                        Filter.westernFilter = buttonView.isChecked();
                    }
                    if (pos == 4) {
                        Filter.onlyFemaleFilter = buttonView.isChecked();
                    }
                    if (pos == 5){
                        Filter.unisexFilter = buttonView.isChecked();
                    }


                    if (pos == 7) {
                        Filter.washletFilter = buttonView.isChecked();
                        Log.i("Click Wash","99999");
                    }
                    if (pos == 8) {
                        Filter.warmSearFilter = buttonView.isChecked();
                    }
                    if (pos == 9) {
                        Filter.autoOpen = buttonView.isChecked();
                    }
                    if (pos == 10) {
                        Filter.noVirusFilter = buttonView.isChecked();
                    }
                    if (pos == 11) {
                        Filter.paperForBenkiFilter = buttonView.isChecked();
                    }
                    if (pos == 12) {
                        Filter.cleanerForBenkiFilter = buttonView.isChecked();
                    }
                    if (pos == 13) {
                        Filter.autoToiletWashFilter = buttonView.isChecked();
                    }



                    if (pos == 15) {

                        Filter.sensorHandWashFilter = buttonView.isChecked();
                    }
                    if (pos == 16) {
                        Filter.handSoapFilter = buttonView.isChecked();
                    }
                    if (pos == 17) {
                        Filter.autoHandSoapFilter = buttonView.isChecked();
                    }

                    if (pos == 18) {
                        Filter.paperTowelFilter = buttonView.isChecked();
                    }
                    if (pos == 19) {
                        Filter.handDrierFilter = buttonView.isChecked();
                    }

                    if (pos == 21) {
                        Filter.fancy = buttonView.isChecked();
                    }
                    if (pos == 22) {
                        Filter.smell = buttonView.isChecked();
                    }
                    if (pos == 23) {
                        Filter.confortableWise = buttonView.isChecked();
                    }
                    if (pos == 24) {
                        Filter.clothes = buttonView.isChecked();
                    }
                    if (pos == 25) {
                        Filter.baggageSpaceFilter = buttonView.isChecked();
                    }


                    if (pos == 27) {
                        Filter.noNeedAsk = buttonView.isChecked();
                    } if (pos == 28) {
                        Filter.writtenEnglish = buttonView.isChecked();
                    }
                    if (pos == 29) {
                        Filter.parking = buttonView.isChecked();
                    }
                    if (pos == 30) {
                        Filter.airConditionFilter = buttonView.isChecked();
                    }
                    if (pos == 31) {
                        Filter.wifiFilter = buttonView.isChecked();
                    }



                    if (pos == 33) {
                        Filter.otohime = buttonView.isChecked();
                    }
                    if (pos == 34) {
                        Filter.napkinSelling = buttonView.isChecked();
                    }
                    if (pos == 35) {
                        Filter.makeroomFilter = buttonView.isChecked();
                    }
                    if (pos == 36) {
                        Filter.ladyOmutuFilter = buttonView.isChecked();
                    }
                    if (pos == 37) {
                        Filter.ladyBabyChair = buttonView.isChecked();
                    }
                    if (pos == 38) {

                        try {
                            mAdapterCallback.onMethodCallback();
                        } catch (ClassCastException exception) {
                            // do something
                        }

                        //Baby Safe
                        Filter.ladyBabyChairGood = buttonView.isChecked();
                    }
                    if (pos == 39) {
                        Filter.ladyBabyCarAccess = buttonView.isChecked();
                    }
                    if (pos == 41) {
                        Filter.maleOmutuFilter = buttonView.isChecked();
                    } if (pos == 42) {
                        Filter.maleBabyChair = buttonView.isChecked();
                    }
                    if (pos == 43) {
                        //Baby Safe

                        try {
                            mAdapterCallback.onMethodCallback();
                        } catch (ClassCastException exception) {
                            // do something
                        }

                        Filter.maleBabyChairGood = buttonView.isChecked();
                    }
                    if (pos == 44) {
                        Filter.maleBabyCarAccess = buttonView.isChecked();
                    }




                    if (pos == 46) {
                        Filter.wheelchairFilter = buttonView.isChecked();
                    }
                    if (pos == 47) {
                        Filter.wheelchairAccessFilter = buttonView.isChecked();
                    }
                    if (pos == 48) {
                        Filter.autoDoorFilter = buttonView.isChecked();
                    } if (pos == 49) {
                        Filter.callHelpFilter = buttonView.isChecked();
                    }
                    if (pos == 50) {
                        Filter.ostomateFilter = buttonView.isChecked();
                    }
                    if (pos == 51) {
                        Filter.braille = buttonView.isChecked();
                    }
                    if (pos == 52) {
                        Filter.voiceGuideFilter = buttonView.isChecked();
                    }
                    if (pos == 53) {
                        Filter.familyOmutuFilter = buttonView.isChecked();
                    }
                    if (pos == 54) {
                        Filter.familyBabyChair = buttonView.isChecked();
                    }




                    if (pos == 56) {
                        Filter.milkspaceFilter = buttonView.isChecked();
                    }
                    if (pos == 57) {
                        Filter.babyRoomOnlyFemaleFilter = buttonView.isChecked();
                    }
                    if (pos == 58) {
                        Filter.babyRoomMaleCanEnterFilter = buttonView.isChecked();
                    }
                    if (pos == 59) {
                        Filter.babyRoomPersonalSpaceFilter = buttonView.isChecked();
                    }
                    if (pos == 60) {
                        Filter.babyRoomPersonalWithLockFilter = buttonView.isChecked();
                    }
                    if (pos == 61) {
                        Filter.babyRoomWideSpaceFilter = buttonView.isChecked();
                    }



                    if (pos == 63) {
                        Filter.babyCarRentalFilter = buttonView.isChecked();
                    }

                    if (pos == 64) {
                        Filter.babyCarAccessFilter = buttonView.isChecked();
                    }
                    if (pos == 65) {
                        Filter.omutuFilter = buttonView.isChecked();
                    }
                    if (pos == 66) {
                        Filter.babyHipWashingStuffFilter = buttonView.isChecked();
                    }
                    if (pos == 67) {
                        Filter.omutuTrashCanFilter = buttonView.isChecked();
                    }
                    if (pos == 68) {
                        Filter.omutuSelling = buttonView.isChecked();
                    }



                    if (pos == 70) {
                        Filter.babySinkFilter = buttonView.isChecked();
                    }
                    if (pos == 71) {
                        Filter.babyWashstandFilter = buttonView.isChecked();
                    }
                    if (pos == 72) {
                        Filter.babyHotWaterFilter = buttonView.isChecked();
                    }
                    if (pos == 73) {
                        Filter.babyMicrowaveFilter = buttonView.isChecked();
                    }
                    if (pos == 74) {
                        Filter.babySellingWaterFilter = buttonView.isChecked();
                    }
                    if (pos == 75) {
                        Filter.babyFoodSellingFilter = buttonView.isChecked();
                    }
                    if (pos == 76) {
                        Filter.babyEatingSpaceFilter = buttonView.isChecked();
                    }


                    if (pos == 78) {
                        Filter.babyChairFilter = buttonView.isChecked();
                    }
                    if (pos == 79) {
                        Filter.babySoffaFilter = buttonView.isChecked();
                    }
                    if (pos == 80) {
                        Filter.babyToiletFilter = buttonView.isChecked();
                    }
                    if (pos == 81) {
                        Filter.babyKidsSpaceFilter = buttonView.isChecked();
                    }
                    if (pos == 82) {
                        Filter.babyHeightMeasureFilter = buttonView.isChecked();
                    }
                    if (pos == 83) {
                        Filter.babyWeightMeasureFilter = buttonView.isChecked();
                    }
                    if (pos == 84) {
                        Filter.babyToyFilter = buttonView.isChecked();
                    }
                    if (pos == 85) {
                        Filter.fancy = buttonView.isChecked();
                    }
                    if (pos == 86) {
                        Filter.babyRoomSmellGoodFilter = buttonView.isChecked();
                    }

                    }

            });
            //((FilterViewHolder) holder).booleanSwitch.setChecked();
        } else if (itemType == ITEM_TYPE_HEADER) {
            ((HeaderFilterViewHolder)holder).setHeaderText(filterSparseArray.get(position).booleanName);
        }

    }


    interface AdapterCallback {
        void onMethodCallback();
    }




    @Override
    public int getItemCount() {

        return 86;


    }



    @Override
    public int getItemViewType(int position) {


       if (position == 0 || position == 6 || position == 14 || position == 20 || position == 26|| position == 32
               || position == 40|| position == 45|| position == 55|| position == 62|| position == 69 || position == 77) {
           return ITEM_TYPE_HEADER;

//       } else if (position == 38 || position == 43) {
//
//           return ITEM_TYPE_SAFEBABYCHAIR;
//       }
//
       }else{

           return ITEM_TYPE_NORMAL;
       }


    }


    private class FilterViewHolder extends RecyclerView.ViewHolder {

             Switch booleanSwitch;

            FilterViewHolder(View itemView) {
                super(itemView);

                booleanSwitch = (Switch) itemView.findViewById(R.id.filterBooleanSwitch);

            }

            void bindData(String text) {

                booleanSwitch.setText(text);
//            titleLabel.setText(model.getTitle());
//            descriptionLabel.setText(model.getDescription());
        }
    }


    private class HeaderFilterViewHolder extends RecyclerView.ViewHolder {

        TextView headerText;

        HeaderFilterViewHolder(View itemView) {
            super(itemView);

            headerText = (TextView) itemView.findViewById(R.id.headerTextView);
        }

        void setHeaderText(String text) {
            headerText.setText(text);
        }
    }

    }


