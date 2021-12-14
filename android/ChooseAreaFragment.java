package com.cjj.coolweather.android;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.le.AdvertiseData;
import android.content.Intent;
import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.coolweather.android.db.City;
import com.cjj.coolweather.android.db.Country;
import com.cjj.coolweather.android.db.Province;
import com.cjj.coolweather.android.gson.AQI;
import com.cjj.coolweather.android.util.HttpUtil;
import com.cjj.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***
 * author：caijj1
 * time：2018/5/25
 * mail:caijj1@chanjet.com
 **/
public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE =0;
    public  static  final  int LEVEL_CITY = 1;
    public  static  final  int LEVEL_COUNTRY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String>adapter;

    private List<String> dataList = new ArrayList<>();

    private List<Province>provinceList;//省会列表

    private List<City>cityList;//城市列表

    private List<Country>countryList;//地区列表

    private Province selectedProvince;//选中的省

    private City selectedCity;//选中的城市

    private int currentLevel;//选中的级别

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>( getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTRY){
                    String weatherId = countryList.get(position).getWeatherId();
                    if(getActivity()instanceof  MainActivity){


                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                    }else if(getActivity() instanceof  WeatherActivity){
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefreshLayout.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }

            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();

    }

    /// <summary>
    /// 查询全国所有的省份，优先从数据库查询，若没有从服务器上查询
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.VISIBLE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }


    /// <summary>
    /// 查询选中省份的所有的县，优先从数据库查询，若没有从服务器上查询
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList =DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(Country.class);
        if(countryList.size() > 0){
            dataList.clear();
            for(Country country: countryList){
                dataList.add(country.getCountryName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTRY;
        }else{
            int provinceCode= selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"country");
        }
    }

    /// <summary>
    /// 查询选中省份的所有的市，优先从数据库查询，若没有从服务器上查询
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city: cityList){
                dataList.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    private void queryFromServer(String address, final String type) {

        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("country".equals(type)){
                    result = Utility.handleCountryResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("country".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /// <summary>
    /// 显示进度对话框
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    private void showProgressDialog() {

        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /// <summary>
    /// 关闭进度对话框
    /// </summary>
    /// <param name="XXX"></param>
    /// <returns></returns>
    private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
