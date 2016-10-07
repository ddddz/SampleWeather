package com.example.samsung.weather.util;

/**
 * Created by samsung on 2016/10/7.
 */
public interface HttpCallBackListener{
    void onFinish(String response);
    void onError(Exception e);
}
