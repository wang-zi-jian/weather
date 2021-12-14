package com.cjj.coolweather.android.gson;

/***
 * author：caijj1
 * time：2018/5/26
 * mail:caijj1@chanjet.com
 **/
public class AQI {

    ///空气质量
    public AQICity city;

    /// <summary>
    /// 具体的参数的指标
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    public class AQICity{

        public String aqi;

        public String pm25;
    }


}
