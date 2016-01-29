package com.flower.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.flower.coolweather.R;
import com.flower.coolweather.util.HttpCallBackListener;
import com.flower.coolweather.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class TestActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        final String address = "http://www.weather.com.cn/data/cityinfo/101281601.html";


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    String response = sb.toString();
                    Log.i("tag", "run: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
                    Log.i("tag", "onCreate: " + weatherInfo.getString("city") + weatherInfo
                            .getString
                                    ("temp1") + weatherInfo.getString("temp2") + weatherInfo.getString
                            ("weather")
                            + weatherInfo.getString("ptime"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
