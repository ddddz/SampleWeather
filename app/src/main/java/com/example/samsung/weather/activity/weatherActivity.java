package com.example.samsung.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.weather.R;
import com.example.samsung.weather.service.AutoUpdateWeather;
import com.example.samsung.weather.util.HttpCallBackListener;
import com.example.samsung.weather.util.HttpUtil;
import com.example.samsung.weather.util.Utility;

import java.util.Calendar;

/**
 * Created by samsung on 2016/10/6.
 */
public class weatherActivity extends Activity implements View.OnClickListener{

    private Calendar calendar;
    private String Url;
    private String hour, month, minute;

    private TextView titleWeatherCity;
    private TextView refreshTime;
    private TextView weatherInfo;
    private TextView temperature;
    private Button search;
    private Button refresh;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private Intent mintent;

    private final String urlName="http://op.juhe.cn/onebox/weather/query";//appkey
    private final String keys = "bc5bc07176469e357fc53e1682c4112b";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        intent = getIntent();
        if(sharedPreferences.getBoolean("has_loaded", false) && !intent.getBooleanExtra("is_from_look", false)){
            Url = lookUp_weather.code(sharedPreferences.getString("city_name", ""), urlName, keys);
            sharedPreferences.edit().putString("city_url", Url);
            sharedPreferences.edit().commit();

        } else {
            Url = intent.getStringExtra("city_url");
        }
        getWeather(Url);
        setContentView(R.layout.weather_main);
        titleWeatherCity = (TextView) findViewById(R.id.titleWeatherCity);
        refreshTime = (TextView) findViewById(R.id.refreshTime);
        weatherInfo = (TextView) findViewById(R.id.weatherInfo);
        temperature = (TextView) findViewById(R.id.temperature);
        search = (Button) findViewById(R.id.search);
        refresh = (Button) findViewById(R.id.refresh);
        search.setOnClickListener(this);
        refresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.search:
                mintent = new Intent(weatherActivity.this, lookUp_weather.class);
                mintent.putExtra("is_from_weather", true);
                startActivity(mintent);
                break;
            case R.id.refresh:
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(weatherActivity.this);
                getWeather(lookUp_weather.code(sharedPreferences.getString("city_name", ""), urlName, keys));
                Log.i("refresh_weather", sharedPreferences.getString("weather_info", ""));
            default:
                break;
        }
    }

    private void getWeather(String Url){
        HttpUtil.sendHttpRequest(Url, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(weatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(weatherActivity.this, "Failed to look up weather", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showWeather(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(weatherActivity.this);
        titleWeatherCity.setText(sharedPreferences.getString("city_name", ""));
        refreshTime.setText(sharedPreferences.getString("publish_time", ""));
        weatherInfo.setText(sharedPreferences.getString("weather_info", ""));
        temperature.setText(sharedPreferences.getString("temp", ""));
        intent = new Intent(weatherActivity.this, AutoUpdateWeather.class);
        startService(intent);
    }



}
