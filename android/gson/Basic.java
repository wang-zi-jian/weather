package com.cjj.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/***
 * author：caijj1
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 **/
public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
