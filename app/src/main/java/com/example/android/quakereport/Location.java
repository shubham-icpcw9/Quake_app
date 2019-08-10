package com.example.android.quakereport;

public class Location {

    private String mag;
    private String cityName, url;
    private long timeInMs;

    public Location(String mag, String cityName, long time, String url)
    {
        this.mag = mag;
        this.cityName = cityName;
        this.timeInMs = time;
        this.url = url;
    }

    public String getMagnitude()
    {
        return mag;
    }

    public String getCityName()
    {
        return cityName;
    }

    public long getTime(){ return timeInMs; }

    public String getUrl()
    {
        return url;
    }

}
