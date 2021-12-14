package com.cjj.coolweather.android.db;

import org.litepal.crud.DataSupport;

/***
 * author：caijj1
 * time：2018/5/25
 * mail:caijj1@chanjet.com
 **/
public class Country extends DataSupport {

    private int id;

    ///地区名字
    private String countryName;

    /// 天气Id
    private String weatherId;

    ///城市id
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
