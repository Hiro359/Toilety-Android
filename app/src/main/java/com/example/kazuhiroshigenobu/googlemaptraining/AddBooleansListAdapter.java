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

/**
 * Created by KazuhiroShigenobu on 1/5/17.
 * //Default
 */

class AddBooleansListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //private LayoutInflater inflator;
    private static final int ITEM_TYPE_NORMAL = 0;
    private static final int ITEM_TYPE_HEADER = 1;

    //private List<FilterBooleans> filterList;

    private SparseArray<FilterBooleans> filterSparseArray = new SparseArray<>();

    AddBooleansListAdapter(SparseArray<FilterBooleans> filterSparseArray){
        this.filterSparseArray = filterSparseArray;
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_custom_row, parent, false);
        RecyclerView.ViewHolder returnViewHolder = new AddBooleansViewHolder(view);



        if (viewType == ITEM_TYPE_HEADER) {

            View headerview = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_header_custom_row, parent, false);

            returnViewHolder = new AddHeaderViewHolder(headerview);
                   // HeaderFilterViewHolder(headerview);
            //return new HeaderFilterViewHolder(headerview);

        }

        return returnViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);

        if (itemType == ITEM_TYPE_NORMAL) {
            final int pos = position;


            //((AddBooleansListAdapter.AddBooleansViewHolder) holder).bindString(filterSparseArray.get(position).booleanName);
            //((AddBooleansViewHolder) holder).booleanSwitch.setChecked(booleansSparseArray.get(pos).booleanValue);




            Log.i("OKAY","1");


            Log.i(String.valueOf(position) + "785",String.valueOf(filterSparseArray.get(position).booleanName));

            Log.i(String.valueOf(position) + "786",String.valueOf(filterSparseArray.get(position).booleanValue));

            ((AddBooleansListAdapter.AddBooleansViewHolder)holder).bindString(filterSparseArray.get(position).booleanName);


            ((AddBooleansViewHolder) holder).booleanSwitch.setChecked(filterSparseArray.get(position).booleanValue);

            Log.i("OKAY","99999");

            //((FilterListAdapter.FilterViewHolder)holder).bindData(filterSparseArray.get(position).booleanName);
           // ((AddBooleansListAdapter.AddHeaderViewHolder) holder).setChecked(filterSparseArray.get(position).booleanValue);
            ((AddBooleansListAdapter.AddBooleansViewHolder) holder).booleanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (pos == 1) {
                        AddDetailBooleans.japanesetoilet = buttonView.isChecked();
                    }
                    if (pos == 2) {
                        AddDetailBooleans.westerntoilet = buttonView.isChecked();

                    }
                    if (pos == 3) {
                        AddDetailBooleans.onlyFemale = buttonView.isChecked();
                    }
                    if (pos == 4){
                        AddDetailBooleans.unisex = buttonView.isChecked();
                    }




                    if (pos == 6) {
                        AddDetailBooleans.washlet = buttonView.isChecked();
                    }
                    if (pos == 7) {
                        AddDetailBooleans.warmSeat = buttonView.isChecked();
                    }
                    if (pos == 8) {
                        AddDetailBooleans.autoOpen = buttonView.isChecked();
                    }
                    if (pos == 9) {
                        AddDetailBooleans.noVirus = buttonView.isChecked();
                    }
                    if (pos == 10) {
                        AddDetailBooleans.paperForBenki = buttonView.isChecked();
                    }
                    if (pos == 11) {
                        AddDetailBooleans.cleanerForBenki = buttonView.isChecked();
                    }
                    if (pos == 12) {
                        AddDetailBooleans.autoToiletWash = buttonView.isChecked();
                    }




                    if (pos == 14) {
                        AddDetailBooleans.sensorHandWash = buttonView.isChecked();

                    }
                    if (pos == 15) {
                        AddDetailBooleans.handSoap = buttonView.isChecked();
                    }
                    if (pos == 16) {
                        AddDetailBooleans.autoHandSoap = buttonView.isChecked();
                    }

                    if (pos == 17) {
                        AddDetailBooleans.paperTowel = buttonView.isChecked();
                    }

                    if (pos == 18) {
                        AddDetailBooleans.handDrier = buttonView.isChecked();
                    }



                    if (pos == 20) {
                        AddDetailBooleans.fancy = buttonView.isChecked();
                    }
                    if (pos == 21) {
                        AddDetailBooleans.smell = buttonView.isChecked();
                    }
                    if (pos == 22) {
                        AddDetailBooleans.conforatableWide = buttonView.isChecked();
                    }
                    if (pos == 23) {
                        AddDetailBooleans.clothes = buttonView.isChecked();
                    }
                    if (pos == 24) {
                        AddDetailBooleans.baggageSpace = buttonView.isChecked();
                    }



                    if (pos == 26) {
                        AddDetailBooleans.noNeedAsk = buttonView.isChecked();
                    }
                    if (pos == 27) {
                        AddDetailBooleans.english = buttonView.isChecked();
                    }
                    if (pos == 28) {
                        AddDetailBooleans.parking = buttonView.isChecked();
                    }
                    if (pos == 29) {
                        AddDetailBooleans.airCondition = buttonView.isChecked();
                    }
                    if (pos == 30) {
                        AddDetailBooleans.wifi = buttonView.isChecked();
                    }



                    if (pos == 32) {
                        AddDetailBooleans.otohime = buttonView.isChecked();
                    }
                    if (pos == 33) {
                        AddDetailBooleans.napkinSelling = buttonView.isChecked();
                    }
                    if (pos == 34) {
                        AddDetailBooleans.makeuproom = buttonView.isChecked();
                    }
                    if (pos == 35) {
                        AddDetailBooleans.ladyOmutu = buttonView.isChecked();
                    }
                    if (pos == 36) {
                        AddDetailBooleans.ladyBabyChair = buttonView.isChecked();
                    }
                    if (pos == 37) {
                        AddDetailBooleans.ladyBabyChairGood = buttonView.isChecked();
                    }
                    if (pos == 38) {
                        AddDetailBooleans.ladyBabyCarAccess = buttonView.isChecked();
                    }




                    if (pos == 40) {
                        AddDetailBooleans.maleOmutu = buttonView.isChecked();
                    }
                    if (pos == 41) {
                        AddDetailBooleans.maleBabyChair = buttonView.isChecked();
                    }
                    if (pos == 42) {
                        AddDetailBooleans.maleBabyChairGood = buttonView.isChecked();
                    }
                    if (pos == 43) {
                        AddDetailBooleans.maleBabyCarAccess = buttonView.isChecked();
                    }




                    if (pos == 45) {
                        AddDetailBooleans.wheelchair = buttonView.isChecked();
                    }
                    if (pos == 46) {
                        AddDetailBooleans.wheelchairAccess = buttonView.isChecked();
                    }
                    if (pos == 47) {
                        AddDetailBooleans.autoDoor = buttonView.isChecked();
                    }
                    if (pos == 48) {
                        AddDetailBooleans.callHelp = buttonView.isChecked();
                    }
                    if (pos == 49) {
                        AddDetailBooleans.ostomate = buttonView.isChecked();
                    }
                    if (pos == 50) {
                        AddDetailBooleans.braille = buttonView.isChecked();
                    }
                    if (pos == 51) {
                        AddDetailBooleans.voiceGuide = buttonView.isChecked();
                    }
                    if (pos == 52) {
                        AddDetailBooleans.familyOmutu = buttonView.isChecked();
                    }
                    if (pos == 53) {
                        AddDetailBooleans.familyBabyChair= buttonView.isChecked();
                    }




                    if (pos == 55) {
                        AddDetailBooleans.milkspace = buttonView.isChecked();
                    }
                    if (pos == 56) {
                        AddDetailBooleans.babyroomOnlyFemale = buttonView.isChecked();
                    }
                    if (pos == 57) {
                        AddDetailBooleans.babyroomManCanEnter = buttonView.isChecked();
                    }
                    if (pos == 58) {
                        AddDetailBooleans.babyPersonalSpace = buttonView.isChecked();
                    }
                    if (pos == 59) {
                        AddDetailBooleans.babyPersonalSpaceWithLock = buttonView.isChecked();
                    }
                    if (pos == 60) {
                        AddDetailBooleans.babyRoomWideSpace = buttonView.isChecked();
                    }




                    if (pos == 62) {
                        AddDetailBooleans.babyCarRental = buttonView.isChecked();
                    }

                    if (pos == 63) {
                        AddDetailBooleans.babyCarAccess = buttonView.isChecked();
                    }
                    if (pos == 64) {
                        AddDetailBooleans.omutu = buttonView.isChecked();
                    }
                    if (pos == 65) {
                        AddDetailBooleans.hipWashingStuff = buttonView.isChecked();
                    }
                    if (pos == 66) {
                        AddDetailBooleans.babyTrashCan = buttonView.isChecked();
                    }
                    if (pos == 67) {
                        AddDetailBooleans.omutuSelling = buttonView.isChecked();
                    }


                    if (pos == 69) {
                        AddDetailBooleans.babyRoomSink = buttonView.isChecked();
                    }
                    if (pos == 70) {
                        AddDetailBooleans.babyWashStand = buttonView.isChecked();
                    }
                    if (pos == 71) {
                        AddDetailBooleans.babyHotWater = buttonView.isChecked();
                    }
                    if (pos == 72) {
                        AddDetailBooleans.babyMicroWave = buttonView.isChecked();
                    }
                    if (pos == 73) {
                        AddDetailBooleans.babyWaterSelling = buttonView.isChecked();
                    }
                    if (pos == 74) {
                        AddDetailBooleans.babyFoddSelling = buttonView.isChecked();
                    }
                    if (pos == 75) {
                        AddDetailBooleans.babyEatingSpace = buttonView.isChecked();
                    }


                    if (pos == 77) {
                        AddDetailBooleans.babyChair = buttonView.isChecked();
                    }
                    if (pos == 78) {
                        AddDetailBooleans.babySoffa = buttonView.isChecked();
                    }
                    if (pos == 79) {
                        AddDetailBooleans.babyKidsToilet = buttonView.isChecked();
                    }
                    if (pos == 80) {
                        AddDetailBooleans.babyKidsSpace = buttonView.isChecked();
                    }
                    if (pos == 81) {
                        AddDetailBooleans.babyHeightMeasure = buttonView.isChecked();
                    }
                    if (pos == 82) {
                        AddDetailBooleans.babyWeightMeasure = buttonView.isChecked();
                    }
                    if (pos == 83) {
                        AddDetailBooleans.babyToy = buttonView.isChecked();
                    }
                    if (pos == 84) {
                        AddDetailBooleans.babyFancy = buttonView.isChecked();
                    }
                    if (pos == 85) {
                        AddDetailBooleans.babySmellGood = buttonView.isChecked();
                    }


                }

            });
            //((FilterViewHolder) holder).booleanSwitch.setChecked();
        } else if (itemType == ITEM_TYPE_HEADER) {

            Log.i("Header", "Text123");
            ((AddBooleansListAdapter.AddHeaderViewHolder)holder).setHeaderText(filterSparseArray.get(position).booleanName);
            Log.i("Header", filterSparseArray.get(position).booleanName);
        }



    }

    @Override
    public int getItemCount() {
        return 86;
    }
    @Override
    public int getItemViewType(int position) {


        if (position == 0 || position == 5 || position == 13 || position == 19 || position == 25|| position == 31
                || position == 39|| position == 44|| position == 54|| position == 61|| position == 68 || position == 76) {
            return ITEM_TYPE_HEADER;

        } else {
            return ITEM_TYPE_NORMAL;
        }


    }


    private class AddBooleansViewHolder extends RecyclerView.ViewHolder {

        Switch booleanSwitch;

        private AddBooleansViewHolder(View itemView) {
            super(itemView);

            booleanSwitch = (Switch) itemView.findViewById(R.id.filterBooleanSwitch);

        }

        private void bindString(String text) {

            booleanSwitch.setText(text);
//            titleLabel.setText(model.getTitle());
//            descriptionLabel.setText(model.getDescription());
        }
    }


    private class AddHeaderViewHolder extends RecyclerView.ViewHolder {

        TextView headerText;

        private AddHeaderViewHolder(View itemView) {
            super(itemView);

            headerText = (TextView) itemView.findViewById(R.id.headerTextView);
        }

        private void setHeaderText(String text) {
            headerText.setText(text);
        }
    }


}
