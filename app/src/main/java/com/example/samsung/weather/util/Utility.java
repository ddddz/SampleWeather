package com.example.samsung.weather.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samsung on 2016/10/7.
 */
public class Utility {

    public static void handleWeatherResponse(Context context, String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result").getJSONObject("data");
            String cityName = result.getJSONObject("realtime").getString("city_name");
            String publishTime = result.getJSONObject("realtime").getString("time");
            String temp = result.getJSONObject("realtime").getJSONObject("weather").getString("temperature");
            String weatherInfo = result.getJSONObject("realtime").getJSONObject("weather").getString("info");
            saveWeather(context, cityName, publishTime, temp, weatherInfo);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveWeather(Context context, String cityName, String publishTime, String temp, String weatherInfo){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年m月d日", Locale.CHINA);
        ContentResolver cv = context.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putBoolean("has_loaded", true);
        editor.putString("city_name", cityName);
        editor.putString("publish_time", publishTime);
        editor.putString("temp", temp);
        editor.putString("weather_info", weatherInfo);
        editor.putString("current_data", strTimeFormat);
        editor.commit();
    }

}
