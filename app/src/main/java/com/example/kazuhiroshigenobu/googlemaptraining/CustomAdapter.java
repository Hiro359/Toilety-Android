package com.example.kazuhiroshigenobu.googlemaptraining;


import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by KazuhiroShigenobu on 22/2/17.
 */

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int USER_WRITE = 2;

    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<String> userInputData = new ArrayList<String>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();


    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addUserInputData(final String item) {
        userInputData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

        @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM ;
    }


//    @Override
//    public int getItemViewType(int position) {
//        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM ;
//    }
    //I dont understand here....

    @Override
    public int getViewTypeCount() {
        return 3;
        //2 to 3 5pm
    }
    //I dont understand here....

    @Override
    public int getCount() {
        return mData.size() + userInputData.size();
    }

    @Override
    public String getItem(int position) {
        String returnString;

        if (position <= 4){
            returnString =userInputData.get(position);
        }else{
            returnString = mData.get(position);

        }
        return returnString;
    }

//    @Override
//    public String getItem(int position) {
//        return mData.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public int getPosition(int position) {
//        return position - sectionHeader.headSet(position).size();
//    }


    //Added from a comment on the internet....


//    yourlistview.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
//        @Override
//        public void onItemClick(CustomAdapter customadapter, View view, int position, long l) {
//            if(CustomAdapter.getItemViewType(position) != SectionHeaderAdapter.TYPE_SEPARATOR) {
//                Log.d("APP", "POSITION: ->" + yourAdapter.getPosition(position));
//            }
//        }
//    });
//



    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);


        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
                case USER_WRITE:
                    convertView = mInflater.inflate(R.layout.snippet_item3, null);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.text2);
                    break;

            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position));
        holder.textView2.setText(userInputData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public TextView textView2;
    }

}