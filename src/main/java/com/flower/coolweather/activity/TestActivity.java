package com.flower.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.flower.coolweather.R;
import com.flower.coolweather.util.HttpCallBackListener;
import com.flower.coolweather.util.HttpUtil;

/**
 * Created by flower on 2016/1/27.
 */
public class TestActivity extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        textView=(TextView) findViewById(R.id.textview);
        String address="http://www.weather.com.cn/data/list3/city.xml";
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] str=response.split("\\|");
                        StringBuilder sb = new StringBuilder();
                        for (String a : str) {
                            sb.append(a+" ");
                        }
                        textView.setText(sb.toString());

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
