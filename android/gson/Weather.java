package com.cjj.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/***
 * author：caijj1
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 **/
public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
