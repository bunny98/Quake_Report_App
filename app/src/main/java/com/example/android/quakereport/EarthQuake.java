package com.example.android.quakereport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class EarthQuake {
    private Double mMagnitude;
    private String mTime;
    private String mPlace;
    private String mDate;
    private String mUrl;


    public EarthQuake(Double mag, String place, long time, String url){
        mMagnitude = mag;
        mPlace = place;
        mUrl = url;
        Date dateObject = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL DD, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        mTime = timeFormat.format(dateObject);
        mDate = dateFormat.format(dateObject);
    }

    public Double getMagnitude(){
        return mMagnitude;
    }

    public String getTime(){
        return mTime;
    }

    public String getPlace(){
        return mPlace;
    }

    public String getDate(){ return mDate; }

    public String getUrl(){ return mUrl; }

}
