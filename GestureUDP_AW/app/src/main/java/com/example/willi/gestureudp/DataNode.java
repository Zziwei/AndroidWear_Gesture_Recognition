/*This class defines all the data structures and data
related operations.
 */
package com.example.willi.gestureudp;

import android.hardware.SensorEvent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by willi on 6/24/2016.
 */
public class DataNode implements Parcelable {
    public static final int ACC = 1;
    public static final int GYR = 3;
    public static final int MAG = 5;
    public static final int NONE = 0;

    private float accx;
    private float accy;
    private float accz;
    private float gyrx;
    private float gyry;
    private float gyrz;

    private long timeStamp;
    private long pktNum;

    public DataNode() {

    }

    public DataNode(String[] stringArray) {
        this.setAccx(Float.parseFloat(stringArray[0]));
        this.setAccy(Float.parseFloat(stringArray[1]));
        this.setAccz(Float.parseFloat(stringArray[2]));
        this.setGyrx(Float.parseFloat(stringArray[3]));
        this.setGyry(Float.parseFloat(stringArray[4]));
        this.setGyrz(Float.parseFloat(stringArray[5]));
    }

    public float getAccx() {
        return accx;
    }

    public void setAccx(float accx) {
        this.accx = accx;
    }

    public float getAccy() {
        return accy;
    }

    public void setAccy(float accy) {
        this.accy = accy;
    }

    public float getAccz() {
        return accz;
    }

    public void setAccz(float accz) {
        this.accz = accz;
    }

    public float getGyrx() {
        return gyrx;
    }

    public void setGyrx(float gyrx) {
        this.gyrx = gyrx;
    }

    public float getGyry() {
        return gyry;
    }

    public void setGyry(float gyry) {
        this.gyry = gyry;
    }

    public float getGyrz() {
        return gyrz;
    }

    public void setGyrz(float gyrz) {
        this.gyrz = gyrz;
    }

    public void setACC(SensorEvent event){
        this.setAccx(event.values[0] / Config.NORMALIZATION_VALUE_ACC);
        this.setAccy(event.values[1] / Config.NORMALIZATION_VALUE_ACC);
        this.setAccz(event.values[2] / Config.NORMALIZATION_VALUE_ACC);
    }

    public void setGYR(SensorEvent event){
        this.setGyrx(event.values[0] / Config.NORMALIZATION_VALUE_GYR);
        this.setGyry(event.values[1] / Config.NORMALIZATION_VALUE_GYR);
        this.setGyrz(event.values[2] / Config.NORMALIZATION_VALUE_GYR);
    }

    public float[] toFloatArray(){
        float[] array = new float[6];
        array[0] = this.getAccx();
        array[1] = this.getAccy();
        array[2] = this.getAccz();
        array[3] = this.getGyrx();
        array[4] = this.getGyry();
        array[5] = this.getGyrz();
        return array;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.accx);
        dest.writeFloat(this.accy);
        dest.writeFloat(this.accz);
        dest.writeFloat(this.gyrx);
        dest.writeFloat(this.gyry);
        dest.writeFloat(this.gyrz);
    }

    public DataNode(Parcel in) {
        this.accx = in.readFloat();
        this.accy = in.readFloat();
        this.accz = in.readFloat();
        this.gyrx = in.readFloat();
        this.gyry = in.readFloat();
        this.gyrz = in.readFloat();

    }


    public static final Creator CREATOR = new Creator() {
        public DataNode createFromParcel(Parcel in) {
            return new DataNode(in);
        }

        public DataNode[] newArray(int size) {
            return new DataNode[size];
        }
    };

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getPktNum() {
        return pktNum;
    }

    public void setPktNum(long pktNum) {
        this.pktNum = pktNum;
    }
}
