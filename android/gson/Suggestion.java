package com.cjj.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/***
 * author：caijj1
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 **/
public class Suggestion {
    ///各类介意
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{

        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
