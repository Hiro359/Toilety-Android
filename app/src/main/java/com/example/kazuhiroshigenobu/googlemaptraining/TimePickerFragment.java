package com.example.kazuhiroshigenobu.googlemaptraining;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import java.util.Calendar;
//import android.icu.util.Calendar;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

/**
 * Created by KazuhiroShigenobu on 27/2/17.
 *
 */

public class TimePickerFragment{

}
//        extends DialogFragment
//        implements TimePickerDialog.OnTimeSetListener {
//
//    @Override
//    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the current time as the default values for the picker
//        final Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
////        return new TimePickerDialog(getActivity(), this, TimePickerDialog.OnTimeSetListener callBack,int hour, int minute, boolean is24HourView);
//
//
//
//
////        // Create a new instance of TimePickerDialog and return it
////        return new TimePickerDialog(getActivity(), this, hour, minute,
////                DateFormat.is24HourFormat(getActivity()));
////        return new TimePickerDialog(getActivity(), this, hour, minute,
////                DateFormat.is24HourFormat(getActivity()));
//
//        return new TimePickerDialog(getActivity(), this, 0, 0,
//               true);
//
//
//    }
//
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        // Do something with the time chosen by the user
//        Integer editedMinute;
//        if (minute < 10){
//
//
//
//        } else {
//            editedMinute = minute;
//
//        }
//        Log.i("Time12321", hourOfDay +":" + minute);
//
//    }
//}
