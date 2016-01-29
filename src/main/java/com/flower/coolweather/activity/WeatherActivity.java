package com.flower.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flower.coolweather.R;
import com.flower.coolweather.util.HttpCallBackListener;
import com.flower.coolweather.util.HttpUtil;
import com.flower.coolweather.util.Utilty;

/**
 * Created by flower on 2016/1/28.
 */
public class WeatherActivity extends Activity implements View.OnClickListener {

    private TextView cityName, publishTime, temp1, temp2, weatherDesp, currentDate;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        cityName = (TextView) findViewById(R.id.title_text);
        publishTime = (TextView) findViewById(R.id.publish_time);
        temp1 = (TextView) findViewById(R.id.temp1);
        temp2 = (TextView) findViewById(R.id.temp2);
        currentDate = (TextView) findViewById(R.id.current_text);
        weatherDesp = (TextView) findViewById(R.id.weather_text);
        linearLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        String countryCode = getIntent().getStringExtra("country_code");
        if (!TextUtils.isEmpty(countryCode)) {
            publishTime.setText("同步中。。。");
            linearLayout.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countryCode);
        } else {
            showWeather();
        }
    }

    private void queryWeatherCode(String countryCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
        queryFromServer(address, "weather_code");
    }

    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if ("weather_code".equals(type)) {
                    String[] array = response.split("\\|");
                    if (array != null && array.length == 2) {
                        queryWeatherInfo(array[1]);

                    }
                }
                else if ("weather_info".equals(type)) {
                    Utilty.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTime.setText("同步失败");
                    }
                });
            }
        });

    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weather_info");

    }

    private void showWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        publishTime.setText(prefs.getString("publish_time"+"发布",""));
        currentDate.setText(prefs.getString("current_date",""));
        temp1.setText(prefs.getString("temp1", ""));
        temp2.setText(prefs.getString("temp2", ""));
        weatherDesp.setText(prefs.getString("weather_desp",""));
        cityName.setText(prefs.getString("city_name", ""));
        linearLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {

    }
}
