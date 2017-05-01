package com.example.kazuhiroshigenobu.googlemaptraining;

import android.util.SparseBooleanArray;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KazuhiroShigenobu on 30/4/17.
 */

public class FilterBooleans {
    String booleanName = "";
    Boolean booleanValue = false;

    public FilterBooleans(String booleanName, Boolean booleanValue) {
        this.booleanName = booleanName;
        this.booleanValue = booleanValue;
    }
}
