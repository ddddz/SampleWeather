package com.example.samsung.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.samsung.weather.util.HttpCallBackListener;
import com.example.samsung.weather.util.HttpUtil;
import com.example.samsung.weather.util.Utility;

/**
 * Created by samsung on 2016/10/12.
 */
public class AutoUpdateWeather extends Service {

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 8 * 60 * 60 * 1000;
        long tiggerTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, AutoUpdateReciver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, tiggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void UpdateWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        HttpUtil.sendHttpRequest(sharedPreferences.getString("city_url", ""), new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateWeather.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
