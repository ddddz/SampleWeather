package com.example.samsung.weather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by samsung on 2016/10/12.
 */
public class AutoUpdateReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Intent i = new Intent(context, AutoUpdateWeather.class);
        context.startService(i);
    }
}
