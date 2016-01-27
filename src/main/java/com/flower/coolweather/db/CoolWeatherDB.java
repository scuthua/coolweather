package com.flower.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flower.coolweather.model.City;
import com.flower.coolweather.model.Country;
import com.flower.coolweather.model.Province;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flower on 2016/1/27.
 */
public class CoolWeatherDB {
    /**
     * 数据库名字
     */
    public static final String DB_NAME = "cool_weather";
    /**
     *数据库版本
     */
    public static final int VERSION=1;
    private static CoolWeatherDB coolWeatherDB;
    private static SQLiteDatabase db;
    /**
     * 将构造方法私有化
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, DB_NAME,
                null, VERSION);
        db=coolWeatherOpenHelper.getWritableDatabase();
    }
    /**
     * 获得CoolWeatherDB实例
     */
    public synchronized  static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB!=null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }
    /**
     * 将province实例存储到数据库中
     */
    public void saveProvince(Province province) {
        if (province!=null) {
            ContentValues values=new ContentValues();
            values.put("provinceName",province.getProvinceName());
            values.put("provinceCode",province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     * @return
     */
    public List<Province> loadProvinces() {
        List<Province> provinces = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            } while (cursor.moveToNext());
        }
        return provinces;
    }

    /**
     * 将city实例存储到数据库中
     */
    public void saveCity(City city) {
        ContentValues values = new ContentValues();
        values.put("city_name", city.getCityName());
        values.put("city_code", city.getCityCode());
        values.put("province_id", city.getProvinceId());
        db.insertOrThrow("City", null, values);
    }

    /**
     * 从数据库中读取某个省的所有的城市的信息
     * @return
     */
    public List<City>loadCities(int provinceId) {
        List<City> cities = new ArrayList<>();
        Cursor cursor=db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cities.add(city);
            } while (cursor.moveToNext());

        }
        return cities;
    }

    /**
     * 将country实例存储到数据库中
     */
    public void saveCountry(Country country) {
        ContentValues values = new ContentValues();
        values.put("country_name", country.getCountryName());
        values.put("country_code", country.getCountryCode());
        values.put("city_id", country.getCityId());
        db.insertOrThrow("country", null, values);
    }

    /**
     * 从数据库中读取某个城市的所有的镇的信息
     * @return
     */
    public List<Country>loadCountries(int cityId) {
        List<Country> countries = new ArrayList<>();
        Cursor cursor=db.query("City",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                Country country=new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                countries.add(country);
            } while (cursor.moveToNext());

        }
        return countries;
    }
    




}
