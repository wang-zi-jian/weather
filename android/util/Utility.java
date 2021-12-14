package com.cjj.coolweather.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.cjj.coolweather.android.db.City;
import com.cjj.coolweather.android.db.Country;
import com.cjj.coolweather.android.db.Province;
import com.cjj.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONObject;

import org.json.*;
/***
 * author：caijj1
 * time：2018/5/25
 * mail:caijj1@chanjet.com
 **/
public class Utility {

    /// <summary>
    ///解析和处理服务器返回的省级数据
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                org.json.JSONArray allProvinces = new org.json.JSONArray(response);
                for(int i= 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
                Log.d("Caijingjin","jsOn解析省级数据错误");
            }
        }
        return false;
    }

    /// <summary>
    /// 解析和处理服务器返回的市里的数据
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities= new JSONArray(response);
                for(int i = 0; i< allCities.length(); i++){
                    JSONObject cityObject =allCities.getJSONObject(i);
                    City city =new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
                Log.d("Caijingjin", "handleCityResponse: 市里数据");
            }
        }
        return false;
    }

    /// <summary>
    /// 返回和处理服务器返回的县级数据
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    public static boolean handleCountryResponse(String reponse,int cityId){
        if(!TextUtils.isEmpty(reponse)){
            try{
                JSONArray countryObjects = new JSONArray(reponse);
                for(int i = 0; i < countryObjects.length(); i++){
                    JSONObject countryObject =countryObjects.getJSONObject(i);
                    Country country =new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    /// <summary>
    /// 将返回的JSON数据解析成weather实体
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    public static Weather HandleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
