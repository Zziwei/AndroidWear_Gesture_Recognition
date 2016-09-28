/*
 This class implements model parameters and their related operations.
 */
package com.example.willi.gestureudp;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by willi on 6/28/2016.
 */

/**
 * data structure for a gesture
 */
public class GestureModel {
    private ArrayList<DataMap> gestureData; //raw data of acc and gyro
    private double threshold;               //threshold for recognizing
    private int timespan;                   //time span limitation for recognizing
    private int m;                          //length of raw data
    private String name;                    //name

    public GestureModel() {

    }

    public GestureModel(ArrayList<DataMap> gestureData, double threshold, int timespan) {
        this.gestureData = gestureData;
        this.threshold = threshold;
        this.timespan = timespan;
    }

    public ArrayList<DataMap> getGestureData() {
        return gestureData;
    }

    public void setGestureData(ArrayList<DataMap> gestureData) {
        this.gestureData = gestureData;
        this.m = gestureData.size();
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getTimespan() {
        return timespan;
    }

    public void setTimespan(int timespan) {
        this.timespan = timespan;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
