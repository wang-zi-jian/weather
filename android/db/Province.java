package com.cjj.coolweather.android.db;

import org.litepal.crud.DataSupport;

/***
 * author：caijj1
 * time：2018/5/25
 * mail:caijj1@chanjet.com
 **/
public class Province extends DataSupport {

    private int id ;

    private String provinceName;

    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int privinceCode) {
        this.provinceCode = privinceCode;
    }


}
