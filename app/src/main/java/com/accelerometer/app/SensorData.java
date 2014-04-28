package com.accelerometer.app;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hesanche on 15/03/14.
 */
public class SensorData {

    private ArrayList<Long> timestamp;
    private ArrayList<Float> X;
    private ArrayList<Float> Y;
    private ArrayList<Float> Z;
    private int size_data;

    public SensorData(int size_data) {
        this.size_data = size_data;
        this.timestamp = new ArrayList();
        this.X = new ArrayList();
        this.Y = new ArrayList();
        this.Z = new ArrayList();
    }
    public Long[] getTimestamp() {
        return (Long[])timestamp.toArray();
    }

    public Float[] getX() {
        return X.toArray(new Float[getSize()]);
    }

    public Float[] getY() {
        return Y.toArray(new Float[getSize()]);
    }

    public Float[] getZ() {
        return Z.toArray(new Float[getSize()]);
    }

    public void addItem(Long time,Float x,Float y,Float z){
        if(timestamp.size() >= size_data) {
            timestamp.remove(0);
            X.remove(0);
            Y.remove(0);
            Z.remove(0);
        }
        this.timestamp.add(time);
        this.X.add(x);
        this.Y.add(y);
        this.Z.add(z);
    }

    public String convertTime(Long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date).toString();
    }

    public String toStringLastItem()
    {
        int current = timestamp.size()-1;
        return convertTime((Long)timestamp.get(current))+"\t "+ getSize() + ": [" +
                "x="+String.format("%.2f", X.get(current))+", " +
                "y="+String.format("%.2f", Y.get(current))+", " +
                "z="+String.format("%.2f", Z.get(current)) +"]";
    }

    public void clear() {
        timestamp.clear();
        X.clear();
        Y.clear();
        Z.clear();
    }

    public int getSize() {
        return timestamp.size();
    }
    public int getMaxSize() {
        return size_data;
    }
}
