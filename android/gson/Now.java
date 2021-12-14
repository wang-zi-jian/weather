package com.cjj.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/***
 * author：caijj1
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 **/
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
