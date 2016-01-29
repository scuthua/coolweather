package com.flower.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.flower.coolweather.db.CoolWeatherDB;
import com.flower.coolweather.model.City;
import com.flower.coolweather.model.Country;
import com.flower.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utilty {

    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if ( allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String
            response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries.length > 0) {
                for (String c : allCountries) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setCountryName(array[1]);
                    country.setCountryCode(array[0]);
                    country.setCityId(cityId);
                    coolWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static void handleWeatherResponse(Context context, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
                String cityName = weatherInfo.getString("city");
                String cityCode = weatherInfo.getString("cityid");
                String temp1 = weatherInfo.getString("temp1");
                String temp2 = weatherInfo.getString("temp2");
                String weatherDesp = weatherInfo.getString("weather");
                String publishTime = weatherInfo.getString("ptime");
                saveWeatherInfo(context, cityName, temp1, temp2, weatherDesp, publishTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private static void saveWeatherInfo(Context context, String cityName, String temp1, String
            temp2, String weatherDesp, String publishTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}
