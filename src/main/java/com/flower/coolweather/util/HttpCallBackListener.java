package com.flower.coolweather.util;

/**
 * Created by flower on 2016/1/27.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
