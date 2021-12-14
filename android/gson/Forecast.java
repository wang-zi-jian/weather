package com.cjj.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/***
 * author：caijj
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 * {
 "date": "2018-05-26",
 "cond": {
 "txt_d": "阴"
 },
 "tmp": {
 "max": "25",
 "min": "20"
 }
 }
 *
 **/
public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{

        public String max;

        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
